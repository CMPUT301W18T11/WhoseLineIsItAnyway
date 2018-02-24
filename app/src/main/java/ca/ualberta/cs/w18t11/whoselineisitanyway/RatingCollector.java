package ca.ualberta.cs.w18t11.whoselineisitanyway;

/**
 * RatingCollector is designed to hold and manage overall ratings for any particular user
 * @author Lucas Thalen
 * @version 0.1
 * @see Rating
 */

import java.util.ArrayList;

public class RatingCollector {
    private int avgRating = 0;                              // Holds the overall average agg. rating
    private int avgQuality = 0;                             // Holds overall avg quality rating
    private int avgProf = 0;                                // Holds overall avg Professionalism
    private int avgTTC = 0;                                 // overall avg time-to-completion rating

    private ArrayList<Rating> ratings = new ArrayList<Rating>();

    private void updateAvg() {
        double reviewSum = 0.0;
        for (int i = 0; i < ratings.size(); i ++) {
            reviewSum += ratings.get(i).getAggRating();
        }
        reviewSum = reviewSum / ratings.size();
        avgRating = (int) reviewSum;
    }
    public void addRating(Rating rate) {
        ratings.add(rate);
        update();
    }
    private void update() {
        getAvgProf();
        getAvgQuality();
        getAvgTTC();
        updateAvg();
    }
    public void getAvgTTC() {
        double reviewSum = 0.0;
        for (int i = 0; i < ratings.size(); i ++) {
            reviewSum += ratings.get(i).getTTCRating();
        }
        reviewSum = reviewSum / ratings.size();
        avgTTC = (int) reviewSum;
    }

    public void getAvgProf() {
        double reviewSum = 0.0;
        for (int i = 0; i < ratings.size(); i ++) {
            reviewSum += ratings.get(i).getProfRating();
        }
        reviewSum = reviewSum / ratings.size();
        avgProf = (int) reviewSum;
    }

    public void getAvgQuality() {
        double reviewSum = 0.0;
        for (int i = 0; i < ratings.size(); i ++) {
            reviewSum += ratings.get(i).getQualityRating();
        }
        reviewSum = reviewSum / ratings.size();
        avgQuality = (int) reviewSum;
    }

    public int reviewCount() {
        return ratings.size();
    }

}
