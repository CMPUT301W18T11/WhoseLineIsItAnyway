package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.content.Intent;

import org.junit.Assert;
import org.junit.Test;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.Detail;

public class DetailTest
{
    @Test
    public final void testGetTitle()
    {
        final String title = "Detail";
        Assert.assertEquals(title, new Detail(title, "").getTitle());
    }

    @Test
    public final void testGetInfo()
    {
        final String info = "Info";
        Assert.assertEquals(info, new Detail("", info).getInfo());
    }

    @Test
    public final void testIsLinkable_notLinkable()
    {
        Assert.assertFalse(new Detail("", "").isLinkeable());
    }

    @Test
    public final void testIsLinkable_Linkable()
    {
        Intent i = new Intent();
        Assert.assertTrue(new Detail("", "", i).isLinkeable());
    }
}
