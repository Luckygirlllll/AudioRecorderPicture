package com.example.attracti.audiorecorderpicture;

import android.app.Application;
import android.content.Context;

/**
 * Created by Iryna on 6/7/16.
 * <p>
 * Use context in every place of the application using App.context
 */

public class App extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}