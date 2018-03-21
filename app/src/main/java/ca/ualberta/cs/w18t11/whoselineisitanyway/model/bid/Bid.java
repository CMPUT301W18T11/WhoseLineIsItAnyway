package ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detail;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailable;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.DetailActivity;

/**
 * Represents a bid made by a provider on a task.
 *
 * @author Mark Griffith, Samuel Dolha
 * @version 2.0
 */
public final class Bid implements Detailable, Serializable
{
    /**
     * An auto-generated, unique ID to support class versioning for Serializable.
     *
     * @see Serializable
     */
    private static final long serialVersionUID = 330240939337547492L;

    /**
     * The associated provider's ID.
     */
    @NonNull
    private final String providerId;

    /**
     * The associated task's ID.
     */
    @NonNull
    private final String taskId;

    /**
     * The bid's monetary value.
     */
    @NonNull
    private final BigDecimal value;

    /**
     * @param providerId The associated provider's ID.
     * @param taskId     The associated task's ID.
     * @param value      The bid's monetary value.
     * @throws IllegalArgumentException For an empty providerId or taskId, or a non-positive value.
     */
    public Bid(@NonNull final String providerId, @NonNull final String taskId,
               @NonNull final BigDecimal value)
            throws IllegalArgumentException
    {
        if (providerId.isEmpty())
        {
            throw new IllegalArgumentException("providerId cannot be empty");
        }

        if (taskId.isEmpty())
        {
            throw new IllegalArgumentException("taskId cannot be empty");
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("value must be positive");
        }

        this.providerId = providerId;
        this.taskId = taskId;
        this.value = value;
    }

    /**
     * @return The associated provider's ID.
     */
    @NonNull
    public final String getProviderId()
    {
        return this.providerId;
    }

    /**
     * @return The associated task's ID.
     */
    @NonNull
    public final String getTaskId()
    {
        return this.taskId;
    }

    /**
     * @return The bid's monetary value.
     */
    @NonNull
    public final BigDecimal getValue()
    {
        return this.value;
    }

    @Override
    public final <T extends DetailActivity> void showDetail(
            @NonNull final Class<T> detailActivityClass, @NonNull final Context context)
    {
        ArrayList<Detail> detailList = new ArrayList<>();
        detailList.add(new Detail("Provider ID", getProviderId(), null));
        detailList.add(new Detail("Task ID", getTaskId(), null));
        detailList.add(new Detail("Value", getValue().toString(), null));

        Intent intent = new Intent(context, detailActivityClass);
        intent.putExtra(Detailable.DATA_DETAIL_LIST, detailList);
        intent.putExtra(Detailable.DATA_DETAIL_TITLE, "Bid");

        context.startActivity(intent);
    }

    /**
     * Provide a string to describe a bid
     *
     * @return String representing the bid
     */
    @NonNull
    @Override
    public final String toString()
    {
        return this.getProviderId() + ": " + this.getValue().toString();
    }
}
