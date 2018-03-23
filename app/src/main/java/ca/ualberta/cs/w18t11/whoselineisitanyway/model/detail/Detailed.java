package ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail;

import android.content.Context;
import android.support.annotation.NonNull;

import ca.ualberta.cs.w18t11.whoselineisitanyway.view.DetailActivity;

/**
 * Represents that which can be displayed in detail.
 *
 * @author Brad Ofrim, Samuel Dolha
 * @version 2.0
 */
public interface Detailed
{
    /**
     * The unique key for the title.
     */
    String TITLE_KEY = "DETAILED_TITLE_KEY";

    /**
     * The unique key for the details.
     */
    String DETAILS_KEY = "DETAILED_DETAILS_KEY";

    /**
     * Shows the details in an activity.
     *
     * @param detailActivityClass The activity in which to display the details.
     * @param context             The context in which to start the activity.
     */
    <T extends DetailActivity> void showDetails(@NonNull final Class<T> detailActivityClass,
                                                @NonNull final Context context);
}
