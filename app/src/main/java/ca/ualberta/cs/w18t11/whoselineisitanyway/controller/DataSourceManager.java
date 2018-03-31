package ca.ualberta.cs.w18t11.whoselineisitanyway.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * Manages offline and online data synchronization.
 *
 * @author Brad Ofrim, Samuel Dolha, Mark Griffith
 * @version 3.0
 */
public final class DataSourceManager implements DataSource
{
    /**
     * The shared string preferences key corresponding to the current username.
     */
    private static final String CURRENT_USERNAME_KEY = "CURRENT_USERNAME";

    /**
     * The accumulator for assigning unique identifiers to instances of the class.
     *
     * @see AtomicInteger
     */
    private static AtomicInteger nextId = new AtomicInteger(0);

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
     * The shared preferences filename.
     */
    @NonNull
    private final String preferencesFilename;

    /**
     * The application environment context.
     *
     * @see Context
     */
    @NonNull
    private final Context context;

    /**
     * @param context The application environment context.
     * @see Context
     */
    public DataSourceManager(@NonNull final Context context)
    {
        this.remoteDataSource = new RemoteDataSource();
        this.localDataSource = new LocalDataSource(context);
        this.preferencesFilename = String.format(Locale.getDefault(), "DataSourceManager%d.prefs",
                DataSourceManager.nextId.getAndAdd(1));
        this.context = context;
    }

    /**
     * Synchronizes the users present in the local and remote data sources.
     */
    private void synchronizeUsers()
    {
        final ArrayDiff<User> usersDiff = new ArrayDiff<>(this.localDataSource.getUsers(),
                this.remoteDataSource.getUsers());

        for (User user : usersDiff.getFirstItems())
        {
            this.remoteDataSource.addUser(user);
        }

        for (User user : usersDiff.getSecondItems())
        {
            this.localDataSource.addUser(user);
        }
    }

    /**
     * Synchronizes the tasks present in the local and remote data sources.
     */
    private void synchronizeTasks()
    {
        final ArrayDiff<Task> tasksDiff = new ArrayDiff<>(this.localDataSource.getTasks(),
                this.remoteDataSource.getTasks());

        for (Task task : tasksDiff.getFirstItems())
        {
            this.remoteDataSource.addTask(task);
        }

        for (Task task : tasksDiff.getSecondItems())
        {
            this.localDataSource.addTask(task);
        }
    }

    /**
     * Synchronizes the bids present in the local and remote data sources.
     */
    private void synchronizeBids()
    {
        final ArrayDiff<Bid> bidsDiff = new ArrayDiff<>(this.localDataSource.getBids(),
                this.remoteDataSource.getBids());

        for (Bid bid : bidsDiff.getFirstItems())
        {
            this.remoteDataSource.addBid(bid);
        }

        for (Bid bid : bidsDiff.getSecondItems())
        {
            this.localDataSource.addBid(bid);
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
                .getSharedPreferences(this.preferencesFilename, Context.MODE_PRIVATE)
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
                .getSharedPreferences(this.preferencesFilename, Context.MODE_PRIVATE).edit();
        editor.putString(CURRENT_USERNAME_KEY, user.getUsername());
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
        this.synchronizeUsers();
        final boolean localResult = this.localDataSource.addUser(user);
        final boolean remoteResult = this.remoteDataSource.addUser(user);

        return localResult && remoteResult;
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
        this.synchronizeUsers();
        final boolean localResult = this.localDataSource.removeUser(user);
        final boolean remoteResult = this.remoteDataSource.removeUser(user);

        return localResult && remoteResult;
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
        this.synchronizeTasks();
        final boolean localResult = this.localDataSource.addTask(task);
        final boolean remoteResult = this.remoteDataSource.addTask(task);

        return localResult && remoteResult;
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
        this.synchronizeTasks();
        final boolean localResult = this.localDataSource.removeTask(task);
        final boolean remoteResult = this.remoteDataSource.removeTask(task);

        return localResult && remoteResult;
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
        this.synchronizeBids();
        final boolean localResult = this.localDataSource.addBid(bid);
        final boolean remoteResult = this.remoteDataSource.addBid(bid);

        return localResult && remoteResult;
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
        this.synchronizeBids();
        final boolean localResult = this.localDataSource.removeBid(bid);
        final boolean remoteResult = this.remoteDataSource.removeBid(bid);

        return localResult && remoteResult;
    }

    /**
     * Represents two lists consisting of items unique to each array, respectively.
     *
     * @param <T> The expected type of item
     */
    private static final class ArrayDiff<T>
    {
        /**
         * The items unique to the first array.
         *
         * @see ImmutableCollection
         * @see T
         */
        private final ImmutableCollection<T> firstItems;

        /**
         * The items unique to the second array.
         *
         * @see ImmutableCollection
         * @see T
         */
        private final ImmutableCollection<T> secondItems;

        /**
         * @param firstArray  The first array.
         * @param secondArray The second array.
         * @see T
         */
        ArrayDiff(@Nullable final T[] firstArray, @Nullable final T[] secondArray)
        {
            final Collection<T> firstCollection = new ArrayList<>();
            final Collection<T> secondCollection = new ArrayList<>();

            if (secondArray != null)
            {
                secondCollection.addAll(Arrays.asList(secondArray));
            }

            if (firstArray != null)
            {
                for (T firstItem : firstArray)
                {
                    if (secondCollection.contains(firstItem))
                    {
                        secondCollection.remove(firstItem);
                    }
                    else
                    {
                        firstCollection.add(firstItem);
                    }
                }
            }

            this.firstItems = new ImmutableList.Builder<T>().addAll(firstCollection).build();
            this.secondItems = new ImmutableList.Builder<T>().addAll(secondCollection).build();
        }

        /**
         * @return The items unique to the first array.
         * @see ImmutableCollection
         * @see T
         */
        ImmutableCollection<T> getFirstItems()
        {
            return this.firstItems;
        }

        /**
         * @return The items unique to the second array.
         * @see ImmutableCollection
         * @see T
         */
        ImmutableCollection<T> getSecondItems()
        {
            return this.secondItems;
        }
    }
}
