package com.example.cchat;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String CHANNEL_ID="exampleServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationchannel();
    }

    private void createNotificationchannel() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            NotificationChannel servicechannel=new NotificationChannel(
                    CHANNEL_ID,"EXAMPLESERVICE", NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager=getSystemService(NotificationManager.class );
            manager.createNotificationChannel(servicechannel);


        }
    }
}
