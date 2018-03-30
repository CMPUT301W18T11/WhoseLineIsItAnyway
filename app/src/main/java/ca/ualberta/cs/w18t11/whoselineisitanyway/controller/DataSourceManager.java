package ca.ualberta.cs.w18t11.whoselineisitanyway.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
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

    private Context mContext;

    private static final String DB_FILENAME = "Database_Shared_Preference_File";
    private static final String USER_CHANGES = "user_offline_changes";
    private static final String TASK_CHANGES = "task_offline_changes";
    private static final String BID_CHANGES = "bid_offline_changes";

    /**
     * Creates a DataSourceManager
     *
     * @see DataSource
     */
    private DataSourceManager(Context context)
    {
        this.remoteDataSource = new RemoteDataSource();
        this.localDataSource = new LocalDataSource(context);
        this.mContext = context;
    }

    /**
     * @return User[] array of all the current users
     */
    public User[] getAllUsers()
    {
        if (getOfflineUserChanges())
        {
            synchronizeDataSources();
        }

        User[] allUsers;
        if ((allUsers = localDataSource.getUsers()) != null)
        {
            return allUsers;
        }

        if ((allUsers = remoteDataSource.getUsers()) != null)
        {
            return allUsers;
        }

        return null;
    }

    public User getUser(String username)
    {
        if (getOfflineUserChanges())
        {
            synchronizeDataSources();
        }

        User[] users = localDataSource.getUsers();
        if (users != null)
        {
            for (User user : users)
            {
                if (user.getUsername().equals(username))
                {
                    return user;
                }
            }
        }

        User user;
        user = remoteDataSource.getUserByUsername(username);
        if (user != null)
        {
            return user;
        }

        return null;
    }

    /**
     * Adds the given user to the local data source.
     * Attempts to add the given user to the database.
     * Sets offline changes if adding to the database fails.
     *
     * @param user user to add to data sources
     * @return true if user was at least added to local data source, else false
     */
    public boolean addUser(User user)
    {
        // Attempt to add user locally
        if (localDataSource.addUser(user))
        {
            if (!remoteDataSource.addUser(user))
            {
                // Adding the user online failed. Trigger offline changes
                setOfflineUserChanges(true);
            }
            return true;
        }
        Log.i("DataSourceManager.addUser","Failed to add user" + user.getUsername());
        return false;
    }

    /**
     * Removes a user from the local data source.
     * Attempts to remove the user from the database.
     * Sets offline changes if removing from the database fails.
     *
     * @param user user to remove
     * @return true if user was at least removed from the local data source, else false
     */
    public boolean removeUser(User user)
    {
        if (localDataSource.removeUser(user))
        {
            if (!remoteDataSource.removeUser(user))
            {
                setOfflineUserChanges(true);
            }
            return true;
        }
        Log.i("DataSourceManager.addUser","Failed to remove user" + user.getUsername());
        return false;
    }

    /**
     * @return DataSourceManager the singleton copy of the data source manager
     */
    public static DataSourceManager getInstance(Context context)
    {
        if (DataSourceManager.instance != null)
        {
            return DataSourceManager.instance;
        }
        DataSourceManager.instance = new DataSourceManager(context);
        return DataSourceManager.instance;
    }

    /**
     * Synchronize and changes made to the local data source with the remote.
     *
     * @return boolean representing if the synchronize was successful
     */
    private void synchronizeDataSources()
    {
        Log.i("DataSourceManager",
                "Attempting to synchronize local and remote data sources...");

        // If there are offline changes, we need to synchronize the data sources
        if (getOfflineUserChanges())
        {
            // Synchronize the users
            if (synchronizeUserChanges())
            {
                setOfflineUserChanges(false);
            }
        }

        // Check for changes to the tasks
        if (getOfflineTaskChanges())
        {
            // Synchronize the users
            if (synchronizeTaskChanges())
            {
                setOfflineTaskChanges(false);
            }
        }

        // Check for changes to the bids
        if (getOfflineBidChanges())
        {
            // Synchronize the users
            if (synchronizeBidChanges())
            {
                setOfflineBidChanges(false);
            }
        }
    }

    private boolean synchronizeUserChanges()
    {
        User[] localUsers;
        if ((localUsers = localDataSource.getUsers()) != null)
        {
            for (User user : localUsers)
            {
                if (!remoteDataSource.addUser(user))
                {
                    // Adding user to the database failed because connection is still bad
                    // Terminate synchronization
                    Log.i("DataSourceManager.syncUsers", " Synchronization failed.");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean synchronizeTaskChanges()
    {
        Task[] localTasks;
        if ((localTasks = localDataSource.getTasks()) != null)
        {
            for (Task task: localTasks)
            {
                if (!remoteDataSource.addTask(task))
                {
                    // Adding user to the database failed because connection is still bad
                    // Terminate synchronization
                    Log.i("DataSourceManager.syncTasks", " Synchronization failed.");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean synchronizeBidChanges()
    {
        Bid[] localBids;
        if ((localBids = localDataSource.getBids()) != null)
        {
            for (Bid bid : localBids)
            {
                if (!remoteDataSource.addBid(bid))
                {
                    // Adding user to the database failed because connection is still bad
                    // Terminate synchronization
                    Log.i("DataSourceManager.syncUsers", " Synchronization failed.");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Updates the database shared preference file to indicate if offline
     * changes are present for Users
     *
     * @param yesno true if offline changes are present for Users, else false
     */
    private void setOfflineUserChanges(boolean yesno)
    {
        SharedPreferences sp = mContext.getSharedPreferences(DB_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(USER_CHANGES, yesno);
        editor.apply();
    }

    /**
     * Updates the database shared preference file to indicate if offline
     * changes are present for Tasks
     *
     * @param yesno true if offline changes are present for Tasks, else false
     */
    private void setOfflineTaskChanges(boolean yesno)
    {
        SharedPreferences sp = mContext.getSharedPreferences(DB_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(TASK_CHANGES, yesno);
        editor.apply();
    }

    /**
     * Updates the database shared preference file to indicate if offline
     * changes are present for Bids
     *
     * @param yesno true if offline changes are present for Bids, else false
     */
    private void setOfflineBidChanges(boolean yesno)
    {
        SharedPreferences sp = mContext.getSharedPreferences(DB_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(BID_CHANGES, yesno);
        editor.apply();
    }

    /**
     * Checks if there are offline changes to the Users triggered in the database shared
     * preference file
     *
     * @return true if offline changes present for Users, else false
     */
    private boolean getOfflineUserChanges()
    {
        SharedPreferences sp =  mContext.getSharedPreferences(DB_FILENAME, Context.MODE_PRIVATE);
        return sp.getBoolean(USER_CHANGES, false);
    }

    /**
     * Checks if there are offline changes to the Tasks triggered in the database shared
     * preference file
     *
     * @return true if offline changes present for Tasks, else false
     */
    private boolean getOfflineTaskChanges()
    {
        SharedPreferences sp =  mContext.getSharedPreferences(DB_FILENAME, Context.MODE_PRIVATE);
        return sp.getBoolean(TASK_CHANGES, false);
    }

    /**
     * Checks if there are offline changes to the Bids triggered in the database shared
     * preference file
     *
     * @return true if offline changes present for Bid, else false
     */
    private boolean getOfflineBidChanges()
    {
        SharedPreferences sp =  mContext.getSharedPreferences(DB_FILENAME, Context.MODE_PRIVATE);
        return sp.getBoolean(BID_CHANGES, false);
    }

    /**
     * Indicate who the current user is
     */
    public void setCurrentUser(String username)
    {
        User[] users = localDataSource.getUsers();
        if (users != null)
        {
            for (User user : users)
            {
                if (user.getUsername().equals(username))
                {
                    currentUser = user;
                    return;
                }
            }
        }

        User user;
        user = remoteDataSource.getUserByUsername(username);
        if (user != null)
        {
            currentUser = user;
            return;
        }

        // TODO: Create user if they don't exist
        Log.i("DataSourceManager: ", "User not set");
    }

    /**
     * Indicate who the current user is
     */
    public void setCurrentUser(User user) { currentUser = user; }

    /**
     * @return User who the current user is
     */
    public User getCurrentUser()
    {
        return currentUser;
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
}
