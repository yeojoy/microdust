package me.yeojoy.microdustwarning.util;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

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

    // using Throwable
    // Debug
    public static void d(String tag, Throwable tr) {
        if (BuildConfig.DEBUG) Log.d(tag, getLoggerLocation() + getStackTraceString(tr));
    }

    public static void d(Throwable tr) {
        if (BuildConfig.DEBUG) Log.d(TAG, getLoggerLocation()
                + getStackTraceString(tr));
    }

    // Warning
    public static void w(String tag, Throwable tr) {
        if (BuildConfig.DEBUG) Log.w(tag, getLoggerLocation() + getStackTraceString(tr));
    }

    public static void w(Throwable tr) {
        if (BuildConfig.DEBUG) Log.w(TAG, getLoggerLocation() + getStackTraceString(tr));
    }

    // Info
    public static void i(String tag, Throwable tr) {
        if (BuildConfig.DEBUG) Log.i(tag, getLoggerLocation() + getStackTraceString(tr));
    }

    public static void i(Throwable tr) {
        if (BuildConfig.DEBUG) Log.i(TAG, getLoggerLocation() + getStackTraceString(tr));
    }

    // Error
    public static void e(String tag, Throwable tr) {
        if (BuildConfig.DEBUG) Log.e(tag, getLoggerLocation() + getStackTraceString(tr));
    }

    public static void e(Throwable tr) {
        if (BuildConfig.DEBUG) Log.e(TAG, getLoggerLocation() + getStackTraceString(tr));
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
    }

    private static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }
}
