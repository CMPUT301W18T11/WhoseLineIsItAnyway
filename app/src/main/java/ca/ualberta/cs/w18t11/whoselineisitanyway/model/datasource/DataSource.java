package ca.ualberta.cs.w18t11.whoselineisitanyway.model.datasource;

import android.support.annotation.NonNull;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * Represents a user-bid-task data source.
 *
 * @author Brad Ofrim, Samuel Dolha
 * @version 2.0
 */
public interface DataSource
{
    /**
     * @return All users present in the data source.
     * @see User
     */
    @NonNull
    User[] getUsers();

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
     * @return All tasks present in the data source.
     * @see Task
     */
    @NonNull
    Task[] getTasks();

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
     * @return All bids present in the data source.
     * @see Bid
     */
    @NonNull
    Bid[] getBids();

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

    /**
     * @param elasticId The user's elastic id
     * @return The user whose elastic id matches the input parameter
     */
    public User getUserById(String elasticId);

    /**
     * @param username The user's username
     * @return The user whose username matches the input parameter
     */
    User getUserByUsername(String username);
}
