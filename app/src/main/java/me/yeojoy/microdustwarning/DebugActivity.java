package me.yeojoy.microdustwarning;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import me.yeojoy.microdustwarning.db.DustInfoDBConstants;
import me.yeojoy.microdustwarning.entity.DustInfoDto;
import me.yeojoy.microdustwarning.fragment.DustFragment;
import me.yeojoy.microdustwarning.fragment.StartFragment;
import me.yeojoy.microdustwarning.util.DustLog;
import me.yeojoy.microdustwarning.util.DustSharedPreferences;

public class DebugActivity extends Activity implements DustConstants, DustInfoDBConstants,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = DebugActivity.class.getSimpleName();

    private Context mContext;

//    private ListView mLvList;

    private TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        // SharedPreferences init
        DustSharedPreferences.getInstance().init(this);

        mContext = this;

        mTvResult = (TextView) findViewById(R.id.tv_result);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().initLoader(AIR_QUALITY_INDEX_CURSOR_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        DustLog.d(TAG, "onCreateLoader()");

        if (id == AIR_QUALITY_INDEX_CURSOR_LOADER) {
            return new CursorLoader(
                    mContext,                           // Context
                    AIR_QUALITY_SELECT_ALL_QUERY_URI,   // Table to query
                    PROJECTION,                         // Projection to return
                    null,                               // No selection clause
                    null,                               // No selection arguments
                    "measure_time DESC"                 // Default sort order

            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        DustLog.d(TAG, "onLoadFinished()");

        if (cursor != null && cursor.getCount() > 0) {
            DustLog.d(TAG, "onLoadFinished(), cursor is not null");
            cursor.moveToFirst();
            mTvResult.setText("");
            while (cursor.moveToNext()) {
                DustInfoDto dto = new DustInfoDto();

                dto.setMesuredDate(cursor.getString(INDEX_MEASURE_TIME));
                dto.setSavedDate(cursor.getString(INDEX_SAVE_TIME));
                dto.setLocality(cursor.getString(INDEX_MEASURE_LOCALITY));
                dto.setPm10(cursor.getString(INDEX_MICRO_DUST));
                dto.setPm10Index(cursor.getString(INDEX_MICRO_DUST_INDEX));
                dto.setPm24(cursor.getString(INDEX_MICRO_DUST_PM24));
                dto.setPm24Index(cursor.getString(INDEX_MICRO_DUST_PM24_INDEX));
                dto.setPm25(cursor.getString(INDEX_NANO_DUST));
                dto.setOzone(cursor.getString(INDEX_OZON));
                dto.setOzoneIndex(cursor.getString(INDEX_OZON_INDEX));
                dto.setNitrogen(cursor.getString(INDEX_NO2));
                dto.setNitrogenIndex(cursor.getString(INDEX_NO2_INDEX));
                dto.setCarbon(cursor.getString(INDEX_CO));
                dto.setCarbonIndex(cursor.getString(INDEX_CO_INDEX));
                dto.setSulfurous(cursor.getString(INDEX_SO2));
                dto.setSulfurousIndex(cursor.getString(INDEX_SO2_INDEX));
                dto.setDegree(cursor.getString(INDEX_DEGREE));
                dto.setMaxIndex(cursor.getString(INDEX_AIR_QUAL_INDEX));
                dto.setMaterial(cursor.getString(INDEX_MATERIAL));
                mTvResult.append(dto.toString());
                mTvResult.append("\n\n");
            }
        } else {
            Toast.makeText(mContext, "Cursor is null.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
