package me.yeojoy.microdustwarning.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.yeojoy.microdustwarning.util.DustLog;

public class DustInfoDBHelper extends SQLiteOpenHelper implements DustInfoDBConstants {

    private static final String TAG = DustInfoDBHelper.class.getSimpleName();

    public DustInfoDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DustLog.i(TAG, "onCreate()");
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ").append(TABLE_NAME).append(" ( ");
        sb.append(ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(SAVE_TIME).append(" TEXT, ");             // 저장시각
        sb.append(SIDO_NAME).append(" TEXT, ");             // 시도 이름
        sb.append(CO_GRADE).append(" TEXT, ");              // 일산화탄소 등급
        sb.append(NO2_GRADE).append(" TEXT, ");             // 이산화질소 등급
        sb.append(O3_GRADE).append(" TEXT, ");              // 오존 등급
        sb.append(PM10_GRADE).append(" TEXT, ");            // 미세먼지 등급
        sb.append(SO2_GRADE).append(" TEXT, ");             // 이산화황 등급
        sb.append(KHAI_GRADE).append(" TEXT, ");            // 통합 지수 등급
        sb.append(CO_VALUE).append(" TEXT, ");              // 일산화탄소 수치
        sb.append(NO2_VALUE).append(" TEXT, ");             // 이산화질소 수치
        sb.append(O3_VALUE).append(" TEXT, ");              // 오존 수치
        sb.append(PM10_VALUE).append(" TEXT, ");            // 미세먼지 수치
        sb.append(SO2_VALUE).append(" TEXT, ");             // 이산화황 수치
        sb.append(KHAI_VALUE).append(" TEXT, ");            // 통합 지수 수치
        sb.append(NUM_OF_ROWS).append(" TEXT, ");           // row count
        sb.append(ROW_NUM).append(" TEXT, ");               // Row number
        sb.append(RESULT_CODE).append(" TEXT, ");           // Result code
        sb.append(RESULT_MESSAGE).append(" TEXT, ");        // Result message
        sb.append(SERVICE_KEY).append(" TEXT, ");           // Service Key
        sb.append(PAGE_NO).append(" TEXT, ");               // Page Number
        sb.append(DATA_TERM).append(" TEXT, ");             // Data term
        sb.append(DATA_TIME).append(" TEXT, ");             // 측정 시각
        sb.append(STATION_CODE).append(" TEXT, ");          //
        sb.append(STATION_NAME).append(" TEXT, ");          // 상세 지명
        sb.append(TOTAL_COUNT).append(" TEXT").append(" );");

        DustLog.e(TAG, "SQL CREATE -> " + sb.toString());
        db.execSQL(sb.toString());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DustLog.i(TAG, "onUpgrade()");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
