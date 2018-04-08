package ca.ualberta.cs.w18t11.whoselineisitanyway.model.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.controller.DataSource;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detail;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail.Detailed;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.elastic.Elastic;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.location.Location;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.DetailActivity;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.DetailedListActivity;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.TaskDetailActivity;
import ca.ualberta.cs.w18t11.whoselineisitanyway.view.UserProfileActivity;

/**
 * Represents a task.
 *
 * @author Samuel Dolha
 * @version 4.0
 */
public final class Task implements Detailed, Elastic, Serializable
{

    //TODO Implement location value
    //TODO implement photo storage

    /**
     * Used to put the Task into an intent
     */
    public static final String TASK_KEY = "TASK_OBJECT";

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
     * The associated requester's username.
     */
    @NonNull
    private final String requesterUsername;

    /**
     * The associated provider's username.
     */
    @Nullable
    private final String providerUsername;

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
     * The task's elastic ID.
     */
    @Nullable
    private String elasticId;

    /**
     * The task's images.
     */
    @Nullable
    private String[] images;

    /**
     * The task's location.
     *
     * @see Location
     */
    @Nullable
    private Location location;

    /**
     * Creates a task.
     *
     * @param elasticId         The task's elastic ID.
     * @param requesterUsername The associated requester's ID.
     * @param providerUsername  The associated provider's ID.
     * @param bids              The associated bids.
     * @param title             The task's title.
     * @param description       The task's description.
     * @param status            The task's status.
     * @throws IllegalArgumentException For an empty requesterUsername; a non-null, empty
     *                                  providerUsername; an assigned or done task without a bid
     *                                  associated with the providerUsername; or a title or
     *                                  description exceeding their respective maximum lengths.
     * @see Bid
     * @see TaskStatus
     */
    private Task(@Nullable final String elasticId, @NonNull final String requesterUsername,
                 @Nullable final String providerUsername, @Nullable final Bid[] bids,
                 @NonNull final String title, @NonNull final String description,
                 @NonNull final TaskStatus status) throws IllegalArgumentException
    {
        if (elasticId != null && elasticId.isEmpty())
        {
            throw new IllegalArgumentException("elasticId cannot be empty");
        }

        if (requesterUsername.isEmpty())
        {
            throw new IllegalArgumentException("requesterUsername cannot be empty");
        }

        if (providerUsername != null && providerUsername.isEmpty())
        {
            throw new IllegalArgumentException("providerUsername must be null or non-empty");
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

            if (status != TaskStatus.BIDDED && providerUsername == null)
            {
                throw new IllegalArgumentException(
                        "an assigned or done task must have a providerUsername");
            }

            if (bids == null || bids.length < 1)
            {
                throw new IllegalArgumentException("a non-requested task must be at least one bid");
            }

            final Collection<String> seenProviderUsernames = new ArrayList<>(bids.length);
            boolean providerUsernameValid = false;

            for (Bid bid : bids)
            {
                if (!bid.getTaskId().equals(elasticId))
                {
                    throw new IllegalArgumentException("a task's bids must have its ID");
                }

                for (String seenProviderUsername : seenProviderUsernames)
                {
                    if (bid.getProviderUsername().equals(seenProviderUsername))
                    {
                        throw new IllegalArgumentException(
                                "cannot have multiple bids from the same provider");
                    }
                }

                seenProviderUsernames.add(bid.getProviderUsername());

                {
                    if (bid.getProviderUsername().equals(providerUsername))
                    {
                        providerUsernameValid = true;
                    }
                }
            }

            if (status != TaskStatus.BIDDED && !providerUsernameValid)
            {
                throw new IllegalArgumentException(
                        "an assigned or done task must have a bid associated with the providerUsername");
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
        this.requesterUsername = requesterUsername;
        this.providerUsername = providerUsername;
        this.bids = bids;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    /**
     * Creates a requested task without an elastic ID.
     *
     * @param requesterUsername The associated requester's ID.
     * @param title             The task's title.
     * @param description       The task's description.
     */
    public Task(@NonNull final String requesterUsername, @NonNull final String title,
                @NonNull final String description)
    {
        this(null, requesterUsername, null, null, title, description, TaskStatus.REQUESTED);
    }

    /**
     * Creates a requested task with an elastic ID.
     *
     * @param elasticId         The task's elastic ID.
     * @param requesterUsername The associated requester's ID.
     * @param title             The task's title.
     * @param description       The task's description.
     */
    public Task(@NonNull final String elasticId, @NonNull final String requesterUsername,
                @NonNull final String title, @NonNull final String description)
    {
        this(elasticId, requesterUsername, null, null, title, description, TaskStatus.REQUESTED);
    }

    /**
     * Creates a bidded task.
     *
     * @param elasticId         The task's elastic ID.
     * @param requesterUsername The associated requester's ID.
     * @param bids              The associated bids.
     * @param title             The task's title.
     * @param description       The task's description.
     * @see Bid
     */
    public Task(@NonNull final String elasticId, @NonNull final String requesterUsername,
                @NonNull final Bid[] bids, @NonNull final String title,
                @NonNull final String description)
    {
        this(elasticId, requesterUsername, null, bids, title, description, TaskStatus.BIDDED);
    }

    /**
     * Creates an assigned or done task.
     *
     * @param elasticId         The task's elastic ID.
     * @param requesterUsername The associated requester's ID.
     * @param providerUsername  The associated provider's ID.
     * @param bids              The associated bids.
     * @param title             The task's title.
     * @param description       The task's description.
     * @param done              Whether the task has been completed.
     * @see Bid
     */
    public Task(@NonNull final String elasticId, @NonNull final String requesterUsername,
                @NonNull final String providerUsername, @NonNull final Bid[] bids,
                @NonNull final String title, @NonNull final String description, final boolean done)
    {
        this(elasticId, requesterUsername, providerUsername, bids, title, description,
                done ? TaskStatus.DONE : TaskStatus.ASSIGNED);
    }

    private Task addExtras(@Nullable final String[] images, @Nullable final Location location)
    {
        final Task task = new Task(this.getElasticId(), this.getRequesterUsername(),
                this.getProviderUsername(), this.getBids(), this.getTitle(), this.getDescription(),
                this.getStatus());
        task.setImages(images);
        task.setLocation(location);

        return task;
    }

    /**
     * @return The associated requester's ID.
     */
    @NonNull
    public final String getRequesterUsername()
    {
        return this.requesterUsername;
    }

    /**
     * @return The associated provider's ID, if applicable.
     */
    @Nullable
    public final String getProviderUsername()
    {
        return this.providerUsername;
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
     * @return The bid with the lowest value.
     * @see Bid
     */
    @NonNull
    public final Bid getLowestBid()
    {
        if (this.getStatus() == TaskStatus.REQUESTED)
        {
            throw new IllegalStateException("Cannot get the lowest bid of a requested task");
        }

        final Bid[] bids = this.getBids();
        assert bids != null;
        Bid lowestBid = bids[0];

        for (int index = 1; index < bids.length; index += 1)
        {
            final Bid bid = bids[index];

            if (bid.getValue().compareTo(lowestBid.getValue()) < 0)
            {
                lowestBid = bid;
            }
        }

        return lowestBid;
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
     * @return The task's images.
     */
    @Nullable
    public final String[] getImages()
    {
        return this.images;
    }

    /**
     * Set the task's images.
     *
     * @param images The task's images.
     */
    public final void setImages(@Nullable final String[] images)
    {
        this.images = images;
    }

    /**
     * @return The task's location.
     * @see Location
     */
    @Nullable
    public final Location getLocation()
    {
        return this.location;
    }

    /**
     * Set the task's location.
     *
     * @param location The task's location.
     * @see Location
     */
    public final void setLocation(@Nullable final Location location)
    {
        this.location = location;
    }

    /**
     * @param bid        The bid to decline.
     * @param dataSource The data source from which
     * @return A copy of the task without the bid.
     * @see Bid
     * @see DataSource
     */
    @NonNull
    public final Task declineBid(@NonNull final Bid bid, @NonNull final DataSource dataSource)
    {
        dataSource.removeBid(bid);

        switch (this.getStatus())
        {
            case BIDDED:
                final Bid[] oldBids = this.getBids();
                assert oldBids != null;
                final ArrayList<Bid> bids = new ArrayList<>(Arrays.asList(oldBids));

                bids.remove(bid);

                if(bids.size() > 0)
                {
                    return new Task(this.getElasticId(), this.getRequesterUsername(), null,
                            bids.toArray(new Bid[0]), this.getTitle(), this.getDescription(),
                            TaskStatus.BIDDED).addExtras(this.images, this.location);
                }

                return new Task(this.getElasticId(), this.getRequesterUsername(), null,
                        null, this.getTitle(), this.getDescription(),
                        TaskStatus.REQUESTED).addExtras(this.images, this.location);

            default:
                throw new IllegalStateException("Cannot decline a bid on a non-bidded task");
        }
    }

    /**
     * @param bid        The bid to submit.
     * @param dataSource The data source to which to add the bid.
     * @return A copy of the task with the given bid on it, replacing any bid previously made by the
     * same provider.
     * @throws IllegalArgumentException For a bid with a taskId different from the task's Id.
     * @throws IllegalStateException    For a non-requested, non-bidded task.
     * @see Bid
     */
    @NonNull
    public final Task submitBid(@NonNull final Bid bid, @NonNull final DataSource dataSource)
    {
        dataSource.addBid(bid);

        switch (this.getStatus())
        {
            case REQUESTED:
                return new Task(this.getElasticId(), this.getRequesterUsername(), null,
                        new Bid[]{bid}, this.getTitle(), this.getDescription(), TaskStatus.BIDDED)
                        .addExtras(this.images, this.location);

            case BIDDED:
                final Bid[] oldBids = this.getBids();
                assert oldBids != null;
                final ArrayList<Bid> bids = new ArrayList<>();

                for (Bid oldBid : oldBids)
                {
                    // If a bid from the same provider already exists for the task, don't keep it.
                    if (!oldBid.getProviderUsername().equals(bid.getProviderUsername()))
                    {
                        bids.add(oldBid);
                    }
                }

                bids.add(bid);

                return new Task(this.getElasticId(), this.getRequesterUsername(), null,
                        bids.toArray(new Bid[0]), this.getTitle(), this.getDescription(),
                        TaskStatus.BIDDED).addExtras(this.images, this.location);

            default:
                throw new IllegalStateException("Cannot bid on a non-requested, non-bidded task");
        }
    }

    /**
     * @return A copy of the task without an assigned provider.
     * @throws IllegalStateException For a non-assigned task.
     */
    @NonNull
    public final Task unassignProvider() throws IllegalStateException
    {
        if (this.getStatus() != TaskStatus.ASSIGNED)
        {
            throw new IllegalStateException("Cannot unassign a non-assigned task");
        }

        assert this.getBids() != null;
        final ArrayList<Bid> bids = new ArrayList<>(Arrays.asList(this.getBids()));
        Bid bidToRemove = null;

        for (Bid bid : bids)
        {
            if (bid.getProviderUsername().equals(this.getProviderUsername()))
            {
                bidToRemove = bid;
            }
        }

        assert bidToRemove != null;
        bids.remove(bidToRemove);

        if (bids.size() > 0)
        {
            return new Task(this.getElasticId(), this.getRequesterUsername(), null,
                    bids.toArray(new Bid[bids.size()]), this.getTitle(), this.getDescription(),
                    TaskStatus.BIDDED).addExtras(this.images, this.location);
        }

        return new Task(this.getElasticId(), this.getRequesterUsername(), null, null,
                this.getTitle(), this.getDescription(), TaskStatus.REQUESTED)
                .addExtras(images, location);
    }

    /**
     * @param providerUsername The provider's username.
     * @return A copy of the task assigned to the provider.
     * @throws IllegalArgumentException For an empty providerUsername.
     * @throws IllegalStateException    For a non-bidded task.
     */
    @NonNull
    public final Task assignProvider(@NonNull final String providerUsername)
            throws IllegalArgumentException, IllegalStateException
    {
        if (providerUsername.isEmpty())
        {
            throw new IllegalArgumentException("providerUsername cannot be empty");
        }

        if (this.getStatus() != TaskStatus.BIDDED)
        {
            throw new IllegalStateException("Cannot assign a non-bidded task");
        }

        return new Task(this.getElasticId(), this.getRequesterUsername(), providerUsername,
                this.getBids(), this.getTitle(), this.getDescription(), TaskStatus.ASSIGNED)
                .addExtras(this.images, this.location);
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

        return new Task(this.getElasticId(), this.getRequesterUsername(),
                this.getProviderUsername(), this.getBids(), this.getTitle(), this.getDescription(),
                TaskStatus.DONE).addExtras(this.images, this.location);
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

    @Override
    @NonNull
    public final String toString()
    {
        return this.getRequesterUsername() + ": " + this.getTitle();
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
        context.startActivity(getDetailsIntent(TaskDetailActivity.class, context));
    }

    /**
     * Get an intent to show the details of the Task.
     *
     * @param detailActivityClass The activity in which to display the details.
     * @param context             The context in which to start the activity.
     * @param <T>                 The type of DetailActivity.
     * @see Detailed
     */
    @Override
    public final <T extends DetailActivity> Intent getDetailsIntent(
            @NonNull final Class<T> detailActivityClass, @NonNull final Context context)
    {
        final ArrayList<Detail> details = new ArrayList<>(Arrays.asList(
                new Detail(context.getString(R.string.detail_label_title), this.getTitle(), null),
                new Detail(context.getString(R.string.detail_label_description),
                        this.getDescription(), null),
                new Detail(context.getString(R.string.detail_label_status),
                        this.getStatus().toString(), null),
                new Detail(context.getString(R.string.detail_label_requester),
                        this.getRequesterUsername(),
                        buildUserLinkIntent(context, this.getRequesterUsername())),
                new Detail(context.getString(R.string.detail_label_empty), "Bids",
                        this.buildBidsListLinkIntent(context))));
        if (this.getProviderUsername() != null)
        {
            details.add(new Detail(context.getString(R.string.detail_label_provider),
                    this.getProviderUsername(),
                    buildUserLinkIntent(context, this.getProviderUsername())));
        }
        if (this.getStatus().equals(TaskStatus.BIDDED))
        {
            Bid lowestBid = this.getLowestBid();
            details.add(new Detail(context.getString(R.string.detail_label_lowest_bid),
                    lowestBid.getValue().toString(),
                    lowestBid.getDetailsIntent(DetailActivity.class, context)));
        }

        final Intent intent = new Intent(context, detailActivityClass);
        intent.putExtra(Detailed.DETAILS_KEY, details);
        intent.putExtra(Detailed.TITLE_KEY, "Task");

        intent.putExtra(Task.TASK_KEY, this);
        return intent;
    }

    /**
     * @return A hashcode of the task.
     * @see Object
     */
    @Override
    public final int hashCode()
    {
        return Objects
                .hash(this.getElasticId(), this.getRequesterUsername(), this.getProviderUsername(),
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

        if (this.getElasticId() != null && task.getElasticId() != null)
        {
            return this.getElasticId().equals(task.getElasticId());
        }

        return new EqualsBuilder().append(this.getRequesterUsername(), task.getRequesterUsername())
                .append(this.getProviderUsername(), task.getProviderUsername())
                .append(this.getTitle(), task.getTitle())
                .append(this.getDescription(), task.getDescription())
                .append(this.getStatus(), task.getStatus()).isEquals();
    }

    /**
     * Make an intent for displaying the tasks bids
     *
     * @param context to show from
     * @return Intent used to show the list of bids.
     */
    private Intent buildBidsListLinkIntent(Context context)
    {
        Intent outgoingIntent = new Intent(context, DetailedListActivity.class);
        String outgoingTitle = "Bids";
        ArrayList<Detailed> bidsArrayList = new ArrayList<>();
        if (this.getBids() != null)
        {
            for (Bid bid : this.getBids())
            {
                bidsArrayList.add(bid);
            }
        }
        outgoingIntent.putExtra(DetailedListActivity.DATA_TITLE, outgoingTitle);
        outgoingIntent.putExtra(DetailedListActivity.DATA_DETAILABLE_LIST, bidsArrayList);
        return outgoingIntent;
    }

    /**
     * Make an intent for displaying related users.
     *
     * @param context  to show from.
     * @param username of the user to show.
     * @return Intent used to show the user profile.
     */
    private Intent buildUserLinkIntent(Context context, String username)
    {
        Bundle bundle = new Bundle();
        Intent outgoingIntent = new Intent(context, UserProfileActivity.class);
        bundle.putString(UserProfileActivity.DATA_EXISTING_USERNAME, username);

        outgoingIntent.putExtras(bundle);

        return outgoingIntent;
    }
}
