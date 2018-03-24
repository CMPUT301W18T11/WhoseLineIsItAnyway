package ca.ualberta.cs.w18t11.whoselineisitanyway.model.task;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detail;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.elastic.Elastic;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.DetailActivity;

/**
 * Represents a task.
 *
 * @author Samuel Dolha
 * @version 3.1
 */
public final class Task implements Detailed, Elastic, Serializable
{
    /**
     * An auto-generated, unique ID to support class versioning for Serializable.
     *
     * @see Serializable
     */
    private static final long serialVersionUID = -1856506144548111590L;

    /**
     * The maximum title length allowed.
     */
    private static final int MAXIMUM_TITLE_LENGTH = 30;

    /**
     * The maximum description length allowed.
     */
    private static final int MAXIMUM_DESCRIPTION_LENGTH = 300;

    /**
     * The task's unique ID in Elasticsearch.
     */
    @Nullable
    private String elasticId;

    /**
     * The associated requester's ID.
     */
    @NonNull
    private final String requesterId;

    /**
     * The associated provider's ID.
     */
    @Nullable
    private final String providerId;

    /**
     * The associated bids.
     *
     * @see Bid
     */
    @Nullable
    private final Bid[] bids;

    /**
     * The task's title.
     */
    @NonNull
    private final String title;

    /**
     * The task's description.
     */
    @NonNull
    private final String description;

    /**
     * The task's status.
     *
     * @see TaskStatus
     */
    @NonNull
    private final TaskStatus status;

