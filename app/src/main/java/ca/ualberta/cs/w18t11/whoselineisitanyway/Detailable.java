package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.content.Intent;
import android.util.Pair;

import java.util.ArrayList;

public interface Detailable {
    /**
     * Get the a list containing the details about the object
     * @return ArrayList<Pair<String, String>> representation of the object's details
     * where the Pair contains (Detail title, Detail data)
     */
    ArrayList<Detail> getDetails();

    /**
     * View the details of the Detailable
     * Go to a new DetailActivity representing the object
     * @param detailActivity the type of activity to display the object's details
     * @return boolean representing if the object is represented by an image
     */
    boolean showDetail(DetailActivity detailActivity);
}
