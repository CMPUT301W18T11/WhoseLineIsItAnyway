package ca.ualberta.cs.w18t11.whoselineisitanyway.model.rating;

/**
 * Rating is a generalized class to hold ratings data about a user's performance in fulfilling
 * tasks they have agreed to perform. A rating is comprised of 4 things:
 * <ul>
 * <li>An aggregate average of all categories</li>
 * <li>A TTC, or time-to-completion rating reflecting the speed of completion by provider</li>
 * <li>A prof, or professionalism rating of how courteous, etc. the provider was</li>
 * <li>A quality, or quality rating based on the workmanship of a task</li>
 * </ul>
 *
 * @author Lucas Thalen, Samuel Dolha
 * @version 1.0
 * @see RatingCollector
 */
public final class Rating
{
    private int ttcRating = 0;                        // Time to completion

    private int qualityRating = 0;                    // Quality of worksmanship

    private int profRating = 0;                       // Professionalism

    private int aggRating = 0;                        // Aggregate rating (all categories averaged)

    private String comment = "";

    /**
     * Constructor for a rating; this contains code to ensure all values in a range of 0-5 incl.
     *
     * @param qualityRating [0-5] A rating of the quality of service performed
     * @param ttcRating     [0-5] A rating of the speed of performance of that service
     * @param profRating    [0-5] A rating of the professionalism of conduct in performance thereof
     * @throws IllegalArgumentException Any of the ratings are outside of their designated bounds
     */
    public Rating(final int qualityRating, final int ttcRating, final int profRating)
            throws IllegalArgumentException
    {
        // Check ranges of all values, throw error if these are outside expectations
        if (!(ttcRating <= 5 && 0 <= ttcRating))
        {
            throw new IllegalArgumentException("Invalid range for rating: ttcRating");
        }
        if (!(qualityRating <= 5 && 0 <= qualityRating))
        {
            throw new IllegalArgumentException("Invalid range for rating: qualityRating");
        }
        if (!(profRating <= 5 && 0 <= profRating))
        {
            throw new IllegalArgumentException("Invalid range for rating: profRating");
        }

        // Set all values and update aggregate rating
        this.ttcRating = ttcRating;
        this.qualityRating = qualityRating;
        this.profRating = profRating;
        this.updateAggRating();
    }

    // Get and Set Methods for internal values

    /**
     * Get the quality rating for this particular review
     *
     * @return the integer value of a quality rating stored in this review
     */
    public final int getQualityRating()
    {
        return this.qualityRating;
    }

    /**
     * Set the quality rating; this function is intended to be called by GUI; check it's in bounds
     *
     * @param rating [0-5] the rating of the service performed
     * @throws IllegalArgumentException The provided rating is outside of the designated bounds
     */
    final void setQualityRating(final int rating) throws IllegalArgumentException
    {
        if (!(0 <= rating && rating <= 5))
        {
            throw new IllegalArgumentException("Invalid range for rating: qualityRating");
        }

        this.qualityRating = rating;
        this.updateAggRating();
    }

    /**
     * Get the current TTC rating for this particular review
     *
     * @return the time-to-completion rating for this particular review
     */
    public final int getTtcRating()
    {
        return this.ttcRating;
    }

    /**
     * Set the TTC ratingl this function is intended to be called by the GUI; checks boundaries
     *
     * @param rating [0-5] the speed of completion of the service performed
     * @throws IllegalArgumentException The provided rating is outside of the designated bounds
     */
    public final void setTtcRating(final int rating) throws IllegalArgumentException
    {
        if (!(0 <= rating && rating <= 5))
        {
            throw new IllegalArgumentException("Invalid range for rating: ttcRating");
        }

        this.ttcRating = rating;
        this.updateAggRating();
    }

    /**
     * Get the current professionalism rating for this review only
     *
     * @return the integer value of the professionalism rating for this review
     */
    public final int getProfRating()
    {
        return this.profRating;
    }

    /**
     * Set the professionalism rating for this review; intended to be called by the GUI; bounds check
     *
     * @param rating [0-5] the professionalism of the person performing the service
     */
    public final void setProfRating(final int rating) throws IllegalArgumentException
    {
        if (!(0 <= rating && rating <= 5))
        {
            throw new IllegalArgumentException("Invalid range for rating: profRating");
        }

        this.profRating = rating;
        this.updateAggRating();
    }

    /**
     * Get the aggregation of the review metrics as an average for this review only
     *
     * @return average of all metrics in a review
     */
    public final int getAggRating()
    {
        return this.aggRating;
    }

    // End get/set methods

    /**
     * This triggers an update to the review's values whenever they are adjusted by outside entities
     */
    private void updateAggRating()
    {
        double aggregate = 0.0;             // Hold values as a double due to average; round to int.
        aggregate += ttcRating;             // Average the three rating metrics
        aggregate += profRating;
        aggregate += qualityRating;

        this.aggRating = (int) (aggregate
                / 3);  // Cast rating to integer; rounded value precise enough
    }

    /**
     * Allows the program to set a review comment; these can be null if desired
     *
     * @param reviewText
     * @return
     */
    public final boolean setReviewMSG(final String reviewText)
    {
        if (reviewText.length() <= 200)
        {
            comment = reviewText;
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * This gets the truncated tweet-like form of the review comment for showing in the general list
     *
     * @return String max 30 chars or slightly larger if it allows a finished sentence
     */
    public final String toString()
    {
        int sentenceLen = 0;
        StringBuilder reviewConstruct = new StringBuilder();

        // https://stackoverflow.com/a/41762573/4914842
        // Sohan | Creative Commons 3.0 Attrib SA
        // Posted 01/20/2017 | Accessed 03/17/2018
        // Creating string of repeating chars using Java native-constructs
        reviewConstruct.append("Review: " + new String(new char[aggRating]).replace("\0", "*"));
        if (comment.contains("."))
        {
            sentenceLen = comment.indexOf(".");
        }

        reviewConstruct.append("\n");

        if (comment.length() > 30)
        {
            if (sentenceLen - 30 <= 6)
            {
                reviewConstruct.append(comment.substring(0, comment.indexOf(".")));
            }
            else
            {
                reviewConstruct.append(comment.substring(0, 30));
            }
        }
        else
        {
            reviewConstruct.append(comment);
        }

        return reviewConstruct.toString();

    }

    public final String fullReview()
    {
        StringBuilder strCreate = new StringBuilder();
        strCreate.append(
                justifyText("Quality:", 18) +
                        new String(new char[qualityRating]).replace("\0", "*") +
                        "\n"
        );
        strCreate.append(
                justifyText("Speed:", 18) +
                        new String(new char[ttcRating]).replace("\0", "*") +
                        "\n"
        );
        strCreate.append(
                justifyText("Professionalism:", 18) +
                        new String(new char[profRating]).replace("\0", "*") +
                        "\n"
        );
        strCreate.append(comment);

        return strCreate.toString();
    }

    /**
     * Generic justify method for string text; java has a formatter but it's not quite the flavour needed
     *
     * @param input String to justify
     * @param len   Len (with spaces) desired for string
     * @return string with spaces added to make it a fixed langth
     */
    private String justifyText(String input, int len)
    {
        int spaceCount = 0;
        String trimmedInput = input.trim();
        if (trimmedInput.length() > len)
        {
            return trimmedInput;
        }
        else
        {
            spaceCount = len - trimmedInput.length();
        }

        return trimmedInput + new String(new char[spaceCount]).replace("\0", " ");
    }
}
