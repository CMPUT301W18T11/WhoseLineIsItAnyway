package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * A custom DetailActivity for a task.
 */

public class TaskDetailActivity extends DetailActivity {

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

        addEditTaskButton(task, viewGroup, 0);
        addDeleteTaskButton(task, viewGroup, 1);
    }

    /**
     * Render one of the current user's tasks that has a status of 'Bidded'.
     * Show the edit, delete, and accept buttons.
     * @param viewGroup Parent for adding interface elements.
     */
    private void renderMyBiddedTask(Task task, ViewGroup viewGroup)
    {
        addEditTaskButton(task, viewGroup, 0);
        addDeleteTaskButton(task, viewGroup, 1);
        addAcceptBidButton(task, viewGroup, 2);
    }

    /**
     * Render one of the current user's tasks that has a status of 'Assigned'.
     * Show the complete button.
     * @param viewGroup Parent for adding interface elements.
     */
    private void renderMyAssignedTask(Task task, ViewGroup viewGroup)
    {
        addCompleteTaskButton(task, viewGroup, 0);
        addUnassignTaskButton(task, viewGroup, 1);
    }

    /**
     * Render one of the current user's tasks that has a status of 'Done'.
     *
     * @param viewGroup Parent for adding interface elements.
     */
    private void renderMyDoneTask(Task task, ViewGroup viewGroup)
    {
        // Show archive button?
    }

    /**
     * Render a task from a user that is not the current user with a status of 'Requested'.
     * Show the complete button.
     * @param viewGroup Parent for adding interface elements.
     */
    private void renderOtherRequestedTask(Task task, ViewGroup viewGroup)
    {
        //  * task is requested: show add bid
        addBidButton(task, viewGroup, 0);
    }

    /**
     * Render a task from a user that is not the current user with a status of 'Bidded'.
     * Show the complete button.
     * @param viewGroup Parent for adding interface elements.
     */
    private void renderOtherBiddedTask(Task task, ViewGroup viewGroup)
    {
        //  * task is bidded && current user has not bid: show bid
        //  * task is bidded && current user has bid: show change bid, remove bid
        addBidButton(task, viewGroup, 0);
    }

    /**
     * Render a task from a user that is not the current user with a status of 'Assigned'.
     * Show the complete button.
     * @param viewGroup Parent for adding interface elements.
     */
    private void renderOtherAssignedTask(Task task, ViewGroup viewGroup)
    {
        //  * task is assigned, but not to current user: show nothing
        //  * task is assigned to the current user: show ??
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

        insertImagesFromTask(task);
    }

    /**
     * Insert images from the task
     * @param task to insert images from
     */
    private void insertImagesFromTask(Task task)
    {
        // TODO: Add the images from the task.
    }

    private void addBidButton(final Task task, ViewGroup viewGroup, int index)
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
            }
        });

        ViewGroup insertPoint = findViewById(R.id.header_linear_layout);

        insertPoint.addView(view, index, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void addAcceptBidButton(final Task task, ViewGroup viewGroup, int index)
    {
        // Make a view for the button
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_detail_button, viewGroup);
        Button bidButton = (Button) view.findViewById(R.id.detail_button);
        bidButton.setText(R.string.button_accept_bid);
        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Add accept bid button functionality
            }
        });

        ViewGroup insertPoint = findViewById(R.id.header_linear_layout);
        insertPoint.addView(view, index, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void addEditTaskButton(final Task task, ViewGroup viewGroup, int index)
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
            }
        });

        ViewGroup insertPoint = findViewById(R.id.header_linear_layout);
        insertPoint.addView(view, index, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void addDeleteTaskButton(final Task task, ViewGroup viewGroup, int index)
    {
        // Make a view for the button
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_detail_button, viewGroup);
        Button bidButton = (Button) view.findViewById(R.id.detail_button);
        bidButton.setText(R.string.button_delete_task);
        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Add delete task button functionality
            }
        });

        ViewGroup insertPoint = findViewById(R.id.header_linear_layout);
        insertPoint.addView(view, index, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void addCompleteTaskButton(final Task task, ViewGroup viewGroup, int index)
    {
        // Make a view for the button
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_detail_button, viewGroup);
        Button bidButton = (Button) view.findViewById(R.id.detail_button);
        bidButton.setText(R.string.button_complete_task);
        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Add complete task button functionality
            }
        });

        ViewGroup insertPoint = findViewById(R.id.header_linear_layout);
        insertPoint.addView(view, index, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void addUnassignTaskButton(final Task task, ViewGroup viewGroup, int index)
    {
        // Make a view for the button
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_detail_button, viewGroup);
        Button bidButton = (Button) view.findViewById(R.id.detail_button);
        bidButton.setText(R.string.button_unassign_task);
        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Add unassign task button functionality
            }
        });

        ViewGroup insertPoint = findViewById(R.id.header_linear_layout);
        insertPoint.addView(view, index, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private Task getTaskFromIntent() throws NullPointerException
    {
        Intent detailIntent = getIntent();
        return (Task) detailIntent.getSerializableExtra(Task.TASK_KEY);
    }
}
