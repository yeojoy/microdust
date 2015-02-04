package me.yeojoy.microdustwarning.alarm;

import android.app.AlarmManager;
import android.content.Context;

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

    /**
     * 알람 매니저 종료
     */
    public static void cancelAlarmManager(Context context) {
        DustLog.i(TAG, "cancelAlarmManager()");
        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); 
        alarmManager.cancel(DustApplication.mPendingIntent);

        // TODO 이후 변경 필요. 임시방편으로 지역만 저장
        DustSharedPreferences.getInstance().clear();
        DustSharedPreferences.getInstance().putString(KEY_PREFS_LOCALITY,
                DustApplication.locality);
    }

    /**
     * 알람 매니저 실행
     */
    public static void launchAlarmManager(Context context) {
        DustLog.i(TAG, "launchAlarmManager()");

        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long notiTime = NOTI_TIME_REAL;

        if (BuildConfig.DEBUG) {
            notiTime = NOTI_TIME_TEST;
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 1000l, notiTime,
                DustApplication.mPendingIntent);
    }
    
}
