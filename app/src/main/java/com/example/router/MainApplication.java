package com.example.router;

import android.app.Application;

import com.example.routeapi.Routers;

/**
 * Created by mj on 2021/2/10 15:10
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Routers.init(this);
    }
}
