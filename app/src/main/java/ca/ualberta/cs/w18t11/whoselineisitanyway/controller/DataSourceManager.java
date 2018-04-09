package ca.ualberta.cs.w18t11.whoselineisitanyway.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * Manages offline and online data synchronization.
 *
 * @author Brad Ofrim, Samuel Dolha, Mark Griffith
 * @version 3.1
 */
public final class DataSourceManager implements DataSource
{
    /**
     * The shared preferences filename.
     */
    @NonNull
    private static final String PREFERENCES_FILENAME = "DataSourceManager.prefs";

    /**
     * The shared string preferences key corresponding to the current username.
     */
    @NonNull
    private static final String CURRENT_USERNAME_KEY = "CURRENT_USERNAME";

    /**
     * The local data source that is always available.
     *
     * @see DataSource
     */
    @NonNull
    private final DataSource localDataSource;

    /**
     * The remote data source, requiring Internet connectivity.
     *
     * @see DataSource
     */
    @NonNull
    private final DataSource remoteDataSource;

    /**
     * The application environment context.
     *
     * @see Context
     */
    @NonNull
    private final Context context;

    private final ArrayList<User> usersToAdd;

    private final ArrayList<User> usersToRemove;

    private final ArrayList<Task> tasksToAdd;

    private final ArrayList<Task> tasksToRemove;

    private final ArrayList<Bid> bidsToAdd;

    private final ArrayList<Bid> bidsToRemove;

    /**
     * @param context The application environment context.
     * @see Context
     */
    public DataSourceManager(@NonNull final Context context)
    {
        this.remoteDataSource = new RemoteDataSource();
        this.localDataSource = new LocalDataSource(context);
        this.context = context;

        this.usersToAdd = new ArrayList<>();
        this.usersToRemove = new ArrayList<>();
        this.tasksToAdd = new ArrayList<>();
        this.tasksToRemove = new ArrayList<>();
        this.bidsToAdd = new ArrayList<>();
        this.bidsToRemove = new ArrayList<>();
    }

    /**
     * Attempts to push any pending users to the remote data source and then pull changes from the
     * remote data source.
     */
    private void synchronizeUsers()
    {
        final ArrayList<User> committedUsers = new ArrayList<>();

        for (User user : this.usersToAdd)
        {
            if (this.remoteDataSource.addUser(user))
            {
                committedUsers.add(user);
            }
        }

        this.usersToAdd.removeAll(committedUsers);
        committedUsers.clear();

        for (User user : this.usersToRemove)
        {
            if (this.remoteDataSource.removeUser(user))
            {
                committedUsers.add(user);
            }
        }

        this.usersToRemove.removeAll(committedUsers);

        final User[] remoteUsers = this.remoteDataSource.getUsers();

        if (remoteUsers != null)
        {
            this.localDataSource.clearUsers();

            for (User user : remoteUsers)
            {
                this.localDataSource.addUser(user);
            }
        }
    }

    /**
     * Attempts to push any pending tasks to the remote data source and then pull changes from the
     * remote data source.
     */
    private void synchronizeTasks()
    {
        final ArrayList<Task> committedTasks = new ArrayList<>();

        for (Task task : this.tasksToAdd)
        {
            if (this.remoteDataSource.addTask(task))
            {
                committedTasks.add(task);
            }
        }

        this.tasksToAdd.removeAll(committedTasks);
        committedTasks.clear();

        for (Task task : this.tasksToRemove)
        {
            if (this.remoteDataSource.removeTask(task))
            {
                committedTasks.add(task);
            }
        }

        this.tasksToRemove.removeAll(committedTasks);
        final Task[] remoteTasks = this.remoteDataSource.getTasks();

        if (remoteTasks != null)
        {
            this.localDataSource.clearTasks();

            for (Task task : remoteTasks)
            {
                this.localDataSource.addTask(task);
            }
        }
    }

