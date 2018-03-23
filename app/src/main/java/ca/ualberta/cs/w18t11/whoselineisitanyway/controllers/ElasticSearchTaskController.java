package ca.ualberta.cs.w18t11.whoselineisitanyway.controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.cs.w18t11.whoselineisitanyway.model.task.Task;
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
public class ElasticSearchTaskController {
    private static String typeStr = "tasks";
    private static String idxStr = "cmput301w18t11_whoselineisitanyways";
    private static JestDroidClient client;

    public static class GetTaskByIdTask extends AsyncTask<String, Void, Task> {

        @Override
        protected Task doInBackground(String... taskId) {
            verifyConfig();

            Get get = new Get.Builder(idxStr, taskId[0]).type(typeStr).build();

            try {
                JestResult result = client.execute(get);
                if(result.isSucceeded()) {
                    // User found
                    Task task = result.getSourceAsObject(Task.class);
                    return task;
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
            // Task not found
            return null;
        }
    }

    public static class GetTaskssTask extends AsyncTask<String, Void, ArrayList<Task>> {

        @Override
        protected ArrayList<Task> doInBackground(String... query) {
            verifyConfig();

            ArrayList<Task> task = new ArrayList<Task>();

            // Build the query
            if (query.length < 1){
                Log.i("Elasticsearch Error","Invalid query");
                return null;
            }

            Search search = new Search.Builder(query[0])
                    .addIndex(idxStr)
                    .addType(typeStr)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if(result.isSucceeded()) {
                    List<Task> foundUsers = result.getSourceAsObjectList(Task.class);
                    task.addAll(foundUsers);
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
            return task;
        }
    }

    public static class AddTasksTask extends AsyncTask<Task, Void, String> {

        @Override
        protected String doInBackground(Task... tasks) {
            verifyConfig();

            for (Task task : tasks) {
                Index idx = new Index.Builder(task).index(idxStr).type(typeStr).build();

                try {
                    DocumentResult result = client.execute(idx);
                    if (result.isSucceeded()) {
                        // Elasticsearch was successful
                        Log.i("Elasticsearch Success", "Setting task id");
                        task.setElasticId(result.getId());
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

    public static class UpdateTaskTask extends AsyncTask<Task, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Task... task) {
            verifyConfig();

            Index index = new Index.Builder(task[0]).index(idxStr).type(typeStr).id(task[0].getElasticId()).build();

            try {
                DocumentResult result = client.execute(index);
                if (result.isSucceeded()) {
                    Log.i("Elasticsearch Success", "updated task: " + task[0].getElasticId());
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

    public static class RemoveTaskTask extends AsyncTask<Task, Void, Void> {

        @Override
        protected Void doInBackground(Task... task) {
            verifyConfig();

            Delete delete = new Delete.Builder(task[0].getElasticId()).index(idxStr).type(typeStr).build();

            try {
                DocumentResult result = client.execute(delete);
                if (result.isSucceeded()) {
                    Log.i("Elasticsearch Success", "deleted task: " + task[0].getElasticId());
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
