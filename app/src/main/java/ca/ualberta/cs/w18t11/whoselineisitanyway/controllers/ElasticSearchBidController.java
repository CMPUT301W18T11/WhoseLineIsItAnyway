package ca.ualberta.cs.w18t11.whoselineisitanyway.controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;
import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Elastic Search controller for handling Task queries
 *
 * @author Mark Griffith
 * @version 1.0
 */

public class ElasticSearchBidController {
    private static String typeStr = "bids";
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

            ArrayList<Bid> bids = new ArrayList<Bid>();

            // Build the query
            if (query.length < 1){
                Log.i("Elasticsearch Error","GetMultipleUsersTask params.length < 1");
                return null;
            }

            Search search = new Search.Builder(query[0])
                    .addIndex(idxStr)
                    .addType(typeStr)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if(result.isSucceeded()) {
                    List<User> foundUsers = result.getSourceAsObjectList(User.class);
                    bids.addAll(foundUsers);
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
            return bids;
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
                        Log.i("Elasticsearch Success", "Setting bid id");
                        bid.setElasticId(result.getId());
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

            Index index = new Index.Builder(bid[0]).index(idxStr).type(typeStr).id(bid[0].getId()).build();

            try {
                DocumentResult result = client.execute(index);
                if (result.isSucceeded()) {
                    Log.i("Elasticsearch Success", "updated bid");
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

    public static class RemoveBidTask extends AsyncTask<Bid, Void, Void> {

        @Override
        protected Void doInBackground(Bid... bid) {
            verifyConfig();

            Delete delete = new Delete.Builder(bid[0].getElasticId()).index(idxStr).type(typeStr).build();

            try {
                DocumentResult result = client.execute(delete);
                if (result.isSucceeded()) {
                    Log.i("Elasticsearch Success", "deleted bid";
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

            DroidClientConfig config = builder.build();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }

}
