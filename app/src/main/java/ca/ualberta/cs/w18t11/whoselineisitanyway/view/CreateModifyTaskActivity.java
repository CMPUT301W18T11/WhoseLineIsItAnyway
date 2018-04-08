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
import java.util.Arrays;
import java.util.HashMap;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bitmap.BitmapManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.location.Location;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.TaskStatus;

/**
 * <h1>CreateModifyTaskActivity</h1>
 * This activity is designed for creating new tasks, or editing existing tasks
 * INPUT: Bundle containing a Task object, if for editing; SET TAG AS EXISTING_TASK
 *
 * @author Lucas
 * @see Task
 */
//TODO Pre-initialize existingtask or use filter - as it's calling on a void thing in location setting
public class CreateModifyTaskActivity extends AppCompatActivity implements SetMapLocationDialog.MapDialogReturnListener, ActivityCompat.OnRequestPermissionsResultCallback  {
    private DataSourceManager DSM = new DataSourceManager(this);
    private LinearLayout filmstrip; // Hold the container for the objects
    final int MAX_CHARLIMIT_TITLE = 30; // Maximum length of Task Title
    final int MAX_CHARLIMIT_DESCRIPTION = 300; // Maximum length of Task Description
    private int PICK_IMAGES = 1256;
    private Task existingTask;
    private Task resultTask;
    private ArrayList<String> image = new ArrayList<String>();
    private Location tempLoc = null;
    private final int PERMISSION_REQUEST_READ_STORAGE = 0;
    private final int PERMISSION_REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_modify);

        conditionalSetup(getIntent().getExtras());
        setTitle(R.string.title_CreateTaskActivity);
        filmstrip = findViewById(R.id.filmstrip_panel);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    private void conditionalSetup(final Bundle bundle)
    {
        if (bundle != null)
        {
            if (bundle.size() > 0)
            {
                setTitle(R.string.title_EditTaskActivity);
                existingTask = (Task) bundle.get("EXISTING_TASK");
                populateFields();
            }
        }
    }

    // If an existing task object was passed in, populate fields for editing the task with existing data
    private void populateFields()
    {
        if (existingTask.getStatus() == TaskStatus.BIDDED)
        {
            Toast.makeText(this, "Bidded tasks cannot be edited.\n" +
                            "You can delete the task from the tasks menu to cancel the contract but you can no longer change task details. Returning...",
                    Toast.LENGTH_LONG);
            finish();
        }

        EditText titleField = findViewById(R.id.etxt_Title);
        EditText descrField = findViewById(R.id.etxt_Description);
        TextView locField = findViewById(R.id.txt_location_set);
        titleField.setText(existingTask.getTitle());
        descrField.setText(existingTask.getDescription());
        String locString = "Location Set\n(" + String.valueOf(existingTask.getLocation().getLatitude()) + ", " +
                String.valueOf(existingTask.getLocation().getLongitude()) + ")";
        locField.setText(locString);

        // Check to ensure that images are in the task, if not don't bother changing anything
        if (existingTask.getImages().length > 0) {
            ArrayList<String> images = new ArrayList<>(Arrays.asList(existingTask.getImages()));
            for (int i = 0; i < images.size(); i ++) {
                add_image(new BitmapManager(images.get(i)));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("Permissions Listener", "RequestCode:" + String.valueOf(requestCode));
        switch (requestCode)
        {
            case PERMISSION_REQUEST_READ_STORAGE:
                if (grantResults.length == 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Intent photoPickerIntent = new Intent();
                    photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, PICK_IMAGES);
                }
                break;
            case PERMISSION_REQUEST_LOCATION:
                if (grantResults.length == 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    SetMapLocationDialog setLocation = new SetMapLocationDialog(
                            CreateModifyTaskActivity.this);
                    setLocation.showDialog();
                }
                break;

        }

    }

    //region Character Limit Event Handler
    private void taskTitleField_onCharLimitReached()
    {
        EditText textField = findViewById(R.id.etxt_Title);
        charLimitHandler(MAX_CHARLIMIT_TITLE, textField);
    }

    private void taskDescriptionField_onCharLimitReached()
    {
        EditText textField = findViewById(R.id.etxt_Description);
        charLimitHandler(MAX_CHARLIMIT_DESCRIPTION, textField);
    }

    private void charLimitHandler(final int charlimit, final EditText controlReference)
    {
        final EditText editField = controlReference;
        editField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (editField.getText().length() == charlimit)
                {
                    editField.setError("This field has a limit of " + String.valueOf(charlimit) +
                            " character(s).");
                }
                else
                {
                    editField.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

    }

    // Reset recorded location status if the field is cleared via btnClearLocation
    private void taskLocationField_onEmpty()
    {
        final TextView locationField = findViewById(R.id.txt_location_set);
        locationField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (locationField.getText().length() == 0)
                {
                    locationField.setText("(No location selected.)");
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });
    }
    //endregion

    private void startImageSelection()
    {
        boolean canContinue
                = false; // Allows one button press to call permissions and proceed to code
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_READ_STORAGE);
        }
        else
        {
            Intent photoPickerIntent = new Intent();
            photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, PICK_IMAGES);
        }


    }

    //region Image Modification

//region ImageSelection

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGES && resultCode == Activity.RESULT_OK)
        {
            Uri selectedImage = data.getData();

            Log.i("TaskCreate_ImageSelect", selectedImage.toString());
            continueImageAdding(selectedImage);
        }
    }

    //endregion
