package ca.ualberta.cs.w18t11.whoselineisitanyway.model.datasource;

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
import java.util.ArrayList;
import java.util.Arrays;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * DataSource used for storing data in files
 *
 * @author Samuel Dolha
 * @version 1.0
 */
public final class FileDataSource implements DataSource
{
    private static final String EXTENSION = ".data";

    @NonNull
    private static final Gson gson = new Gson();

    /**
     * The data storage filename.
     */
    @NonNull
    private final String prefix;

    /**
     * The intent context of the application
     */
    @NonNull
    private final Context context;

    private Task[] tasks;

    private Bid[] bids;

    private User[] users;

    /**
     * @param prefix The date storage filename prefix.
     * @throws IllegalArgumentException For an empty filename.
     */
    public FileDataSource(@NonNull final String prefix, @NonNull final Context context)
    {
        if (prefix.isEmpty())
        {
            throw new IllegalArgumentException("prefix cannot be empty");
        }

        this.prefix = prefix;
        this.context = context;

        this.tasks = readFile(this.getTasksFilename());
        this.bids = readFile(this.getBidsFilename());
        this.users = readFile(this.getUsersFilename());
    }

    /**
     * @return String name of file containing the Tasks
     */
    @NonNull
    private String getTasksFilename()
    {
        return this.prefix + "Tasks" + FileDataSource.EXTENSION;
    }

    /**
     * @return String name of file containing the Bids
     */
    @NonNull
    private String getBidsFilename()
    {
        return this.prefix + "Bids" + FileDataSource.EXTENSION;
    }

    /**
     * @return String name of file containing the Users
     */
    @NonNull
    private String getUsersFilename()
    {
        return this.prefix + "Users" + FileDataSource.EXTENSION;
    }

    /**
     * Read from the specified file
     *
     * @param filename name of the file to read
     * @return type read or null if IOException
     */
    @Nullable
    private <T> T readFile(@NonNull final String filename)
    {
        try (FileInputStream fileInputStream = this.context
                .openFileInput(filename))
        {
            try (InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream))
            {
                try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
                {
                    return gson.fromJson(bufferedReader, new TypeToken<User[]>()
                    {
                    }.getType());
                }
            }
        }
        catch (IOException exception)
        {
            exception.printStackTrace();

            return null;
        }
    }

    /**
     * Write to the specified file
     *
     * @param filename name of file to write to
     * @param collection collection of objects to write
     * @param <T> type of objects to write
     */
    private <T> boolean writeFile(@NonNull final String filename, @NonNull final T collection)
    {
        try (FileOutputStream fileOutputStream = this.context
                .openFileOutput(filename, Context.MODE_PRIVATE))
        {
            try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream))
            {
                try (BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter))
                {
                    gson.toJson(collection, bufferedWriter);
                    bufferedWriter.flush();
                }
            }
        }
        catch (IOException exception)
        {
            return false;
        }

        return true;
    }

    /**
     * @return ArrayList containing all of the tasks
     */
    @Override
    public final Task[] getAllTasks()
    {
        return this.tasks;
    }

    /**
     * @return ArrayList containing all of the bids
     */
    @Override
    public final Bid[] getAllBids()
    {
        return this.bids;
    }

    /**
     * @return ArrayList containing all of the users
     */
    @Override
    public final User[] getAllUsers()
    {
        return this.users;
    }

    /**
     * Add a new task to the data source
     * Return Boolean representing if the addition was succesful
     */
    @Override
    public final Boolean addTask(final Task task)
    {
        final ArrayList<Task> tasks = new ArrayList<>();

        tasks.addAll(Arrays.asList(this.tasks));
        tasks.add(task);
        this.tasks = tasks.toArray(new Task[0]);

        return this.writeFile(this.getTasksFilename(), this.tasks);
    }

    /**
     * Add a new bid to the data source
     * Return Boolean representing if the addition was succesful
     */
    @Override
    public final Boolean addBid(final Bid bid)
    {
        final ArrayList<Bid> bids = new ArrayList<>();

        bids.addAll(Arrays.asList(this.bids));
        bids.add(bid);
        this.bids = bids.toArray(new Bid[0]);

        return this.writeFile(this.getBidsFilename(), this.bids);
    }

    /**
     * Add a new user to the data source
     * Return Boolean representing if the addition was succesful
     */
    @Override
    public final Boolean addUser(final User user)
    {
        final ArrayList<User> users = new ArrayList<>();

        users.addAll(Arrays.asList(this.users));
        users.add(user);
        this.users = users.toArray(new User[0]);

        return this.writeFile(this.getUsersFilename(), this.users);
    }

    /**
     * Remove a  task from the data source
     * Return Boolean representing if the remove was succesful
     */
    @Override
    public final Boolean removeTask(final Task task)
    {
        final ArrayList<Task> tasks = new ArrayList<>();

        tasks.addAll(Arrays.asList(this.tasks));
        tasks.remove(task);
        this.tasks = tasks.toArray(new Task[0]);

        return this.writeFile(this.getTasksFilename(), this.tasks);
    }

    /**
     * Remove a bid from the data source
     * Return Boolean representing if the addition was succesful
     */
    @Override
    public final Boolean removeBid(final Bid bid)
    {
        final ArrayList<Bid> bids = new ArrayList<>();

        bids.addAll(Arrays.asList(this.bids));
        bids.remove(bid);
        this.bids = bids.toArray(new Bid[0]);

        return this.writeFile(this.getBidsFilename(), this.bids);
    }

    /**
     * Remove user from the data source
     * Return Boolean representing if the addition was succesful
     */
    @Override
    public final Boolean removeUser(final User user)
    {
        final ArrayList<User> users = new ArrayList<>();

        users.addAll(Arrays.asList(this.users));
        users.remove(user);
        this.users = users.toArray(new User[0]);

        return this.writeFile(this.getUsersFilename(), this.users);
    }
}
