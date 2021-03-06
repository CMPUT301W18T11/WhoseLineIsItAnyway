package ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.detailedlistbuilder;

import android.content.Context;
import android.support.annotation.NonNull;

import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;

/**
 * A class to construct a list of all tasks.
 *
 * @author Brad Ofrim
 * @see Detailed
 */

public class AllTasksListBuilder extends DetailedListBuilder
{

    /**
     * Build a list of all tasks.
     *
     * @return ArrayList<Detailed> of all tasks.
     */
    @NonNull
    @Override
    public Detailed[] buildDetailedList(Context context)
    {
        Task[] allTasks = new DataSourceManager(context).getTasks();

        if (allTasks == null)
        {
            return new Task[0];
        }

        return allTasks;
    }
}
