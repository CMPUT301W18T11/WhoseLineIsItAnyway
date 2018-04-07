package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Assert;
import org.junit.Test;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.rating.Rating;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.rating.RatingCollector;

public final class RatingCollectorUnitTest
{
    @Test
    public final void testAddRating()
    {
        final RatingCollector coll = new RatingCollector();
        final Rating rate = new Rating(1, 2, 3);
        coll.addRating(rate);
        Assert.assertEquals(coll.getRating(0), rate);
    }

    @Test
    // This tests that the short summary matches expectations - the replace() stuff is to avoid issues with lineterminators
    // because I have no clue what is being used for those and if they're different it will fire, even if output is the same.
    public final void testShortSummary() {
        String expectedRes = "SUMMARY | (3) Reviews\n===========================\nOverall:          **" +
                "    (2)\nQuality:          **    (2)\nSpeed:            ***   (3)\nProfessionalism:  ***   (3)";
        expectedRes = expectedRes.replace("\n","").replace("\r","");
        Rating r = new Rating(1, 4, 3);
        Rating r2 = new Rating(1, 1, 2);
        Rating r3 = new Rating(4,4,4);

        RatingCollector rtc = new RatingCollector();
        rtc.addRating(r);
        rtc.addRating(r2);
        rtc.addRating(r3);

        Assert.assertEquals("RatingColl ShortSummary Output Test", rtc.toString().replace("\n","")
                .replace("\r", ""), expectedRes);
        System.out.print(rtc.toString());
    }
    @Test
    // Test that the summary is not null output after ratings have been added
    public final void testShortNullSummary() {
        Rating r = new Rating(1, 4, 3);
        Rating r2 = new Rating(1, 1, 2);
        Rating r3 = new Rating(4,4,4);
        RatingCollector rtc = new RatingCollector();
        rtc.addRating(r);
        rtc.addRating(r2);
        rtc.addRating(r3);


        Assert.assertEquals("RatingColl ShortSummary Null Test", rtc.toString() != null, true);

    }

    @Test
    // Test that the short summary is correct when 0 reviews are available
    public final void testShortSummary0() {
        String expected = "SUMMARY (0 Reviews - Unrated User)";
        RatingCollector rtc = new RatingCollector();

        Assert.assertEquals("RTC Short Summary 0Rev:", rtc.toString(), expected);

    }
}