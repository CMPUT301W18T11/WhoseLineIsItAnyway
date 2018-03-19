package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;

import io.searchbox.client.JestResult;
import io.searchbox.core.Get;

/**
 * Created by nafeekhan on 2018-02-27.
 */

public class ESUpdateService extends IntentService
{
    private static JestDroidClient client;
    private static String DATABASE_URL = "http://cmput301.softwareprocess.es:8080/";
    public final String action = "activity";
    public final String USER = "user";

    private static void verifySettings()
    {
        if (client == null)
        {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder(DATABASE_URL);
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();

            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }

    //public ESUpdateService(){
    //super.()
    //}
    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (intent != null)
        {
            if (isNetworkAvailable())
            {
                verifySettings();
                if (intent.getAction().equals("signIn"))
                {
                    String email = intent.getStringExtra("email");
                    String password = intent.getStringExtra("password");
                    Get get = new Get.Builder("data", "email").type("user").build();
                    try
                    {
                        JestResult result = client.execute(get);
                        if (result.isSucceeded())
                        {

                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }


            }
        }
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
