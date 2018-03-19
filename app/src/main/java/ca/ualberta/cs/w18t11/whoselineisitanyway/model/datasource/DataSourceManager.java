package ca.ualberta.cs.w18t11.whoselineisitanyway.model.datasource;

import android.util.Log;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * Manages the remote and local data sources used withing the application
 *
 * @author Brad Ofrim
 * @version 1.0
 */
public class DataSourceManager {

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
        this.remoteDataSource = new MockDataSource();
        this.localDataSource = new MockDataSource();
    }

    /**
     * @return DataSourceManager the singleton copy of the data source manager
     */
    public static DataSourceManager getInstance()
    {
        if(DataSourceManager.instance != null)
        {
            return DataSourceManager.instance;
        }
        DataSourceManager.instance = new DataSourceManager();
        return DataSourceManager.instance;
    }

    /**
     * Synchronize and changes made to the local data source with the remote.
     * @return boolean representing if the synchronize was successful
     */
    public boolean synchronizeDataSources(){
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
        for(User user: getLocalDataSource().getAllUsers())
        {
            if(user.getUsername().equals(username))
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
