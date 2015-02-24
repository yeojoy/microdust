package me.yeojoy.microdustwarning;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import me.yeojoy.microdustwarning.alarm.AlarmHelper;

/**
 * Created by yeojoy on 15. 1. 7..
 */
public class PhoneTurnOnReceiver extends BroadcastReceiver {
    private static final String TAG = PhoneTurnOnReceiver.class.getSimpleName();
    
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive()");
        AlarmHelper.getInstance(context.getApplicationContext())
                .launchAlarmManager();
    }
}
