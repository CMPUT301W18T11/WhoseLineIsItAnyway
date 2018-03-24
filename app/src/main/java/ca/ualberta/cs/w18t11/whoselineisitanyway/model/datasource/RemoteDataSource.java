package ca.ualberta.cs.w18t11.whoselineisitanyway.model.datasource;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ca.ualberta.cs.w18t11.whoselineisitanyway.controllers.ElasticSearchTaskController;
import ca.ualberta.cs.w18t11.whoselineisitanyway.controllers.ElasticSearchUserController;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * Data source for managing data in the database
 *
 * @author Mark Griffith
 * @version 1.0
 */
public class RemoteDataSource implements DataSource
{
    /**
     * Gets a user from the database by their elastic id
     * @param elasticId User's elasticId
     * @return User if found. null if not found
     */
    public static User getUserById(String elasticId)
    {
        ElasticSearchUserController.GetUserByIdTask getUserTask =
                new ElasticSearchUserController.GetUserByIdTask();
        getUserTask.execute(elasticId);

        try
        {
            return getUserTask.get();
        }
        catch (InterruptedException e)
        {
            Log.i("RemoteDataSource.getUserById", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("RemoteDataSource.getUserById", "execution exception:" + e.toString());
        }
        return null;
    }

    /**
     * Gets all the users from the database if connected
     * @return Array of Users
     */
    @NonNull
    @Override
    public User[] getUsers()
    {
        // TODO test if this gets all the users.
        String query = "{" +
                "  \"from\" :0, \"size\" : 5000," +
                "  \"query\": {" +
                "    \"match_all\": {}" +
                "    }" +
                "}";

        ElasticSearchUserController.GetUsersTask getAllUsersTask
                = new ElasticSearchUserController.GetUsersTask();
        getAllUsersTask.execute(query);

        try
        {
            return (User[]) getAllUsersTask.get().toArray();
        }
        catch (InterruptedException e)
        {
            Log.i("RemoteDataSource.getUsers", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("RemoteDataSource.getUsers", "execution exception:" + e.toString());
        }

        // TODO signal dataSourceManager to get offline users
        // If get() fails to return an array, that means we are not connected
        // to the network. This needs to be somehow handled.
    }

    @Override
    public boolean addUser(@NonNull User user) {
        ElasticSearchUserController.AddUsersTask addUserTask =
                new ElasticSearchUserController.AddUsersTask();
        addUserTask.execute(user);

        try
        {
            if (addUserTask.get() != null)
            {
                return true;
            }
        }
        catch (InterruptedException e)
        {
            Log.i("RemoteDataSource.addUser", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("UserLogin.addUser", "execution exception:" + e.toString());
        }
        return false;
    }

    @Override
    public boolean removeUser(@NonNull User user) {
        return false;
    }

    /**
     * Gets all the tasks from the database if connected
     * @return Array of Tasks
     */
    @NonNull
    @Override
    public Task[] getTasks()
    {
        // TODO test this
        String query = "{" +
                "  \"from\" :0, \"size\" : 5000," +
                "  \"query\": {" +
                "    \"match_all\": {}" +
                "    }" +
                "}";

        ElasticSearchTaskController.GetTaskssTask getAllTasksTask
                = new ElasticSearchTaskController.GetTaskssTask();
        getAllTasksTask.execute(query);

        try
        {
            return (Task[]) getAllTasksTask.get().toArray();
        }
        catch (InterruptedException e)
        {
            Log.i("RemoteDataSource.getTasks", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("RemoteDataSource.getTasks", "execution exception:" + e.toString());
        }

        // TODO signal dataSourceManager to get offline tasks
        // If get() fails to return an array, that means we are not connected
        // to the network. This needs to be somehow handled.
    }

    @Override
    public boolean addTask(@NonNull Task task) {
        return false;
    }

    @Override
    public boolean removeTask(@NonNull Task task) {
        return false;
    }

    /**
     * Gets all the bids from the database if connected
     * @return Array of Bids
     */
    @NonNull
    @Override
    public Bid[] getBids()
    {
        // TODO test this
        String query = "{" +
                "  \"from\" :0, \"size\" : 5000," +
                "  \"query\": {" +
                "    \"match_all\": {}" +
                "    }" +
                "}";

        ElasticSearchTaskController.GetTaskssTask getAllBidsTask
                = new ElasticSearchTaskController.GetTaskssTask();
        getAllBidsTask.execute(query);

        try
        {
            return (Bid[]) getAllBidsTask.get().toArray();
        }
        catch (InterruptedException e)
        {
            Log.i("RemoteDataSource.getBids", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("RemoteDataSource.getBids", "execution exception:" + e.toString());
        }

        // TODO signal dataSourceManager to get offline bids
        // If get() fails to return an array, that means we are not connected
        // to the network. This needs to be somehow handled.
    }

    @Override
    public boolean addBid(@NonNull Bid bid) {
        return false;
    }

    @Override
    public boolean removeBid(@NonNull Bid bid) {
        return false;
    }
}
