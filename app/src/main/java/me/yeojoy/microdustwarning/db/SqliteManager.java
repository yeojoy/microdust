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

    public synchronized void saveData(List<DustInfoDto> data) {
        DustLog.i(TAG, "saveData()");
        if (!isAleadySaved(data.get(0))) {
            DBInsertAsyncTask task = new DBInsertAsyncTask();
            task.execute(data);
        }
    }

    // TODO Database에 data를 넣어야 함.
    public synchronized void saveData(DtoList data) {
        DustLog.i(TAG, "saveData()");

    }

    private void insertData(List<DustInfoDto> data) {
        DustLog.i(TAG, "insertData()");
        
        if (data == null)
            return;
        
        SQLiteDatabase db = null;
        try {
            // 1. get reference to writable DB
            db = mDBHelper.getWritableDatabase();
            db.beginTransaction();
            DustLog.i(TAG, "insertData(), beginTransaction");
            ContentValues values = null;
            // 2. create ContentValues to add key "column"/value
            for (DustInfoDto dto : data) {
                values = new ContentValues();
                values.put(SAVE_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
                values.put(MEASURE_TIME, dto.getMesuredDate());
                values.put(MEASURE_LOCALITY, dto.getLocality());
                values.put(MICRO_DUST, dto.getPm10());
                values.put(MICRO_DUST_INDEX, dto.getPm10Index());
                values.put(MICRO_DUST_PM24, dto.getPm24());
                values.put(MICRO_DUST_PM24_INDEX, dto.getPm24Index());
                values.put(NANO_DUST, dto.getPm25());
                values.put(OZON, dto.getOzone());
                values.put(OZON_INDEX, dto.getOzoneIndex());
                values.put(NO2, dto.getNitrogen());
                values.put(NO2_INDEX, dto.getNitrogenIndex());
                values.put(CO, dto.getCarbon());
                values.put(CO_INDEX, dto.getCarbonIndex());
                values.put(SO2, dto.getSulfurous());
                values.put(SO2_INDEX, dto.getSulfurousIndex());
                values.put(DEGREE, dto.getDegree());
                values.put(AIR_QUAL_INDEX, dto.getMaxIndex());
                values.put(MATERIAL, dto.getMaterial());
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
     * @param dustInfoDto
     * @return 이미 같은 시간이 저장 됐으면 true
     *         없으면 false
     */
    private boolean isAleadySaved(DustInfoDto dustInfoDto) {
        DustLog.i(TAG, "isAleadySaved()");

        if (dustInfoDto == null) return false;
        Cursor cursor = null;

        // TODO select last data
        SQLiteDatabase db = null;
        try {
            db = mDBHelper.getReadableDatabase();

            String selection = MEASURE_TIME + " = ?";
            String[] selectionArgs = { dustInfoDto.getMesuredDate() };
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs,
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
    private class DBInsertAsyncTask extends AsyncTask<List<DustInfoDto>, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(List<DustInfoDto>... params) {
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
