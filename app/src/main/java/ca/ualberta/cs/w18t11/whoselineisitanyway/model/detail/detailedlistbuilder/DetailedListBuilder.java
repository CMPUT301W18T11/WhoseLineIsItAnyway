package ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.detailedlistbuilder;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;

/**
 * A class to construct a list of objects that implement the 'Detailed' interface
 * @author Brad Ofrim
 * @see Detailed
 */

public abstract class DetailedListBuilder implements Serializable {

    /**
     * Build a list of 'Detailed' objects
     * @return ArrayList<Detailed> of 'Detailed' objects
     */
    @NonNull
    abstract Detailed[] buildDetailedList(Context context);
}
