package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;

// TODO Ask about detailable and how it is supposed to be implemented.

/**
 * This is an activity to handle creating a task
 *
 * @author Lucas Thalen
 * Input: Intent with minimum 0/1 to indicate new/edit and if edit, a task object
 * NO OUTPUT TO RECEIVE
 * CREATES/SETS NEW TASK
 * (INCOMPLETE, WILL REFACTOR FOR BACKEND
 */
public abstract class EditCreateTaskTemplate extends AppCompatActivity
{
    final private DataSourceManager DSM = new DataSourceManager(this);

    private Task newTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_task_modify_template);

    }

    /**
     * EVENTHANDLER: Cancel button onClick() for closing out the form without making changes
     */
    protected void btn_Cancel_onClick()
    {
        Button cancel = findViewById(R.id.btn_Cancel);
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    /**
     * EVENTHANDLER: Submission button onClick() for creating the task and committing it
     * to either local or remote depending on what is available; further, calling any necessary
     * field updates in activities or objects to make sure the changes are reflected.
     */
    protected abstract void btn_Submit_onClick(); // Partial refactor this somehow into common code for the verification portions of the stuff

    /**
     * This is code that is shareable between thw two activities (Edit/Create Task). Mostly the verification components.
     */
    protected void submit_CommonCore()
    {
        // MOCK OFFLINE DATA STUFF
        Button submit = findViewById(R.id.btn_Submit);
        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String title = ((EditText) findViewById(R.id.etxt_Title)).getText().toString();
                String descr = ((EditText) findViewById(R.id.etxt_Description)).getText()
                        .toString();

                createTask(title, descr, "IMAGE_PLACEHOLDER");
                if (!(newTask == null))
                {
                    DSM.addTask(newTask);
                }
                finish();

            }
        });

        // TODO implement task creation backend

        // finish();
    }

    protected void btn_UploadImage_onClick()
    {

    }

    protected void btn_ClearImages_onClick()
    {

    }

    /**
     * EVENTHANDLER: This is a generic method signature for the TextOnChanged handler, specifically to
     * address for character limited fields when they heat their charlimits so a warning can be shown.
     *
     * @param ID     R.id.x where the ID is for an EditText control.
     * @param maxLen The maximum character limit for this field that triggers a warning when hit
     */
    protected void textFields_onCharLimitReached(final int ID, final int maxLen)
    {
        final EditText textbox = findViewById(ID);
        textbox.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            } // Useless mandatory empty methods of the TextChangedEvent Handlers

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (textbox.getText().length() == maxLen)
                {
                    textbox.setError(
                            "This field has a limit of " + String.valueOf(maxLen) + " characters.");
                }
                else
                {
                    textbox.setError(null);
                }

            }
        });
    }

    // TODO Later release: Add image functionality (with preview?)
    // TODO replace placeholders with User ID object information; adjust parameters
    protected void createTask(String title, String description, Object image)
    {
        Task newTask = new Task(title, description, "PLACEHOLDER_REQUESTER_ID",
                "PLACEHOLDER_PROVIDER_ID");
        this.newTask = newTask;
    }

}
