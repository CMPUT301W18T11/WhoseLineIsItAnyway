package ca.ualberta.cs.w18t11.whoselineisitanyway.controller;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.EmailAddress;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.PhoneNumber;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;
import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Elastic Search controller for handling User queries
 *
 * @author Mark Griffith
 * @version 1.1
 */
public class ElasticsearchUserController
{
    private static String typeStr = "users";
    private static String idxStr = "cmput301w18t11";
    private static JestDroidClient client;

    /**
     * Async task for updating the users in the database
     */
    public static class AddUsersTask extends AsyncTask<User, Void, Boolean>
    {
        /**
         * It first attempts to update the user in the database.
         * If the user isn't found, it adds the user to the database and sets its elastic id
         *
         * @param users Users to update/add to the database
         * @return Boolean.true if user was updated added, else Boolean.false
         */
        @Override
        protected Boolean doInBackground(User... users)
        {
            verifyConfig();

            for (User user : users)
            {
                // First, we try to see if it already exists in the database
                Index index = new Index.Builder(user)
                        .index(idxStr)
                        .type(typeStr)
                        .id(user.getElasticId())
                        .build();

                try
                {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded())
                    {
                        Log.i("Elasticsearch Success", "updated user: " +
                                user.getUsername());
                        return Boolean.TRUE;
                    }
                    else
                    {
                        Log.i("Elasticsearch Error",
                                "index missing or could not connect:" +
                                        Integer.toString(result.getResponseCode()));

                        // If we couldn't find the object with the index, we add it to the databse
                        Index idx = new Index.Builder(user).index(idxStr).type(typeStr).build();

                        try
                        {
                            result = client.execute(idx);
                            if (result.isSucceeded())
                            {
                                // Elasticsearch was successful
                                Log.i("Elasticsearch Success", "Added new user to db!");
                                user.setElasticId(result.getId());
                                return Boolean.TRUE;
                            }
                            else
                            {
                                Log.i("Elasticsearch Error",
                                        "index missing or could not connect:" +
                                                Integer.toString(result.getResponseCode()));
                                return Boolean.FALSE;
                            }
                        }
                        catch (Exception e)
                        {
                            // Probably disconnected
                            Log.i("Elasticsearch Error", "Unexpected exception: " +
                                    e.toString());
                        }
                    }
                }
                catch (Exception e)
                {
                    Log.i("Elasticsearch Error", "Unexpected exception: " +
                            e.toString());
                }
            }
            return Boolean.FALSE;
        }
    }

    /**
     * Async task for getting a user from the database using its elastic id
     */
    public static class GetUserByIdTask extends AsyncTask<String, Void, User>
    {
        /**
         * Gets a user from the database based on its elastic id
         *
         * @param userId elastic id of the user to get
         * @return the found user on success else null
         */
        @Override
        protected User doInBackground(String... userId)
        {
            verifyConfig();

            Get get = new Get.Builder(idxStr, userId[0]).type(typeStr).build();

            try
            {
                JestResult result = client.execute(get);
                if (result.isSucceeded())
                {
                    // User found
                    User user = result.getSourceAsObject(User.class);
                    return user;
                }
                else
                {
                    Log.i("Elasticsearch Error",
                            "index missing or could not connect:" +
                                    Integer.toString(result.getResponseCode()));
                    return null;
                }
            }
            catch (Exception e)
            {
                // Probably disconnected
                Log.i("Elasticsearch Error", "Unexpected exception: " + e.toString());
            }
            // User not found, return blank User object
            return new User("Default Name",
                    new EmailAddress("DefaultLocalPart", "Domain@Default.com"),
                    new PhoneNumber(0, 0, 0, 0));
        }
    }

    /**
     * Async task for getting users from the database
     */
    public static class GetUsersTask extends AsyncTask<String, Void, ArrayList<User>>
    {
        /**
         * Gets a list of users in the database matching a search query
         *
         * @param query search query detailing which users to get from the database
         * @return list of users on success else null
         */
        @Override
        protected ArrayList<User> doInBackground(String... query)
        {
            verifyConfig();

            ArrayList<User> users = new ArrayList<User>();

            // Build the query
            if (query.length < 1)
            {
                Log.i("Elasticsearch Error", "Invalid query");
                return null;
            }

            Search search = new Search.Builder(query[0])
                    .addIndex(idxStr)
                    .addType(typeStr)
                    .build();

            try
            {
                SearchResult result = client.execute(search);
                if (result.isSucceeded())
                {
                    List<User> foundUsers = result.getSourceAsObjectList(User.class);
                    users.addAll(foundUsers);
                }
                else
                {
                    Log.i("Elasticsearch Error",
                            "index missing or could not connect:" +
                                    Integer.toString(result.getResponseCode()));
                    return null;
                }
            }
            catch (Exception e)
            {
                // Probably disconnected
                Log.i("Elasticsearch Error", "Unexpected exception: " + e.toString());
                return null;
            }
            return users;
        }
    }

    /**
     * Async task for removing a user in the database
     */
    public static class RemoveUserTask extends AsyncTask<User, Void, Void>
    {
        /**
         * Removes the given user from the database
         *
         * @param user User to remove
         * @return null
         */
        @Override
        protected Void doInBackground(User... user)
        {
            verifyConfig();

            Delete delete =
                    new Delete.Builder(user[0].getElasticId()).index(idxStr).type(typeStr).build();

            try
            {
                DocumentResult result = client.execute(delete);
                if (result.isSucceeded())
                {
                    Log.i("Elasticsearch Success", "deleted user: " +
                            user[0].getUsername());
                }
                else
                {
                    Log.i("Elasticsearch Error",
                            "index missing or could not connect:" +
                                    Integer.toString(result.getResponseCode()));
                }
            }
            catch (Exception e)
            {
                Log.i("Elasticsearch Error", "Unexpected exception: " + e.toString());
            }
            return null;
        }
    }

    /**
     * Async task for updating a user in the database
     */
    public static class UpdateUserTask extends AsyncTask<User, Void, Boolean>
    {
        /**
         * Finds and replaces the user in the database having the same elastic id
         * as the given user
         *
         * @param user User to update
         * @return Boolean.TRUE on success else Boolean.FALSE
         */
        @Override
        protected Boolean doInBackground(User... user)
        {
            verifyConfig();

            Index index = new Index.Builder(user[0]).index(idxStr).type(typeStr)
                    .id(user[0].getElasticId()).build();

            try
            {
                DocumentResult result = client.execute(index);
                if (result.isSucceeded())
                {
                    Log.i("Elasticsearch Success", "updated user: " +
                            user[0].getUsername());
                    return Boolean.TRUE;
                }
                else
                {
                    Log.i("Elasticsearch Error",
                            "index missing or could not connect:" +
                                    Integer.toString(result.getResponseCode()));
                    return Boolean.FALSE;
                }
            }
            catch (Exception e)
            {
                Log.i("Elasticsearch Error", "Unexpected exception: " + e.toString());
            }
            return Boolean.FALSE;
        }
    }

    /**
     * Async task for adding users to the database
     */
    public static class OldAddUsersTask extends AsyncTask<User, Void, String>
    {
        /**
         * Adds the given list of Users to the database and sets their elastic id's
         *
         * @param users Users to add to the database
         * @return assigned elastic id on success else null
         */
        @Override
        protected String doInBackground(User... users)
        {
            verifyConfig();

            for (User user : users)
            {
                Index idx = new Index.Builder(user).index(idxStr).type(typeStr).build();

                try
                {
                    DocumentResult result = client.execute(idx);
                    if (result.isSucceeded())
                    {
                        // Elasticsearch was successful
                        Log.i("Elasticsearch Success", "Setting user id");
                        user.setElasticId(result.getId());
                        return result.getId();
                    }
                    else
                    {
                        Log.i("Elasticsearch Error",
                                "index msising or could not connect:" +
                                        Integer.toString(result.getResponseCode()));
                        return null;
                    }
                }
                catch (Exception e)
                {
                    // Probably disconnected
                    Log.i("Elasticsearch Error", "Unexpected exception: " + e.toString());
                }
            }
            return null;
        }
    }

    /**
     * Sets up the configuration of the server if not yet established
     */
    public static void verifyConfig()
    {
        if (client == null)
        {
            Log.i("ElasticSearch", "verifying config...");
            DroidClientConfig.Builder builder =
                    new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            JestClientFactory factory = new JestClientFactory();

            DroidClientConfig config = builder.build();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
