package ca.ualberta.cs.w18t11.whoselineisitanyway.model;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Represents a detail for something that is detialable.
 *
 * @author Brad Ofrim
 * @version 1.0
 */
public class Detail implements Serializable
{
    /**
     * Title to to describe the detail
     */
    @NonNull
    private final String title;

    /**
     * Information associated with the detail.
     */
    @NonNull
    private final String info;

    /**
     * Intent to use if the detail links to other activities
     */
    @Nullable
    private final Intent linkIntent;

    /**
     * Creates a detail.
     *
     * @param title String representing the detail title
     * @param info String representing the detail's information
     * @param linkIntent Intent to be used to show to another activity
     *
     * @see Detailable
     */
    public Detail(String title, String info, Intent linkIntent)
    {
        this.title = title;
        this.info = info;
        this.linkIntent = linkIntent;
    }

    /**
     * Create a detial without linked intent
     * @param title String representing the detail title
     * @param info String representing the detail's information
     *
     * @see Detailable
     */
    public Detail(String title, String info)
    {
        this(title, info, null);
    }

    /**
     * @return String representing the title of the detail
     */
    public String getTitle()
    {
        return this.title;
    }

    /**
     * @return String representing the detail's information
     */
    public String getInfo()
    {
        return this.info;
    }

    /**
     * @return Boolean representing if the detail has a link or not
     */
    public boolean isLinkeable()
    {
        return this.linkIntent != null;
    }
}
