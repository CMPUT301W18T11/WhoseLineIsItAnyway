package ca.ualberta.cs.w18t11.whoselineisitanyway.controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.EmailAddress;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.PhoneNumber;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;
import io.searchbox.client.JestResult;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;

/**
 * Elastic Search controller for handling Task queries
 *
 * @author Mark Griffith
 * @version 1.0
 */

public class ElasticSearchBidController {
    private static String typeStr = "tasks";
    private static String idxStr = "cmput301w18t11_whoselineisitanyways";
    private static JestDroidClient client;

    public static class GetBidByIdTask extends AsyncTask<String, Void, Bid> {

        @Override
        protected Bid doInBackground(String... bidId) {
            verifyConfig();

            Get get = new Get.Builder(idxStr, bidId[0]).type(typeStr).build();

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
            // User not found
            return null;
        }

    }

    public static class GetBidsTask extends AsyncTask<String, Void, ArrayList<Bid>> {

        @Override
        protected ArrayList<Bid> doInBackground(String... query) {
            verifyConfig();

        }
    }

    public static class AddBidsTask extends AsyncTask<Bid, Void, String> {

        @Override
        protected String doInBackground(Bid... bids) {
            verifyConfig();

            for (Bid bid: bids) {
                Index idx = new Index.Builder(bid).index(idxStr).type(typeStr).build();

                try {
                    DocumentResult result = client.execute(idx);
                    if (result.isSucceeded()) {
                        // Elasticsearch was successful
                        Log.i("Elasticsearch Success", "Setting user id");
                        bid.setId(result.getId());
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

    public static class UpdateBidTask extends AsyncTask<Bid, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Bid... bid) {
            verifyConfig();
        }

    }

    public static class RemoveBidTask extends AsyncTask<Bid, Void, Void> {

        @Override
        protected Void doInBackground(Bid... bid) {
            verifyConfig();
        }
    }

    public static void verifyConfig() {
        if (client == null) {
            Log.i("ElasticSearch", "verifying config...");
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            JestClientFactory factory = new JestClientFactory();

            DroidClientConfig config = builder.build();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }

}
