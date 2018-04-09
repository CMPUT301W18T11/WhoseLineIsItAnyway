package ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.detailedlistbuilder;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * A class to construct a list of all task that the current user has a bid on.
 * @author Brad Ofrim
 * @see Detailed
 */

public class MyBidTasksListBuilder extends DetailedListBuilder {
    /**
     * Build a list of all task that the current user has a bid on.
     * @return ArrayList<Detailed> of all tasks that the current user has a bid on.
     */
    @NonNull
    @Override
    ArrayList<Detailed> buildDetailedList(Context context) {
        ArrayList<Detailed> detailedArrayList = new ArrayList<>();
        DataSourceManager dataSourceManager = new DataSourceManager(context);
        Bid[] allBids = dataSourceManager.getBids();
        User currentUser = new DataSourceManager(context).getCurrentUser();

        if (allBids != null)
        {
            for (Bid bid : allBids)
            {
                if (bid.getProviderUsername().equals(currentUser.getUsername()))
                {
                    detailedArrayList.add(dataSourceManager.getTask(bid.getTaskId()));
                }
            }
        }

        return detailedArrayList;
    }
}
