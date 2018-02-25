package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Assert;
import org.junit.Test;
import java.util.Random;

 /** RatingTest is the Junit Testing class for <b>Rating</b>
   * @author Lucas Thalen
   * @version 0.1
   * @see Rating
*/
public class RatingTest {

    /**
     * Test to see if negative values (Expected OOB) throws exception when creating new Rating
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void newRatingNegativeParam() throws Exception {

        Random randomNegative = new Random();
        int failInt = -1 * randomNegative.nextInt(Integer.MIN_VALUE);
        Assert.assertTrue(failInt < 0);
        Rating rate = new Rating(failInt, failInt, failInt);
    }

    /**
     * Test to see if positive values > 5 (Expected OOB) throws exception when creating new Rating
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void newRatingPositiveOOB () throws IllegalArgumentException {
        Random randomPos = new Random();
        int failInt = randomPos.nextInt(Integer.MAX_VALUE);
        while (failInt < 5) {
            failInt = randomPos.nextInt(Integer.MAX_VALUE);
        }
        Assert.assertTrue(failInt > 5);
        Rating rate = new Rating(failInt, failInt, failInt);
    }

    /**
     * Test to see if positive values [0-5] (Expected Inbounds) successfully creates new Rating
     * @throws Exception
     */
    @Test
    public void newRatingZeroFive() {
        Random randomPos = new Random();
        for (int i = 0; i < 100; i ++) {
            int p1 = randomPos.nextInt(5);
            int p2 = randomPos.nextInt(5);
            int p3 = randomPos.nextInt(5);
            Assert.assertTrue(0 <= p1 && p1 <= 5);
            Assert.assertTrue(0 <= p2 && p2 <= 5);
            Assert.assertTrue(0 <= p3 && p3 <= 5);
            Rating rate = new Rating(p1, p2, p3);
        }
    }

    // TTC Stuff
    @Test
    public void getTTC() {
        Random randomPos = new Random();
        for (int i = 0; i < 100; i ++) {
            int ttc = randomPos.nextInt(5);
            Rating rate1 = new Rating(0, ttc,0);
            Assert.assertEquals(rate1.getTTCRating(), ttc);
        }
    }
    @Test
    public void setTTC() {
        Random posInt = new Random();
        Rating rate1 = new Rating(0, 0, 0);
        Assert.assertEquals(rate1.getTTCRating(), 0);
        for (int i = 0; i < 100; i ++) {
            int ttc = posInt.nextInt(5);
            rate1.setTTCRating(ttc);
            Assert.assertEquals(rate1.getTTCRating(), ttc);
        }
    }

    // QUALITY STUFF
    @Test
    public void getQuality() {
        Random randomPos = new Random();
        for (int i = 0; i < 100; i ++) {
            int quality = randomPos.nextInt(5);
            Rating rate1 = new Rating(quality, 0,0);
            Assert.assertEquals(rate1.getQualityRating(), quality);
        }
    }
    @Test
    public void setQuality() {
        Random posInt = new Random();
        Rating rate1 = new Rating(0, 0, 0);
        Assert.assertEquals(rate1.getQualityRating(), 0);
        for (int i = 0; i < 100; i ++) {
            int quality = posInt.nextInt(5);
            rate1.setQualityRating(quality);
            Assert.assertEquals(rate1.getQualityRating(), quality);
        }
    }

    // PROF STUFF
    @Test
    public void getProf() {
        Random randomPos = new Random();
        for (int i = 0; i < 100; i ++) {
            int prof = randomPos.nextInt(5);
            Rating rate1 = new Rating(0, 0,prof);
            Assert.assertEquals(rate1.getProfRating(), prof);
        }
    }
    @Test
    public void setProf() {
        Random posInt = new Random();
        Rating rate1 = new Rating(0, 0, 0);
        Assert.assertEquals(rate1.getProfRating(), 0);
        for (int i = 0; i < 100; i ++) {
            int prof = posInt.nextInt(5);
            rate1.setProfRating(prof);
            Assert.assertEquals(rate1.getProfRating(), prof);
        }
    }

    @Test
    public void getAggregateRating() {
        Random posInt = new Random();
        for (int i = 0; i < 100; i ++) {
            int p1 = posInt.nextInt(5);
            int p2 = posInt.nextInt(5);
            int p3 = posInt.nextInt(5);
            Rating rate1 = new Rating(p1, p2, p3);
            Assert.assertEquals(rate1.getAggRating(), (int)((p1 + p2 + p3) / 3));
        }
    }

}