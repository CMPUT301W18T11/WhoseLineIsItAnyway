package ca.ualberta.cs.w18t11.whoselineisitanyway.model.datasource;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * A simple interface to consilidate how data is supplied within the application
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
}
