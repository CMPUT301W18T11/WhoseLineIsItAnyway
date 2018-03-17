package ca.ualberta.cs.w18t11.whoselineisitanyway;


import android.content.Intent;

public class Detail {
    private final String title;
    private final String info;
    private final Intent linkIntent;
    Detail(String title, String info, Intent linkIntent)
    {
        this.title = title;
        this.info = info;
        this.linkIntent = linkIntent;
    }

    Detail(String title, String info)
    {
        this(title, info, null);
    }

    String getTitle() {return this.title;}

    String getInfo() {return this.info;}

    boolean isLinkeable(){return this.linkIntent != null;}
}
