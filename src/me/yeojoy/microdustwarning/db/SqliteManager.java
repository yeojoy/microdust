package me.yeojoy.microdustwarning.db;

import java.util.ArrayList;
import java.util.Arrays;

import my.lib.MyLog;
import android.content.Context;

public class SqliteManager {
    
    private static final String TAG = SqliteManager.class.getSimpleName();
    
    private static SqliteManager mManager;
    
    private Context mContext;
    
    private DustInfoDBHelper mDBHelper;
    
    public static SqliteManager getInstance() {
        if (mManager == null)
            mManager = new SqliteManager();
        return mManager;
    }
 
    public void init(Context context) {
        MyLog.i(TAG, "init()");
        mContext = context;
        
        mDBHelper = new DustInfoDBHelper(mContext);
    }
    
    /**
     * context를 갖고 있고 mDBHelper obj가 있는지 확인
     * @return
     *  true : NOT NULL
     *  false : NULL
     */
    public boolean isDoneInit() {
        MyLog.i(TAG, "isDoneInit()");
        return mContext != null && mDBHelper != null;
    }
    
    public void saveData(String time, String rawData) {
        MyLog.i(TAG, "saveData()");
        ArrayList<String> data = new ArrayList<String>();
        data.add(time);
        data.addAll(Arrays.asList(rawData.split(" ")));
        mDBHelper.insertData(data);
    }
    
}
