package ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.detailedlistbuilder;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * A class to construct a list of all of the current user's bids.
 * @author Brad Ofrim
 * @see Detailed
 */

public class MyBidsListBuilder extends DetailedListBuilder {
    /**
     * Build a list of the current user's bids.
     * @return ArrayList<Detailed> of all of the current user's bids.
     */
    @NonNull
    @Override
    public Detailed[] buildDetailedList(Context context) {
        ArrayList<Bid> bidArrayList = new ArrayList<>();
        Bid[] allBids = new DataSourceManager(context).getBids();
        User currentUser = new DataSourceManager(context).getCurrentUser();

        if (allBids != null)
        {
            for (Bid bid : allBids)
            {
                if (bid.getProviderUsername().equals(currentUser.getUsername()))
                {
                    bidArrayList.add(bid);
                }
            }
        }

        return bidArrayList.toArray(new Bid[0]);
    }
}
