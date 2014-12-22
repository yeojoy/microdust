package me.yeojoy.microdustwarning.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DustFileLogger {

    private static final String TAG = DustFileLogger.class.getSimpleName();

    private static final int MAX_FILE_SIZE = 100;
    private static String PREFIX_FILE = "dust_log_";
    private static final String SUBFIX_FILE = ".txt";
    private static final String PATH_LOG = "/dustLog/";
    private static String mTimeForFileName;
    private static String mTime;
    private static String mFileName;
    private static String mFilePath;

    private static Context mContext;

    private static DustFileLogger fileLogger;

    public static DustFileLogger getInstance() {
        if (fileLogger == null) {
            fileLogger = new DustFileLogger();
        }

        return fileLogger;
    }

    public void init(Context context) {
        mContext = context;
    }

    public void startLogger(String startMessage) {
        DustLog.i(TAG, "startLogger()");

        if (makeLoggerFile()) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n=================================================\n");
            sb.append(startMessage).append("\n");
            sb.append("=================================================\n");

            writeLogToFile(sb.toString());
        }
    }

    private boolean makeLoggerFile() {
        DustLog.i(TAG, "makeLoggerFile()");

        mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + PATH_LOG;

        File dir = new File(mFilePath);

        if (!dir.exists()) {
            DustLog.i(TAG, "makeLoggerFile(), path doesn't exist. mkdir");
            dir.mkdir();
        }

        if (!DustSharedPreferences.getInstance().hasPrefs())
            DustSharedPreferences.getInstance().init(mContext);

        mTimeForFileName = DustSharedPreferences.getInstance().getString("log_date", "");

        if (TextUtils.isEmpty(mTimeForFileName)) {
            mTimeForFileName = new SimpleDateFormat("MMdd_HHmm").format(new Date());
            DustSharedPreferences.getInstance().putString("log_date", mTimeForFileName);
        }

        mTime = mTimeForFileName;
        mFileName = PREFIX_FILE + mTime + SUBFIX_FILE;

        File logFile = new File(mFilePath + mFileName);
        DustLog.i(TAG, "makeLoggerFile(), File Path is " + logFile.getAbsolutePath());

        if (logFile.exists()) {
            DustLog.i(TAG, "makeLoggerFile(), File exists.");
            // 파일 크기 비교 후 새로 생성

            checkoutLogFile(logFile);
        } else {
            // 파일 생성
            try {
                logFile.createNewFile();
                DustLog.i(TAG, "makeLoggerFile(), create a new file.");
            } catch (IOException e) {
                e.printStackTrace();

                return false;
            }
        }

        return true;
    }

    private void checkoutLogFile(File logFile) {
        if (logFile.length() > (MAX_FILE_SIZE * 1024)) {
            DustLog.i(TAG, "makeLoggerFile(), over Max size");

            DustSharedPreferences.getInstance().putString("log_date", null);

            startLogger("File is over max size.");
        }
    }

    public void writeLogToFile(String message) {
        if (message == null) return;

        DustLog.i(TAG, "writeLogToFile()");

        File logFile = new File(mFilePath + mFileName);

        if (!logFile.exists()) {
            startLogger("시작합니다.");
        }

        checkoutLogFile(logFile);

        DustLog.i(TAG, "writeLogToFile(), File is " + logFile.getAbsolutePath());
        FileInputStream fis = null;
        StringBuilder preDataBuilder = new StringBuilder();
        String preData = null;
        try {
            if (logFile.exists() && logFile.length() > 0) {
                DustLog.i(TAG, "writeLogToFile(), Log file exists.");
                fis = new FileInputStream(logFile);
                int avail = fis.available();
                byte[] data = new byte[avail];

                while (fis.read(data) != -1) {
                    preDataBuilder.append(new String(data));
                }

                preData = preDataBuilder.toString();
                if (!TextUtils.isEmpty(preData))
                    DustLog.d(TAG, "Message : " + preData);
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (IOException e) {}
        }

        FileOutputStream fos = null;

        StringBuilder msgBuilder = new StringBuilder();

        try {
            fos = new FileOutputStream(logFile);

            // 먼저 데이터를 쓴 다음 새로운 데이터를 쓴다.
            if (!TextUtils.isEmpty(preData))
                fos.write(preData.getBytes());

            msgBuilder.append(getTimeNow(new Date())).append(" : ").append(message).append("\n\n");
            fos.write(msgBuilder.toString().getBytes());
            DustLog.i(TAG, "writeLogToFile(), write that successfully.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getTimeNow(Date now) {
        DustLog.i(TAG, "getTimeNow()");
        if (now == null)
            now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return formatter.format(now);
    }
}