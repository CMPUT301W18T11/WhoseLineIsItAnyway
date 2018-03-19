package ca.ualberta.cs.w18t11.whoselineisitanyway.model.datasource;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * A simple interface to consolidate how data is supplied within the application
 *
 * @author Brad Ofrim
 * @version 1.0
 */
public interface DataSource {

    /**
     * @return ArrayList containing all of the tasks
     */
    Task[] getAllTasks();

    /**
     * @return ArrayList containing all of the bids
     */
    Bid[] getAllBids();

    /**
     * @return ArrayList containing all of the users
     */
    User[] getAllUsers();

    /**
     * Add a new task to the data source
     * Return Boolean representing if the addition was succesful
     */
    Boolean addTask(Task task);

    /**
     * Add a new bid to the data source
     * Return Boolean representing if the addition was succesful
     */
    Boolean addBid(Bid bid);

    /**
     * Add a new user to the data source
     * Return Boolean representing if the addition was succesful
     */
    Boolean addUser(User user);

    /**
     * Remove a  task from the data source
     * Return Boolean representing if the remove was succesful
     */
    Boolean removeTask(Task task);

    /**
     * Remove a bid from the data source
     * Return Boolean representing if the addition was succesful
     */
    Boolean removeBid(Bid bid);

    /**
     * Remove user from the data source
     * Return Boolean representing if the addition was succesful
     */
    Boolean removeUser(User user);
}
