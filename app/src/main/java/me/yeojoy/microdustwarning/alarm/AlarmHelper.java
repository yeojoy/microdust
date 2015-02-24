package me.yeojoy.microdustwarning.alarm;

import android.content.Context;
import android.content.Intent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import me.yeojoy.microdustwarning.BuildConfig;
import me.yeojoy.microdustwarning.DustApplication;
import me.yeojoy.microdustwarning.DustConstants;
import me.yeojoy.microdustwarning.util.DustLog;
import me.yeojoy.microdustwarning.util.DustSharedPreferences;

/**
 * Created by yeojoy on 15. 1. 7..
 */
public class AlarmHelper implements DustConstants {
    private static final String TAG = AlarmHelper.class.getSimpleName();

    private static ScheduledFuture scheduledFuture;
    
    private static AlarmHelper mAlarmHelper;
    
    private Context mContext;

    public AlarmHelper(Context context) {
        mContext = context;
    }
    
    public static AlarmHelper getInstance(Context context) {
        if (mAlarmHelper == null) {
            mAlarmHelper = new AlarmHelper(context);
        }
        
        return mAlarmHelper;
    }
    
    /**
     * 알람 매니저 종료
     */
    public void cancelAlarmManager() {
        DustLog.i(TAG, "cancelAlarmManager()");

        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        
        // TODO 이후 변경 필요. 임시방편으로 지역만 저장
        DustSharedPreferences.getInstance().clear();
        DustSharedPreferences.getInstance().putString(KEY_PREFS_LOCALITY,
                DustApplication.locality);
    }

    /**
     * 알람 매니저 실행
     */
    public void launchAlarmManager() {
        DustLog.i(TAG, "launchAlarmManager()");

        ScheduledExecutorService service =  Executors.newScheduledThreadPool(1);

        long notiTime = NOTI_TIME_REAL;

        if (BuildConfig.DEBUG) {
            notiTime = 10000;
        }
        
        scheduledFuture = service.scheduleAtFixedRate(mServiceLauncherRunnable, 
                1000, notiTime, TimeUnit.MILLISECONDS);
    }
    
    private Runnable mServiceLauncherRunnable = new Runnable() {
        @Override
        public void run() {
            DustLog.i(TAG, "Runnable, run()");
            Intent intent = new Intent("me.yeojoy.microdust.action.RUN_SERVICE");
            mContext.startService(intent);
        }
    };
    
}
