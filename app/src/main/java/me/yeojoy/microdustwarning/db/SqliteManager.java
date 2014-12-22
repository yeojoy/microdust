package me.yeojoy.microdustwarning.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import me.yeojoy.microdustwarning.util.DustLog;

public class SqliteManager implements DustInfoDBConstants {
    
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
        DustLog.i(TAG, "init()");
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
        DustLog.i(TAG, "isDoneInit()");
        return mContext != null && mDBHelper != null;
    }
    
    public void saveData(String time, String rawData) {
        DustLog.i(TAG, "saveData()");
        ArrayList<String> data = new ArrayList<String>();
        data.add(time);
        data.addAll(Arrays.asList(rawData.split(" ")));
        insertData(data);
    }
    
    
    
    public void insertData(List<String> data) {
        DustLog.i(TAG, "insertData()");
        
        if (data == null || data.size() != 11)
            return;
        
        StringBuilder sb = new StringBuilder();
        for (String s : data) {
            sb.append(s).append(" ");
        }
        DustLog.d(TAG, sb.toString().trim());
        
        SQLiteDatabase db = null;
        try {
            // 1. get reference to writable DB
            db = mDBHelper.getWritableDatabase();
            
            // 2. create ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put(SAVE_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
            values.put(MEASURE_TIME, data.get(0));
            values.put(MEASURE_LOCATION, data.get(1));
            values.put(MICRO_DUST, data.get(2));
            values.put(NANO_DUST, data.get(3));
            values.put(OZON, data.get(4));
            values.put(NO2, data.get(5));
            values.put(CO, data.get(6));
            values.put(SO2, data.get(7));
            values.put(GRADE, data.get(8));
            values.put(DEGREE, data.get(9));
            values.put(MATTER, data.get(10));
            
            // 3. insert
            db.insert(TABLE_NAME, // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values
        } catch (SQLiteException e) {
            DustLog.e(TAG, e.getMessage());
        } finally {
            // 4. close
            if (db != null) db.close();
        }
    }
    
    public Cursor getDBData() {
        Cursor cursor = null;
        
        // TODO select last data
        SQLiteDatabase db = null;
        try {
            db = mDBHelper.getReadableDatabase();

//            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            String selectQuery = "SELECT * FROM " + TABLE_NAME;

            db.rawQuery(selectQuery, null);
        } catch (SQLiteException e) {
            DustLog.e(TAG, e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return cursor;
    }
    
}
