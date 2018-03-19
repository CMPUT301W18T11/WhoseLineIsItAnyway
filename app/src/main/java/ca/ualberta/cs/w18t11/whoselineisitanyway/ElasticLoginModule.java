package com.example.nafeekhan.wliaskel2;

/**
 * Created by nafeekhan on 2018-02-21.
 */

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;

import io.searchbox.client.JestResult;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;


public class ElasticLoginModule
{
    private static JestDroidClient client;
    //JestDroidClient client;
    Context context;
    Looper looper;

    public ElasticLoginModule(Context context, Looper looper)
    {
        this.context = context;
        this.looper = looper;
    }

    public static void verifySettings()
    {
        if (client == null)
        {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder(
                    "http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }


    public interface ElasticLoginCallback
    {

    }

    public static class SignupHandler extends Handler
    {


        @Override
        protected void handleMessage

        {
            verifySettings();

            for (Task task : params)
            {
                Index index = new Index.Builder(task).index().type().build();

                try
                {
                    // where is the client?
                    DocumentResult result = client.execute(index);

                    if (result.isSucceeded())
                    {
                        task.setID(result.getId());

                    }
                    else
                    {
                        Log.i("Error", "Some error =(");
                    }

                }
                catch (Exception e)
                {
                    Log.i("Error", "The application failed to build ....");
                }
            }

        }

    }

    //public static class SearchByField extends AsyncTask<SearchByField>

    public class LoginHandler extends Handler
    {

        @Override
        protected void handleMessage(Message)
        {
            //protected Boolean doInBackground(User...params){
            verifySettings();
            User u = (User) params[0];
            String email = u.getEmail();

            Get get = new Get.Builder("AllUserProfiles", email).type("User").build();
            try
            {
                JestResult result = client.execute(get);
                if (result.isSucceeded())
                {
                    //already exists by this emailID;
                    return false;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }


        }
    }

}
