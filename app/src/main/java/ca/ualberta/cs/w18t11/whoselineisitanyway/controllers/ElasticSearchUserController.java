package ca.ualberta.cs.w18t11.whoselineisitanyway.controllers;

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
 * @version 1.0
 */
public class ElasticSearchUserController {
    private static String typeStr = "users";
    private static String idxStr = "cmput301w18t11_whoselineisitanyways";
    private static JestDroidClient client;

    public static class AddUsersTask extends AsyncTask<User, Void, String> {

        @Override
        protected String doInBackground(User... users) {
            verifyConfig();

            for (User user : users) {
                Index idx = new Index.Builder(user).index(idxStr).type(typeStr).build();

                try {
                    DocumentResult result = client.execute(idx);
                    if (result.isSucceeded()) {
                        // Elasticsearch was successful
                        Log.i("Elasticsearch Success", "Setting user id");
                        user.setId(result.getId());
                        return result.getId();
                    } else {
                        Log.i("Elasticsearch Error",
                                "index msising or could not connect:" +
                                Integer.toString(result.getResponseCode()));
                        return null;
                    }
                } catch (Exception e) {
                    // Probably disconnected
                    Log.i("Elasticsearch Error", "Unexpected exception: " + e.toString());
                }
            }
            return null;
        }
    }

    public static class GetUserTask extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... username) {
            verifyConfig();

            Get get = new Get.Builder(idxStr, username[0]).type(typeStr).build();

            try {
                JestResult result = client.execute(get);
                if(result.isSucceeded()) {
                    // User found
                    User user = result.getSourceAsObject(User.class);
                    return user;
                } else {
                    Log.i("Elasticsearch Error",
                            "index missing or could not connect:" +
                                    Integer.toString(result.getResponseCode()));
                    return null;
                }
            } catch (Exception e) {
                // Probably disconnected
                Log.i("Elasticsearch Error", "Unexpected exception: " + e.toString());
            }
            // User not found, return blank User object
            return new User(new EmailAddress("DefaultLocalPart","Domain@Default.com"),
                    new PhoneNumber(0,0,0,0),
                    "Default Name");
        }
    }

    public static class GetMultipleUsersTask extends AsyncTask<String, Void, ArrayList<User>> {

        @Override
        protected ArrayList<User> doInBackground(String... params) {
            verifyConfig();

            ArrayList<User> users = new ArrayList<User>();

            // Build the query
            if (params.length < 1){
                Log.i("Elasticsearch Error","GetMultipleUsersTask params.length < 1");
                return null;
            }

            Log.i("INDEX", idxStr);

            Search search = new Search.Builder(params[0])
                    .addIndex(idxStr)
                    .addType(typeStr)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if(result.isSucceeded()) {
                    List<User> foundUsers = result.getSourceAsObjectList(User.class);
                    users.addAll(foundUsers);
                } else {
                    Log.i("Elasticsearch Error",
                            "index missing or could not connect:" +
                                    Integer.toString(result.getResponseCode()));
                    return null;
                }
            } catch (Exception e) {
                // Probably disconnected
                Log.i("Elasticsearch Error", "Unexpected exception: " + e.toString());
                return null;
            }
            return users;
        }
    }

    public static class UpdateUsersTask extends AsyncTask<User, Void, Boolean> {

        @Override
        protected Boolean doInBackground(User... users) {
            verifyConfig();

            Index index = new Index.Builder(users[0]).index(idxStr).type(typeStr).id(users[0].getId()).build();

            try {
                DocumentResult result = client.execute(index);
                if (result.isSucceeded()) {
                    Log.i("Elasticsearch Success", "updated user: " + users[0].getUsername());
                    return Boolean.TRUE;
                } else {
                    Log.i("Elasticsearch Error",
                            "index missing or could not connect:" +
                                    Integer.toString(result.getResponseCode()));
                    return Boolean.FALSE;
                }
            } catch (Exception e) {
                Log.i("Elasticsearch Error", "Unexpected exception: " + e.toString());
            }
            return Boolean.FALSE;
        }
    }

    public static class RemoveUserTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            verifyConfig();

            Delete delete = new Delete.Builder(users[0].getId()).index(idxStr).type(typeStr).build();

            try {
                DocumentResult result = client.execute(delete);
                if (result.isSucceeded()) {
                    Log.i("Elasticsearch Success", "deleted user: " + users[0].getUsername());
                } else {
                    Log.i("Elasticsearch Error",
                            "index missing or could not connect:" +
                                    Integer.toString(result.getResponseCode()));
                }
            } catch (Exception e) {
                Log.i("Elasticsearch Error", "Unexpected exception: " + e.toString());
            }
            return null;
        }
    }

    public static void verifyConfig() {
        if (client == null) {
            Log.i("ElasticSearch", "verifying config...");
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            JestClientFactory factory = new JestClientFactory();
            Log.i("ElasticSearch", "Jest = " + factory.toString());

            DroidClientConfig config = builder.build();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
