package ca.ualberta.cs.w18t11.whoselineisitanyway.controller;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
     *
     * @param elasticId User's elasticId
     * @return User if found. null if not found
     */
    public User getUserById(String elasticId)
    {
        ElasticsearchUserController.GetUserByIdTask getUserTask =
                new ElasticsearchUserController.GetUserByIdTask();
        getUserTask.execute(elasticId);

        try
        {
            return getUserTask.get();
        }
        catch (InterruptedException e)
        {
            Log.i("RemoteDataSource", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("RemoteDataSource", "execution exception:" + e.toString());
        }
        return null;
    }

    /**
     * Gets all the users from the database if connected
     *
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

        ElasticsearchUserController.GetUsersTask getAllUsersTask
                = new ElasticsearchUserController.GetUsersTask();
        getAllUsersTask.execute(query);

        try
        {
            ArrayList<User> users = getAllUsersTask.get();

            if (users == null)
            {
                return new User[0];
            }

            return users.toArray(new User[users.size()]);
        }
        catch (InterruptedException e)
        {
            Log.i("RemoteDataSource", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("RemoteDataSource", "execution exception:" + e.toString());
        }
        return null;
    }

    /**
     * Gets a user from the database
     *
     * @param username User's username
     * @return User if found. null if not found
     */
    @Nullable
    @Override
    public User getUser(@NonNull final String username) throws IllegalArgumentException
    {
        String query = "{" +
                "  \"query\": {" +
                "    \"match\": {" +
                "      \"username\": \"" + username + "\"" +
                "    }" +
                "  }" +
                "}";

        ElasticsearchUserController.GetUsersTask getUsersTask =
                new ElasticsearchUserController.GetUsersTask();
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
            Log.i("RemoteDataSource", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("RemoteDataSource", "execution exception:" + e.toString());
        }
        return null;
    }

    /**
     * Adds a user to the database
     *
     * @param user The user to add
     * @return true if user was successfully added to the database
     */
    @Override
    public boolean addUser(@NonNull User user)
    {
        ElasticsearchUserController.AddUsersTask addUserTask =
                new ElasticsearchUserController.AddUsersTask();
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
            Log.i("RemoteDataSource", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("UserLogin", "execution exception:" + e.toString());
        }
        return false;
    }

    /**
     * Removes a user from the database
     *
     * @param user The user to remove.
     * @return
     */
    @Override
    public boolean removeUser(@NonNull User user)
    {
        ElasticsearchUserController.RemoveUserTask removeUserTask =
                new ElasticsearchUserController.RemoveUserTask();
        removeUserTask.execute(user);

        try
        {
            removeUserTask.get();
            return true;
        }
        catch (InterruptedException e)
        {
            Log.i("RemoteDataSource", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("UserLogin.addUser", "execution exception:" + e.toString());
        }
        return false;
    }

    @Override
    public boolean clearUsers()
    {
        return false;
    }

    /**
     * Gets all the tasks from the database if connected
     *
     * @return Array of Tasks
     */
    @NonNull
    @Override
    public Task[] getTasks()
    {
        String query = "{" +
                "  \"from\" :0, \"size\" : 5000," +
                "  \"query\": {" +
                "    \"match_all\": {}" +
                "    }" +
                "}";

        ElasticsearchTaskController.GetTasksTask getAllTasksTask
                = new ElasticsearchTaskController.GetTasksTask();
        getAllTasksTask.execute(query);

        try
        {
            ArrayList<Task> tasks = getAllTasksTask.get();

            if (tasks == null)
            {
                return new Task[0];
            }

            return tasks.toArray(new Task[tasks.size()]);
        }
        catch (InterruptedException e)
        {
            Log.i("RemoteDataSource", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("RemoteDataSource", "execution exception:" + e.toString());
        }
        return null;
    }

    @Nullable
    @Override
    public Task getTask(@NonNull final String requesterUsername, @NonNull final String title)
            throws IllegalArgumentException
    {
        String query = "{" +
                "  \"query\": {" +
                "    \"match\": {" +
                "      \"requesterUsername\": \"" + requesterUsername + "\"" +
                "      \"title\": \"" + title + "\"" +
                "    }" +
                "  }" +
                "}";

        ElasticsearchTaskController.GetTasksTask getTasksTask =
                new ElasticsearchTaskController.GetTasksTask();
        getTasksTask.execute(query);

        try
        {
            ArrayList<Task> task = getTasksTask.get();
            if (task != null && task.size() == 1)
            {
                return task.get(0);
            }
        }
        catch (InterruptedException e)
        {
            Log.i("RemoteDataSource", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("RemoteDataSource", "execution exception:" + e.toString());
        }
        return null;
    }

    @Nullable
    @Override
    public Task getTask(@NonNull final String taskId)
            throws IllegalArgumentException
    {
        ElasticsearchTaskController.GetTaskByIdTask getTaskTask =
                new ElasticsearchTaskController.GetTaskByIdTask();
        getTaskTask.execute(taskId);

        try
        {
            return getTaskTask.get();
        }
        catch (InterruptedException e)
        {
            Log.i("RemoteDataSource", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("RemoteDataSource", "execution exception:" + e.toString());
        }
        return null;
    }

    /**
     * Adds a task to the database
     *
     * @param task The task to add
     * @return true if task was successfully added to the database
     */
    @Override
    public boolean addTask(@NonNull Task task)
    {
        ElasticsearchTaskController.AddTasksTask addTaskTask =
                new ElasticsearchTaskController.AddTasksTask();
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
            Log.i("RemoteDataSource", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("UserLogin.addUser", "execution exception:" + e.toString());
        }
        return false;
    }

    @Override
    public boolean removeTask(@NonNull Task task)
    {
        if(task.getBids() != null)
        {
            for(Bid bid: task.getBids())
            {
                removeBid(bid);
            }
        }

        ElasticsearchTaskController.RemoveTaskTask removeTaskTask =
                new ElasticsearchTaskController.RemoveTaskTask();
        removeTaskTask.execute(task);

        try
        {
            removeTaskTask.get();
            return true;
        }
        catch (InterruptedException e)
        {
            Log.i("RemoteDataSource", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("UserLogin", "execution exception:" + e.toString());
        }
        return false;
    }

    @Override
    public boolean clearTasks()
    {
        return false;
    }

    /**
     * Gets all the bids from the database if connected
     *
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

        ElasticsearchBidController.GetBidsTask getAllBidsTask
                = new ElasticsearchBidController.GetBidsTask();
        getAllBidsTask.execute(query);

        try
        {
            ArrayList<Bid> bids = getAllBidsTask.get();

            if (bids == null)
            {
                return new Bid[0];
            }

            return bids.toArray(new Bid[bids.size()]);
        }
        catch (InterruptedException e)
        {
            Log.i("RemoteDataSource", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("RemoteDataSource", "execution exception:" + e.toString());
        }
        return null;
    }

    @Nullable
    @Override
    public Bid getBid(@NonNull final String providerUsername, @NonNull final String taskId)
            throws IllegalArgumentException
    {
        String query = "{" +
                "  \"query\": {" +
                "    \"match\": {" +
                "      \"providerUsername\": \"" + providerUsername + "\"" +
                "      \"taskId\": \"" + taskId + "\"" +
                "    }" +
                "  }" +
                "}";

        ElasticsearchBidController.GetBidsTask getBidsTask =
                new ElasticsearchBidController.GetBidsTask();
        getBidsTask.execute(query);

        try
        {
            ArrayList<Bid> bid = getBidsTask.get();
            if (bid != null && bid.size() == 1)
            {
                return bid.get(0);
            }
        }
        catch (InterruptedException e)
        {
            Log.i("RemoteDataSource", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("RemoteDataSource", "execution exception:" + e.toString());
        }
        return null;
    }

    /**
     * Adds a bid to the database
     *
     * @param bid The bid to add
     * @return true if bid was successfully added to the database
     */
    @Override
    public boolean addBid(@NonNull Bid bid)
    {
        ElasticsearchBidController.AddBidsTask addBidTask =
                new ElasticsearchBidController.AddBidsTask();
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
            Log.i("RemoteDataSource", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("UserLogin", "execution exception:" + e.toString());
        }
        return false;
    }

    @Override
    public boolean removeBid(@NonNull Bid bid)
    {
        ElasticsearchBidController.RemoveBidTask removeBidTask =
                new ElasticsearchBidController.RemoveBidTask();
        removeBidTask.execute(bid);

        try
        {
            removeBidTask.get();
            return true;
        }
        catch (InterruptedException e)
        {
            Log.i("RemoteDataSource", "interrupted:" + e.toString());
        }
        catch (ExecutionException e)
        {
            Log.i("UserLogin", "execution exception:" + e.toString());
        }
        return false;
    }

    @Override
    public boolean clearBids()
    {
        return false;
    }
}
