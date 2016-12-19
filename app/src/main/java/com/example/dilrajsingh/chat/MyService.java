package com.example.dilrajsingh.chat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Binder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MyService extends Service {

    private final IBinder mybinder = new MyLocalBinder();


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mybinder;
    }

    public String gettime(){
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss dd MMMM, yyyy",Locale.ENGLISH);
        return (df.format(new Date()));
    }

    public class MyLocalBinder extends Binder{
        MyService getService(){
            return MyService.this;
        }
    }
}
