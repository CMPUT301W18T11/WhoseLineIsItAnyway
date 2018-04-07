package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bitmap.BitmapManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.TaskStatus;

/**
 * <h1>CreateModifyTaskActivity</h1>
 * This activity is designed for creating new tasks, or editing existing tasks
 * INPUT: Bundle containing a Task object, if for editing; SET TAG AS EXISTING_TASK
 * @author Lucas
 * @see Task
 */
public class CreateModifyTaskActivity extends AppCompatActivity implements SetMapLocationDialog.MapDialogReturnListener {
    private DataSourceManager DSM = new DataSourceManager(this);
    private LinearLayout filmstrip; // Hold the container for the objects
    final int MAX_CHARLIMIT_TITLE = 30; // Maximum length of Task Title
    final int MAX_CHARLIMIT_DESCRIPTION = 300; // Maximum length of Task Description
    private int PICK_IMAGES = 1256;

    private HashMap<KEYS, Object> TaskParameters = new HashMap<KEYS, Object>();  // Hold parameters of a task for easy Access during Creation
    private enum KEYS {
        TITLE("TITLE"),
        DESCR("DESCRIPTION"),
        LOCATION("LOCATION"),
        IMAGES("IMAGES"),
        STATUS("STATUS"),
        ID("ID");

        private String value;
        KEYS(String val) {
            this.value = val;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_modify);
        if (true) { conditionalSetup(getIntent().getExtras()); }
        setTitle(R.string.title_CreateTaskActivity);
        filmstrip = (LinearLayout) findViewById(R.id.filmstrip_panel);

        TaskParameters.put(KEYS.TITLE, null);
        TaskParameters.put(KEYS.DESCR, null);
        TaskParameters.put(KEYS.LOCATION, null);
        TaskParameters.put(KEYS.IMAGES, new ArrayList<String>());
        TaskParameters.put(KEYS.STATUS, null);
        TaskParameters.put(KEYS.ID, null);

        // EVENTHANDLERS
        taskTitleField_onCharLimitReached(); // Set warning flag when Title is == MAX_CHARLIMIT_TITLE
        taskDescriptionField_onCharLimitReached(); // set warning flag when Descr == MAX_CHARLIMIT_DESCRIPTION
        taskLocationField_onEmpty(); // Reset status warning on the field if the text is deleted
        btnAddImg_onClick(); // Handle functions for adding image(s) to the filmstrip/task
        btnClearImg_onClick(); // Remove all images from filmstrip
        btnSetLocation_onClick(); // Set a new location for the task
        btnClearLocation_onClick(); // Clear set location
        btnSubmit_onClick(); // Save or create the new task
        btnCancel_onClick(); // Cancel and return to previous context (Do nothing)

    }

    // Check if fields need to be prepopulated or if the task is a new task and can be started empty
    private void conditionalSetup(final Bundle bundle) {
        if (bundle != null) {
            if (bundle.size() > 0) {
                setTitle(R.string.title_EditTaskActivity);
                populateFields((Task) bundle.get("EXISTING_TASK"));
            }
        }
    }