//region Image Management
    private void btnAddImg_onClick()
    {
        final LinearLayout filmstrip = this.filmstrip;

        Button button = findViewById(R.id.btn_UploadImage);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startImageSelection();
            }
        });
    }

    // DO NOT CALL ELSEWHERE
    private void continueImageAdding(Uri imgUri)
    {
        if (!(imgUri == null))
        {
            Log.i("CreateTask_Image", imgUri.getPath());
            BitmapManager newItem = new BitmapManager(imgUri, this);
            add_image(newItem);

        }
    }

    private void add_image(BitmapManager img)
    {
        boolean isDupe = false;
        if (!(filmstrip.getChildCount() == 0))
        {
            for (int i = 0; i < filmstrip.getChildCount(); i++)
            {
                ImageView imviewOther = (ImageView) filmstrip.getChildAt(i);
                BitmapManager other = (BitmapManager) imviewOther.getTag();

                isDupe = img.getSame(other);
                if (isDupe)
                {
                    Toast.makeText(this, "The image is a duplicate.", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
        if (!isDupe)
        {
            filmstrip.addView(generateImgView(img));
        }

    }

    private ImageView generateImgView(final BitmapManager bitmap)
    {

        final int MARGIN_SIZE = dpToPixels(3);
        final int WIDTH_DP = 120;


        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setMargins(MARGIN_SIZE, 0, MARGIN_SIZE, 0);
        layout.width = dpToPixels(WIDTH_DP);
        final ImageView img = new ImageView(this);
        img.setLayoutParams(layout);
        img.setPadding(3, 3, 3, 3);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        img.setTag(bitmap);

        if (bitmap.getStatus())
        {
            setImgViewBorder(img);
        }
        else
        {
            setImgViewBorder(img);
        }

        img.setImageBitmap(bitmap.getScaledBitmap());


        img.setClickable(true);
        img.setLongClickable(true);
        img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ImageView imgview = (ImageView) v;
                ImageBlowupDialog bigImgView = new ImageBlowupDialog();
                BitmapManager imData = (BitmapManager) v.getTag();
                bigImgView.showDialog(CreateModifyTaskActivity.this, imData);
            }
        });
        img.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                filmstrip.removeView(v);
                return true;
            }
        });


        return img;
    }

    private void setImgViewBorder(final ImageView img)
    {
        BitmapManager imgData = (BitmapManager) img.getTag();

        final int GREEN = 0xFF00C10C;
        final int RED = 0xFFC70039;

        ShapeDrawable border = new ShapeDrawable();
        border.getPaint().setStrokeWidth(6);
        border.getPaint().setStyle(Paint.Style.STROKE);

        if (imgData.getStatus())
        {
            border.getPaint().setColor(GREEN);
        }
        else
        {
            border.getPaint().setColor(RED);
        }

        img.setBackground(border);

    }

    private void btnClearImg_onClick()
    {
        final LinearLayout filmstrip = this.filmstrip;
        Button button = findViewById(R.id.btn_ClearAllUploadImages);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                filmstrip.removeAllViews();
            }
        });

    }

    //endregion
    private void btnSetLocation_onClick()
    {
        Button button = findViewById(R.id.btn_callLocationDiag);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // First check if the user has given permission to access location
                if (!(ContextCompat.checkSelfPermission(
                        CreateModifyTaskActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED))
                {
                    ActivityCompat.requestPermissions(CreateModifyTaskActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_REQUEST_LOCATION);
                }
                else
                {
                    SetMapLocationDialog setLocation = new SetMapLocationDialog(
                            CreateModifyTaskActivity.this);
                    setLocation.showDialog();
                }
            }
        });
    }

    @Override
    public void MapSetDialog_PosResult(LatLng result) {
        if (result != null) {

            TextView locField = (TextView) findViewById(R.id.txt_location_set);
            String locString = "Location Set\n(" + String.valueOf(result.latitude) + ", " +
                    String.valueOf(result.longitude) + ")";
            tempLoc = new Location(result);
            locField.setText(locString);
        }
    }

    private void btnClearLocation_onClick()
    {
        Button button = findViewById(R.id.btn_ClearLocation);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                existingTask.setLocation(null);
                TextView locField = (TextView) findViewById(R.id.txt_location_set);
                String locString = "(Location not set)";
                locField.setText(locString);
            }
        });

    }

    private void btnSubmit_onClick()
    {
        Button button = findViewById(R.id.btn_Submit);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (validateAllFields() != true) { return; }
                EditText title = (EditText) findViewById(R.id.etxt_Title);
                EditText descr = (EditText) findViewById(R.id.etxt_Description);


                DSM.addTask(existingTask);
                finish();

            }
        });

    }

    private void btnCancel_onClick()
    {
        Button button = findViewById(R.id.btn_Cancel);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    private boolean validateAllFields() {
        EditText titleField = (EditText) findViewById(R.id.etxt_Title);
        EditText descrField = (EditText) findViewById(R.id.etxt_Description);
        if (titleField.getText().length() == 0) {
            titleField.setError("You must enter a title for your task.");
        }
        if (descrField.getText().length() == 0)
        {
            descrField.setError("You must enter a description for your task.");
        }
        return (titleField.getText().length() != 0 && descrField.getText().length() != 0);
    }

    private int dpToPixels(int dp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}
