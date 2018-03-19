package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.content.Context;
import android.content.Intent;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Bid is a class for storing and managing information associated with a bid made by a user on
 * any task.
 * @author Mark Griffith
 * @version 1.0
 */
final class Bid  implements Detailable
{
    private final String providerId; // The id of the User who made the bid

    private final String taskId; // The title of the task bid on

    private final BigDecimal value; // The amount bidded

    /**
     * Constructor for creating a Bid object.
     * @param providerId The id of the User who provides the task bid on
     * @param taskId The title of the task bid on
     * @param value The amount bidded
     * @throws IllegalArgumentException if either providerId or taskId are empty or Bid is <= 0
     */
    Bid(final String providerId, final String taskId, final BigDecimal value)
            throws IllegalArgumentException
    {
        if (providerId.isEmpty())
        {
            throw new IllegalArgumentException("Provider ID cannot be an empty string");
        }

        if (taskId.isEmpty())
        {
            throw new IllegalArgumentException("Task ID cannot be an empty string");
        }

        if (value.compareTo(BigDecimal.ONE) < 0)
        {
            throw new IllegalArgumentException("Bid value must be greater than 0");
        }

        this.providerId = providerId;
        this.taskId = taskId;
        this.value = value;
    }

    /**
     * Get the id of the User that made the Bid
     * @return String representation of the User's id
     */
    final String getProviderId()
    {
        return this.providerId;
    }

    /**
     * Get the title of the Task being bid on
     * @return String representation of the Task's title
     */
    final String getTaskId()
    {
        return this.taskId;
    }

    /**
     * Get the amount that the User bid
     * @return BigDecimal value of the Bid amount
     */
    final BigDecimal getValue()
    {
        return this.value;
    }

    @Override
    public <T extends DetailActivity> void showDetail(Class<T> detailActivityClass, Context context)
    {
        ArrayList<Detail> detailList = new ArrayList<>();
        detailList.add(new Detail("Provider ID", getProviderId()));
        detailList.add(new Detail("Task ID", getTaskId()));
        detailList.add(new Detail("Value", getValue().toString()));

        Intent intent = new Intent(context, detailActivityClass);
        intent.putExtra(Detailable.DATA_DETAIL_LIST, detailList);
        intent.putExtra(Detailable.DATA_DETAIL_TITLE, "Bid");

        context.startActivity(intent);
    }

    /**
     * Provide a string to describe a bid
     * @return String representing the bid
     */
    @Override
    public String toString()
    {
        return this.getProviderId() + ": " + this.getValue().toString();
    }
}
