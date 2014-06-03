package me.yeojoy.microdustwarning.db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import my.lib.MyLog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DustInfoDBHelper extends SQLiteOpenHelper {

    private static final String TAG = DustInfoDBHelper.class.getSimpleName();
    
    public static final String DB_NAME = "dust_info.db";
    public static final int DB_VERSION = 7;

    private static final String TABLE_NAME = "dust_info";
    
    private static final String SAVE_TIME = "save_time";
    private static final String MEASURE_TIME = "measure_time";
    private static final String MEASURE_LOCATION = "location";
    private static final String MICRO_DUST = "micro_dust";
    private static final String NANO_DUST = "nano_dust";
    private static final String OZON = "ozon";
    private static final String NO2 = "no2";
    private static final String CO = "co";
    private static final String SO2 = "so2";
    private static final String GRADE = "grade";
    private static final String DEGREE = "degree";
    private static final String MATTER = "matter";
    
    
    public DustInfoDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    
    public DustInfoDBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        MyLog.i(TAG, "onCreate()");
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
        sb.append(BaseColumns._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(SAVE_TIME).append(" TEXT, ");         // 저장시각
        sb.append(MEASURE_TIME).append(" TEXT, ");      // 측정시각
        sb.append(MEASURE_LOCATION).append(" TEXT, ");  // 측정장소 (~~구)
        sb.append(MICRO_DUST).append(" TEXT, ");        // 미세먼지
        sb.append(NANO_DUST).append(" TEXT, ");         // 초미세먼지
        sb.append(OZON).append(" TEXT, ");              // 오존
        sb.append(NO2).append(" TEXT, ");               // 이산화질소
        sb.append(CO).append(" TEXT, ");                // 일산화탄소
        sb.append(SO2).append(" TEXT, ");               // 아황산가스
        sb.append(GRADE).append(" TEXT, ");             // 등급
        sb.append(DEGREE).append(" TEXT, ");            // 지수
        sb.append(MATTER).append(" TEXT").append(" )"); // 등급결정물질
        
        db.beginTransaction();
        db.execSQL(sb.toString());
        db.endTransaction();
        
        String insert = "INSERT INTO " + TABLE_NAME + " (" + SAVE_TIME + ", " + MEASURE_TIME + ", "
                + MEASURE_LOCATION + ", " + MICRO_DUST + ", " + NANO_DUST + ", " + OZON + ", "
                + NO2 + ", " + CO + ", " + SO2 + ", " + GRADE + ", " + DEGREE + ", " + MATTER + ") "
                + "VALUES (\'a\', \'b\', \'c\', \'d\', \'e\', \'f\', \'g\', \'h\', \'i\', \'j\', \'k\', \'l\')";
        db.beginTransaction();
        db.execSQL(insert);
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MyLog.i(TAG, "onUpgrade()");
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }

    public void insertData(List<String> data) {
        MyLog.i(TAG, "insertData()");
        
        if (data == null || data.size() != 11)
            return;
        
        StringBuilder sb = new StringBuilder();
        for (String s : data) {
            sb.append(s).append(" ");
        }
        MyLog.d(TAG, sb.toString().trim());
        
        SQLiteDatabase db = null;
        try {
            // 1. get reference to writable DB
            db = this.getWritableDatabase();
            
            // 2. create ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put(SAVE_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
            values.put(MEASURE_TIME, data.get(0));
//            values.put(MEASURE_TIME, "2014-06-03 16 PM");
//            values.put(MEASURE_LOCATION, "DONJACK");
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
            
            db.beginTransaction();
            // 3. insert
            db.insert(TABLE_NAME, // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values
            db.endTransaction();
        } catch (SQLiteException e) {
            MyLog.e(TAG, e.getMessage());
        } finally {
            // 4. close
            db.close();
        }
    }
    
    
}
