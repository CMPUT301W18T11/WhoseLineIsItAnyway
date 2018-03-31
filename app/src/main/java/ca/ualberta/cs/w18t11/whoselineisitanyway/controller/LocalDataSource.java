package ca.ualberta.cs.w18t11.whoselineisitanyway.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * Represents a file-based data source.
 *
 * @author Samuel Dolha, Mark Griffith
 * @version 3.0
 */
final class LocalDataSource implements DataSource
{
    /**
     * The file types.
     */
    private enum Filename
    {
        USERS,
        TASKS,
        BIDS;

        @NonNull
        @Override
        public final String toString()
        {
            return this.name() + ".data";
        }
    }

    /**
     * The JSON converter.
     *
     * @see Gson
     */
    @NonNull
    private static final Gson GSON = new Gson();

    /**
     * The context of the application.
     *
     * @see Context
     */
    @NonNull
    private final Context context;

    /**
     * @param context The context of the application.
     * @see Context
     */
    LocalDataSource(@NonNull final Context context)
    {
        this.context = context;
    }

    /**
     * @param filename The name of the file to read.
     * @param <T>      The expected type of items.
     * @return All items present in the file, or null if an error occurs.
     */
    @Nullable
    private <T> ArrayList<T> readFile(@NonNull final Filename filename)
    {
        try (FileInputStream fileInputStream = this.context.openFileInput(filename.toString()))
        {
            try (InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream))
            {
                try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
                {
                    return LocalDataSource.GSON
                            .fromJson(bufferedReader, new TypeToken<ArrayList<T>>()
                            {
                            }.getType());
                }
            }
        }
        catch (IOException exception)
        {
            return null;
        }
    }

    /**
     * Writes the data to a file.
     *
     * @param filename The name of the file to which to write.
     * @param <T>      The expected type of items.
     */
    private <T> void writeFile(@NonNull final Filename filename, @NonNull final ArrayList<T> items)
            throws IOException
    {
        try (FileOutputStream fileOutputStream = this.context
                .openFileOutput(filename.toString(), Context.MODE_PRIVATE))
        {
            try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream))
            {
                try (BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter))
                {
                    LocalDataSource.GSON.toJson(items, bufferedWriter);
                    bufferedWriter.flush();
                }
            }
        }
    }

    /**
     * @param predicate The predicate with which to test the items.
     * @param <T>       The expected type of items.
     * @return The item satisfying the predicate, or null if no such item exists in the file.
     */
    @Nullable
    private <T> T get(@NonNull final Predicate<T> predicate)
    {
        final ArrayList<T> items = this.readFile(Filename.USERS);

        if (items == null)
        {
            return null;
        }

        for (T item : items)
        {
            if (predicate.test(item))
            {
                return item;
            }
        }

        return null;
    }

    /**
     * @param filename The name of the file to which to write.
     * @param item     The item to add.
     * @param <T>      The expected type of items.
     * @return Whether the item is present in the file.
     */
    private <T> boolean add(@NonNull final Filename filename, @NonNull final T item)
    {
        ArrayList<T> items = this.readFile(filename);

        if (items == null)
        {
            try
            {
                items = new ArrayList<>();
                items.add(item);
                this.writeFile(filename, items);

                return true;
            }
            catch (IOException exception)
            {
                return false;
            }
        }

        final ArrayList<T> itemsToReplace = new ArrayList<>();

        for (T oldItem : items)
        {
            if (oldItem.equals(item))
            {
                itemsToReplace.add(oldItem);
            }
        }

        for (T itemToReplace : itemsToReplace)
        {
            items.remove(itemToReplace);
        }

        items.add(item);

        try
        {
            this.writeFile(Filename.USERS, items);
        }
        catch (IOException exception)
        {
            return false;
        }

        return true;

    }

    /**
     * @param filename The name of the file to which to write.
     * @param item     The item to remove.
     * @param <T>      The expected type of items.
     * @return Whether the item is absent from the file.
     */
    private <T> boolean remove(@NonNull final Filename filename, @NonNull final T item)
    {
        ArrayList<T> items = this.readFile(filename);

        if (items == null)
        {
            try
            {
                this.writeFile(filename, new ArrayList<T>());

                return true;
            }
            catch (IOException exception)
            {
                return false;
            }
        }

        while (items.contains(item))
        {
            items.remove(item);
        }

        try
        {
            this.writeFile(Filename.USERS, items);
        }
        catch (IOException exception)
        {
            return false;
        }

        return true;

    }

    /**
     * @return All users present in the filesystem, or null if an error occurs.
     * @see DataSource
     * @see User
     */
    @Nullable
    @Override
    public final User[] getUsers()
    {
        final ArrayList<User> users = this.readFile(Filename.USERS);

        if (users == null)
        {
            return null;
        }

        return users.toArray(new User[0]);
    }

    /**
     * @return The user with that username, or null if no such user exists in the filesystem.
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

        return this.get(new Predicate<User>()
        {
            @Override
            public final boolean test(@NonNull final User user)
            {
                return user.getUsername().equals(username);
            }
        });
    }

    /**
     * Adds a user to the filesystem.
     *
     * @param user The user to add.
     * @return Whether the user is present in the filesystem.
     * @see DataSource
     * @see User
     */
    @Override
    public final boolean addUser(@NonNull final User user)
    {
        return this.add(Filename.USERS, user);
    }

    /**
     * Removes a user from the filesystem.
     *
     * @param user The user to remove.
     * @return Whether the user is absent from the filesystem.
     * @see DataSource
     * @see User
     */
    @Override
    public final boolean removeUser(@NonNull final User user)
    {
        return this.remove(Filename.USERS, user);
    }

    /**
     * @return All tasks present in the filesystem, or null if an error occurs.
     * @see DataSource
     * @see Task
     */
    @Nullable
    @Override
    public final Task[] getTasks()
    {
        final ArrayList<Task> tasks = this.readFile(Filename.TASKS);

        if (tasks == null)
        {
            return null;
        }

        return tasks.toArray(new Task[0]);
    }

    /**
     * @return The task with that requester and title, or null if no such task exists in the
     * filesystem.
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

        return this.get(new Predicate<Task>()
        {
            @Override
            public final boolean test(@NonNull final Task task)
            {
                return task.getRequesterUsername().equals(requesterUsername) && task.getTitle()
                        .equals(title);
            }
        });
    }

    /**
     * Adds a task to the filesystem.
     *
     * @param task The task to add.
     * @return Whether the task is present in the filesystem.
     * @see DataSource
     * @see Task
     */
    @Override
    public final boolean addTask(@NonNull final Task task)
    {
        return this.add(Filename.TASKS, task);
    }

    /**
     * Removes a task from the filesystem.
     *
     * @param task The task to remove.
     * @return Whether the task is absent from the filesystem.
     * @see DataSource
     * @see Task
     */
    @Override
    public final boolean removeTask(@NonNull final Task task)
    {
        return this.remove(Filename.TASKS, task);
    }

    /**
     * @return All bids present in the filesystem, or null if an error occurs.
     * @see DataSource
     * @see Bid
     */
    @Nullable
    @Override
    public final Bid[] getBids()
    {
        final ArrayList<Bid> bids = this.readFile(Filename.BIDS);

        if (bids == null)
        {
            return null;
        }

        return bids.toArray(new Bid[0]);
    }

    /**
     * @return The bid from that provider on that task, or null if no such bid exists in the
     * filesystem.
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

        return this.get(new Predicate<Bid>()
        {
            @Override
            public final boolean test(@NonNull final Bid bid)
            {
                return bid.getProviderUsername().equals(providerUsername) && bid.getTaskId()
                        .equals(taskId);
            }
        });
    }

    /**
     * Adds a bid to the filesystem.
     *
     * @param bid The bid to add.
     * @return Whether the bid is present in the filesystem.
     * @see Bid
     */
    @Override
    public final boolean addBid(@NonNull final Bid bid)
    {
        return this.add(Filename.BIDS, bid);
    }

    /**
     * Adds a bid to the filesystem.
     *
     * @param bid The bid to add.
     * @return Whether the bid is present in the filesystem.
     * @see Bid
     */
    @Override
    public final boolean removeBid(@NonNull final Bid bid)
    {
        return this.remove(Filename.BIDS, bid);
    }
}
