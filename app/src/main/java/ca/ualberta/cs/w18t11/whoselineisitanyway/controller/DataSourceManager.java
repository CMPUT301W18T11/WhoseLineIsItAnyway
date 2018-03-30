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
 * @author Brad Ofrim, Samuel Dolha, Mark Griffith
 * @version 2.0
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
        synchronizeUserChanges();

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

    /**
     * @return Task[] array of all the current tasks
     */
    public Task[] getAllTasks()
    {
        synchronizeTaskChanges();

        Task[] allTasks;
        if ((allTasks = localDataSource.getTasks()) != null)
        {
            return allTasks;
        }

        if ((allTasks= remoteDataSource.getTasks()) != null)
        {
            return allTasks;
        }

        return null;
    }

    /**
     * @return Bids[] array of all the current users
     */
    public Bid[] getAllBids()
    {
        synchronizeBidChanges();

        Bid[] allBids;
        if ((allBids = localDataSource.getBids()) != null)
        {
            return allBids;
        }

        if ((allBids = remoteDataSource.getBids()) != null)
        {
            return allBids;
        }

        return null;
    }

    /**
     * @param username
     * @return the user with the username if found, else null
     */
    public User getUser(String username)
    {
        synchronizeUserChanges();

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
     * Finds and returns a task based on it's requester's username and the task's title
     *
     * @param requesterUsername username of the task's requester
     * @param taskTitle title of the desired task
     * @return Task if found, else null
     */
    public Task getTask(String requesterUsername, String taskTitle)
    {
        synchronizeTaskChanges();

        Task[] tasks = localDataSource.getTasks();
        if (tasks != null)
        {
            for (Task task : tasks)
            {
                if (task.getRequesterUsername().equals(requesterUsername) &&
                        task.getTitle().equals(taskTitle))
                {
                    return task;
                }
            }
        }

        // TODO implement ElasticSearchTaskController getTasksByTitle async task
//        tasks = remoteDataSource.getTasksByTitle(taskTitle);
//        for (Task task : tasks)
//        {
//            if (task.getRequesterUsername().equals(requesterUsername))
//            {
//                return task;
//            }
//        }

        return null;
    }

    /**
     * Finds and returns a bid based on it's provider's username and the bid's id
     *
     * @param providerUsername username of the bid's provider
     * @param taskId id of the task on which the bid was made
     * @return Bid if success, else null
     */
    public Bid getBid(String providerUsername, String taskId)
    {
        synchronizeBidChanges();

        Bid[] bids = localDataSource.getBids();
        if (bids != null)
        {
            for (Bid bid : bids)
            {
                if (bid.getProviderUsername().equals(providerUsername) &&
                        bid.getTaskId().equals(taskId))
                {
                    return bid;
                }
            }
        }

        // TODO implement ElasticsearchBidController getBidsByTaskId async task
//        bids = remoteDataSource.getBidsByTaskId(taskId);
//        if (bids != null)
//        {
//            for (Bid bid : bids)
//            {
//                if (bid.getProviderUsername().equals(providerUsername))
//                {
//                    return bid;
//                }
//            }
//        }

        return null;
    }

    /**
     * Adds the given user to the local data source.
     * Attempts to add the given user to the database.
     * Triggers offline changes if adding to the database fails.
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
     * Adds the given task to the local data source.
     * Attempts to add the given task to the database.
     * Triggers offline changes if adding to the database fails.
     *
     * @param task task to add to data sources
     * @return true if user was at least added to local data source, else false
     */
    public boolean addTask(Task task)
    {
        // Attempt to add user locally
        if (localDataSource.addTask(task))
        {
            if (!remoteDataSource.addTask(task))
            {
                // Adding the user online failed. Trigger offline changes
                setOfflineTaskChanges(true);
            }
            return true;
        }
        Log.i("DataSourceManager.addUser","Failed to add task" + task.getTitle());
        return false;
    }

    /**
     * Adds the given bid to the local data source.
     * Attempts to add the given bid to the database.
     * Trigger offline changes if adding to the database fails.
     *
     * @param bid bid to add to data sources
     * @return true if user was at least added to local data source, else false
     */
    public boolean addBid(Bid bid)
    {
        // Attempt to add user locally
        if (localDataSource.addBid(bid))
        {
            if (!remoteDataSource.addBid(bid))
            {
                // Adding the user online failed. Trigger offline changes
                setOfflineBidChanges(true);
            }
            return true;
        }
        Log.i("DataSourceManager.addUser","Failed to add bid" + bid.getValue());
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
     * Removes a task from the local data source.
     * Attempts to remove the task from the database.
     * Triggers offline changes if removing from the database fails.
     *
     * @param task task to remove
     * @return true if task was at least removed from the local data source, else false
     */
    public boolean removeTask(Task task)
    {
        if (localDataSource.removeTask(task))
        {
            if (!remoteDataSource.removeTask(task))
            {
                setOfflineUserChanges(true);
            }
            return true;
        }
        Log.i("DataSourceManager.addUser","Failed to remove user" + task.getTitle());
        return false;
    }

    /**
     * Removes a bid from the local data source.
     * Attempts to remove the bid from the database.
     * Triggers offline changes if removing from the database fails.
     *
     * @param bid bid to remove
     * @return true if bid was at least removed from the local data source, else false
     */
    public boolean removeUser(Bid bid)
    {
        if (localDataSource.removeBid(bid))
        {
            if (!remoteDataSource.removeBid(bid))
            {
                setOfflineUserChanges(true);
            }
            return true;
        }
        Log.i("DataSourceManager.addUser","Failed to remove user" + bid.getValue());
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
     * Synchronizes the users in the localDataSource to the remoteDataSource.
     *
     * @return boolean true if sync was successful, else false
     */
    private void synchronizeUserChanges()
    {
        if (getOfflineUserChanges())
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
                        return;
                    }
                }
            }
            Log.i("DataSourceManager.syncUsers", " Synchronization success!");
            setOfflineUserChanges(false);
        }
    }
    /**
     * Synchronizes the tasks in the localDataSource to the remoteDataSource.
     *
     * @return boolean true if sync was successful, else false
     */
    private void synchronizeTaskChanges()
    {
        if (getOfflineTaskChanges())
        {
            Task[] localTasks;
            if ((localTasks = localDataSource.getTasks()) != null)
            {
                for (Task task : localTasks)
                {
                    if (!remoteDataSource.addTask(task))
                    {
                        // Adding user to the database failed because connection is still bad
                        // Terminate synchronization
                        Log.i("DataSourceManager.syncTasks", " Synchronization failed.");
                        return;
                    }
                }
            }
            Log.i("DataSourceManager.syncTasks", " Synchronization success!");
            setOfflineTaskChanges(false);
        }
    }

    /**
     * Synchronizes the bids in the localDataSource to the remoteDataSource.
     *
     * @return boolean true if sync was successful, else false
     */
    private void synchronizeBidChanges()
    {
        if (getOfflineBidChanges())
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
                        Log.i("DataSourceManager.syncBids", " Synchronization failed.");
                        return;
                    }
                }
            }
            Log.i("DataSourceManager.syncBids", " Synchronization success!");
            setOfflineBidChanges(false);
        }
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
