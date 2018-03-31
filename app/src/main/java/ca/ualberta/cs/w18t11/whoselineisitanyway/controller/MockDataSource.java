package ca.ualberta.cs.w18t11.whoselineisitanyway.controller;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.Collection;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * Represents an in-memory data source.
 *
 * @author Brad Ofrim, Samuel Dolha
 * @version 3.0
 */
final class MockDataSource implements DataSource
{
    /**
     * The users currently present in the data source.
     *
     * @see User
     */
    @NonNull
    private final Collection<User> users;

    /**
     * The tasks currently present in the data source.
     *
     * @see Task
     */
    @NonNull
    private final Collection<Task> tasks;

    /**
     * The bids currently present in the data source.
     *
     * @see Bid
     */
    @NonNull
    private final Collection<Bid> bids;

    /**
     * @param users The users initially present in the data source.
     * @param tasks The tasks initially present in the data source.
     * @param bids  The bids initially present in the data source.
     * @see Bid
     * @see Task
     * @see User
     */
    public MockDataSource(@NonNull final User[] users, @NonNull final Task[] tasks,
                          @NonNull final Bid[] bids)
    {
        this.users = Arrays.asList(users);
        this.tasks = Arrays.asList(tasks);
        this.bids = Arrays.asList(bids);
    }

    /**
     * @return All users present in the data source.
     * @see DataSource
     * @see User
     */
    @NonNull
    @Override
    public final User[] getUsers()
    {
        return this.users.toArray(new User[0]);
    }

    /**
     * @return The user with that username, or null if no such user exists in the data source.
     * @throws IllegalArgumentException For an empty username.
     * @see DataSource
     * @see User
     */
    @Nullable
    @Override
    public final User getUser(@NonNull final String username) throws IllegalArgumentException
    {
        if (username.isEmpty())
        {
            throw new IllegalArgumentException("username cannot be empty");
        }

        for (User user : this.users)
        {
            if (user.getUsername().equals(username))
            {
                return user;
            }
        }

        return null;
    }

    /**
     * Adds a user to the data source.
     *
     * @param user The user to add.
     * @return Whether the user is present in the data source.
     * @see DataSource
     * @see User
     */
    @Override
    public final boolean addUser(@NonNull final User user)
    {
        this.users.add(user);

        return true;
    }

    /**
     * Removes a user from the data source.
     *
     * @param user The user to remove.
     * @return Whether the user is absent from the data source.
     * @see DataSource
     * @see User
     */
    @Override
    public final boolean removeUser(@NonNull final User user)
    {
        this.users.remove(user);

        return true;
    }

    /**
     * @return All tasks present in the data source.
     * @see DataSource
     * @see Task
     */
    @NonNull
    @Override
    public final Task[] getTasks()
    {
        return this.tasks.toArray(new Task[0]);
    }

    /**
     * @return The task with that requester and title, or null if no such task exists in the data
     * source.
     * @throws IllegalArgumentException For an empty requesterUsername or title.
     * @see DataSource
     * @see Task
     */
    @Nullable
    @Override
    public final Task getTask(@NonNull final String requesterUsername, @NonNull final String title)
            throws IllegalArgumentException
    {
        for (Task task : this.tasks)
        {
            if (task.getRequesterUsername().equals(requesterUsername) && task.getTitle()
                    .equals(title))
            {
                return task;
            }
        }

        return null;
    }

    /**
     * Adds a task to the data source.
     *
     * @param task The task to add.
     * @return Whether the task is present in the data source.
     * @see DataSource
     * @see Task
     */
    @Override
    public final boolean addTask(@NonNull final Task task)
    {
        this.tasks.add(task);

        return true;
    }

    /**
     * Removes a task from the data source.
     *
     * @param task The task to remove.
     * @return Whether the task is absent from the data source.
     * @see DataSource
     * @see Task
     */
    @Override
    public final boolean removeTask(@NonNull final Task task)
    {
        this.tasks.remove(task);

        return true;
    }

    /**
     * @return All bids present in the data source.
     * @see Bid
     * @see DataSource
     */
    @NonNull
    @Override
    public final Bid[] getBids()
    {
        return this.bids.toArray(new Bid[0]);
    }

    /**
     * @return The bid from that provider on that task, or null if no such bid exists in the data
     * source.
     * @throws IllegalArgumentException For an empty providerUsername or taskId.
     * @see Bid
     * @see DataSource
     */
    @Nullable
    @Override
    public final Bid getBid(@NonNull final String providerUsername, @NonNull final String taskId)
            throws IllegalArgumentException
    {
        for (Bid bid : this.bids)
        {
            if (bid.getProviderUsername().equals(providerUsername) && bid.getTaskId()
                    .equals(taskId))
            {
                return bid;
            }
        }

        return null;
    }

    /**
     * Adds a bid to the data source.
     *
     * @param bid The bid to add.
     * @return Whether the bid is present in the data source.
     * @see Bid
     * @see DataSource
     */
    @Override
    public final boolean addBid(@NonNull final Bid bid)
    {
        this.bids.add(bid);

        return true;
    }

    /**
     * Removes a bid from the data source.
     *
     * @param bid The user to remove.
     * @return Whether the bid is absent from the data source.
     * @see Bid
     * @see DataSource
     */
    @Override
    public final boolean removeBid(@NonNull final Bid bid)
    {
        this.bids.remove(bid);

        return true;
    }
}
