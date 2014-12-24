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
import android.os.AsyncTask;
import android.view.ViewOutlineProvider;
import android.widget.Toast;

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
 
    private static  void init(Context context) {
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
    
    public void saveData(List<DustInfoDto> data) {
        DustLog.i(TAG, "saveData()");
        DBInsertAsyncTask task = new DBInsertAsyncTask();
        task.execute(data);
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
                values.put(MEASURE_TIME, dto.getDate());
                values.put(MEASURE_LOCALITY, dto.getLocality());
                values.put(MICRO_DUST, dto.getPm10());
                values.put(MICRO_DUST_INDEX, dto.getPm10Index());
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
    
    private class DBInsertAsyncTask extends AsyncTask<List<DustInfoDto>, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(List<DustInfoDto>... params) {
            insertData(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(mContext, "DB Insert Finished.", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
