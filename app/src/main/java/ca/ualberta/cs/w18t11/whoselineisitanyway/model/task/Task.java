package ca.ualberta.cs.w18t11.whoselineisitanyway.model.task;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detail;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.elastic.Elastic;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.DetailActivity;

/**
 * Represents a task.
 *
 * @author Samuel Dolha
 * @version 2.0
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
    private Task(@NonNull final String requesterId, @Nullable final String providerId,
                 @Nullable final Bid[] bids, @NonNull final String title,
                 @NonNull final String description, @NonNull final TaskStatus status)
    {
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

        if (status == TaskStatus.ASSIGNED || status == TaskStatus.DONE)
        {
            if (providerId == null)
            {
                throw new IllegalArgumentException(
                        "an assigned or done task must have a providerId");
            }

            if (bids == null)
            {
                throw new IllegalArgumentException("an assigned or done task must have bids");
            }

            boolean providerIdValid = false;

            for (Bid bid : bids)
            {
                if (!bid.getTaskId().equals(this.getElasticId()))
                {
                    throw new IllegalArgumentException("bid's task's ID must match task's ID");
                }

                if (bid.getProviderId().equals(providerId))
                {
                    providerIdValid = true;
                }
            }

            if (!providerIdValid)
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
     * @param requesterId The associated requester's ID.
     * @param title       The task's title.
     * @param description The task's description.
     */
    public Task(@NonNull final String requesterId, @NonNull final String title,
                @NonNull final String description)
    {
        this(requesterId, null, null, title, description, TaskStatus.REQUESTED);
    }

    /**
     * Creates a bidded task.
     *
     * @param requesterId The associated requester's ID.
     * @param bids        The associated bids.
     * @param title       The task's title.
     * @param description The task's description.
     * @see Bid
     */
    public Task(@NonNull final String requesterId, @NonNull final Bid[] bids,
                @NonNull final String title, @NonNull final String description)
    {
        this(requesterId, null, bids, title, description, TaskStatus.BIDDED);
    }

    /**
     * Creates an assigned task.
     *
     * @param requesterId The associated requester's ID.
     * @param providerId  The associated provider's ID.
     * @param bids        The associated bids.
     * @param title       The task's title.
     * @param description The task's description.
     * @param done        Whether the task has been completed.
     * @see Bid
     */
    public Task(@NonNull final String requesterId, @NonNull final String providerId,
                @NonNull final Bid[] bids, @NonNull final String title,
                @NonNull final String description, final boolean done)
    {
        this(requesterId, providerId, bids, title, description,
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
     * @return A copy of the task with the given bid on it.
     * @throws IllegalArgumentException For a bid with a taskId different from the task's Id.
     * @throws IllegalStateException    For a non-requested, non-bidded task.
     * @see Bid
     */
    @NonNull
    public final Task submitBid(@NonNull Bid bid)
    {
        switch (this.getStatus())
        {
            case REQUESTED:
                return new Task(this.getRequesterId(), new Bid[]{bid}, this.getTitle(),
                        this.getDescription());

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

                return new Task(this.getRequesterId(), bids.toArray(new Bid[0]), this.getTitle(),
                        this.getDescription());

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
    public final Task assignProvider(@NonNull String providerId)
    {
        if (providerId.isEmpty())
        {
            throw new IllegalArgumentException("providerId cannot be empty");
        }

        if (this.getStatus() != TaskStatus.BIDDED)
        {
            throw new IllegalStateException("Cannot assign a non-bidded task");
        }

        assert this.getBids() != null;

        return new Task(this.getRequesterId(), providerId, this.getBids(), this.getTitle(),
                this.getDescription(), false);
    }

    /**
     * @return A copy of the task marked as done.
     * @throws IllegalStateException For a non-assigned task.
     */
    @NonNull
    public final Task markDone()
    {
        if (this.getStatus() != TaskStatus.ASSIGNED)
        {
            throw new IllegalStateException("Cannot mark a non-assigned task as done");
        }

        final Bid[] bids = this.getBids();
        final String providerId = this.getProviderId();
        assert bids != null;
        assert providerId != null;

        return new Task(this.getRequesterId(), providerId, bids, this.getTitle(),
                this.getDescription(), TaskStatus.DONE);
    }

    @Nullable
    @Override
    public final String getElasticId()
    {
        return this.elasticId;
    }

    @Override
    public final void setElasticId(@NonNull final String id)
    {
        if (id.isEmpty())
        {
            throw new IllegalArgumentException("id cannot be empty");
        }

        this.elasticId = id;
    }

    @Override
    public final <T extends DetailActivity> void showDetails(
            @NonNull final Class<T> detailActivityClass, @NonNull final Context context)
    {
        ArrayList<Detail> detailList = new ArrayList<>();
        detailList.add(new Detail("Title", getTitle(), null));
        detailList.add(new Detail("Description", getDescription(), null));
        detailList.add(new Detail("Requester", getRequesterId(), null));
        detailList.add(new Detail("Status", getStatus().name(), null));
        if (getProviderId() != null)
        {
            detailList.add(new Detail("Provider", getProviderId(), null));
        }
        else
        {
            detailList.add(new Detail("Provider", "N/A", null));
        }

        Intent intent = new Intent(context, detailActivityClass);
        intent.putExtra(Detailed.DETAILS_KEY, detailList);
        intent.putExtra(Detailed.TITLE_KEY, "Task");

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
        return this.getTitle();
    }
}
