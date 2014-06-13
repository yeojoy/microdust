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
        if (BuildConfig.DEBUG) Log.d(tag, message);
    }

    public static void d(CharSequence message) {
        if (BuildConfig.DEBUG) Log.d(TAG, message.toString());
    }

    // Warning
    public static void w(String tag, String message) {
        if (BuildConfig.DEBUG) Log.w(tag, message);
    }

    public static void w(CharSequence message) {
        if (BuildConfig.DEBUG) Log.w(TAG, message.toString());
    }

    // Info
    public static void i(String tag, String message) {
        if (BuildConfig.DEBUG) Log.i(tag, message);
    }

    public static void i(CharSequence message) {
        if (BuildConfig.DEBUG) Log.i(TAG, message.toString());
    }

    // Error
    public static void e(String tag, String message) {
        if (BuildConfig.DEBUG) Log.e(tag, message);
    }

    public static void e(CharSequence message) {
        if (BuildConfig.DEBUG) Log.e(TAG, message.toString());
    }
}
