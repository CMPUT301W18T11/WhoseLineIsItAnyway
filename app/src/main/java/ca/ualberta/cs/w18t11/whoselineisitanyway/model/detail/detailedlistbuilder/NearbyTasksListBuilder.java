package ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.detailedlistbuilder;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSourceManager;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;

/**
 * A class to construct a list of nearby tasks.
 * @author Brad Ofrim
 * @see Detailed
 */

public class NearbyTasksListBuilder extends DetailedListBuilder {

    /**
     * Build a list of tasks near to the current user.
     * @return ArrayList<Detailed> of all of all nearby tasks.
     */
    @NonNull
    @Override
    ArrayList<Detailed> buildDetailedList(Context context) {
        // TODO: Extract filtering to the DataSourceManager
        ArrayList<Detailed> detailedArrayList = new ArrayList<>();
        Detailed[] allTasks = new DataSourceManager(context).getTasks();

        // TODO: Add tasks based on distance (and status?)

        return detailedArrayList;
    }
}


