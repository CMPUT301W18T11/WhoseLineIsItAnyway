package ca.ualberta.cs.w18t11.whoselineisitanyway.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/**
 * Represents a file-based data source.
 *
 * @author Samuel Dolha
 * @version 2.0
 */
public final class LocalDataSource implements DataSource
{
    private static final String SUFFIX = ".data";

    private static final String USERS_FILENAME = "Users" + LocalDataSource.SUFFIX;

    private static final String TASKS_FILENAME = "Tasks" + LocalDataSource.SUFFIX;

    private static final String BIDS_FILENAME = "Bids" + LocalDataSource.SUFFIX;

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
    public LocalDataSource(@NonNull final Context context)
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
        File folder = new File(this.context.getFilesDir().toString());
        File file = new File(folder.getAbsolutePath() + "/" + filename);
        if (!folder.exists())
        {
            folder.mkdir();
        }
        if (!file.exists())
        {
            file.createNewFile();
        }

        try (FileInputStream fileInputStream = this.context.openFileInput(filename))
        {
            try (InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream))
            {
                try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
                {
                    return LocalDataSource.GSON
                            .fromJson(bufferedReader, LocalDataSource.getFileType(filename));
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
                    LocalDataSource.GSON.toJson(items, bufferedWriter);
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
            User[] users = this.readFile(USERS_FILENAME);
            return users;
        }
        catch (IOException exception)
        {
            return null;
        }
    }

    @Override
    public boolean addUser(@NonNull final User user)
    {
        try
        {
            ArrayList<User> users;
            User[] fromFile = this.readFile(USERS_FILENAME);
            if (fromFile == null)
            {
                users = new ArrayList<User>();
            }
            else
            {
                users = new ArrayList<>(Arrays.asList(fromFile));
            }
            users.add(user);
            this.writeFile(USERS_FILENAME, users.toArray(new User[0]));
        }
        catch (IOException exception)
        {
            Log.i("LocalDataSource.addUser", "IOException: " + exception.toString());
            return false;
        }
        return true;
    }

    @Override
    public boolean removeUser(@NonNull final User user)
    {
        try
        {
            ArrayList<User> users;
            User[] fromFile = this.readFile(USERS_FILENAME);
            if (fromFile == null)
            {
                return false;
            }
            else
            {
                users = new ArrayList<>(Arrays.asList(fromFile));
            }
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
            return null;
        }
    }

    @Override
    public boolean addTask(@NonNull final Task task)
    {
        try
        {
            ArrayList<Task> tasks;
            Task[] fromFile = this.readFile(TASKS_FILENAME);
            if (fromFile == null)
            {
                tasks = new ArrayList<Task>();
            }
            else
            {
                tasks = new ArrayList<>(Arrays.asList(fromFile));
            }
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
            ArrayList<Task> tasks;
            Task[] fromFile = this.readFile(TASKS_FILENAME);
            if (fromFile == null)
            {
                return false;
            }
            else
            {
                tasks = new ArrayList<>(Arrays.asList(fromFile));
            }
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
            return null;
        }
    }

    @Override
    public boolean addBid(@NonNull final Bid bid)
    {
        try
        {
            ArrayList<Bid> bids;
            Bid[] fromFile = this.readFile(BIDS_FILENAME);
            if (fromFile == null)
            {
                bids = new ArrayList<Bid>();
            }
            else
            {
                bids = new ArrayList<>(Arrays.asList(fromFile));
            }
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
            ArrayList<Bid> bids;
            Bid[] fromFile = this.readFile(BIDS_FILENAME);
            if (fromFile == null)
            {
                return false;
            }
            else
            {
                bids = new ArrayList<>(Arrays.asList(fromFile));
            }
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
    public User getUserById(String elasticId)
    {
        // TODO implement
        return null;
    }

    @Override
    public User getUserByUsername(String username)
    {
        // TODO implement
        return null;
    }
}
