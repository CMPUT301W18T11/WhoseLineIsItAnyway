package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.content.Intent;
import android.util.Pair;

import java.util.ArrayList;

public interface Detailable {
    String DATA_DETAIL_TITLE = "com.whoselineisitanyway.DATA_DETAIL_TITLE";
    String DATA_DETAIL_LIST = "com.whoselineisitanyway.DATA_DETAIL_LIST";
    String DATA_DETAIL_IMAGE = "com.whoselineisitanyway.DATA_DETAIL_IMAGE";

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
