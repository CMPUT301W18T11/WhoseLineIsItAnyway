package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bitmap.BitmapManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * A custom DetailActivity for a task.
 */

public class TaskDetailActivity extends DetailActivity implements DIALOG_PlaceBid.PlaceBidReturnListener
{

    private Task globalTask = null;

    /**
     * Alter the appearance of the UI to better suit a task.
     * @param viewGroup Parent for adding interface elements.
     */
    public void customizeUserInterface(ViewGroup viewGroup)
    {
        Task task = getTaskFromIntent();
        addTaskImages(task, viewGroup);
        try
        {
            //Task task = getTaskFromIntent();
            renderBasedOnTask(task, viewGroup);
        }
        catch(NullPointerException E)
        {
            // Don't do anything. Only render basic version.
        }
    }

    /**
     * Change what buttons and other UI elements are presented based on who's task it is.
     * @param task to be rendered.
     * @param viewGroup Parent for adding interface elements.
     */
    private void renderBasedOnTask(Task task, ViewGroup viewGroup)
    {
        DataSourceManager dataSourceManager = new DataSourceManager(this);
        User currentUser = dataSourceManager.getCurrentUser();
        if(currentUser.getUsername().equals(task.getRequesterUsername()))
        {
            renderMyTask(task, viewGroup);
        }
        else
        {
            renderOtherTask(task, viewGroup);
        }
    }

    /**
     * Render the task that belongs to the current user.
     * @param task to be rendered.
     * @param viewGroup Parent for adding interface elements.
     */
    private void renderMyTask(Task task, ViewGroup viewGroup)
    {
        switch (task.getStatus())
        {
            case REQUESTED:
                renderMyRequestedTask(task, viewGroup);
                break;
            case BIDDED:
                renderMyBiddedTask(task, viewGroup);
                break;
            case ASSIGNED:
                renderMyAssignedTask(task, viewGroup);
                break;
            case DONE:
                renderMyDoneTask(task, viewGroup);
                break;
        }
    }

    /**
     * Render the task that does not belongs to the current user
     * @param task to be rendered.
     * @param viewGroup Parent for adding interface elements.
     */
    private void renderOtherTask(Task task, ViewGroup viewGroup)
    {
        switch (task.getStatus())
        {
            case REQUESTED:
                renderOtherRequestedTask(task, viewGroup);
                break;
            case BIDDED:
                renderOtherBiddedTask(task, viewGroup);
                break;
            case ASSIGNED:
                renderOtherAssignedTask(task, viewGroup);
                break;
            case DONE:
                renderOtherDoneTask(task, viewGroup);
                break;
        }
    }

    /**
     * Render one of the current user's tasks that has a status of 'Requested'.
     * Show the edit and delete buttons.
     * @param viewGroup Parent for adding interface elements.
     */
    private void renderMyRequestedTask(Task task, ViewGroup viewGroup)
    {

        addEditTaskButton(task, viewGroup);
        addDeleteTaskButton(task, viewGroup);
    }

    /**
     * Render one of the current user's tasks that has a status of 'Bidded'.
     * Show the edit, delete, and accept buttons.
     * @param viewGroup Parent for adding interface elements.
     */
    private void renderMyBiddedTask(Task task, ViewGroup viewGroup)
    {
        addDeleteTaskButton(task, viewGroup);
        addAllBidsButton(task, viewGroup);
    }

    /**
     * Render one of the current user's tasks that has a status of 'Assigned'.
     * Show the complete button.
     * @param viewGroup Parent for adding interface elements.
     */
    private void renderMyAssignedTask(Task task, ViewGroup viewGroup)
    {
        addCompleteTaskButton(task, viewGroup);
        addUnassignTaskButton(task, viewGroup);
    }

    /**
     * Render one of the current user's tasks that has a status of 'Done'.
     *
     * @param viewGroup Parent for adding interface elements.
     */
    private void renderMyDoneTask(Task task, ViewGroup viewGroup)
    {
        addDeleteTaskButton(task, viewGroup);
    }