    // If an existing task object was passed in, populate fields for editing the task with existing data
    private void populateFields(final Task existingTask) {
        if (existingTask.getStatus() == TaskStatus.BIDDED) {
            Toast.makeText(this, "Bidded tasks cannot be edited.\n" +
            "You can delete the task from the tasks menu to cancel the contract but you can no longer change task details. Returning...", Toast.LENGTH_LONG);
            finish();
        }
        EditText titleField = (EditText) findViewById(R.id.etxt_Title);
        EditText descrField = (EditText) findViewById(R.id.etxt_Description);
        TextView locField = (TextView) findViewById(R.id.txt_location_set);
        // TODO populate images from loaded task
        // TODO populate location from task
        titleField.setText(existingTask.getTitle());
        descrField.setText(existingTask.getDescription());
        TaskParameters.put(KEYS.ID, existingTask.getElasticId());


    }

//region Character Limit Event Handler
    private void taskTitleField_onCharLimitReached() {
        EditText textField = (EditText) findViewById(R.id.etxt_Title);
        charLimitHandler(MAX_CHARLIMIT_TITLE, textField);
    }
    private void taskDescriptionField_onCharLimitReached() {
        EditText textField = (EditText) findViewById(R.id.etxt_Description);
        charLimitHandler(MAX_CHARLIMIT_DESCRIPTION, textField);
    }
    private void charLimitHandler(final int charlimit, final EditText controlReference) {
        final EditText editField = controlReference;
        editField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editField.getText().length() == charlimit) {
                    editField.setError("This field has a limit of " + String.valueOf(charlimit) +
                    " character(s).");
                } else { editField.setError(null); }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }
    //endregion

    // Reset recorded location status if the field is cleared via btnClearLocation
    private void taskLocationField_onEmpty() {
        final TextView locationField = (TextView) findViewById(R.id.txt_location_set);
        locationField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (locationField.getText().length() == 0) {
                    locationField.setText("(No location selected.)");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    //region Image Modification

//region ImageSelection

    private void startImageSelection(){

        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            // Request location permissions
            int PERMISSION_LOCATION_REQUEST_CODE = 0;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_LOCATION_REQUEST_CODE);
            return;
        } else {
            Intent photoPickerIntent = new Intent();
            photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, PICK_IMAGES);
        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGES && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();

            Log.i("TaskCreate_ImageSelect",selectedImage.toString());
            continueImageAdding(selectedImage);
        }
    }



