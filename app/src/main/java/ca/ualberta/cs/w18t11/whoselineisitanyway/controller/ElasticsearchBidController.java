package ca.ualberta.cs.w18t11.whoselineisitanyway.controller;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.bid.Bid;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.constants.Constants;
import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Elastic Search controller for handling Bid queries
 *
 * @author Mark Griffith
 * @version 1.1
 */
public class ElasticsearchBidController
{
    private static String typeStr = "bids";

    private static JestDroidClient client;

    /**
     * Async task for updating the bids in the database
     */
    public static class AddBidsTask extends AsyncTask<Bid, Void, Boolean>
    {
        /**
         * It first attempts to update the bid in the database.
         * If the bod isn't found, it adds the bid to the database and sets its elastic id
         *
         * @param bids Bids to update/add to the database
         * @return Boolean.true if bid was updated added, else Boolean.false
         */
        @Override
        protected Boolean doInBackground(Bid... bids)
        {
            verifyConfig();

            for (Bid bid: bids)
            {

                Index index = new Index.Builder(bid)
                        .index(Constants.ELASTICSEARCH_INDEX)
                        .type(typeStr)
                        .id(bid.getElasticId())
                        .build();

                try
                {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded())
                    {
                        Log.i("Elasticsearch Success", "updated bid!");
                        return Boolean.TRUE;
                    }
                    else
                    {
                        Log.i("Elasticsearch Error",
                                "index missing or could not connect:" +
                                        Integer.toString(result.getResponseCode()));
                        Index idx = new Index.Builder(bid).index(Constants.ELASTICSEARCH_INDEX).type(typeStr).build();

                        try
                        {
                            result = client.execute(idx);
                            if (result.isSucceeded())
                            {
                                // Elasticsearch was successful
                                Log.i("Elasticsearch Success", "Added new bid to db!");
                                bid.setElasticId(result.getId());
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
     * Async task for getting a bid from the database using its elastic id
     */
    public static class GetBidByIdTask extends AsyncTask<String, Void, Bid>
    {
        /**
         * Gets a bid from the database based on its elastic id
         *
         * @param bidId elastic id of the bid to get
         * @return the found bid on success else null
         */
        @Override
        protected Bid doInBackground(String... bidId)
        {
            verifyConfig();

            Get get = new Get.Builder(Constants.ELASTICSEARCH_INDEX, bidId[0]).type(typeStr).build();

            try
            {
                JestResult result = client.execute(get);
                if(result.isSucceeded())
                {
                    // Bid found
                    Bid bid = result.getSourceAsObject(Bid.class);
                    return bid;
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
            // Bid not found
            return null;
        }

    }

    /**
     * Async task for getting bids from the database
     */
    public static class GetBidsTask extends AsyncTask<String, Void, ArrayList<Bid>>
    {
        /**
         * Gets a list of bids in the database matching a search query
         *
         * @param query search query detailing which bids to get from the database
         * @return list of bids on success else null
         */
        @Override
        protected ArrayList<Bid> doInBackground(String... query)
        {
            verifyConfig();

            ArrayList<Bid> bids = new ArrayList<Bid>();

            // Build the query
            if (query.length < 1)
            {
                Log.i("Elasticsearch Error","invalid query");
                return null;
            }

            Search search = new Search.Builder(query[0])
                    .addIndex(Constants.ELASTICSEARCH_INDEX)
                    .addType(typeStr)
                    .build();

            try
            {
                SearchResult result = client.execute(search);
                if(result.isSucceeded())
                {
                    List<Bid> foundBids = result.getSourceAsObjectList(Bid.class);
                    bids.addAll(foundBids);
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
            return bids;
        }
    }

    /**
     * Async task for removing a bid in the database
     */
    public static class RemoveBidTask extends AsyncTask<Bid, Void, Void>
    {
        /**
         * Removes the given bid from the database
         *
         * @param bid Bid to remove
         * @return null
         */
        @Override
        protected Void doInBackground(Bid... bid)
        {
            verifyConfig();

            Delete delete =
                    new Delete.Builder(bid[0].getElasticId()).index(Constants.ELASTICSEARCH_INDEX).type(typeStr).build();

            try
            {
                DocumentResult result = client.execute(delete);
                if (result.isSucceeded())
                {
                    Log.i("Elasticsearch Success", "deleted bid");
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
    public static class UpdateBidTask extends AsyncTask<Bid, Void, Boolean>
    {
        /**
         * Finds and replaces a bid in the database having the same elastic id
         * as the given bid
         *
         * @param bid Bid to update
         * @return Boolean.TRUE on success else Boolean.FALSE
         */
        @Override
        protected Boolean doInBackground(Bid... bid)
        {
            verifyConfig();

            Index index = new Index.Builder(bid[0])
                    .index(Constants.ELASTICSEARCH_INDEX)
                    .type(typeStr)
                    .id(bid[0].getElasticId())
                    .build();

            try
            {
                DocumentResult result = client.execute(index);
                if (result.isSucceeded())
                {
                    Log.i("Elasticsearch Success", "updated bid");
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
     * Async task for adding bids to the database
     */
    public static class OldAddBidsTask extends AsyncTask<Bid, Void, String>
    {
        /**
         * Adds the given list of bids to the database and sets their elastic id's
         *
         * @param bids Bids to add to the database
         * @return assigned elastic id on success else null
         */
        @Override
        protected String doInBackground(Bid... bids)
        {
            verifyConfig();

            for (Bid bid: bids)
            {
                Index idx = new Index.Builder(bid).index(Constants.ELASTICSEARCH_INDEX).type(typeStr).build();

                try
                {
                    DocumentResult result = client.execute(idx);
                    if (result.isSucceeded())
                    {
                        // Elasticsearch was successful
                        Log.i("Elasticsearch Success", "Setting bid id");
                        bid.setElasticId(result.getId());
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
                    Log.i("Elasticsearch Error", "Unexpected exception: " +
                            e.toString());
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
                    new DroidClientConfig.Builder(Constants.ELASTICSEARCH_URL);
            JestClientFactory factory = new JestClientFactory();

            DroidClientConfig config = builder.build();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }

}
