package ca.ualberta.cs.w18t11.whoselineisitanyway.model;

import android.content.Context;

import ca.ualberta.cs.w18t11.whoselineisitanyway.view.DetailActivity;

public interface Detailable
{
    String DATA_DETAIL_TITLE = "com.whoselineisitanyway.DATA_DETAIL_TITLE";
    String DATA_DETAIL_LIST = "com.whoselineisitanyway.DATA_DETAIL_LIST";
    String DATA_DETAIL_IMAGE = "com.whoselineisitanyway.DATA_DETAIL_IMAGE";

    /**
     * View the details of the Detailable
     * Go to a new DetailActivity representing the object
     *
     * @param detailActivityClass the type of activity to display the object's details
     * @param context             the context from which the detailActivity will be launched
     */
    <T extends DetailActivity> void showDetail(Class<T> detailActivityClass, Context context);
}
