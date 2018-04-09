package ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.detailedlistbuilder;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;

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
    ArrayList<Detailed> buildDetailedList() {
        return null;
    }
}
