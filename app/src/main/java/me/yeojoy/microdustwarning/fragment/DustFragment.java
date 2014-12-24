
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

    private TextView mTvResult;

    private AlarmManager alarmManager;

    private GridView mGvImages;
    private ImageAdapter mAdapter;
    private ArrayList<String> mUrlList;

    private PendingIntent pending;

    private LinearLayout mLlIndicator;

    private static int INDEX = 0;

    private DustNetworkManager mManager;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DustLog.i(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_dust, container, false);
        mTvResult = (TextView) view.findViewById(R.id.tv_result);

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getActivity(), WebParserService.class);
        pending = PendingIntent.getService(getActivity(), 10002, intent, 0);

        mGvImages = (GridView) view.findViewById(R.id.gv_map);
        mUrlList = new ArrayList<String>();
        mAdapter = new ImageAdapter(getActivity(), mUrlList);
        mGvImages.setAdapter(mAdapter);

        mLlIndicator = (LinearLayout) view.findViewById(R.id.ll_indicator);

        // Start Activity에서 checkbox 상태를 보고 시작시킨다.
        if (getArguments() != null) {
            if (getArguments().getBoolean(KEY_CHECKBOX_AUTO_START, true)) {
                DustLog.i(TAG, ">>>>>> start Service. <<<<<<");
                launchAlarmManager();
                DustSharedPreferences.getInstance().putBoolean(KEY_PREFS_SWITCH_OFF, false);
            } else {
                DustLog.i(TAG, ">>>>>> nothing happend. <<<<<<");
                DustSharedPreferences.getInstance().putBoolean(KEY_PREFS_SWITCH_OFF, true);
            }
        }

        if (!DustSharedPreferences.getInstance().getBoolean(KEY_PREFS_SWITCH_OFF)) {
            mTvResult.setText(R.string.service_is_on_wait_a_minute);
        }

        view.findViewById(R.id.ib_blue).setOnClickListener(this);
        view.findViewById(R.id.ib_light_green).setOnClickListener(this);
        view.findViewById(R.id.ib_yellow).setOnClickListener(this);
        view.findViewById(R.id.ib_orange).setOnClickListener(this);
        view.findViewById(R.id.ib_red).setOnClickListener(this);
         
         /*
         * Initializes the CursorLoader. The URL_LOADER value is eventually passed
         * to onCreateLoader().
         */
        getLoaderManager().initLoader(AIR_QUALITY_INDEX_CURSOR_LOADER, null,
                this);
        
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DustSharedPreferences.getInstance().init(mContext);
        DustApplication.locality 
                = DustSharedPreferences.getInstance()
                        .getString(KEY_PREFS_LOCALITY);
        if (TextUtils.isEmpty(DustApplication.locality)) {
            DustDialogManager.chooseUserLocalityDialog(mContext, 
                    mDialogSelectListener);
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

        INDEX = 0;
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

        int notiTime = NOTI_TIME_REAL;
        
        if (BuildConfig.DEBUG) {
            notiTime = NOTI_TIME_TEST;
        }

        alarmManager.setInexactRepeating(AlarmManager.RTC,
                System.currentTimeMillis() + 1000, notiTime, pending);
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
            // java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()
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

        // DB에 저장
        SqliteManager manager = SqliteManager.getInstance(mContext);
        manager.saveData(dtoList);
        
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
                    "ASC"                                // Default sort order

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
                int id = cursor.getInt(cursor.getColumnIndex(ID));
                String saveTime = cursor.getString(cursor.getColumnIndex(SAVE_TIME));
                String measureTime = cursor.getString(cursor.getColumnIndex(MEASURE_TIME));
                String locality = cursor.getString(cursor.getColumnIndex(MEASURE_LOCALITY));
                String pm10 = cursor.getString(cursor.getColumnIndex(MICRO_DUST));
                String pm10Index = cursor.getString(cursor.getColumnIndex(MICRO_DUST_INDEX));
                String pm25 = cursor.getString(cursor.getColumnIndex(NANO_DUST));
                String ozon = cursor.getString(cursor.getColumnIndex(OZON));
                String ozonIndex = cursor.getString(cursor.getColumnIndex(OZON_INDEX));
                String no2 = cursor.getString(cursor.getColumnIndex(NO2));
                String no2Index = cursor.getString(cursor.getColumnIndex(NO2_INDEX));
                String co = cursor.getString(cursor.getColumnIndex(CO));
                String coIndex = cursor.getString(cursor.getColumnIndex(CO_INDEX));
                String so2 = cursor.getString(cursor.getColumnIndex(SO2));
                String so2Index = cursor.getString(cursor.getColumnIndex(SO2_INDEX));
                String degree = cursor.getString(cursor.getColumnIndex(DEGREE));
                String airQualIndex = cursor.getString(cursor.getColumnIndex(AIR_QUAL_INDEX));
                String material = cursor.getString(cursor.getColumnIndex(MATERIAL));
                DustLog.i(TAG,
                        id + " " +
                        saveTime + " " +
                        measureTime + " " +
                        locality + " " +
                        pm10 + " " +
                        pm10Index + " " +
                        pm25 + " " +
                        ozon + " " +
                        ozonIndex + " " +
                        no2 + " " +
                        no2Index + " " +
                        co + " " +
                        coIndex + " " +
                        so2 + " " +
                        so2Index + " " +
                        degree + " " +
                        airQualIndex + " " +
                        material + "\n"
                );
                
                if (DustApplication.locality.equals(locality)) {
                    dto = new DustInfoDto();
                    dto.setDate(measureTime);
                    dto.setLocality(locality);
                    dto.setPm10(pm10);
                    dto.setPm10Index(pm10Index);
                    dto.setPm25(pm25);
                    dto.setOzone(ozon);
                    dto.setOzoneIndex(ozonIndex);
                    dto.setNitrogen(no2);
                    dto.setNitrogenIndex(no2Index);
                    dto.setCarbon(co);
                    dto.setCarbonIndex(coIndex);
                    dto.setSulfurous(so2);
                    dto.setSulfurousIndex(so2Index);
                    dto.setDegree(degree);
                    dto.setMaxIndex(airQualIndex);
                    dto.setMaterial(material);
                    
                    setText(dto);
                }
            }
        }
        
        if (dto != null) {
            DustLog.d(TAG, "==================================================");
            DustLog.d(TAG, "DTO : " + dto.toString());
            DustLog.d(TAG, "==================================================");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
