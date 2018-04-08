package ca.ualberta.cs.w18t11.whoselineisitanyway.controller;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * Represents a user-bid-task data source.
 *
 * @author Brad Ofrim, Samuel Dolha
 * @version 3.0
 */
public interface DataSource
{
    /**
     * @return All users present in the data source, or null if an error occurs.
     * @see User
     */
    @Nullable
    User[] getUsers();

    /**
     * @return The user with that username, or null if no such user exists in the data source.
     * @throws IllegalArgumentException For an empty username.
     * @see User
     */
    @Nullable
    User getUser(@NonNull final String username) throws IllegalArgumentException;

    /**
     * Adds a user to the data source.
     *
     * @param user The user to add.
     * @return Whether the user is present in the data source.
     * @see User
     */
    boolean addUser(@NonNull final User user);

    /**
     * Removes a user from the data source.
     *
     * @param user The user to remove.
     * @return Whether the user is absent from the data source.
     * @see User
     */
    boolean removeUser(@NonNull final User user);

    /**
     * @return All tasks present in the data source, or null if an error occurs.
     * @see Task
     */
    @Nullable
    Task[] getTasks();

    /**
     * @return The task with that requester and title, or null if no such task exists in the data
     * source.
     * @throws IllegalArgumentException For an empty requesterUsername or title.
     * @see Task
     */
    @Nullable
    Task getTask(@NonNull final String requesterUsername, @NonNull final String title)
            throws IllegalArgumentException;

    /**
     * @return The task with that taskId, or null if no such task exists in the data
     * source.
     * @throws IllegalArgumentException For an empty taskId.
     * @see Task
     */
    @Nullable
    Task getTask(@NonNull final String taskId)
            throws IllegalArgumentException;

    /**
     * Adds a task to the data source.
     *
     * @param task The task to add.
     * @return Whether the task is present in the data source.
     * @see Task
     */
    boolean addTask(@NonNull final Task task);

    /**
     * Removes a task from the data source.
     *
     * @param task The task to remove.
     * @return Whether the task is absent from the data source.
     * @see Task
     */
    boolean removeTask(@NonNull final Task task);

    /**
     * @return All bids present in the data source, or null if an error occurs.
     * @see Bid
     */
    @Nullable
    Bid[] getBids();

    /**
     * @return The bid from that provider on that task, or null if no such bid exists in the data
     * source.
     * @throws IllegalArgumentException For an empty providerUsername or taskId.
     * @see Bid
     */
    @Nullable
    Bid getBid(@NonNull final String providerUsername, @NonNull final String taskId)
            throws IllegalArgumentException;

    /**
     * Adds a bid to the data source.
     *
     * @param bid The bid to add.
     * @return Whether the bid is present in the data source.
     * @see Bid
     */
    boolean addBid(@NonNull final Bid bid);

    /**
     * Removes a bid from the data source.
     *
     * @param bid The user to remove.
     * @return Whether the bid is absent from the data source.
     * @see Bid
     */
    boolean removeBid(@NonNull final Bid bid);
}