    /**
     * Render a task from a user that is not the current user with a status of 'Requested'.
     * Show the complete button.
     * @param viewGroup Parent for adding interface elements.
     */
    private void renderOtherRequestedTask(Task task, ViewGroup viewGroup)
    {
        addBidButton(task, viewGroup);
    }

    /**
     * Render a task from a user that is not the current user with a status of 'Bidded'.
     * Show the complete button.
     * @param viewGroup Parent for adding interface elements.
     */
    private void renderOtherBiddedTask(Task task, ViewGroup viewGroup)
    {
        addBidButton(task, viewGroup);
        addAllBidsButton(task, viewGroup);
    }

    /**
     * Render a task from a user that is not the current user with a status of 'Assigned'.
     * Show the complete button.
     * @param viewGroup Parent for adding interface elements.
     */
    private void renderOtherAssignedTask(Task task, ViewGroup viewGroup)
    {
        // Show no additional UI elements
    }

    /**
     * Render a task from a user that is not the current user with a status of 'Done'.
     * Show the complete button.
     * @param viewGroup Parent for adding interface elements.
     */
    private void renderOtherDoneTask(Task task, ViewGroup viewGroup)
    {
        // Show no additional UI elements
    }

    /**
     * Add a section for viewing images to the UI
     * @param task Task to add images for
     * @param viewGroup Parent for adding user interface elements
     */
    private void addTaskImages(Task task, ViewGroup viewGroup)
    {
        LinearLayout linearLayout = new LinearLayout(this);
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
        LinearLayout filmStrip = new LinearLayout(this);

        filmStrip.setId(R.id.filmstrip_panel);

        horizontalScrollView.addView(filmStrip);
        linearLayout.addView(horizontalScrollView);

        ViewGroup insertPoint = findViewById(R.id.activity_detail_group);
        insertPoint.addView(linearLayout, insertPoint.getChildCount());

        insertImagesFromTask(task, filmStrip);
    }

    /**
     * Insert images from the task
     * @param task to insert images from
     */
    // TODO confirm working image addition
    private void insertImagesFromTask(Task task, LinearLayout filmstrip)
    {
       if (task.getImages() == null) {
           return;
       }
       String[] images = task.getImages();

       for(String img : images) {
           BitmapManager temp = new BitmapManager(img);
           add_image(temp, filmstrip);
       }

    }


    private void add_image(BitmapManager img, LinearLayout filmstrip)
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

        img.setImageBitmap(bitmap.getScaledBitmap());


