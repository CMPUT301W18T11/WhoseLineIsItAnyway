package ca.ualberta.cs.w18t11.whoselineisitanyway;

/**
 * Rating is a generalized class to hold ratings data about a user's performance in fulfilling
 * tasks they have agreed to perform. A rating is comprised of 4 things:
 * <ul>
 *     <li>An aggregate average of all categories</li>
 *     <li>A TTC, or time-to-completion rating reflecting the speed of completion by provider</li>
 *     <li>A prof, or professionalism rating of how courteous, etc. the provider was</li>
 *     <li>A quality, or quality rating based on the workmanship of a task</li>
 * </ul>
 * @author Lucas Thalen
 * @version 0.1
 * @see RatingCollector
 */

public class Rating {
    private int ttcRating = 0;                        // Time to completion
    private int qualityRating = 0;                    // Quality of worksmanship
    private int profRating = 0;                       // Professionalism
    private int aggRating = 0;                        // Aggregate rating (all categories averaged)


    /**
     * Constructor for a rating; this contains code to ensure all values in a range of 0-5 incl.
     * @param qualityRating [0-5] A rating of the quality of service performed
     * @param ttcRating [0-5] A rating of the speed of performance of that service
     * @param profRating [0-5] A rating of the professionalism of conduct in performance thereof
     * @throws IllegalArgumentException
     */
    public Rating(int qualityRating, int ttcRating, int profRating) throws IllegalArgumentException {
        // Check ranges of all values, throw error if these are outside expectations
        if (!(ttcRating <= 5 && 0 <= ttcRating)) {
            throw new IllegalArgumentException("Invalid range for rating: ttcRating");
        }
        if (!(qualityRating <= 5 && 0 <= qualityRating)) {
            throw new IllegalArgumentException("Invalid range for rating: qualityRating");
        }
        if (!(profRating <= 5 && 0 <= profRating)) {
            throw new IllegalArgumentException("Invalid range for rating: profRating");
        }

        // Set all values and update aggregate rating
        this.ttcRating = ttcRating;
        this.qualityRating = qualityRating;
        this.profRating = profRating;
        updateAggRating();
    }

    // Get and Set Methods for internal values

    /**
     * Set the quality rating; this function is intended to be called by GUI; check it's in bounds
     * @param quality [0-5] the quality of the service performed
     * @throws RuntimeException
     */
    public void setQualityRating(int quality) throws RuntimeException {
        if (!(0 <= quality && quality <= 5)) {
            throw new RuntimeException("Invalid range for rating: qualityRating");
        }
        qualityRating = quality;
        updateAggRating();
    }

    /**
     * Get the quality rating for this particular review
     * @return the integer value of a quality rating stored in this review
     */
    public int getQualityRating() {
        return qualityRating;
    }

    /**
     * Set the TTC ratingl this function is intended to be called by the GUI; checks boundaries
     * @param ttc [0-5] the speed of completion of the service performed
     */
    public void setTTCRating(int ttc) {
        if (!(0 <= ttc && ttc <= 5)) {
            throw new RuntimeException("Invalid range for rating: ttcRating");
        }
        ttcRating = ttc;
        updateAggRating();
    }

    /**
     * Get the current TTC rating for this particular review
     * @return the time-to-completion rating for this particular review
     */
    public int getTTCRating() {
        return ttcRating;
    }

    /**
     * Set the professionalism rating for this review; intended to be called by the GUI; bounds check
     * @param prof [0-5] the professionalism of the person performing the service
     */
    public void setProfRating(int prof) {
        if (!(0 <= prof && prof <= 5)) {
            throw new RuntimeException("Invalid range for rating: profRating");
        }
        profRating = prof;
        updateAggRating();
    }

    /**
     * Get the current professionalism rating for this review only
     * @return the integer value of the professionalism rating for this review
     */
    public int getProfRating() {
        return profRating;
    }

    /**
     * Get the aggregation of the review metrics as an average for this review only
     * @return average of all metrics in a review
     */
    public int getAggRating() {
        return aggRating;
    }
    // End get/set methods

    /**
     * This triggers an update to the review's values whenever they are adjusted by outside entities
     */
    public void updateAggRating() {
        double aggregate = 0.0;             // Hold values as a double due to average; round to int.
        aggregate += ttcRating;             // Average the three rating metrics
        aggregate += profRating;
        aggregate += qualityRating;

        aggRating = (int) (aggregate / 3);  // Cast rating to integer; rounded value precise enough
    }


}
