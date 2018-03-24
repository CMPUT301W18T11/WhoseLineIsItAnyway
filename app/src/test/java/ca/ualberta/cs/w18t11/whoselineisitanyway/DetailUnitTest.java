package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.content.Intent;

import org.junit.Assert;
import org.junit.Test;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detail;

public final class DetailUnitTest
{
    @Test
    public final void testGetTitle()
    {
        final String title = "title";
        Assert.assertEquals(title, new Detail(title, "", null).getTitle());
    }

    @Test
    public final void testGetInfo()
    {
        final String information = "information";
        Assert.assertEquals(information, new Detail("", information, null).getInformation());
    }

    @Test
    public final void testIsNotLinked()
    {
        Assert.assertFalse(new Detail("", "", null).isLinked());
    }
}