        img.setClickable(true);
        img.setLongClickable(false);
        img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ImageBlowupDialog bigImgView = new ImageBlowupDialog();
                BitmapManager imData = (BitmapManager) v.getTag();
                bigImgView.showDialog(TaskDetailActivity.this, imData);
            }
        });


        return img;
    }

    private void addBidButton(final Task task, ViewGroup viewGroup)
    {
        // Make a view for the button
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_detail_button, viewGroup);
        Button bidButton = (Button) view.findViewById(R.id.detail_button);
        bidButton.setText(R.string.button_place_bid);
        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Add bid button functionality
                globalTask = task;
                DIALOG_PlaceBid bidDialog = new DIALOG_PlaceBid(TaskDetailActivity.this);
                bidDialog.showDialog();
            }
        });

        ViewGroup insertPoint = findViewById(R.id.header_linear_layout);

        insertPoint.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void addAllBidsButton(final Task task, ViewGroup viewGroup)
    {
        // Make a view for the button
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_detail_button, viewGroup);
        Button bidButton = (Button) view.findViewById(R.id.detail_button);
        bidButton.setText(R.string.button_all_bids_task);
        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent outgoingIntent = new Intent(view.getContext(), DetailedListActivity.class);
                String outgoingTitle = "Bids";
                ArrayList<Detailed> bidsArrayList = new ArrayList<>();
                if (task.getBids() != null)
                {
                    for (Bid bid : task.getBids())
                    {
                        bidsArrayList.add(bid);
                    }
                }
                outgoingIntent.putExtra(DetailedListActivity.DATA_TITLE, outgoingTitle);
                outgoingIntent.putExtra(DetailedListActivity.DATA_DETAILABLE_LIST, bidsArrayList);
                startActivity(outgoingIntent);
            }
        });

        ViewGroup insertPoint = findViewById(R.id.header_linear_layout);

        insertPoint.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void addEditTaskButton(final Task task, ViewGroup viewGroup)
    {
        // Make a view for the button
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_detail_button, viewGroup);
        Button bidButton = (Button) view.findViewById(R.id.detail_button);
        bidButton.setText(R.string.button_edit_task);
        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Add edit task button functionality
                Bundle bundle = new Bundle();
                Intent outgoingIntent = new Intent(view.getContext(), CreateModifyTaskActivity.class);
                bundle.putSerializable("EXISTING_TASK", task);
                outgoingIntent.putExtras(bundle);
                finish();
                startActivity(outgoingIntent);

            }
        });

        ViewGroup insertPoint = findViewById(R.id.header_linear_layout);
        insertPoint.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void addDeleteTaskButton(final Task task, ViewGroup viewGroup)
    {
        // Make a view for the button
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_detail_button, viewGroup);
        Button bidButton = (Button) view.findViewById(R.id.detail_button);
        bidButton.setText(R.string.button_delete_task);
        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Prompt for confirmation
                DataSourceManager dataSourceManager = new DataSourceManager(view.getContext());
                dataSourceManager.removeTask(task);
                finish();
            }
        });

        ViewGroup insertPoint = findViewById(R.id.header_linear_layout);
        insertPoint.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void addCompleteTaskButton(final Task task, ViewGroup viewGroup)
    {
        // Make a view for the button
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_detail_button, viewGroup);
        Button bidButton = (Button) view.findViewById(R.id.detail_button);
        bidButton.setText(R.string.button_complete_task);
        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataSourceManager dataSourceManager = new DataSourceManager(view.getContext());
                Task completedTask = task.markDone();
                dataSourceManager.addTask(completedTask);
                finish();
                completedTask.showDetails(TaskDetailActivity.class, view.getContext());
                new DIALOG_WriteReview(TaskDetailActivity.this, dataSourceManager.getUser(task.getProviderUsername()));
            }
        });

        ViewGroup insertPoint = findViewById(R.id.header_linear_layout);
        insertPoint.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void addUnassignTaskButton(final Task task, ViewGroup viewGroup)
    {
        // Make a view for the button
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_detail_button, viewGroup);
        Button bidButton = (Button) view.findViewById(R.id.detail_button);
        bidButton.setText(R.string.button_unassign_task);
        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataSourceManager dataSourceManager = new DataSourceManager(view.getContext());
                Task unassignedTask = task.unassignProvider();
                dataSourceManager.addTask(unassignedTask);
                finish();
                unassignedTask.showDetails(TaskDetailActivity.class, view.getContext());
            }
        });

        ViewGroup insertPoint = findViewById(R.id.header_linear_layout);
        insertPoint.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private Task getTaskFromIntent() throws NullPointerException
    {
        Intent detailIntent = getIntent();
        return (Task) detailIntent.getSerializableExtra(Task.TASK_KEY);
    }

    @Override
    public void PlaceBidDialog_PosResult(BigDecimal result) {
        DataSourceManager dataSourceManager = new DataSourceManager(this);
        Bid newBid = new Bid(dataSourceManager.getCurrentUser().getUsername(), globalTask.getElasticId(), result);
        globalTask.submitBid(newBid, dataSourceManager);
    }

    // Get the dp -> pixel conversion
    private int dpToPixels(int dp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
