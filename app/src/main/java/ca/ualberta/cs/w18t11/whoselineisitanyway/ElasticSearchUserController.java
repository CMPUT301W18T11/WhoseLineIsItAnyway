package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import io.searchbox.client.JestResult;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;

public class ElasticSearchUserController {



    private static ElasticSearchUserController instance = new ElasticSearchUserController();

    protected ElasticSearchUserController() {
    }

    /**
     * gets instance of the db
     * @return instance
     */
    public static ElasticSearchUserController getInstance() {
        return instance;
    }

    private static JestDroidClient client;
    //points to server
    private static String DATABASE_URL = "http://cmput301.softwareprocess.es:8080/";
    //type of object stored on DB
    private static final String TYPE = "User";
    //index on server
    private static final String INDEX = "cmput301w18t11";

    /**
     * function used to check if user exists and get them from db, or if they dont exist, add them
     * to db
     * @param user
     * @return boolean
     */
    public boolean verifyUser(User user) {
        GetUserTask getUserTask = new GetUserTask();
        User temp = new User();

        try {
            temp = getUserTask.execute(user.getDocId()).get();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }

        //if user did not exist, add one
        if(temp == null){
            ElasticSearchUserController.AddUserTask addUserTask = new ElasticSearchUserController.AddUserTask();
            addUserTask.execute(user);
            return true;
        }
        return  false;
    }

    /**
     * delete user from db
     * @param user
     * @return boolean
     */



    /**
     * Add user to ES DB
     */
    public static class AddUserTask extends AsyncTask<User, Void, Boolean> {

        @Override
        protected Boolean doInBackground(User...search_parameters){
            verifySettings();

            boolean userExist = false;
            Index index = new Index.Builder(search_parameters[0]).index(INDEX).type(TYPE).id(search_parameters[0].getUserName()).build();

            try {
                DocumentResult result = client.execute(index);

                if (result.isSucceeded()){
                    userExist = true;
                    Log.d("user synced", result.getId());
                }
            }
            catch (IOException e) {
                Log.d("Error", "The application failed to build and send the User");
            }

            return userExist;
        }
    }



    /**
     * Search for username in DB, and return user, either as null or as the object
     */
    public static class GetUserTask extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... search_parameters) {
            verifySettings();

            Log.d("Error", "name: " + search_parameters[0]);
            Get get = new Get.Builder(INDEX, search_parameters[0]).type(TYPE).build();


            User user = null;
            try {
                JestResult result = client.execute(get);

                if(result.isSucceeded()){
                    Log.d("Error", "success getting user");
                    //user = result.getSourceAsObject(User.class);
                    Log.d("Error", "Name that was gotten: " + result.getJsonObject());
//
                    String userJson = result.getSourceAsString();


                    Log.d("Error", "JsonString: " + userJson);

                    //Gson gson = new Gson();
                    //Taken from http://stackoverflow.com/questions/7910734/gsonbuilder-setdateformat-for-2011-10-26t202959-0700
                    //Date: 3/21/2017
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
//                    //GsonBuilder gsonBuilder = new GsonBuilder();
//                    //Gson gson = gsonBuilder.create();
                    user = gson.fromJson(userJson, User.class);
                }

                else{
                    Log.d("Error", "Elasticsearch was not able to get the user.");
                }

                //Log.d("test1", user.getUserName());
            }
            catch (IOException e) {
                Log.i("Error", "The application failed to connect to DB");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return user;
        }

    }


    private static void verifySettings(){
        if(client == null){
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder(DATABASE_URL);
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