    /**
     * Attempts to push any pending bids to the remote data source and then pull changes from the
     * remote data source.
     */
    private void synchronizeBids()
    {
        final ArrayList<Bid> committedBids = new ArrayList<>();

        for (Bid bid : this.bidsToAdd)
        {
            if (this.remoteDataSource.addBid(bid))
            {
                committedBids.add(bid);
            }
        }

        this.bidsToAdd.removeAll(committedBids);
        committedBids.clear();

        for (Bid bid : this.bidsToRemove)
        {
            if (this.remoteDataSource.removeBid(bid))
            {
                committedBids.add(bid);
            }
        }

        this.bidsToRemove.removeAll(committedBids);

        final Bid[] remoteBids = this.remoteDataSource.getBids();

        if (remoteBids != null)
        {
            this.localDataSource.clearBids();

            for (Bid bid : remoteBids)
            {
                this.localDataSource.addBid(bid);
            }
        }
    }

    /**
     * @return The current user, or null if there is no current user.
     * @throws IllegalStateException If the current user is absent from the local data source.
     * @see User
     */
    @Nullable
    public final User getCurrentUser() throws IllegalStateException
    {
        this.synchronizeUsers();
        final String username = this.context
                .getSharedPreferences(DataSourceManager.PREFERENCES_FILENAME, Context.MODE_PRIVATE)
                .getString(DataSourceManager.CURRENT_USERNAME_KEY, null);

        if (username == null)
        {
            return null;
        }

        final User user = this.localDataSource.getUser(username);

        if (user == null)
        {
            throw new IllegalStateException("current user is absent from the local data source");
        }

        return user;
    }

    /**
     * Sets the current user.
     *
     * @param user The user to become the current user.
     * @throws IllegalArgumentException For a user not present in the local data source.
     * @see User
     */
    public final void setCurrentUser(@NonNull final User user) throws IllegalArgumentException
    {
        this.synchronizeUsers();
        final String errorMessage = "user must be present in the local data source";
        final User[] localUsers = this.localDataSource.getUsers();

        if (localUsers == null)
        {
            throw new IllegalArgumentException(errorMessage);
        }

        boolean presentInLocal = false;

        for (User localUser : localUsers)
        {
            if (localUser.equals(user))
            {
                presentInLocal = true;

                break;
            }
        }

        if (!presentInLocal)
        {
            throw new IllegalArgumentException(errorMessage);
        }

        final SharedPreferences.Editor editor = this.context
                .getSharedPreferences(DataSourceManager.PREFERENCES_FILENAME, Context.MODE_PRIVATE)
                .edit();
        editor.putString(CURRENT_USERNAME_KEY, user.getUsername());
        editor.apply();
    }

    /**
     * Unsets the current user.
     */
    public final void unsetCurrentUser()
    {
        final SharedPreferences.Editor editor = this.context
                .getSharedPreferences(DataSourceManager.PREFERENCES_FILENAME, Context.MODE_PRIVATE)
                .edit();
        editor.remove(CURRENT_USERNAME_KEY);
        editor.apply();
    }

    /**
     * @return All users present in the data source, or null if an error occurs.
     * @see DataSource
     * @see User
     */
    @Nullable
    @Override
    public final User[] getUsers()
    {
        this.synchronizeUsers();

        return this.localDataSource.getUsers();
    }

    /**
     * @return The user with that username, or null if no such user exists in the data source.
     * @throws IllegalArgumentException For an empty username.
     * @see DataSource
     * @see User
     */
    @Nullable
    @Override
    public final User getUser(@NonNull final String username) throws IllegalArgumentException
    {
        if (username.isEmpty())
        {
            throw new IllegalArgumentException("username cannot be empty");
        }

        this.synchronizeUsers();

        return this.localDataSource.getUser(username);
    }

    /**
     * Adds a user to the data source.
     *
     * @param user The user to add.
     * @return Whether the user is present in the data source.
     * @see DataSource
     * @see User
     */
    @Override
    public final boolean addUser(@NonNull final User user)
    {
        this.usersToAdd.add(user);
        this.synchronizeUsers();

        return this.usersToAdd.contains(user);
    }

    /**
     * Removes a user from the data source.
     *
     * @param user The user to remove.
     * @return Whether the user is absent from the data source.
     * @see DataSource
     * @see User
     */
    @Override
    public final boolean removeUser(@NonNull final User user)
    {
        this.usersToRemove.add(user);
        this.synchronizeUsers();

        return this.usersToRemove.contains(user);
    }

