package ca.ualberta.cs.w18t11.whoselineisitanyway.model.datasource;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
     * @return List of Users
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
            Log.i("RemoteDataSource.getAllUsers", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("RemoteDataSource.getAllUsers", "execution exception:" + e.toString());
        }

        // TODO signal dataSourceManager to get offline users
        // If get() fails to return an array, that means we are not connected
        // to the network. This needs to be somehow handled.
    }

    @Override
    public boolean addUser(@NonNull User user) {
        return false;
    }

    @Override
    public boolean removeUser(@NonNull User user) {
        return false;
    }

    @NonNull
    @Override
    public Task[] getTasks() {
        return new Task[0];
    }

    @Override
    public boolean addTask(@NonNull Task task) {
        return false;
    }

    @Override
    public boolean removeTask(@NonNull Task task) {
        return false;
    }

    @NonNull
    @Override
    public Bid[] getBids() {
        return new Bid[0];
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
