package ca.ualberta.cs.w18t11.whoselineisitanyway.controller;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.Collection;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * Represents an in-memory data source.
 *
 * @author Brad Ofrim, Samuel Dolha
 * @version 2.0
 */
public final class MockDataSource implements DataSource
{
    /**
     * The users currently present in the data source.
     *
     * @see User
     */
    private final Collection<User> users;

    /**
     * The tasks currently present in the data source.
     *
     * @see Task
     */
    private final Collection<Task> tasks;

    /**
     * The bids currently present in the data source.
     *
     * @see Bid
     */
    private final Collection<Bid> bids;

    /**
     * @param users The users initially present in the data source.
     * @param tasks The tasks initially present in the data source.
     * @param bids  The bids initially present in the data source.
     * @see User
     * @see Task
     * @see Bid
     */
    public MockDataSource(@NonNull final User[] users, @NonNull final Task[] tasks,
                          @NonNull final Bid[] bids)
    {
        this.users = Arrays.asList(users);
        this.tasks = Arrays.asList(tasks);
        this.bids = Arrays.asList(bids);
//        // Build a list of users
//        User currentUser = new User(new EmailAddress("bob", "gmail.com"),
//                new PhoneNumber(0, 123, 456, 7890), "bob");
//        User providerUserA = new User(new EmailAddress("alice", "gmail.com"),
//                new PhoneNumber(0, 123, 456, 7890), "alice");
//        User providerUserB = new User(new EmailAddress("eve", "gmail.com"),
//                new PhoneNumber(0, 123, 456, 7890), "eve");
//
//        allUsers.add(currentUser);
//        allUsers.add(providerUserA);
//        allUsers.add(providerUserB);
//
//        // Build a list of bids
//        Bid bid1a = new Bid(providerUserA.getUsername(), "task1ID", new BigDecimal(5));
//        Bid bid1b = new Bid(providerUserB.getUsername(), "task1ID", new BigDecimal(6));
//        Bid bid2b = new Bid(providerUserB.getUsername(), "task2ID", new BigDecimal(500));
//        Bid bid2c = new Bid(currentUser.getUsername(), "task2ID", new BigDecimal(750));
//        Bid bid3b = new Bid(providerUserB.getUsername(), "task3ID", new BigDecimal(5));
//        Bid bid3c = new Bid(currentUser.getUsername(), "task3ID", new BigDecimal(7));
//
//        allBids.add(bid1a);
//        allBids.add(bid1b);
//        allBids.add(bid2b);
//        allBids.add(bid2c);
//        allBids.add(bid3b);
//        allBids.add(bid3c);
//
//        Bid[] mockBidList = {bid3b, bid3c};
//
//        //Build a list of tasks
//        Task task1 = new Task("id1", currentUser.getUsername(), "Demo Task 1", "A really good task");
//        Task task2 = new Task("id2", providerUserA.getUsername(), "Demo Task 2", "A really great task");
//        Task task3 = new Task("id3", providerUserA.getUsername(), currentUser.getUsername(), mockBidList, "Demo Task 3", "A alright task", Boolean.FALSE);
//
//        allTasks.add(task1);
//        allTasks.add(task2);
//        allTasks.add(task3);
    }

    /**
     * @return All users present in the data source.
     * @see User
     */
    @NonNull
    public final User[] getUsers()
    {
        return this.users.toArray(new User[0]);
    }

    /**
     * Adds a user to the data source.
     *
     * @param user The user to add.
     * @return Whether the user is present in the data source.
     * @see User
     */
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
     * @see User
     */
    public boolean removeUser(@NonNull final User user)
    {
        this.users.remove(user);

        return true;
    }

    /**
     * @return All tasks present in the data source.
     * @see Task
     */
    @NonNull
    public final Task[] getTasks()
    {
        return this.tasks.toArray(new Task[0]);
    }

    /**
     * Adds a task to the data source.
     *
     * @param task The task to add.
     * @return Whether the task is present in the data source.
     * @see Task
     */
    public boolean addTask(@NonNull final Task task)
    {
        this.tasks.add(task);

        return true;
    }

    /**
     * Removes a task from the data source.
     *
     * @param task The task to remove.
     * @return Whether the task is absent from the data source.
     * @see Task
     */
    public boolean removeTask(@NonNull final Task task)
    {
        this.tasks.remove(task);

        return true;
    }

    /**
     * @return All bids present in the data source.
     * @see Bid
     */
    @NonNull
    public final Bid[] getBids()
    {
        return this.bids.toArray(new Bid[0]);
    }

    /**
     * Adds a bid to the data source.
     *
     * @param bid The bid to add.
     * @return Whether the bid is present in the data source.
     * @see Bid
     */
    public boolean addBid(@NonNull final Bid bid)
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
     */
    public boolean removeBid(@NonNull final Bid bid)
    {
        this.bids.remove(bid);

        return true;
    }

    @Override
    public User getUserById(String elasticId)
    {
        // TODO implement
        return null;
    }

    @Override
    public User getUserByUsername(String username)
    {
        // TODO implement
        return null;
    }
}