    @Override
    public boolean clearUsers()
    {
        return false;
    }

    /**
     * @return All tasks present in the data source, or null if an error occurs.
     * @see DataSource
     * @see Task
     */
    @Nullable
    @Override
    public final Task[] getTasks()
    {
        this.synchronizeTasks();

        return this.localDataSource.getTasks();
    }

    /**
     * @return The task with that requester and title, or null if no such task exists in the data
     * source.
     * @throws IllegalArgumentException For an empty requesterUsername or title.
     * @see DataSource
     * @see Task
     */
    @Nullable
    @Override
    public final Task getTask(@NonNull final String requesterUsername, @NonNull final String title)
            throws IllegalArgumentException
    {
        if (requesterUsername.isEmpty())
        {
            throw new IllegalArgumentException("requesterUsername cannot be empty");
        }

        if (title.isEmpty())
        {
            throw new IllegalArgumentException("title cannot be empty");
        }

        this.synchronizeTasks();

        return this.localDataSource.getTask(requesterUsername, title);
    }

    /**
     * @return The task with that taskId, or null if no such task exists in the data
     * source.
     * @throws IllegalArgumentException For an empty taskId.
     * @see DataSource
     * @see Task
     */
    @Nullable
    @Override
    public final Task getTask(@NonNull final String taskId)
            throws IllegalArgumentException
    {
        if (taskId.isEmpty())
        {
            throw new IllegalArgumentException("taskId cannot be empty");
        }

        this.synchronizeTasks();

        return this.localDataSource.getTask(taskId);
    }

    /**
     * Adds a task to the data source.
     *
     * @param task The task to add.
     * @return Whether the task is present in the data source.
     * @see DataSource
     * @see Task
     */
    @Override
    public final boolean addTask(@NonNull final Task task)
    {
        this.tasksToAdd.add(task);
        this.synchronizeTasks();

        return !this.tasksToAdd.contains(task);
    }

    /**
     * Removes a task from the data source.
     *
     * @param task The task to remove.
     * @return Whether the task is absent from the data source.
     * @see DataSource
     * @see Task
     */
    @Override
    public final boolean removeTask(@NonNull final Task task)
    {
        this.tasksToRemove.add(task);
        this.synchronizeTasks();

        return !this.tasksToRemove.contains(task);
    }

    @Override
    public boolean clearTasks()
    {
        return false;
    }

    /**
     * @return All bids present in the data source, or null if an error occurs.
     * @see DataSource
     * @see Bid
     */
    @Nullable
    @Override
    public final Bid[] getBids()
    {
        this.synchronizeBids();

        return this.localDataSource.getBids();
    }

    /**
     * @return The bid from that provider on that task, or null if no such bid exists in the data
     * source.
     * @throws IllegalArgumentException For an empty providerUsername or taskId.
     * @see DataSource
     * @see Bid
     */
    @Nullable
    @Override
    public final Bid getBid(@NonNull final String providerUsername, @NonNull final String taskId)
            throws IllegalArgumentException
    {
        if (providerUsername.isEmpty())
        {
            throw new IllegalArgumentException("providerUsername cannot be empty");
        }

        if (taskId.isEmpty())
        {
            throw new IllegalArgumentException("taskId cannot be empty");
        }

        this.synchronizeBids();

        return this.localDataSource.getBid(providerUsername, taskId);
    }

    /**
     * Adds a bid to the data source.
     *
     * @param bid The bid to add.
     * @return Whether the bid is present in the data source.
     * @see DataSource
     * @see Bid
     */
    @Override
    public final boolean addBid(@NonNull final Bid bid)
    {
        this.bidsToAdd.add(bid);
        this.synchronizeBids();

        return !this.bidsToAdd.contains(bid);
    }

    /**
     * Removes a bid from the data source.
     *
     * @param bid The user to remove.
     * @return Whether the bid is absent from the data source.
     * @see DataSource
     * @see Bid
     */
    @Override
    public final boolean removeBid(@NonNull final Bid bid)
    {
        this.bidsToRemove.add(bid);
        this.synchronizeBids();

        return !this.bidsToRemove.contains(bid);
    }

    @Override
    public boolean clearBids()
    {
        return false;
    }
}
