package ca.ualberta.cs.w18t11.whoselineisitanyway.model.datasource;

import android.util.Log;

import java.math.BigDecimal;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.EmailAddress;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.PhoneNumber;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * Manages the remote and local data sources used withing the application
 *
 * @author Brad Ofrim, Samuel Dolha
 * @version 1.1
 */
public class DataSourceManager
{

    /**
     * Singleton instance
     */
    private static DataSourceManager instance;

    /**
     * The server
     */
    private DataSource remoteDataSource;

    /**
     * A local copy of data used when remote is not accessible
     */
    private DataSource localDataSource;

    /**
     * Indication of who the current user is
     */
    private User currentUser;

    //TODO: Ensure all commands check if we can grab or set data in remote before using local

    /**
     * Creates a DataSourceManager
     *
     * @see DataSource
     */
    private DataSourceManager()
    {
        final User[] users = new User[]{
                new User(new EmailAddress("bob", "gmail.com"), new PhoneNumber(0, 123, 456, 7890),
                        "bob"),
                new User(new EmailAddress("alice", "gmail.com"), new PhoneNumber(0, 123, 456, 7890),
                        "alice"),
                new User(new EmailAddress("eve", "gmail.com"), new PhoneNumber(0, 123, 456, 7890),
                        "eve")
        };
        final Bid[] bids = new Bid[]{
                new Bid(users[1].getUsername(), "id1", new BigDecimal(5)),
                new Bid(users[2].getUsername(), "id1", new BigDecimal(6)),
                new Bid(users[2].getUsername(), "id2", new BigDecimal(500)),
                new Bid(users[0].getUsername(), "id2", new BigDecimal(750)),
                new Bid(users[2].getUsername(), "id3", new BigDecimal(5)),
                new Bid(users[0].getUsername(), "id3", new BigDecimal(7))
        };
        final Task[] tasks = new Task[]{
                new Task("id1", users[0].getUsername(), "Demo Task 1", "A really good task"),
                new Task("id2", users[1].getUsername(), "Demo Task 2", "A really great task"),
                new Task("id3", users[1].getUsername(), users[0].getUsername(),
                        new Bid[]{bids[4], bids[5]}, "Demo Task 3", "A alright task", false)
        };

        this.remoteDataSource = new MockDataSource(users, tasks, bids);
        this.localDataSource = new MockDataSource(users, tasks, bids);
    }

    /**
     * @return DataSourceManager the singleton copy of the data source manager
     */
    public static DataSourceManager getInstance()
    {
        if (DataSourceManager.instance != null)
        {
            return DataSourceManager.instance;
        }
        DataSourceManager.instance = new DataSourceManager();
        return DataSourceManager.instance;
    }

    /**
     * Synchronize and changes made to the local data source with the remote.
     *
     * @return boolean representing if the synchronize was successful
     */
    public boolean synchronizeDataSources()
    {
        // TODO: Implement
        return true;
    }

    /**
     * @return DataSource the  copy of the local data source
     */
    public DataSource getLocalDataSource()
    {
        return localDataSource;
    }

    /**
     * @return DataSource the  copy of the remote data source
     */
    public DataSource getRemoteDataSource()
    {
        return remoteDataSource;
    }

    /**
     * Indicate who the current user is
     */
    public void setCurrentUser(String username)
    {
        // TODO: make this check remote first
        for (User user : getLocalDataSource().getUsers())
        {
            if (user.getUsername().equals(username))
            {
                currentUser = user;
                return;
            }
        }
        // TODO: Create user if they don't exist
        Log.i("DataSourceManager: ", "User not set");
    }

    /**
     * @return User who the current user is
     */
    public User getCurrentUser()
    {
        return currentUser;
    }
}
