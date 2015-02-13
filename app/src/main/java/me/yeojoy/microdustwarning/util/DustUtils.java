package me.yeojoy.microdustwarning.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import me.yeojoy.microdustwarning.BuildConfig;
import me.yeojoy.microdustwarning.DustActivity;
import me.yeojoy.microdustwarning.DustConstants;
import me.yeojoy.microdustwarning.R;
import me.yeojoy.microdustwarning.entity.STATUS;

public class DustUtils implements DustConstants {

    public static final String TAG = DustUtils.class.getSimpleName();

    public static void sendNotification(Context context, STATUS s, String[] args) {
        DustLog.i(TAG, "sendNotification()");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Resources res = context.getResources();

        mBuilder.setTicker("미세먼지 알림");
        
        NotificationCompat.BigTextStyle style2 = new NotificationCompat.BigTextStyle();
        style2.setBigContentTitle("미세먼지 알림");

        switch (s) {
            case GOOD:
                mBuilder.setSmallIcon(R.drawable.icon_good);
                mBuilder.setLargeIcon(BitmapFactory.decodeResource(res, 
                        R.drawable.icon_good));
                style2.bigText(context.getString(R.string.dlg_status_good_title));
                break;
            case NORMAL:
                mBuilder.setSmallIcon(R.drawable.icon_normal);
                mBuilder.setLargeIcon(BitmapFactory.decodeResource(res,
                        R.drawable.icon_normal));
                style2.bigText(context.getString(R.string.dlg_status_normal_title));
                break;
            case BAD:
                mBuilder.setSmallIcon(R.drawable.icon_bad);
                mBuilder.setLargeIcon(BitmapFactory.decodeResource(res,
                        R.drawable.icon_bad));
                style2.bigText(context.getString(R.string.dlg_status_bad_title));
                // 불빛 설정
                mBuilder.setLights(0xFFFFFF00, 500, 500);
                break;
            case WORSE:
                mBuilder.setSmallIcon(R.drawable.icon_worse);
                mBuilder.setLargeIcon(BitmapFactory.decodeResource(res,
                        R.drawable.icon_worse));
                style2.bigText(context.getString(R.string.dlg_status_worse_title));
                // 불빛 설정
                mBuilder.setLights(0xFFFF8900, 500, 500);
                break;
            case WORST:
                mBuilder.setSmallIcon(R.drawable.icon_worst);
                mBuilder.setLargeIcon(BitmapFactory.decodeResource(res,
                        R.drawable.icon_worst));
                style2.bigText(context.getString(R.string.dlg_status_worst_title));
                // 불빛 설정
                mBuilder.setLights(0xFFFF0000, 500, 500);
                break;
            default:
                return;
        }

        mBuilder.setStyle(style2);
        
        Intent intent = new Intent("me.yeojoy.microdustwarning.action.START_APP");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 10002,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        // Add Action Buttons to Wear
        // REFS : http://developer.android.com/training/wearables/notifications/creating.html#ActionButtons

//        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
//        Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode("37.239217, 127.384724"));
//        mapIntent.setData(geoUri);
//        PendingIntent mapPendingIntent =
//                PendingIntent.getActivity(context, 0, mapIntent, 0);
//
//        mBuilder.addAction(R.drawable.ic_launcher, "함 눌러봐?", mapPendingIntent);
        mBuilder.setAutoCancel(true);

        DustSharedPreferences.getInstance().init(context);

        // 진동 설정
        if (s != STATUS.GOOD && s != STATUS.NORMAL &&
                DustSharedPreferences.getInstance()
                        .getBoolean(KEY_PREFS_NOTICE_VIBRATE, true)) {
            DustLog.i(TAG, "sendNotification(), Vibrator on");
            mBuilder.setVibrate(new long[]{0, 200, 200, 200});
        } else {
            DustLog.i(TAG, "sendNotification(), Vibrator off");
        }
        Notification noti = mBuilder.build();
        NotificationManager mng = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        mng.notify(100, noti);
    }

    /**
     * get Application Version
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        
        if (!TextUtils.isEmpty(BuildConfig.VERSION_NAME))
            return BuildConfig.VERSION_NAME;
        
        PackageInfo info = null;
        try {
                info = context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        if (info != null) return info.versionName;
        return null;
    }
}
