package me.yeojoy.microdustwarning.util;

import android.util.Log;

import me.yeojoy.microdustwarning.BuildConfig;

/**
 * Created by yeojoy on 2014. 6. 13..
 */
public class DustLog {

    private static final String TAG = DustLog.class.getSimpleName();

    // Debug
    public static void d(String tag, String message) {
        if (BuildConfig.DEBUG) Log.d(tag, getLoggerLocation() + message);
    }

    public static void d(CharSequence message) {
        if (BuildConfig.DEBUG) Log.d(TAG, getLoggerLocation() 
                + message.toString());
    }

    // Warning
    public static void w(String tag, String message) {
        if (BuildConfig.DEBUG) Log.w(tag, getLoggerLocation() + message);
    }

    public static void w(CharSequence message) {
        if (BuildConfig.DEBUG) Log.w(TAG, getLoggerLocation() + message.toString());
    }

    // Info
    public static void i(String tag, String message) {
        if (BuildConfig.DEBUG) Log.i(tag, getLoggerLocation() + message);
    }

    public static void i(CharSequence message) {
        if (BuildConfig.DEBUG) Log.i(TAG, getLoggerLocation() + message.toString());
    }

    // Error
    public static void e(String tag, String message) {
        if (BuildConfig.DEBUG) Log.e(tag, getLoggerLocation() + message);
    }

    public static void e(CharSequence message) {
        if (BuildConfig.DEBUG) Log.e(TAG, getLoggerLocation() + message.toString());
    }

    /**
     * Class 이름, method이름, line number를 가져옴.
     * @return
     */
    private static String getLoggerLocation() {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(ste.getFileName().substring(0, ste.getFileName().indexOf(".")));
        sb.append(" > ");
        sb.append(ste.getMethodName());
        sb.append(" > #");
        sb.append(ste.getLineNumber());
        sb.append("] >>> ");

        return sb.toString();
//
//        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
//        StringBuffer tempBuf = new StringBuffer();
//
//        String[] temp = ste[4].getClassName().split(“\\.”);
//        tempBuf.append(“[“ + temp[temp.length - 1] + “]”);
//        tempBuf.append(“[“ + ste[depth].getLineNumber() + “] “);
//
//        return tempBuf.toString();
    }
}
