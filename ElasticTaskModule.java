package com.example.nafeekhan.wliaskel2;

import android.content.Context;
import android.os.Looper;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

/**
 * Created by nafeekhan on 2018-03-03.
 */

public class ElasticTaskModule {
    private static JestDroidClient client;
    Context context;
    Looper looper;
    public ElasticTaskModule(Context context, Looper looper){
        this.context = context;
        this.looper = looper;

    }
    public interface ElasticTaskCallback{

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
    public static class PushTaskHandler{

    }
    public static class PullTaskHandler{

    }
    public static class PlaceBid{

    }
}
