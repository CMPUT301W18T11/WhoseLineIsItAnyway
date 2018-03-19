package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

// TODO Ask about detailable and how it is supposed to be implemented.

/**
 * This is an activity to handle creating a task; might be possible to re-use for editing one as well.
 *
 */
public class CreateTaskActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        // ADD EVENT HANDLERS FOR BUTTONS
        btn_Cancel_onClick();
        btn_Submit_onClick();
    }
    private void btn_Cancel_onClick() {
       // finish();
    }
    private void btn_Submit_onClick() {


        // TODO implement task creation backend

       // finish();
    }
    private void btn_UploadImage_onClick() {

    }

    // TODO Later release: Add image functionality (with preview?)
    private Task createTask(String title, String description, Object image) {
        Task newTask = new Task(title, description);
        return newTask;
    }


}
