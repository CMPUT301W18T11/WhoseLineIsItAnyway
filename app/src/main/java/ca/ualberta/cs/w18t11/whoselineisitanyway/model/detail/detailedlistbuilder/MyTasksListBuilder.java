package ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.detailedlistbuilder;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * A class to construct a list of all current user's tasks.
 * @author Brad Ofrim
 * @see Detailed
 */

public class MyTasksListBuilder extends DetailedListBuilder {

    /**
     * Build a list of tasks belonging to the current user.
     * @return ArrayList<Detailed> of all of the current user's tasks.
     */
    @NonNull
    @Override
    public Detailed[] buildDetailedList(Context context) {
        ArrayList<Task> detailedArrayList = new ArrayList<>();
        Task[] allTasks = new DataSourceManager(context).getTasks();
        User currentUser = new DataSourceManager(context).getCurrentUser();

        if (allTasks != null)
        {
            for (Task task : allTasks)
            {
                if (task.getRequesterUsername().equals(currentUser.getUsername()))
                {
                    detailedArrayList.add(task);
                }
            }
        }

        return detailedArrayList.toArray(new Task[0]);
    }
}
