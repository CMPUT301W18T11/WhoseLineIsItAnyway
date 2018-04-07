package ca.ualberta.cs.w18t11.whoselineisitanyway.model.rating;

import java.util.ArrayList;

/**
 * RatingCollector is designed to hold and manage overall ratings for any particular user
 *
 * @author Lucas Thalen, Samuel Dolha
 * @version 1.0
 * @see Rating
 */
public final class RatingCollector
{
    private final ArrayList<Rating> ratings = new ArrayList<>();
    private int avgRating = 0;                              // Holds the overall average agg. rating
    private int avgQuality = 0;                             // Holds overall avg quality rating
    private int avgProf = 0;                                // Holds overall avg Professionalism
    private int avgTTC = 0;                                 // overall avg time-to-completion rating

    /**
     * Update Averages across all reviews
     */
    private void updateAvg()
    {
        double reviewSum = 0.0;
        double avgQuality = 0.0;
        double avgTTC = 0.0;
        double avgProf = 0.0;

        for (int index = 0; index < ratings.size(); index++)
        {
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
     *
     * @param rate A rating object to be added to the collection
     */
    public final void addRating(final Rating rate)
    {
        this.ratings.add(rate);
        this.update();
    }

    /**
     * Get a rating from the collection
     *
     * @param index index of the rating; this should correspond with the listview likely to display
     * @return Rating at specific index
     */
    public final Rating getRating(final int index)
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
     *
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
     *
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
     *
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

    /**
     * Output the grand summary of reviews in collector
     *
     * @return String in the format of
     * <p>
     * Summary | (#) Reviews
     * =====================
     * Rating1 ***** (# of stars)
     * etc.
     */
    final public String toString()
    {
        int JUSTIFY_LEN = 18;
        if (ratings.isEmpty() != true) {
            StringBuilder strCreate = new StringBuilder();
            strCreate.append(
                    "SUMMARY | (" + String.valueOf(getRatingCount()) + ") Reviews" + "\n" +
                            "===========================\n"
            );
            strCreate.append(
                    String.format("%-" + String.valueOf(JUSTIFY_LEN) + "s", "Overall:") +
                            String.format("%-6s", new String(new char[avgRating]).replace("\0", "*")) +
                            "(" + String.valueOf(avgRating) + ")" +
                            "\n"
            );
            strCreate.append(
                    String.format("%-" + String.valueOf(JUSTIFY_LEN) + "s", "Quality:") +
                            String.format("%-6s", new String(new char[avgQuality]).replace("\0", "*")) +
                            "(" + String.valueOf(avgQuality) + ")" +
                            "\n"
            );
            strCreate.append(
                    String.format("%-" + String.valueOf(JUSTIFY_LEN) + "s", "Speed:") +
                            String.format("%-6s", new String(new char[avgTTC]).replace("\0", "*")) +
                            "(" + String.valueOf(avgTTC) + ")" +
                            "\n"
            );
            strCreate.append(
                    String.format("%-" + String.valueOf(JUSTIFY_LEN) + "s", "Professionalism:") +
                            String.format("%-6s", new String(new char[avgProf]).replace("\0", "*")) +
                            "(" + String.valueOf(avgProf) + ")" +
                            "\n"
            );

            return strCreate.toString();
        } else {
            return "SUMMARY (0 Reviews - Unrated User)";
        }
            
    }
    final public ArrayList<Rating> getRatingsList() {
        return ratings;
    }

    final public boolean empty() {
        return ratings.isEmpty();
    }

    //TODO Implement some sort of access to the arraylist for ListViews
}
