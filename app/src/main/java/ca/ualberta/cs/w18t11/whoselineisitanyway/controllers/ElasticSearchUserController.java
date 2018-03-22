package ca.ualberta.cs.w18t11.whoselineisitanyway.controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;

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
                        return result.getId();
                    } else {
                        Log.i("Elasticsearch Error",
                                "index does not exist or could not connect:" +
                                Integer.toString(result.getResponseCode()));
                        return null;
                    }
                }
                catch (Exception e) {
                    Log.i("Elasticsearch Error", "Exception in client.execute(idx)");
                }
            }
            return null;
        }
    }

    public static class GetUserTask extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... params) {
            verifyConfig();

            return null;
        }
    }

    public static class GetMultipleUsersTask extends AsyncTask<String, Void, ArrayList<User>> {

        @Override
        protected ArrayList<User> doInBackground(String... params) {
            verifyConfig();

            return null;
        }
    }

    public static class UpdateUsersTask extends AsyncTask<User, Void, Integer> {

        @Override
        protected Integer doInBackground(User... users) {
            verifyConfig();

            return null;
        }
    }

    public static class RemoveUserTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            verifyConfig();

            return null;
        }
    }

    public static void verifyConfig() {
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            JestClientFactory factory = new JestClientFactory();

            DroidClientConfig config = builder.build();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
