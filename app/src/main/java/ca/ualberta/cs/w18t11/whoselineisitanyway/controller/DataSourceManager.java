package ca.ualberta.cs.w18t11.whoselineisitanyway.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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
        if (getOfflineChanges())
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
        if (getOfflineChanges())
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
                setOfflineChanges(true);
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
                setOfflineChanges(true);
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
     * Updates the database shared preference file to indicate if offline
     * changes are present or not
     *
     * @param yesno true if offline changes are present, else false
     */
    private void setOfflineChanges(boolean yesno)
    {
        SharedPreferences sp = mContext.getSharedPreferences(DB_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("offline_changes", yesno);
        editor.apply();
    }

    /**
     * Checks if there are offline changes triggered in the database shared
     * preference file
     *
     * @return true if offline changes present, else false
     */
    private boolean getOfflineChanges()
    {
        SharedPreferences sp =  mContext.getSharedPreferences(DB_FILENAME, Context.MODE_PRIVATE);
        return sp.getBoolean("offline_changes", false);
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

        // TODO: Implement for Tasks and Bids

        // If there are offline changes, we need to synchronize the data sources
        if (getOfflineChanges() == true)
        {
            // Synchronize the users
            User[] localUsers;
            if ((localUsers = localDataSource.getUsers()) != null)
            {
                for (User user : localUsers)
                {
                    if (!remoteDataSource.addUser(user))
                    {
                        // Adding user to the database failed because connection is still bad
                        // Terminate synchronization
                        Log.i("DataSourceManager.sync", " Synchronization failed.");
                        return;
                    }
                }
                // Make sure to set offline changes to false after syncing
                setOfflineChanges(false);
            }
        }
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
}
