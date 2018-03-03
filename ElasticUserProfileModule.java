package com.example.nafeekhan.wliaskel2;

import android.content.Context;
import android.os.Looper;

import com.searchly.jestdroid.JestDroidClient;

/**
 * Created by nafeekhan on 2018-03-03.
 */

public class ElasticUserProfileModule {
    private static JestDroidClient client;
    Context context;
    Looper looper;

    public ElasticUserProfileModule(Context context, Looper looper){
        this.context = context;
        this.looper = looper;
    }
    public interface ElasticUserprofileCallback{

    }
    public static class PushUserHandler{

    }
    public static class PullUserHandler{

    }

}