//endregion
//region Image Management
    private void btnAddImg_onClick(){
        final LinearLayout filmstrip = this.filmstrip;

        Button button = (Button) findViewById(R.id.btn_UploadImage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImageSelection();
            }
        });
    }

    // DO NOT CALL ELSEWHERE
    private void continueImageAdding(Uri imgUri) {
        if (! (imgUri == null)) {
            Log.i("CreateTask_Image", imgUri.getPath());
            BitmapManager newItem = new BitmapManager(imgUri, this);
            add_image(newItem);

        }
    }

    private void add_image(BitmapManager img) {
        boolean isDupe = false;
        if (! (filmstrip.getChildCount() == 0)) {
            for (int i = 0; i < filmstrip.getChildCount(); i ++) {
                ImageView imviewOther = (ImageView) filmstrip.getChildAt(i);
                BitmapManager other = (BitmapManager) imviewOther.getTag();

                isDupe = img.getSame(other);
                if (isDupe) {
                    Toast.makeText(this, "The image is a duplicate.", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
        if (! isDupe) { filmstrip.addView(generateImgView(img)); }

    }

    private ImageView generateImgView(final BitmapManager bitmap) {

        final int MARGIN_SIZE = dpToPixels(3);
        final int WIDTH_DP = 120;


        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setMargins(MARGIN_SIZE,0, MARGIN_SIZE, 0);
        layout.width = dpToPixels(WIDTH_DP);
        final ImageView img = new ImageView(this);
        img.setLayoutParams(layout);
        img.setPadding(3,3,3,3);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        img.setTag(bitmap);

        if (bitmap.getStatus()) {
            setImgViewBorder(img);
        } else {
            setImgViewBorder(img);
        }

        img.setImageBitmap(bitmap.getScaledBitmap());


        img.setClickable(true);
        img.setLongClickable(true);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imgview = (ImageView) v;
                ImageBlowupDialog bigImgView = new ImageBlowupDialog();
                BitmapManager imData = (BitmapManager) v.getTag();
                bigImgView.showDialog(CreateModifyTaskActivity.this, imData);
            }
        });
        img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                filmstrip.removeView(v);
                return true;
            }
        });


        return img;
    }


    private void setImgViewBorder(final ImageView img) {
        BitmapManager imgData = (BitmapManager) img.getTag();

        final int GREEN = 0xFF00C10C;
        final int RED = 0xFFC70039;

        ShapeDrawable border = new ShapeDrawable();
        border.getPaint().setStrokeWidth(6);
        border.getPaint().setStyle(Paint.Style.STROKE);

        if (imgData.getStatus()) {
            border.getPaint().setColor(GREEN);
        } else {
            border.getPaint().setColor(RED);
        }

        img.setBackground(border);

    }

    private void btnClearImg_onClick(){
        final LinearLayout filmstrip = this.filmstrip;
        Button button = (Button) findViewById(R.id.btn_ClearAllUploadImages);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filmstrip.removeAllViews();
            }
        });

    }
    //endregion
    private void btnSetLocation_onClick(){
        Button button = findViewById(R.id.btn_callLocationDiag);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First check if the user has given permission to access location
                if (!(ContextCompat.checkSelfPermission(
                        CreateModifyTaskActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                    // Request location permissions
                    int PERMISSION_LOCATION_REQUEST_CODE = 0;
                    ActivityCompat.requestPermissions(CreateModifyTaskActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_REQUEST_CODE);
                    if (PERMISSION_LOCATION_REQUEST_CODE == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(CreateModifyTaskActivity.this, "You must enable locations access. You will not be able to set a location without this.", Toast.LENGTH_SHORT).show();
                    } else {
                        return;
                    }
                } else {
                    SetMapLocationDialog setLocation = new SetMapLocationDialog(CreateModifyTaskActivity.this);
                    setLocation.showDialog();
                }
            }
        });
    }

    @Override
    public void MapSetDialog_PosResult(LatLng result) {
        if (result != null) {
            TaskParameters.put(KEYS.LOCATION, result);
            TextView locField = (TextView) findViewById(R.id.txt_location_set);
            String locString = "Location Set\n(" + String.valueOf(result.latitude) + ", " +
                    String.valueOf(result.longitude) + ")";
            locField.setText(locString);
        }
    }

    private void btnClearLocation_onClick(){
        Button button = (Button) findViewById(R.id.btn_ClearLocation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskParameters.put(KEYS.LOCATION, null);
                TextView locField = (TextView) findViewById(R.id.txt_location_set);
                String locString = "(Location not set)";
                locField.setText(locString);
            }
        });

    }
    private void btnSubmit_onClick(){
        Button button = (Button) findViewById(R.id.btn_Submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText title = (EditText) findViewById(R.id.etxt_Title);
                EditText descr = (EditText) findViewById(R.id.etxt_Description);
                ArrayList<String> images = new ArrayList<String>();
                TaskParameters.put(KEYS.TITLE, title.getText().toString());
                TaskParameters.put(KEYS.DESCR, descr.getText().toString());

                for (int i = 0; i < filmstrip.getChildCount(); i ++) {
                    ImageView preview = (ImageView) filmstrip.getChildAt(i);
                    BitmapManager tag = (BitmapManager) preview.getTag();
                    images.add(tag.getBase64Bitmap());
                }

                TaskParameters.put(KEYS.IMAGES, images);
                buildTask();
            }
        });

    }
    private void btnCancel_onClick(){
        Button button = (Button) findViewById(R.id.btn_Cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void buildTask() {
        //TODO Once Tasks is able to take photos + location, add these parameters
        Task buildTask;
        String userID = DSM.getCurrentUser().getUsername();
        if (TaskParameters.get(KEYS.ID) == null) {
            buildTask = new Task(
                    userID,
                    (String) TaskParameters.get(KEYS.TITLE),
                    (String) TaskParameters.get(KEYS.DESCR)
            );
        } else {
           buildTask = new Task(
                    userID,
                    (String) TaskParameters.get(KEYS.TITLE),
                    (String) TaskParameters.get(KEYS.DESCR)
                    );
        }
        DSM.addTask(buildTask);
        finish();
    }

    private int dpToPixels(int dp) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

}
