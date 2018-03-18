package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.content.Intent;
import android.util.Pair;

import java.util.ArrayList;

public interface Detailable {
    String DATA_DETAIL_TITLE = "com.whoselineisitanyway.DATA_DETAIL_TITLE";
    String DATA_DETAIL_LIST = "com.whoselineisitanyway.DATA_DETAIL_LIST";
    String DATA_DETAIL_IMAGE = "com.whoselineisitanyway.DATA_DETAIL_IMAGE";

    /**
     * View the details of the Detailable
     * Go to a new DetailActivity representing the object
     * @param detailActivity the type of activity to display the object's details
     */
    <T extends DetailActivity> void showDetail(Class<T> detailActivity);
}
