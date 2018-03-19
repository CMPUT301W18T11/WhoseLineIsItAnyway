package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

// TODO Ask about detailable and how it is supposed to be implemented.

/**
 * This is an activity to handle creating a task
 *
 * @author Lucas Thalen
 *         NO INPUT REQUIRED
 *         NO OUTPUT
 *         SETS NEW TASK
 *         (INCOMPLETE, WILL REFACTOR FOR BACKEND
 */
public class CreateTaskActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        // ADD EVENT HANDLERS FOR CONTROLS
        btn_Cancel_onClick();
        btn_Submit_onClick();
        etxt_Title_afterTextChanged();
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
        // finish();
    }

    /**
     * EVENTHANDLER: Submission button onClick() for creating the task and committing it
     * to either local or remote depending on what is available; further, calling any necessary
     * field updates in activities or objects to make sure the changes are reflected.
     */
    private void btn_Submit_onClick()
    {


        // TODO implement task creation backend

        // finish();
    }

    private void btn_UploadImage_onClick()
    {

    }

    /**
     * EVENTHANDLER: This handles changes in the Title field to warn the user when they have
     * approached the character limit of the field.
     */
    private void etxt_Title_afterTextChanged()
    {
        final EditText textbox = (EditText) findViewById(R.id.etxt_Title);
        textbox.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (textbox.getText().length() == 30)
                {
                    textbox.setError("Task titles have a limit of 30 characters.");
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
    private Task createTask(String title, String description, Object image)
    {
        Task newTask = new Task(title, description, "PLACEHOLDER_REQUESTER_ID",
                "PLACEHOLDER_PROVIDER_ID");
        return newTask;
    }

}
