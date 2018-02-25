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

    /**
     * Update Averages across all reviews
     */
    private void updateAvg() {
        double reviewSum = 0.0;
        double avgQuality = 0.0;
        double avgTTC = 0.0;
        double avgProf = 0.0;

        for (int i = 0; i < ratings.size(); i ++) {
            reviewSum += ratings.get(i).getAggRating();
            avgQuality += ratings.get(i).getQualityRating();
            avgTTC += ratings.get(i).getTTCRating();
            avgProf += ratings.get(i).getProfRating();
        }
        reviewSum = reviewSum / ratings.size();
        avgRating = (int) reviewSum;
        this.avgQuality = (int) (avgQuality / ratings.size());
        this.avgTTC = (int) (avgTTC / ratings.size());
        this.avgProf = (int) (avgProf / ratings.size());

    }

    /**
     * Adds a rating to the overall collection for management
     * @param rate A rating object to be added to the collection
     */
    public void addRating(Rating rate) {
        ratings.add(rate);
        update();
    }

    /**
     * Get a rating from the collection
     * @param index index of the rating; this should correspond with the listview likely to display
     * @return Rating at specific index
     */
    public Rating getRating(int index) {
        return ratings.get(index);
    }
    private void update() {
        getAvgProf();
        getAvgQuality();
        getAvgTTC();
        updateAvg();
    }

    /**
     * Get the overall average Time-to-completion value
     * @return TTC_average
     */
    public int getAvgTTC() {
        double reviewSum = 0.0;
        for (int i = 0; i < ratings.size(); i ++) {
            reviewSum += ratings.get(i).getTTCRating();
        }
        reviewSum = reviewSum / ratings.size();
        avgTTC = (int) reviewSum;
        return avgTTC;
    }

    /**
     * Get the average professionalism rating over all entries
     * @return The average professionalism rating across all entries
     */
    public int getAvgProf() {
        double reviewSum = 0.0;
        for (int i = 0; i < ratings.size(); i ++) {
            reviewSum += ratings.get(i).getProfRating();
        }
        reviewSum = reviewSum / ratings.size();
        avgProf = (int) reviewSum;
        return avgProf;
    }

    /**
     * Get the average quality across all entries
     * @return the average quality rating of all entries
     */
    public int getAvgQuality() {
        double reviewSum = 0.0;
        for (int i = 0; i < ratings.size(); i ++) {
            reviewSum += ratings.get(i).getQualityRating();
        }
        reviewSum = reviewSum / ratings.size();
        avgQuality = (int) reviewSum;
        return avgQuality;
    }

    public int getRatingCount() {
        return ratings.size();
    }

}
