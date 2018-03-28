package ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detail;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.elastic.Elastic;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.DetailActivity;

/**
 * Represents a bid.
 *
 * @author Mark Griffith, Samuel Dolha
 * @version 3.0
 */
public final class Bid implements Detailed, Elastic, Serializable
{
    /**
     * An auto-generated, unique ID to support class versioning for Serializable.
     *
     * @see Serializable
     */
    private static final long serialVersionUID = 330240939337547492L;

    /**
     * The bid's elastic ID.
     */
    @Nullable
    private String elasticId;

    /**
     * The associated provider's username.
     */
    @NonNull
    private final String providerUsername;

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
     * Creates a bid without an elastic ID.
     *
     * @param providerUsername The associated provider's ID.
     * @param taskId     The associated task's ID.
     * @param value      The bid's monetary value.
     * @throws IllegalArgumentException For an empty providerUsername or taskId, or a non-positive value.
     */
    public Bid(@NonNull final String providerUsername, @NonNull final String taskId,
               @NonNull final BigDecimal value)
    {
        if (providerUsername.isEmpty())
        {
            throw new IllegalArgumentException("providerUsername cannot be empty");
        }

        if (taskId.isEmpty())
        {
            throw new IllegalArgumentException("taskId cannot be empty");
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("value must be positive");
        }

        this.providerUsername = providerUsername;
        this.taskId = taskId;
        this.value = value;
    }

    /**
     * Creates a bid with an elastic ID.
     *
     * @param elasticId  The bid's elastic ID.
     * @param providerUsername The associated provider's ID.
     * @param taskId     The associated task's ID.
     * @param value      The bid's monetary value.
     * @throws IllegalArgumentException For an empty providerUsername or taskId, or a non-positive value.
     */
    public Bid(@NonNull final String elasticId, @NonNull final String providerUsername,
               @NonNull final String taskId, @NonNull final BigDecimal value)
    {
        if (elasticId.isEmpty())
        {
            throw new IllegalArgumentException("elasticId cannot be empty");
        }

        if (providerUsername.isEmpty())
        {
            throw new IllegalArgumentException("providerUsername cannot be empty");
        }

        if (taskId.isEmpty())
        {
            throw new IllegalArgumentException("taskId cannot be empty");
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("value must be positive");
        }

        this.elasticId = elasticId;
        this.providerUsername = providerUsername;
        this.taskId = taskId;
        this.value = value;
    }

    /**
     * @return The associated provider's ID.
     */
    @NonNull
    public final String getProviderUsername()
    {
        return this.providerUsername;
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
    public final <T extends DetailActivity> void showDetails(
            @NonNull final Class<T> detailActivityClass, @NonNull final Context context)
    {
        final Intent intent = new Intent(context, detailActivityClass);
        intent.putExtra(Detailed.DETAILS_KEY, (ArrayList<Detail>) Arrays
                .asList(new Detail("providerUsername", this.getProviderUsername(), null),
                        new Detail("taskId", this.getTaskId(), null),
                        new Detail("value", this.getValue().toString(), null)));
        intent.putExtra(Detailed.TITLE_KEY, "Bid");

        context.startActivity(intent);
    }

    /**
     * @return The bid's elastic ID.
     * @see Elastic
     */
    @Nullable
    @Override
    public final String getElasticId()
    {
        return this.elasticId;
    }

    /**
     * Set the bid's elastic ID.
     *
     * @param id The bid's new elastic ID.
     * @see Elastic
     */
    @Override
    public final void setElasticId(@NonNull final String id)
    {
        if (id.isEmpty())
        {
            throw new IllegalArgumentException("id cannot be empty");
        }

        this.elasticId = id;
    }

    /**
     * @return A hashcode of the bid.
     * @see Object
     */
    @Override
    public final int hashCode()
    {
        return Objects.hash(this.getProviderUsername(), this.getTaskId(), this.getValue());
    }

    /**
     * @param object The object with which to compare the bid.
     * @return Whether the object represents the same bid.
     * @see Object
     */
    @Override
    public final boolean equals(@Nullable final Object object)
    {
        if (!(object instanceof Bid))
        {
            return false;
        }

        if (object == this)
        {
            return true;
        }

        final Bid bid = (Bid) object;

        return new EqualsBuilder().append(this.getProviderUsername(), bid.getProviderUsername())
                .append(this.getTaskId(), bid.getTaskId()).append(this.getValue(), bid.getValue())
                .isEquals();
    }
}
