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
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.yeojoy.microdustwarning.BuildConfig;
import me.yeojoy.microdustwarning.DustActivity;
import me.yeojoy.microdustwarning.DustConstants;
import me.yeojoy.microdustwarning.R;
import me.yeojoy.microdustwarning.data.TextDataUtil;
import me.yeojoy.microdustwarning.entity.STATUS;

public class DustUtils implements DustConstants {

    public static final String TAG = DustUtils.class.getSimpleName();

    public static void sendNotification(Context context, STATUS[] status) {
        DustLog.i(TAG, "sendNotification()");
        boolean needToSendNoti = false;
        for (STATUS s : status) {
            if (s == STATUS.NONE) continue;

            if (s != STATUS.GOOD && s != STATUS.NORMAL) needToSendNoti = needToSendNoti || true;
        }

        if (!needToSendNoti) return;

        StringBuilder sb = new StringBuilder();
        sb.append("미세먼지   : ")
                .append(TextDataUtil.getStatusToString(status[0])).append("\n");
        sb.append("초미세먼지  : ")
                .append(TextDataUtil.getStatusToString(status[1])).append("\n");
        sb.append("오존      : ")
                .append(TextDataUtil.getStatusToString(status[2])).append("\n");
        sb.append("이산화질소  : ")
                .append(TextDataUtil.getStatusToString(status[3])).append("\n");
        sb.append("일산화탄소  : ")
                .append(TextDataUtil.getStatusToString(status[4])).append("\n");
        sb.append("아황산가스  : ")
                .append(TextDataUtil.getStatusToString(status[5])).append("\n");
        sb.append("통합지수   : ")
                .append(TextDataUtil.getStatusToString(status[6]));

        String shortMessage = "외출하기에 좋지 않습니다.";

        for (STATUS s : status) {
            if (s == STATUS.WORSE)
                shortMessage = "실내활동을 권장합니다.";
        }

        for (STATUS s : status) {
            if (s == STATUS.WORST)
                shortMessage = "위험합니다. 나가지 마세요!";
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("미세먼지 알림")
                .setContentText(shortMessage);

//        NotificationCompat.BigPictureStyle style1 = new NotificationCompat.BigPictureStyle();
//        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
//        Bitmap picture = BitmapFactory.decodeResource(context.getResources(), R.drawable.s1);
//        style1.bigLargeIcon(icon).bigPicture(picture);
//        style1.setBigContentTitle("큰화면에서 보여주는 거");

        NotificationCompat.BigTextStyle style2 = new NotificationCompat.BigTextStyle();
        style2.setBigContentTitle("미세먼지 알림");
        style2.bigText(sb);
        mBuilder.setStyle(style2);

//        RemoteViews views2 = new RemoteViews(context.getPackageName(), R.layout.noti);
//        views2.setTextViewText(R.id.tv_noti_msg, sb);

        Intent intent = new Intent(context, me.yeojoy.microdustwarning.DustActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
        if (DustSharedPreferences.getInstance().getBoolean(KEY_PREFS_NOTICE_VIBRATE, true)) {
            DustLog.i(TAG, "sendNotification(), Vibrator on");
            mBuilder.setVibrate(new long[]{0, 200, 200, 200});
            // 불빛 설정
            mBuilder.setLights(0xFFFF0000, 500, 500);
        } else {
            DustLog.i(TAG, "sendNotification(), Vibrator off");
        }
        Notification noti = mBuilder.build();
//        noti.bigContentView = views2;

        NotificationManager mng = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mng.notify(100, noti);
    }
    
    public static void sendNotification(Context context, STATUS s, String[] args) {
        DustLog.i(TAG, "sendNotification()");

        if (s == STATUS.GOOD || s == STATUS.NORMAL) return;
        
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Resources res = context.getResources();

        mBuilder.setContentTitle("미세먼지 알림");

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
                break;
            case WORSE:
                mBuilder.setSmallIcon(R.drawable.icon_worse);
                mBuilder.setLargeIcon(BitmapFactory.decodeResource(res,
                        R.drawable.icon_worse));
                style2.bigText(context.getString(R.string.dlg_status_worse_title));
                break;
            case WORST:
                mBuilder.setSmallIcon(R.drawable.icon_worst);
                mBuilder.setLargeIcon(BitmapFactory.decodeResource(res,
                        R.drawable.icon_worst));
                style2.bigText(context.getString(R.string.dlg_status_worst_title));
                break;
            default:
                return;
        }

        mBuilder.setStyle(style2);
        Intent intent = new Intent(context, DustActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK 
                | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, 
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
        if (DustSharedPreferences.getInstance().getBoolean(KEY_PREFS_NOTICE_VIBRATE, true)) {
            DustLog.i(TAG, "sendNotification(), Vibrator on");
            mBuilder.setVibrate(new long[]{0, 200, 200, 200});
            // 불빛 설정
            mBuilder.setLights(0xFFFF0000, 500, 500);
        } else {
            DustLog.i(TAG, "sendNotification(), Vibrator off");
        }
        Notification noti = mBuilder.build();
//        noti.bigContentView = views2;

        NotificationManager mng = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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
