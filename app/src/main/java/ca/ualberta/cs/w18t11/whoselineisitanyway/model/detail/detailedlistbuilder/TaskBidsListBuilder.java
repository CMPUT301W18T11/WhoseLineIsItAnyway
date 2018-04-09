package ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.detailedlistbuilder;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;

/**
 * A class to construct a list of all of the bids on a certain task.
 * @author Brad Ofrim
 * @see Detailed
 */

public class TaskBidsListBuilder extends DetailedListBuilder {

    Task taskToBuildFor = null;
    public TaskBidsListBuilder(@NonNull final Task task)
    {
        taskToBuildFor = task;
    }

    @NonNull
    @Override
    public Detailed[] buildDetailedList(Context context) {
        /**
         * Build a list of the 'taskToBuildFor' bids.
         * @return ArrayList<Detailed> of all of the current user's bids.
         */
        ArrayList<Bid> bidArrayList = new ArrayList<>();
        if (taskToBuildFor.getBids() != null)
        {
            for (Bid bid : taskToBuildFor.getBids())
            {
                bidArrayList.add(bid);
            }
        }
        return bidArrayList.toArray(new Bid[0]);
    }
}
