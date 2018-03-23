package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.os.Bundle;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;

/**
 * Created by lucas on 2018-03-21.
 */

public class EditTaskActivity extends EditCreateTaskTemplate{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_modify_template);

        setTitle(R.string.title_EditTaskActivity);

        // ADD EVENT HANDLERS FOR CONTROLS
        btn_Cancel_onClick();
        btn_Submit_onClick();
        textFields_onCharLimitReached(R.id.etxt_Title, 30); // Set Length warning for Title
        textFields_onCharLimitReached(R.id.etxt_Description, 300); // Set Length warning for Description
    }

    protected void btn_Submit_onClick() {

    }
}
