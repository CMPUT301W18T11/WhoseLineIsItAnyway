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

    //TODO Add test for text output for this function in its ToString()
    //TODO add test for string when empty
}