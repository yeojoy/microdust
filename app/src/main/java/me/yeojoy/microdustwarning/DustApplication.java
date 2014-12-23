package me.yeojoy.microdustwarning;

import android.app.Application;

import com.squareup.otto.Bus;

/**
 * Created by yeojoy on 2014. 6. 12..
 */
public class DustApplication extends Application {
    public static Bus bus;
    
    public static String locality;
    
    @Override
    public void onCreate() {
        super.onCreate();
        bus = new Bus();
    }
}
