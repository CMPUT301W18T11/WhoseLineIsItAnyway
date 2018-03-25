package ca.ualberta.cs.w18t11.whoselineisitanyway.model.datasource;

import android.content.Context;
import android.support.annotation.NonNull;

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
import java.util.Arrays;
import java.util.Collection;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * Represents a file-based data source.
 *
 * @author Samuel Dolha
 * @version 2.0
 */
public final class FileDataSource implements DataSource
{
    private static final String SUFFIX = ".data";

    private static final String USERS_FILENAME = "Users" + FileDataSource.SUFFIX;

    private static final String TASKS_FILENAME = "Tasks" + FileDataSource.SUFFIX;

    private static final String BIDS_FILENAME = "Bids" + FileDataSource.SUFFIX;

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
    private Context context;

    /**
     * @param context The context of the application.
     * @see Context
     */
    public FileDataSource(@NonNull final Context context)
    {
        this.context = context;
    }

    @NonNull
    private static Type getFileType(@NonNull final String filename)
    {
        switch (filename)
        {
            case USERS_FILENAME:
                return new TypeToken<User[]>()
                {
                }.getType();

            case TASKS_FILENAME:
                return new TypeToken<Task[]>()
                {
                }.getType();

            case BIDS_FILENAME:
                return new TypeToken<Bid[]>()
                {
                }.getType();

            default:
                throw new IllegalArgumentException("Unrecognized filename");
        }
    }

    private <T> T[] readFile(@NonNull final String filename)
            throws IOException
    {
        try (FileInputStream fileInputStream = this.context.openFileInput(filename))
        {
            try (InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream))
            {
                try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
                {
                    return FileDataSource.GSON
                            .fromJson(bufferedReader, FileDataSource.getFileType(filename));
                }
            }
        }
    }

    private <T> void writeFile(@NonNull final String filename, @NonNull final T[] items)
            throws IOException
    {
        try (FileOutputStream fileOutputStream = this.context
                .openFileOutput(filename, Context.MODE_PRIVATE))
        {
            try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream))
            {
                try (BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter))
                {
                    FileDataSource.GSON.toJson(items, bufferedWriter);
                    bufferedWriter.flush();
                }
            }
        }
    }

    @NonNull
    @Override
    public User[] getUsers()
    {
        try
        {
            return this.readFile(USERS_FILENAME);
        }
        catch (IOException exception)
        {
            return new User[0];
        }
    }

    @Override
    public boolean addUser(@NonNull final User user)
    {
        try
        {
            final Collection<User> users = Arrays.asList((User[]) this.readFile(USERS_FILENAME));
            users.add(user);
            this.writeFile(USERS_FILENAME, users.toArray(new User[0]));
        }
        catch (IOException exception)
        {
            return false;
        }

        return true;
    }

    @Override
    public boolean removeUser(@NonNull final User user)
    {
        try
        {
            final Collection<User> users = Arrays.asList((User[]) this.readFile(USERS_FILENAME));
            users.remove(user);
            this.writeFile(USERS_FILENAME, users.toArray(new User[0]));
        }
        catch (IOException exception)
        {
            return false;
        }

        return true;
    }

    @NonNull
    @Override
    public Task[] getTasks()
    {
        try
        {
            return this.readFile(TASKS_FILENAME);
        }
        catch (IOException exception)
        {
            return new Task[0];
        }
    }

    @Override
    public boolean addTask(@NonNull final Task task)
    {
        try
        {
            final Collection<Task> tasks = Arrays.asList((Task[]) this.readFile(TASKS_FILENAME));
            tasks.add(task);
            this.writeFile(TASKS_FILENAME, tasks.toArray(new Task[0]));
        }
        catch (IOException exception)
        {
            return false;
        }

        return true;
    }

    @Override
    public boolean removeTask(@NonNull final Task task)
    {
        try
        {
            final Collection<Task> tasks = Arrays.asList((Task[]) this.readFile(TASKS_FILENAME));
            tasks.remove(task);
            this.writeFile(TASKS_FILENAME, tasks.toArray(new Task[0]));
        }
        catch (IOException exception)
        {
            return false;
        }

        return true;
    }

    @NonNull
    @Override
    public Bid[] getBids()
    {
        try
        {
            return this.readFile(BIDS_FILENAME);
        }
        catch (IOException exception)
        {
            return new Bid[0];
        }
    }

    @Override
    public boolean addBid(@NonNull final Bid bid)
    {
        try
        {
            final Collection<Bid> bids = Arrays.asList((Bid[]) this.readFile(BIDS_FILENAME));
            bids.add(bid);
            this.writeFile(BIDS_FILENAME, bids.toArray(new Bid[0]));
        }
        catch (IOException exception)
        {
            return false;
        }

        return true;
    }

    @Override
    public boolean removeBid(@NonNull final Bid bid)
    {
        try
        {
            final Collection<Bid> bids = Arrays.asList((Bid[]) this.readFile(BIDS_FILENAME));
            bids.remove(bid);
            this.writeFile(BIDS_FILENAME, bids.toArray(new Bid[0]));
        }
        catch (IOException exception)
        {
            return false;
        }

        return true;
    }

    @Override
    public User getUserById(String elasticId) {
        // TODO implement
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        // TODO implement
        return null;
    }
}
