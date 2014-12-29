
package me.yeojoy.microdustwarning.fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import me.yeojoy.microdustwarning.AboutActivity;
import me.yeojoy.microdustwarning.BuildConfig;
import me.yeojoy.microdustwarning.DustApplication;
import me.yeojoy.microdustwarning.DustConstants;
import me.yeojoy.microdustwarning.R;
import me.yeojoy.microdustwarning.adapter.ImageAdapter;
import me.yeojoy.microdustwarning.db.DustInfoDBConstants;
import me.yeojoy.microdustwarning.db.SqliteManager;
import me.yeojoy.microdustwarning.entity.DustInfoDto;
import me.yeojoy.microdustwarning.entity.OttoEventEntity;
import me.yeojoy.microdustwarning.entity.STATUS;
import me.yeojoy.microdustwarning.network.DustNetworkManager;
import me.yeojoy.microdustwarning.service.WebParserService;
import me.yeojoy.microdustwarning.util.DustDialogManager;
import me.yeojoy.microdustwarning.util.DustLog;
import me.yeojoy.microdustwarning.util.DustSharedPreferences;
import me.yeojoy.microdustwarning.util.DustUtils;

public class DustFragment extends Fragment implements DustConstants, 
        View.OnClickListener, DustNetworkManager.OnReceiveDataListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        DustInfoDBConstants {

    private static final String TAG = DustFragment.class.getSimpleName();
    private static final String VIEW_NAME = "main dust fragment";
    
    private TextView mTvResult;

    private AlarmManager alarmManager;

    private GridView mGvImages;
    private ImageAdapter mAdapter;
    private ArrayList<String> mUrlList;

    private PendingIntent pending;

    private LinearLayout mLlIndicator;

    private DustNetworkManager mManager;
    private Context mContext;

    private Bundle mReceivedArguments;
    
    private Tracker mTracker;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;

        if (mTracker == null) {
            mTracker = ((DustApplication) getActivity().getApplication())
                    .getTracker(DustApplication.TrackerName.APP_TRACKER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DustLog.i(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_dust, container, false);
        mTvResult = (TextView) view.findViewById(R.id.tv_result);

        mGvImages = (GridView) view.findViewById(R.id.gv_map);
        mUrlList = new ArrayList<String>();
        mAdapter = new ImageAdapter(getActivity(), mUrlList);
        mGvImages.setAdapter(mAdapter);

        mLlIndicator = (LinearLayout) view.findViewById(R.id.ll_indicator);

        if (!DustSharedPreferences.getInstance().getBoolean(KEY_PREFS_SWITCH_OFF)) {
            mTvResult.setText(R.string.service_is_on_wait_a_minute);
        }

        view.findViewById(R.id.ib_blue).setOnClickListener(this);
        view.findViewById(R.id.ib_light_green).setOnClickListener(this);
        view.findViewById(R.id.ib_yellow).setOnClickListener(this);
        view.findViewById(R.id.ib_orange).setOnClickListener(this);
        view.findViewById(R.id.ib_red).setOnClickListener(this);
         
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        alarmManager = (AlarmManager) 
                getActivity().getSystemService(Context.ALARM_SERVICE);
        
        Intent intent = new Intent(getActivity(), WebParserService.class);
        pending = PendingIntent.getService(getActivity(), 10002, intent, 0);

        DustSharedPreferences.getInstance().init(mContext);
        mReceivedArguments = getArguments();

        DustApplication.locality
                = DustSharedPreferences.getInstance()
                        .getString(KEY_PREFS_LOCALITY);

        if (TextUtils.isEmpty(DustApplication.locality)) {
            DustDialogManager.chooseUserLocalityDialog(mContext,
                    mDialogSelectListener);
        } else {
            // Start Activity에서 checkbox 상태를 보고 시작시킨다.
            if (mReceivedArguments != null) {
                if (mReceivedArguments.getBoolean(KEY_CHECKBOX_AUTO_START, true)) {
                    DustLog.i(TAG, ">>>>>> start Service. <<<<<<");
                    launchAlarmManager();
                    DustSharedPreferences.getInstance().putBoolean(KEY_PREFS_SWITCH_OFF, false);
                } else {
                    DustLog.i(TAG, ">>>>>> nothing happend. <<<<<<");
                    DustSharedPreferences.getInstance().putBoolean(KEY_PREFS_SWITCH_OFF, true);
                }
            }
            /*
             * Initializes the CursorLoader. The URL_LOADER value is eventually passed
             * to onCreateLoader().
             */
            getLoaderManager().initLoader(AIR_QUALITY_INDEX_CURSOR_LOADER,
                    null, DustFragment.this);
        }

        
        mManager = DustNetworkManager.getInstance(mContext);
        mManager.setOnReceiveDataListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DustLog.i(TAG, "onOptionsItemSelected()");
        switch (item.getItemId()) {
            case R.id.action_on_off:
                // LESSON AND LEARN
                // checked true를 ON상태로 나타내 주기 위해선 isChecked()로 해줘야 한다.
                boolean isChecked = !item.isChecked();
                if (isChecked)
                    launchAlarmManager();
                else
                    cancelAlarmManager();

                DustSharedPreferences.getInstance().putBoolean(KEY_PREFS_SWITCH_OFF, !isChecked);

                return true;

            case R.id.action_refresh:
                setRefreshActionButtonState(true);
                refreshData();
                return true;

            case R.id.action_settings:
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.container, new SettingFragment())
                        .addToBackStack(null).commit();
                return true;

            case R.id.action_about:

                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;
            
            case R.id.action_change_locality:
                DustDialogManager.chooseUserLocalityDialog(mContext,
                        mDialogSelectListener);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // onPrepareOptionsMenu는 "..."을 누를 때 불려진다. 밖으로 나와있는 건 안 불림
        super.onPrepareOptionsMenu(menu);
        DustLog.i(TAG, "onPrepareOptionsMenu()");
        MenuItem item = menu.findItem(R.id.action_on_off);
        // LESSON AND LEARN
        // checked true를 ON상태로 나타내 주기 위해선 isChecked()로 해줘야 한다.
        boolean serviceSwitchStatus
                = !DustSharedPreferences.getInstance().getBoolean(KEY_PREFS_SWITCH_OFF, true);
        DustLog.i(TAG, "onPrepareOptionsMenu(), Service Switch status : " + serviceSwitchStatus);
        item.setChecked(serviceSwitchStatus);

        if (serviceSwitchStatus) {
            item.setTitle(R.string.action_swtich_on);
        } else {
            item.setTitle(R.string.action_swtich_off);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private Menu mOptionMenu;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        DustLog.i(TAG, "onCreateOptionsMenu()");
        mOptionMenu = menu;
        mOptionMenu.clear();
        inflater.inflate(R.menu.dust, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        DustApplication.bus.register(this);
        getActivity().getActionBar().setTitle(R.string.app_name);
        
        // Set screen name.
        mTracker.setScreenName(VIEW_NAME);

        // Send a screen view.
        mTracker.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    public void onPause() {
        super.onPause();
        DustApplication.bus.unregister(this);
    }

    /**
     * Actionbar에 Refresh를 눌렀을 때 Circular Progressbar를 보여줌
     * @param refreshing
     */
    public void setRefreshActionButtonState(final boolean refreshing) {
        if (mOptionMenu != null) {
            final MenuItem refreshItem = mOptionMenu
                    .findItem(R.id.action_refresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.circular_progress);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

    @Subscribe
    public void receiveOttoEventEntity(OttoEventEntity entity) {
        DustLog.i(TAG, "receiveOttoEventEntity()");
        switch (entity.command) {

            case GET_DATA_WITH_DTO:
                setDataToView(entity.dtoList);
                
                setRefreshActionButtonState(false);
                break;

            case REFRESH:
                refreshData();
                break;

            case ON_OFF:
                if (entity.on_off) {
                    launchAlarmManager();
                } else {
                    cancelAlarmManager();
                }

                break;
        }
    }

    /**
     * 알람 매니저 종료
     */
    private void cancelAlarmManager() {
        DustLog.i(TAG, "cancelAlarmManager()");
        alarmManager.cancel(pending);
        DustSharedPreferences.getInstance().clear();
    }

    /**
     * 알람 매니저 실행
     */
    private void launchAlarmManager() {
        DustLog.i(TAG, "launchAlarmManager()");
        mTvResult.setText(R.string.service_is_on_wait_a_minute);

        long notiTime = NOTI_TIME_REAL;
        
        if (BuildConfig.DEBUG) {
            notiTime = NOTI_TIME_TEST;
        }

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 1000l, notiTime, pending);
    }

    private void refreshData() { mManager.getMicrodustInfo(); }

    /**
     * 기상지도이미지를 가져와 보여준다.
     */
    private void setImage() {
        DustLog.i(TAG, "setImage()");
        mLlIndicator.setVisibility(View.VISIBLE);
        if (mUrlList == null) {
            mUrlList = new ArrayList<String>();
        }
        mUrlList.clear();
        
        mUrlList.add("http://www.webairwatch.com/kaq/modelimg/PM10_24H_AVG.09KM.Day1.gif");
        mUrlList.add("http://www.webairwatch.com/kaq/modelimg/PM10_24H_AVG.09KM.Day2.gif");
        mUrlList.add("http://www.webairwatch.com/kaq/modelimg/PM2_5_24H_AVG.09KM.Day1.gif");
        mUrlList.add("http://www.webairwatch.com/kaq/modelimg/PM2_5_24H_AVG.09KM.Day2.gif");

        mAdapter.notifyDataSetChanged();
    }

    /**
     * Data를 추가한다.
     * 추가한 후 setImage()를 호출한다.
     * @param dto
     */
    private void setText(final DustInfoDto dto) {
        DustLog.i(TAG, "setText()");
        // UI Thread인지 확인
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            DustLog.i(TAG, "setText(), running on UI Thread.");
            
            STATUS[] statuses = DustUtils.analyzeMicroDust(dto);

            SpannableStringBuilder ssb = new SpannableStringBuilder();

            Resources res = getResources();

            if (BuildConfig.DEBUG)
                ssb.append(DustUtils.convertString(res, "측정시각 : " + dto.getDate() + "\n", null));
            else
                ssb.append(DustUtils.convertString(res, "측정시각 : " + dto.getDate() + "\n", STATUS.NONE));

            ssb.append(DustUtils.convertString(res, "지역 : " + dto.getLocality() + "\n", STATUS.NONE));

            ssb.append(DustUtils.convertString(res, "미세먼지 : " + dto.getPm10() + "\n", statuses[0]));
            ssb.append(DustUtils.convertString(res, "초미세먼지 : " + dto.getPm25() + "\n", statuses[1]));
            ssb.append(DustUtils.convertString(res, "오존 : " + dto.getOzone() + "\n", statuses[2]));
            ssb.append(DustUtils.convertString(res, "이산화질소 : " + dto.getNitrogen() + "\n", statuses[3]));
            ssb.append(DustUtils.convertString(res, "일산화탄소 : " + dto.getCarbon() + "\n", statuses[4]));
            ssb.append(DustUtils.convertString(res, "아황산가스 : " + dto.getSulfurous() + "\n", statuses[5]));
            ssb.append(DustUtils.convertString(res, "등급 : " + dto.getDegree() + "\n", STATUS.NONE));
            ssb.append(DustUtils.convertString(res, "통합지수 : " + dto.getMaxIndex() + "\n", statuses[6]));
            ssb.append(DustUtils.convertString(res, "지수결정물질 : " + dto.getMaterial(), STATUS.NONE));
            mTvResult.setText(ssb.toString());
            setImage();
        } else {
            // UI Thread가 아님
            DustLog.i(TAG, "setText(), running on worker Thread.");
            // ERROR 발생으로 호출해 줘야함.
            // java.lang.RuntimeException: Can'mTracker create handler inside thread that has not called Looper.prepare()
            Looper.prepare();
            
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    STATUS[] statuses = DustUtils.analyzeMicroDust(dto);

                    SpannableStringBuilder ssb = new SpannableStringBuilder();

                    Resources res = getResources();

                    if (BuildConfig.DEBUG)
                        ssb.append(DustUtils.convertString(res, "측정시각 : " + dto.getDate() + "\n", null));
                    else
                        ssb.append(DustUtils.convertString(res, "측정시각 : " + dto.getDate() + "\n", STATUS.NONE));

                    ssb.append(DustUtils.convertString(res, "지역 : " + dto.getLocality() + "\n", STATUS.NONE));

                    ssb.append(DustUtils.convertString(res, "미세먼지 : " + dto.getPm10() + "\n", statuses[0]));
                    ssb.append(DustUtils.convertString(res, "초미세먼지 : " + dto.getPm25() + "\n", statuses[1]));
                    ssb.append(DustUtils.convertString(res, "오존 : " + dto.getOzone() + "\n", statuses[2]));
                    ssb.append(DustUtils.convertString(res, "이산화질소 : " + dto.getNitrogen() + "\n", statuses[3]));
                    ssb.append(DustUtils.convertString(res, "일산화탄소 : " + dto.getCarbon() + "\n", statuses[4]));
                    ssb.append(DustUtils.convertString(res, "아황산가스 : " + dto.getSulfurous() + "\n", statuses[5]));
                    ssb.append(DustUtils.convertString(res, "등급 : " + dto.getDegree() + "\n", STATUS.NONE));
                    ssb.append(DustUtils.convertString(res, "통합지수 : " + dto.getMaxIndex() + "\n", statuses[6]));
                    ssb.append(DustUtils.convertString(res, "지수결정물질 : " + dto.getMaterial(), STATUS.NONE));
                    mTvResult.setText(ssb.toString());
                    setImage();
                }
            });
        }
        
    }

    private void setDataToView(List<DustInfoDto> dtoList) {
        DustLog.i(TAG, "setDataToView()");
        if (dtoList == null) {
            DustLog.i(TAG, "setDataToView(), dtoList is null");
            return;
        }
        
        if (TextUtils.isEmpty(DustApplication.locality)) {
            Toast.makeText(mContext, R.string.toast_warning_not_found_locality,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        DustInfoDto dto = null;
        for (DustInfoDto d : dtoList) {
            if (DustApplication.locality.equals(d.getLocality())) {
                dto = d;
                break;
            }
        }

        if (dto != null) DustLog.i(TAG, dto.toString());

        setText(dto);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_blue:
                DustDialogManager.showDialogWarningMessage(getActivity(), STATUS.GOOD);
                break;
            case R.id.ib_light_green:
                DustDialogManager.showDialogWarningMessage(getActivity(), STATUS.NORMAL);
                break;
            case R.id.ib_yellow:
                DustDialogManager.showDialogWarningMessage(getActivity(), STATUS.BAD);
                break;
            case R.id.ib_orange:
                DustDialogManager.showDialogWarningMessage(getActivity(), STATUS.WORSE);
                break;
            case R.id.ib_red:
                DustDialogManager.showDialogWarningMessage(getActivity(), STATUS.WORST);
                break;
        }
    }

    @Override
    public void onReceiveData(List<DustInfoDto> data) {
        DustLog.i(TAG, "onReceiveData()");
        setDataToView(data);
        
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setRefreshActionButtonState(false);
            }
        });
    }
    
    private DialogInterface.OnClickListener mDialogSelectListener = 
            new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            DustLog.d(TAG, "onClick()");
            String[] localityArray = 
                    getResources().getStringArray(R.array.all_localities_array);
            DustApplication.locality = localityArray[which];

            // Start Activity에서 checkbox 상태를 보고 시작시킨다.
            if (mReceivedArguments != null) {
                if (mReceivedArguments.getBoolean(KEY_CHECKBOX_AUTO_START, true)) {
                    DustLog.i(TAG, ">>>>>> start Service. <<<<<<");
                    launchAlarmManager();
                    DustSharedPreferences.getInstance().putBoolean(KEY_PREFS_SWITCH_OFF, false);
                } else {
                    DustLog.i(TAG, ">>>>>> nothing happend. <<<<<<");
                    DustSharedPreferences.getInstance().putBoolean(KEY_PREFS_SWITCH_OFF, true);
                }
            }
            /*
             * Initializes the CursorLoader. The URL_LOADER value is eventually passed
             * to onCreateLoader().
             */
            getLoaderManager().initLoader(AIR_QUALITY_INDEX_CURSOR_LOADER,
                    null, DustFragment.this);
            
            DustLog.d(TAG, "onClick(), selected Locality : " 
                    + DustApplication.locality);

            DustSharedPreferences.getInstance().putString(KEY_PREFS_LOCALITY, 
                    DustApplication.locality);
            
            refreshData();
            
        }
    };

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
                    "measure_time DESC"                  // Default sort order

            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        DustLog.d(TAG, "onLoadFinished()");
        DustInfoDto dto = null;
        
        
        if (cursor != null && cursor.getCount() > 0) {
            DustLog.d(TAG, "onLoadFinished(), cursor is not null");    
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                if (DustApplication.locality.equals(cursor.getString(
                        cursor.getColumnIndex(MEASURE_LOCALITY)))) {
                    dto = new DustInfoDto();
                    dto.setDate(cursor.getString(cursor.getColumnIndex(MEASURE_TIME)));
                    dto.setLocality(cursor.getString(cursor.getColumnIndex(MEASURE_LOCALITY)));
                    dto.setPm10(cursor.getString(cursor.getColumnIndex(MICRO_DUST)));
                    dto.setPm10Index(cursor.getString(cursor.getColumnIndex(MICRO_DUST_INDEX)));
                    dto.setPm25(cursor.getString(cursor.getColumnIndex(NANO_DUST)));
                    dto.setOzone(cursor.getString(cursor.getColumnIndex(OZON)));
                    dto.setOzoneIndex(cursor.getString(cursor.getColumnIndex(OZON_INDEX)));
                    dto.setNitrogen(cursor.getString(cursor.getColumnIndex(NO2)));
                    dto.setNitrogenIndex(cursor.getString(cursor.getColumnIndex(NO2_INDEX)));
                    dto.setCarbon(cursor.getString(cursor.getColumnIndex(CO)));
                    dto.setCarbonIndex(cursor.getString(cursor.getColumnIndex(CO_INDEX)));
                    dto.setSulfurous(cursor.getString(cursor.getColumnIndex(SO2)));
                    dto.setSulfurousIndex(cursor.getString(cursor.getColumnIndex(SO2_INDEX)));
                    dto.setDegree(cursor.getString(cursor.getColumnIndex(DEGREE)));
                    dto.setMaxIndex(cursor.getString(cursor.getColumnIndex(AIR_QUAL_INDEX)));
                    dto.setMaterial(cursor.getString(cursor.getColumnIndex(MATERIAL)));
                    
                    break;
                }
            }
        }
        
        if (dto != null) {
            DustLog.d(TAG, "==================================================");
            DustLog.d(TAG, "DTO : " + dto.toString());
            DustLog.d(TAG, "==================================================");
            setText(dto);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        
    }
}
