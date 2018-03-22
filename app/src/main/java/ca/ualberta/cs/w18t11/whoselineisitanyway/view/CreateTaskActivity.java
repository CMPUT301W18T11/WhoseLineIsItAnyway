package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.datasource.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;

// TODO Ask about detailable and how it is supposed to be implemented.

/**
 * This is an activity to handle creating a task
 *
 * @author Lucas Thalen
 *         NO INPUT REQUIRED
 *         NO OUTPUT TO RECEIVE
 *         CREATES/SETS NEW TASK
 *         (INCOMPLETE, WILL REFACTOR FOR BACKEND
 */
public class CreateTaskActivity extends AppCompatActivity
{

    final private DataSourceManager DSM = DataSourceManager.getInstance();
    private Task newTask;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        setTitle(R.string.title_CreateTaskActivity);
        // ADD EVENT HANDLERS FOR CONTROLS
        btn_Cancel_onClick();
        btn_Submit_onClick();
        textFields_onCharLimitReached(R.id.etxt_Title, 30); // Set Length warning for Title
        textFields_onCharLimitReached(R.id.etxt_Description, 300); // Set Length warning for Description
    }

    /**
     * EVENTHANDLER: Cancel button onClick() for closing out the form without making changes
     */
    private void btn_Cancel_onClick()
    {
        Button cancel = (Button) findViewById(R.id.btn_Cancel);
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
    private void btn_Submit_onClick()
    {
        // MOCK OFFLINE DATA STUFF
        Button submit = (Button) findViewById(R.id.btn_Submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = ((EditText) findViewById(R.id.etxt_Title)).getText().toString();
                String descr = ((EditText) findViewById(R.id.etxt_Description)).getText().toString();

                createTask(title, descr, "IMAGE_PLACEHOLDER");
                if (! (newTask == null)) {
                    DSM.getLocalDataSource().addTask(newTask);
                }
                finish();

            }
        });

        // TODO implement task creation backend

        // finish();
    }

    private void btn_UploadImage_onClick()
    {

    }

    /**
     * EVENTHANDLER: This is a generic method signature for the TextOnChanged handler, specifically to
     * address for character limited fields when they heat their charlimits so a warning can be shown.
     * @param ID R.id.x where the ID is for an EditText control.
     * @param maxLen The maximum character limit for this field that triggers a warning when hit
     */
    private void textFields_onCharLimitReached(final int ID, final int maxLen)
    {
        final EditText textbox = (EditText) findViewById(ID);
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
                    textbox.setError("This field has a limit of " + String.valueOf(maxLen) + " characters.");
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
    private void createTask(String title, String description, Object image)
    {
        Task newTask = new Task(title, description, "PLACEHOLDER_REQUESTER_ID",
                "PLACEHOLDER_PROVIDER_ID");
        this.newTask = newTask;
    }

}
