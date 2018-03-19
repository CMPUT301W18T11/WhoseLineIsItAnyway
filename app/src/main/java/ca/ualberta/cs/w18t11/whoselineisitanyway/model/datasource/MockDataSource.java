package ca.ualberta.cs.w18t11.whoselineisitanyway.model.datasource;

import java.math.BigDecimal;
import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.EmailAddress;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.PhoneNumber;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * Mock data used for testing and demo purposes
 *
 * @author Brad Ofrim
 * @version 1.0
 */
public class MockDataSource implements DataSource {

    private ArrayList<Task> allTasks = new ArrayList<>();
    private ArrayList<Bid> allBids = new ArrayList<>();
    private ArrayList<User> allUsers = new ArrayList<>();

    /**
     * Build a bunch of test data.
     */
    public MockDataSource()
    {

        // Build a list of users
        User currentUser = new User(new EmailAddress("bob", "gmail.com"),
                new PhoneNumber(0, 123, 456, 7890), "bob");
        User providerUserA = new User(new EmailAddress("alice", "gmail.com"),
                new PhoneNumber(0, 123, 456, 7890), "alice");
        User providerUserB = new User(new EmailAddress("eve", "gmail.com"),
                new PhoneNumber(0, 123, 456, 7890), "eve");

        allUsers.add(currentUser);
        allUsers.add(providerUserA);
        allUsers.add(providerUserB);

        // Build a list of bids
        Bid bid1a = new Bid(providerUserA.getUsername(), "task1ID", new BigDecimal(5));
        Bid bid1b = new Bid(providerUserB.getUsername(), "task1ID", new BigDecimal(6));
        Bid bid2b = new Bid(providerUserB.getUsername(), "task2ID", new BigDecimal(500));
        Bid bid2c = new Bid(currentUser.getUsername(), "task2ID", new BigDecimal(750));
        Bid bid3b = new Bid(providerUserB.getUsername(), "task3ID", new BigDecimal(5));
        Bid bid3c = new Bid(currentUser.getUsername(), "task3ID", new BigDecimal(7));

        allBids.add(bid1a);
        allBids.add(bid1b);
        allBids.add(bid2b);
        allBids.add(bid2c);
        allBids.add(bid3b);
        allBids.add(bid3c);

        Bid[] mockBidList = {bid3b, bid3c};

        //Build a list of tasks
        Task task1 = new Task("id1", currentUser.getUsername(), "Demo Task 1", "A really good task");
        Task task2 = new Task("id2", providerUserA.getUsername(), "Demo Task 2", "A really great task");
        Task task3 = new Task("id3", providerUserA.getUsername(), currentUser.getUsername(), mockBidList, "Demo Task 3", "A alright task", Boolean.FALSE);

        allTasks.add(task1);
        allTasks.add(task2);
        allTasks.add(task3);
    }

    /**
     * @return ArrayList containing all of the tasks
     */
    public Task[] getAllTasks() {
        return allTasks.toArray(new Task[0]);
    }

    /**
     * @return ArrayList containing all of the bids
     */
    public Bid[] getAllBids() {
        return allBids.toArray(new Bid[0]);
    }

    /**
     * @return ArrayList containing all of the users
     */
    public User[] getAllUsers() {
        return allUsers.toArray(new User[0]);
    }

    /**
     * Add a new task to the data source
     * Return Boolean representing if the addition was succesful
     */
    public Boolean addTask(Task task) {
        return true;
    }

    /**
     * Add a new bid to the data source
     * Return Boolean representing if the addition was succesful
     */
    public Boolean addBid(Bid bid) {
        return true;
    }

    /**
     * Add a new user to the data source
     * Return Boolean representing if the addition was succesful
     */
    public Boolean addUser(User user) {
        return true;
    }

    /**
     * Remove a  task from the data source
     * Return Boolean representing if the remove was succesful
     */
    public Boolean removeTask(Task task) {
        return true;
    }

    /**
     * Remove a bid from the data source
     * Return Boolean representing if the addition was succesful
     */
    public Boolean removeBid(Bid bid) {
        return true;
    }

    /**
     * Remove user from the data source
     * Return Boolean representing if the addition was succesful
     */
    public Boolean removeUser(User user) {
        return true;
    }
}
