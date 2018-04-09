package ca.ualberta.cs.w18t11.whoselineisitanyway.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

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
     * @param fileType The type of file to read.
     * @param <T>      The expected type of items.
     * @return All items present in the file, or null if an error occurs.
     * @see FileType
     * @see T
     */
    @Nullable
    private <T> ArrayList<T> readFile(@NonNull final FileType fileType)
    {
        try (FileInputStream fileInputStream = this.context.openFileInput(fileType.getFilename()))
        {
            try (InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream))
            {
                try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
                {
                    return LocalDataSource.GSON.fromJson(bufferedReader, fileType.getType());
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
     * @param fileType The type of file to which to write.
     * @param <T>      The expected type of items.
     * @throws IOException If an error occurs while writing.
     * @see FileType
     * @see T
     */
    private <T> void writeFile(@NonNull final FileType fileType,
                               @NonNull final ArrayList<T> items) throws IOException
    {
        try (FileOutputStream fileOutputStream = this.context
                .openFileOutput(fileType.getFilename(), Context.MODE_PRIVATE))
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
     * @param fileType  The type of file to which to write.
     * @param predicate The predicate with which to test the items.
     * @param <T>       The expected type of items.
     * @return The item satisfying the predicate, or null if no such item exists in the file.
     * @see FileType
     * @see Predicate
     * @see T
     */
    @Nullable
    private <T> T get(@NonNull final FileType fileType, @NonNull final Predicate<T> predicate)
    {
        final ArrayList<T> items = this.readFile(fileType);

        if (items == null)
        {
            return null;
        }

        for (T item : items)
        {
            if (predicate.apply(item))
            {
                return item;
            }
        }

        return null;
    }

    /**
     * @param fileType The type of file to which to write.
     * @param item     The item to add.
     * @param <T>      The expected type of items.
     * @return Whether the item is present in the file.
     * @see FileType
     * @see T
     */
    private <T> boolean add(@NonNull final FileType fileType, @NonNull final T item)
    {
        ArrayList<T> items = this.readFile(fileType);

        if (items == null)
        {
            try
            {
                items = new ArrayList<T>(1);
                items.add(item);
                this.writeFile(fileType, items);

                return true;
            }
            catch (IOException exception)
            {
                return false;
            }
        }

        items.remove(item); // Remove an existing copy, if one exists.
        items.add(item);

        try
        {
            this.writeFile(fileType, items);
        }
        catch (IOException exception)
        {
            return false;
        }

        return true;

    }

    /**
     * @param fileType The type of file to which to write.
     * @param item     The item to remove.
     * @param <T>      The expected type of items.
     * @return Whether the item is absent from the file.
     * @see FileType
     * @see T
     */
    private <T> boolean remove(@NonNull final FileType fileType, @NonNull final T item)
    {
        ArrayList<T> items = this.readFile(fileType);

        if (items == null)
        {
            try
            {
                this.writeFile(fileType, new ArrayList<T>());

                return true;
            }
            catch (IOException exception)
            {
                return false;
            }
        }

        items.remove(item);

        try
        {
            this.writeFile(fileType, items);
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
        final ArrayList<User> users = this.readFile(FileType.USERS);

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

        return this.get(FileType.USERS, new Predicate<User>()
        {
            @Override
            public final boolean apply(@Nullable final User user)
            {
                return user != null && user.getUsername().equals(username);
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
        return this.add(FileType.USERS, user);
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
        return this.remove(FileType.USERS, user);
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
        final ArrayList<Task> tasks = this.readFile(FileType.TASKS);

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

        return this.get(FileType.TASKS, new Predicate<Task>()
        {
            @Override
            public final boolean apply(@Nullable final Task task)
            {
                return task != null && task.getRequesterUsername().equals(requesterUsername) && task
                        .getTitle().equals(title);
            }
        });
    }

    /**
     * @return The task with that taskId, or null if no such task exists in the
     * filesystem.
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

        return this.get(FileType.TASKS, new Predicate<Task>()
        {
            @Override
            public final boolean apply(@Nullable final Task task)
            {
                return task != null && task.getElasticId().equals(taskId);
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
        return this.add(FileType.TASKS, task);
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
        if(task.getBids() != null)
        {
            for(Bid bid: task.getBids())
            {
                removeBid(bid);
            }
        }

        return this.remove(FileType.TASKS, task);
    }

    /**
     * @return All bids present in the filesystem, or null if an error occurs.
     * @see Bid
     * @see DataSource
     */
    @Nullable
    @Override
    public final Bid[] getBids()
    {
        final ArrayList<Bid> bids = this.readFile(FileType.BIDS);

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
     * @see Bid
     * @see DataSource
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

        return this.get(FileType.BIDS, new Predicate<Bid>()
        {
            @Override
            public final boolean apply(@Nullable final Bid bid)
            {
                return bid != null && bid.getProviderUsername().equals(providerUsername) && bid
                        .getTaskId().equals(taskId);
            }
        });
    }

    /**
     * Adds a bid to the filesystem.
     *
     * @param bid The bid to add.
     * @return Whether the bid is present in the filesystem.
     * @see Bid
     * @see DataSource
     */
    @Override
    public final boolean addBid(@NonNull final Bid bid)
    {
        return this.add(FileType.BIDS, bid);
    }

    /**
     * Adds a bid to the filesystem.
     *
     * @param bid The bid to add.
     * @return Whether the bid is present in the filesystem.
     * @see Bid
     * @see DataSource
     */
    @Override
    public final boolean removeBid(@NonNull final Bid bid)
    {
        return this.remove(FileType.BIDS, bid);
    }

    /**
     * The file types.
     */
    private enum FileType
    {
        USERS(new TypeToken<ArrayList<User>>()
        {
        }.getType()),
        TASKS(new TypeToken<ArrayList<Task>>()
        {
        }.getType()),
        BIDS(new TypeToken<ArrayList<Bid>>()
        {
        }.getType());

        @NonNull
        private final Type arrayType;

        FileType(@NonNull final Type arrayType)
        {
            this.arrayType = arrayType;
        }

        @NonNull
        public final String getFilename()
        {
            return this.name() + ".data";
        }

        @NonNull
        public final Type getType()
        {
            return this.arrayType;
        }
    }
}
