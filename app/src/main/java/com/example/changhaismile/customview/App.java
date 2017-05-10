package com.example.changhaismile.customview;

import android.app.Application;

import com.orhanobut.logger.Logger;

/**
 * Created by changhaismile on 2017/4/11.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("log");
    }
}
