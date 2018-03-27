package ca.ualberta.cs.w18t11.whoselineisitanyway.model.datasource;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ca.ualberta.cs.w18t11.whoselineisitanyway.controllers.ElasticSearchBidController;
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
     * Constructor
     */
    public RemoteDataSource() {}

    /**
     * Gets a user from the database by their elastic id
     * @param elasticId User's elasticId
     * @return User if found. null if not found
     */
    public User getUserById(String elasticId)
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
     * Gets a user from the database
     * @param username User's username
     * @return User if found. null if not found
     */
    public User getUserByUsername(String username)
    {
        String query = "{" +
                "  \"query\": {" +
                "    \"match\": {" +
                "      \"username\": \"" + username + "\"" +
                "    }" +
                "  }" +
                "}";

        ElasticSearchUserController.GetUsersTask getUsersTask =
                new ElasticSearchUserController.GetUsersTask();
        getUsersTask.execute(query);

        try
        {
            ArrayList<User> users = getUsersTask.get();
            if (users != null && users.size() == 1)
            {
                return users.get(0);
            }
        }
        catch (InterruptedException e)
        {
            Log.i("RemoteDataSource.getUser", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("RemoteDataSource.getUser", "execution exception:" + e.toString());
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
            ArrayList<User> users = getAllUsersTask.get();
            return users.toArray(new User[users.size()]);
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
        return null;
    }

    /**
     * Adds a user to the database
     * @param user The user to add
     * @return true if user was successfully added to the database
     */
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

    /**
     * Removes a user from the database
     * @param user The user to remove.
     * @return
     */
    @Override
    public boolean removeUser(@NonNull User user) {
        ElasticSearchUserController.RemoveUserTask removeUserTask =
                new ElasticSearchUserController.RemoveUserTask();
        removeUserTask.execute(user);

        try
        {
            removeUserTask.get();
            return true;
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
            ArrayList<Task> tasks = getAllTasksTask.get();
            return tasks.toArray(new Task[tasks.size()]);
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
        return null;
    }

    /**
     * Adds a task to the database
     * @param task The task to add
     * @return true if task was successfully added to the database
     */
    @Override
    public boolean addTask(@NonNull Task task) {
        ElasticSearchTaskController.AddTasksTask addTaskTask =
                new ElasticSearchTaskController.AddTasksTask();
        addTaskTask.execute(task);

        try
        {
            if (addTaskTask.get() != null)
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

        ElasticSearchBidController.GetBidsTask getAllBidsTask
                = new ElasticSearchBidController.GetBidsTask();
        getAllBidsTask.execute(query);

        try
        {
            ArrayList<Bid> bids = getAllBidsTask.get();
            return bids.toArray(new Bid[bids.size()]);
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
        return null;
    }

    /**
     * Adds a bid to the database
     * @param bid The bid to add
     * @return true if bid was successfully added to the database
     */
    @Override
    public boolean addBid(@NonNull Bid bid) {
        ElasticSearchBidController.AddBidsTask addBidTask =
                new ElasticSearchBidController.AddBidsTask ();
        addBidTask.execute(bid);

        try
        {
            if (addBidTask.get() != null)
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
    public boolean removeBid(@NonNull Bid bid) {
        return false;
    }
}
