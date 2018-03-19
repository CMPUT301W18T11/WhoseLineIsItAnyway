package com.example.nafeekhan.wliaskel2;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

/**
 * Created by nafeekhan on 2018-03-03.
 */

public class ElasticMainModule {
    private static JestDroidClient client;
    Context context;
    Looper looper;
    //JestDroidClient client;
    public ElasticMainModule(Context context, Looper looper){
        this.context = context;
        this.looper = looper;
    }
    public interface ElasticMainCallback{

    }
    public static void verifySettings() {
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }

    }

    public class MyRequestsHandler extends Handler{

    }
    public class MyBiddedRequestsHandler extends Handler{

    }
    public class MyAssignedRequestsHandler extends Handler{

    }

    public class MyBidsTaskHandler extends Handler{

    }
    public class MySearchTasksHandler extends Handler{

    }
    public class MyTasksTaskHandler extends Handler{

    }

}
