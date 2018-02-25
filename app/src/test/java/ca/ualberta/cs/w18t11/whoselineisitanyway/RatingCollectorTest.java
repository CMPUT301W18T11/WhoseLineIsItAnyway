package ca.ualberta.cs.w18t11.whoselineisitanyway;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by lucas on 2018-02-24.
 */

public class RatingCollectorTest {

    @Test
    public void addRating() throws Exception {
        RatingCollector coll = new RatingCollector();
        Rating rate = new Rating(1,2,3);
        coll.addRating(rate);
        assertEquals(coll.getRating(0), rate);
    }

    @Test
    public void getAvgTTC() throws Exception {
        Random posInt = new Random();
        RatingCollector coll = new RatingCollector();

        int ttcSum = 0;
        for (int i = 0; i < 50; i ++) {
            int p1 = posInt.nextInt(5);
            int p2 = posInt.nextInt(5);
            ttcSum += p2;
            int p3 = posInt.nextInt(5);

            coll.addRating(new Rating(p1, p2, p3));
        }
        int avg = (int) (ttcSum / coll.getRatingCount());
        assertEquals(coll.getAvgTTC(), avg);
    }

    @Test
    public void getAvgProf() throws Exception {
        Random posInt = new Random();
        RatingCollector coll = new RatingCollector();

        int profSum = 0;
        for (int i = 0; i < 50; i ++) {
            int p1 = posInt.nextInt(5);
            int p2 = posInt.nextInt(5);
            int p3 = posInt.nextInt(5);
            profSum += p3;

            coll.addRating(new Rating(p1, p2, p3));
        }
        int avg = (int) (profSum / coll.getRatingCount());
        assertEquals(coll.getAvgProf(), avg);
    }

    @Test
    public void getAvgQuality() throws Exception {
        Random posInt = new Random();
        RatingCollector coll = new RatingCollector();

        int qualitySum = 0;
        for (int i = 0; i < 50; i ++) {
            int p1 = posInt.nextInt(5);
            int p2 = posInt.nextInt(5);
            qualitySum += p1;
            int p3 = posInt.nextInt(5);

            coll.addRating(new Rating(p1, p2, p3));
        }
        int avg = (int) (qualitySum / coll.getRatingCount());
        assertEquals(coll.getAvgQuality(), avg);
    }

}