    /**
     * Creates a task.
     *
     * @param elasticId   The task's unique ID in elasticsearch.
     * @param requesterId The associated requester's ID.
     * @param providerId  The associated provider's ID.
     * @param bids        The associated bids.
     * @param title       The task's title.
     * @param description The task's description.
     * @param status      The task's status.
     * @throws IllegalArgumentException For an empty requesterId; a non-null, empty providerId; an
     *                                  assigned or done task without a bid associated with the
     *                                  providerId; or a title or description exceeding their
     *                                  respective maximum lengths.
     * @see Bid
     * @see TaskStatus
     */
    private Task(@Nullable final String elasticId, @NonNull final String requesterId,
                 @Nullable final String providerId, @Nullable final Bid[] bids,
                 @NonNull final String title, @NonNull final String description,
                 @NonNull final TaskStatus status) throws IllegalArgumentException
    {
        if (elasticId != null && elasticId.isEmpty())
        {
            throw new IllegalArgumentException("elasticId cannot be empty");
        }

        if (requesterId.isEmpty())
        {
            throw new IllegalArgumentException("requesterId cannot be empty");
        }

        if (providerId != null && providerId.isEmpty())
        {
            throw new IllegalArgumentException("providerId must be null or non-empty");
        }

        if (status == TaskStatus.REQUESTED && bids != null)
        {
            throw new IllegalArgumentException("a requested task cannot have bids");
        }

        if (status == TaskStatus.BIDDED || status == TaskStatus.ASSIGNED
                || status == TaskStatus.DONE)
        {
            if (elasticId == null)
            {
                throw new IllegalArgumentException("a task with bids must have an ID");
            }

            if (status != TaskStatus.BIDDED && providerId == null)
            {
                throw new IllegalArgumentException(
                        "an assigned or done task must have a providerId");
            }

            if (bids == null || bids.length < 1)
            {
                throw new IllegalArgumentException("a non-requested task must be at least one bid");
            }

            final Collection<String> seenProviderIds = new ArrayList<>(bids.length);
            boolean providerIdValid = false;

            for (Bid bid : bids)
            {
                if (!bid.getTaskId().equals(elasticId))
                {
                    throw new IllegalArgumentException("a task's bids must have its ID");
                }

                for (String seenProviderId : seenProviderIds)
                {
                    if (bid.getProviderId().equals(seenProviderId))
                    {
                        throw new IllegalArgumentException(
                                "cannot have multiple bids from the same provider");
                    }
                }

                seenProviderIds.add(bid.getProviderId());

                {
                    if (bid.getProviderId().equals(providerId))
                    {
                        providerIdValid = true;
                    }
                }
            }

            if (status != TaskStatus.BIDDED && !providerIdValid)
            {
                throw new IllegalArgumentException(
                        "an assigned or done task must have a bid associated with the providerId");
            }
        }

        if (title.length() > Task.MAXIMUM_TITLE_LENGTH)
        {
            throw new IllegalArgumentException(
                    String.format(Locale.getDefault(), "title cannot exceed %d characters",
                            Task.MAXIMUM_TITLE_LENGTH));
        }

        if (description.length() > Task.MAXIMUM_DESCRIPTION_LENGTH)
        {
            throw new IllegalArgumentException(
                    String.format(Locale.getDefault(), "description cannot exceed %d characters",
                            Task.MAXIMUM_DESCRIPTION_LENGTH));
        }

        this.elasticId = elasticId;
        this.requesterId = requesterId;
        this.providerId = providerId;
        this.bids = bids;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    /**
     * Creates a requested task.
     *
     * @param elasticId   The task's unique ID in elasticsearch.
     * @param requesterId The associated requester's ID.
     * @param title       The task's title.
     * @param description The task's description.
     */
    public Task(@Nullable final String elasticId, @NonNull final String requesterId,
                @NonNull final String title, @NonNull final String description)
    {
        this(elasticId, requesterId, null, null, title, description, TaskStatus.REQUESTED);
    }

    /**
     * Creates a bidded task.
     *
     * @param elasticId   The task's unique ID in Elasticsearch.
     * @param requesterId The associated requester's ID.
     * @param bids        The associated bids.
     * @param title       The task's title.
     * @param description The task's description.
     * @see Bid
     */
    public Task(@NonNull final String elasticId, @NonNull final String requesterId,
                @NonNull final Bid[] bids, @NonNull final String title,
                @NonNull final String description)
    {
        this(elasticId, requesterId, null, bids, title, description, TaskStatus.BIDDED);
    }

    /**
     * Creates an assigned or done task.
     *
     * @param elasticId   The task's unique ID in Elasticsearch.
     * @param requesterId The associated requester's ID.
     * @param providerId  The associated provider's ID.
     * @param bids        The associated bids.
     * @param title       The task's title.
     * @param description The task's description.
     * @param done        Whether the task has been completed.
     * @see Bid
     */
    public Task(@NonNull final String elasticId, @NonNull final String requesterId,
                @NonNull final String providerId, @NonNull final Bid[] bids,
                @NonNull final String title, @NonNull final String description, final boolean done)
    {
        this(elasticId, requesterId, providerId, bids, title, description,
                done ? TaskStatus.DONE : TaskStatus.ASSIGNED);
    }

    /**
     * @return The associated requester's ID.
     */
    @NonNull
    public final String getRequesterId()
    {
        return this.requesterId;
    }

    /**
     * @return The associated provider's ID, if applicable.
     */
    @Nullable
    public final String getProviderId()
    {
        return this.providerId;
    }

    /**
     * @return The associated bids.
     * @see Bid
     */
    @Nullable
    public final Bid[] getBids()
    {
        return this.bids;
    }

    /**
     * @return The task's title.
     */
    @NonNull
    public final String getTitle()
    {
        return this.title;
    }

    /**
     * @return The task's description.
     */
    @NonNull
    public final String getDescription()
    {
        return this.description;
    }

    /**
     * @return The task's status.
     * @see TaskStatus
     */
    @NonNull
    public final TaskStatus getStatus()
    {
        return this.status;
    }

    /**
     * @return A copy of the task with the given bid on it, replacing any bid previously made by the
     * same provider.
     * @throws IllegalArgumentException For a bid with a taskId different from the task's Id.
     * @throws IllegalStateException    For a non-requested, non-bidded task.
     * @see Bid
     */
    @NonNull
    public final Task submitBid(@NonNull final Bid bid)
    {
        switch (this.getStatus())
        {
            case REQUESTED:
                return new Task(this.getElasticId(), this.getRequesterId(), null, new Bid[]{bid},
                        this.getTitle(), this.getDescription(), TaskStatus.BIDDED);

            case BIDDED:
                final Bid[] oldBids = this.getBids();
                assert oldBids != null;
                final ArrayList<Bid> bids = new ArrayList<>();

                for (Bid oldBid : oldBids)
                {
                    // If a bid from the same provider already exists for the task, replace it.
                    if (!oldBid.getProviderId().equals(bid.getProviderId()))
                    {
                        bids.add(oldBid);
                    }
                }

                bids.add(bid);

                return new Task(this.getElasticId(), this.getRequesterId(), null,
                        bids.toArray(new Bid[0]), this.getTitle(), this.getDescription(),
                        TaskStatus.BIDDED);

            default:
                throw new IllegalStateException("Cannot bid on a non-requested, non-bidded task");
        }
    }

    /**
     * @return A copy of the task assigned to the provider.
     * @throws IllegalArgumentException For an empty providerId.
     * @throws IllegalStateException    For a non-bidded task.
     */
    @NonNull
    public final Task assignProvider(@NonNull final String providerId)
            throws IllegalArgumentException, IllegalStateException
    {
        if (providerId.isEmpty())
        {
            throw new IllegalArgumentException("providerId cannot be empty");
        }

        if (this.getStatus() != TaskStatus.BIDDED)
        {
            throw new IllegalStateException("Cannot assign a non-bidded task");
        }

        return new Task(this.getElasticId(), this.getRequesterId(), providerId, this.getBids(),
                this.getTitle(), this.getDescription(), TaskStatus.ASSIGNED);
    }

    /**
     * @return A copy of the task marked as done.
     * @throws IllegalStateException For a non-assigned task.
     */
    @NonNull
    public final Task markDone() throws IllegalStateException
    {
        if (this.getStatus() != TaskStatus.ASSIGNED)
        {
            throw new IllegalStateException("Cannot mark a non-assigned task as done");
        }

        return new Task(this.getElasticId(), this.getRequesterId(), this.getProviderId(),
                this.getBids(), this.getTitle(), this.getDescription(), TaskStatus.DONE);
    }

    /**
     * @return The task's elastic ID.
     * @see Elastic
     */
    @Nullable
    @Override
    public final String getElasticId()
    {
        return this.elasticId;
    }

    /**
     * Sets the task's elastic ID.
     *
     * @param id The task's new ID.
     * @throws IllegalArgumentException For an empty task.
     * @see Elastic
     */
    @Override
    public final void setElasticId(@NonNull final String id) throws IllegalArgumentException
    {
        if (id.isEmpty())
        {
            throw new IllegalArgumentException("id cannot be empty");
        }

        this.elasticId = id;
    }

    /**
     * Shows the task's details.
     *
     * @param detailActivityClass The activity in which to display the details.
     * @param context             The context in which to start the activity.
     * @param <T>                 The type of DetailActivity.
     * @see Detailed
     */
    @Override
    public final <T extends DetailActivity> void showDetails(
            @NonNull final Class<T> detailActivityClass, @NonNull final Context context)
    {
        final ArrayList<Detail> details = (ArrayList<Detail>) Arrays
                .asList(new Detail("title", this.getTitle(), null),
                        new Detail("description", this.getDescription(), null),
                        new Detail("status", this.getStatus().toString(), null),
                        new Detail("requesterId", this.getRequesterId(), null));

        if (this.getProviderId() != null)
        {
            details.add(new Detail("providerId", this.getProviderId(), null));
        }

        final Intent intent = new Intent(context, detailActivityClass);
        intent.putExtra(Detailed.DETAILS_KEY, details);
        intent.putExtra(Detailed.TITLE_KEY, "Task");

        context.startActivity(intent);
    }

    /**
     * @return A hashcode of the task.
     * @see Object
     */
    @Override
    public final int hashCode()
    {
        return Objects.hash(this.getElasticId(), this.getRequesterId(), this.getProviderId(),
                this.getBids(), this.getTitle(), this.getDescription(), this.getStatus());
    }

    /**
     * @param object The object with which to compare the task.
     * @return Whether the object represents the same task.
     * @see Object
     */
    @Override
    public final boolean equals(@Nullable final Object object)
    {
        if (!(object instanceof Task))
        {
            return false;
        }

        if (object == this)
        {
            return true;
        }

        final Task task = (Task) object;

        final EqualsBuilder builder = new EqualsBuilder();

        if (this.getElasticId() == null && task.getElasticId() == null)
        {
            builder.append(this.getTitle(), task.getTitle())
                    .append(this.getDescription(), task.getDescription());
        }
        else
        {
            builder.append(this.getElasticId(), task.getElasticId());
        }

        return builder.append(this.getRequesterId(), task.getRequesterId()).isEquals();
    }
}
