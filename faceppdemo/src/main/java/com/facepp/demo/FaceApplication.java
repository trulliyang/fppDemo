package com.facepp.demo;

import android.app.Application;

/**
 * Created by mrsimple on 13/3/2019
 */
public class FaceApplication extends Application {

    public static Application sContext ;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this ;
    }
}
