package ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.detailedlistbuilder;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * A class to construct a list of objects that implement the 'Detailed' interface
 * @author Brad Ofrim
 * @see Detailed
 */

public class AssignedTasksListBuilder extends DetailedListBuilder {

    /**
     * Build a list of tasks belonging to the current user.
     * @return ArrayList<Detailed> of all of the current user's tasks.
     */
    @NonNull
    @Override
    ArrayList<Detailed> buildDetailedList(Context context) {
        ArrayList<Detailed> detailedArrayList = new ArrayList<>();
        Task[] allTasks = new DataSourceManager(context).getTasks();
        User currentUser = new DataSourceManager(context).getCurrentUser();

        if (allTasks != null)
        {
            for (Task task : allTasks)
            {
                if (task.getProviderUsername() != null && task.getProviderUsername()
                        .equals(currentUser.getUsername()))
                {
                    detailedArrayList.add(task);
                }
            }
        }

        return detailedArrayList;
    }
}
