package ca.ualberta.cs.w18t11.whoselineisitanyway.model;


import android.content.Intent;

import java.io.Serializable;

public class Detail implements Serializable
{
    private final String title;
    private final String info;
    private final Intent linkIntent;

    public Detail(String title, String info, Intent linkIntent)
    {
        this.title = title;
        this.info = info;
        this.linkIntent = linkIntent;
    }

    public Detail(String title, String info)
    {
        this(title, info, null);
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getInfo()
    {
        return this.info;
    }

    public boolean isLinkeable()
    {
        return this.linkIntent != null;
    }
}
