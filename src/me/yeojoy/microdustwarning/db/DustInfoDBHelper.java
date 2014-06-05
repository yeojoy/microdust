package me.yeojoy.microdustwarning.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DustInfoDBHelper extends SQLiteOpenHelper implements DustInfoDBConstants {

    private static final String TAG = DustInfoDBHelper.class.getSimpleName();
    
    public DustInfoDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    
    public DustInfoDBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate()");
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
        sb.append(MATTER).append(" TEXT").append(" );"); // 등급결정물질
        
        Log.e(TAG, "SQL CREATE -> " + sb.toString());
        db.execSQL(sb.toString());
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade()");
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }
    
}
