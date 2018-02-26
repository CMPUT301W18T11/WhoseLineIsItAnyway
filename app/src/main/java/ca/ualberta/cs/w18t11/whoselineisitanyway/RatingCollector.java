package ca.ualberta.cs.w18t11.whoselineisitanyway;

import java.util.ArrayList;

/**
 * RatingCollector is designed to hold and manage overall ratings for any particular user
 * @author Lucas Thalen and Samuel Dolha
 * @version 0.1
 * @see Rating
 */
final class RatingCollector
{
    private int avgRating = 0;                              // Holds the overall average agg. rating

    private int avgQuality = 0;                             // Holds overall avg quality rating

    private int avgProf = 0;                                // Holds overall avg Professionalism

    private int avgTTC = 0;                                 // overall avg time-to-completion rating

    private final ArrayList<Rating> ratings = new ArrayList<>();

    /**
     * Update Averages across all reviews
     */
    private void updateAvg()
    {
        double reviewSum = 0.0;
        double avgQuality = 0.0;
        double avgTTC = 0.0;
        double avgProf = 0.0;

        for (int index = 0; index < ratings.size(); index ++) {
            reviewSum += ratings.get(index).getAggRating();
            avgQuality += ratings.get(index).getQualityRating();
            avgTTC += ratings.get(index).getTtcRating();
            avgProf += ratings.get(index).getProfRating();
        }

        reviewSum /= ratings.size();
        this.avgRating = (int) reviewSum;
        this.avgQuality = (int) (avgQuality / ratings.size());
        this.avgTTC = (int) (avgTTC / ratings.size());
        this.avgProf = (int) (avgProf / ratings.size());
    }

    /**
     * Adds a rating to the overall collection for management
     * @param rate A rating object to be added to the collection
     */
    final void addRating(final Rating rate)
    {
        this.ratings.add(rate);
        this.update();
    }

    /**
     * Get a rating from the collection
     * @param index index of the rating; this should correspond with the listview likely to display
     * @return Rating at specific index
     */
    final Rating getRating(final int index)
    {
        return this.ratings.get(index);
    }

    private void update()
    {
        this.getAvgProf();
        this.getAvgQuality();
        this.getAvgTtc();
        this.updateAvg();
    }

    /**
     * Get the overall average Time-to-completion value
     * @return TTC_average
     */
    final int getAvgTtc()
    {
        double reviewSum = 0.0;

        for (int index = 0; index < this.ratings.size(); index += 1)
        {
            reviewSum += this.ratings.get(index).getTtcRating();
        }

        reviewSum /= this.ratings.size();
        this.avgTTC = (int) reviewSum;

        return this.avgTTC;
    }

    /**
     * Get the average professionalism rating over all entries
     * @return The average professionalism rating across all entries
     */
    final int getAvgProf()
    {
        double reviewSum = 0.0;

        for (int index = 0; index < this.ratings.size(); index += 1)
        {
            reviewSum += this.ratings.get(index).getProfRating();
        }

        reviewSum /= this.ratings.size();
        this.avgProf = (int) reviewSum;

        return this.avgProf;
    }

    /**
     * Get the average quality across all entries
     * @return the average quality rating of all entries
     */
    final int getAvgQuality()
    {
        double reviewSum = 0.0;

        for (int index = 0; index < this.ratings.size(); index += 1)
        {
            reviewSum += this.ratings.get(index).getQualityRating();
        }

        reviewSum /= this.ratings.size();
        this.avgQuality = (int) reviewSum;

        return this.avgQuality;
    }

    final int getRatingCount()
    {
        return this.ratings.size();
    }
}
