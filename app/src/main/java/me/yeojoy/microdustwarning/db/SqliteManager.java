package me.yeojoy.microdustwarning.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import me.yeojoy.microdustwarning.BuildConfig;
import me.yeojoy.microdustwarning.entity.AllStateDustInfoDto;
import me.yeojoy.microdustwarning.entity.DtoList;
import me.yeojoy.microdustwarning.entity.DustInfoDto;
import me.yeojoy.microdustwarning.util.DustLog;

public class SqliteManager implements DustInfoDBConstants {
    
    private static final String TAG = SqliteManager.class.getSimpleName();
    
    private static SqliteManager mManager;
    
    private static Context mContext;
    
    private static DustInfoDBHelper mDBHelper;
    
    public static SqliteManager getInstance(Context context) {
        if (mManager == null)
            mManager = new SqliteManager();
        
        init(context); 
        return mManager;
    }
 
    private static void init(Context context) {
        DustLog.i(TAG, "init()");
        mContext = context;
        mDBHelper = new DustInfoDBHelper(mContext);
    }

    public synchronized void saveData(DtoList data) {
        DustLog.i(TAG, "saveData()");
        if (data != null && data.getList() != null && data.getList().size() > 0) {
            DBInsertAsyncTask task = new DBInsertAsyncTask();
            task.execute(data);
        }
    }

    private void insertData(DtoList data) {
        DustLog.i(TAG, "insertData()");
        
        if (data == null || data.getList() == null)
            return;

        if (isAleadySaved(data.getList().get(0))) {
            DustLog.d(TAG, "Data is already saved.");
            return;
        }

        SQLiteDatabase db = null;
        try {
            // 1. get reference to writable DB
            db = mDBHelper.getWritableDatabase();
            db.beginTransaction();
            DustLog.i(TAG, "insertData(), beginTransaction");
            ContentValues values = null;
            // 2. create ContentValues to add key "column"/value
            for (AllStateDustInfoDto dto : data.getList()) {
                values = new ContentValues();
                values.put(SAVE_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
                values.put(CO_GRADE, dto.getCoGrade());
                values.put(NO2_GRADE, dto.getNo2Grade());
                values.put(O3_GRADE, dto.getO3Grade());
                values.put(PM10_GRADE, dto.getPm10Grade());
                values.put(SO2_GRADE, dto.getSo2Grade());
                values.put(KHAI_GRADE, dto.getKhaiGrade());
                values.put(CO_VALUE, dto.getCoValue());
                values.put(NO2_VALUE, dto.getNo2Value());
                values.put(O3_VALUE, dto.getO3Value());
                values.put(PM10_VALUE, dto.getPm10Value());
                values.put(SO2_VALUE, dto.getSo2Value());
                values.put(KHAI_VALUE, dto.getKhaiValue());
                values.put(NUM_OF_ROWS, dto.getNumOfRows());
                values.put(ROW_NUM, dto.getRnum());
                values.put(RESULT_CODE, dto.getResultCode());
                values.put(RESULT_MESSAGE, dto.getResultMsg());
                values.put(SERVICE_KEY, dto.getServiceKey());
                values.put(PAGE_NO, dto.getPageNo());
                values.put(DATA_TERM, dto.getDataTerm());
                values.put(DATA_TIME, dto.getDataTime());
                values.put(SIDO_NAME, dto.getSidoName());
                values.put(STATION_CODE, dto.getStationCode());
                values.put(STATION_NAME, dto.getStationName());
                values.put(TOTAL_COUNT, dto.getTotalCount());

                // 3. insert
                db.insert(TABLE_NAME, // table
                        null, //nullColumnHack
                        values); // key/value -> keys = column names/ values = column values
                DustLog.i(TAG, "insertData(), insert Data");
            }
            
            db.setTransactionSuccessful();
            db.endTransaction();
            DustLog.i(TAG, "insertData(), endTransaction");
        } catch (SQLiteException e) {
            DustLog.e(TAG, e.getMessage());
        } finally {
            // 4. close
            if (db != null) db.close();
        }
    }

    /**
     * 동일한 시간대에 데이터가 저장 됐는지 확인한다.
     * @param AllStateDustInfoDto
     * @return 이미 같은 시간이 저장 됐으면 true
     *         없으면 false
     */
    private boolean isAleadySaved(AllStateDustInfoDto dto) {
        DustLog.i(TAG, "isAleadySaved()");

        if (dto == null) return false;

        Cursor cursor = null;

        // TODO select last data
        SQLiteDatabase db = null;
        try {
            db = mDBHelper.getReadableDatabase();

            String[] selectionArgs = { dto.getDataTime() };
            cursor = db.query(TABLE_NAME, null, DATA_TIME + "= ?", selectionArgs,
                    null, null, null);
            if (cursor != null && cursor.getCount() > 0) return true;
        } catch (SQLiteException e) {
            DustLog.e(TAG, e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        
        return false;
    }

    /**
     * AsyncTask를 사용해서 DB에 데이터를 입력한다.
     */
    private class DBInsertAsyncTask extends AsyncTask<DtoList, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(DtoList... params) {
            DustLog.i(TAG, "DBInsertAsyncTask, doInBackground()");
            insertData(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (BuildConfig.DEBUG)
                Toast.makeText(mContext, "DB Insert Finished.", 
                        Toast.LENGTH_SHORT).show();
        }
    }
}
