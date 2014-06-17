
package me.yeojoy.microdustwarning.fragment;

import me.yeojoy.microdustwarning.BuildConfig;
import me.yeojoy.microdustwarning.DustApplication;
import me.yeojoy.microdustwarning.DustConstants;
import me.yeojoy.microdustwarning.R;
import me.yeojoy.microdustwarning.adapter.ImageAdapter;
import me.yeojoy.microdustwarning.entity.OttoEventEntity;
import me.yeojoy.microdustwarning.entity.STATUS;
import me.yeojoy.microdustwarning.service.WebParserService;
import me.yeojoy.microdustwarning.util.DustDialogManager;
import me.yeojoy.microdustwarning.util.DustLog;
import me.yeojoy.microdustwarning.util.DustSharedPreferences;
import me.yeojoy.microdustwarning.util.DustUtils;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.squareup.otto.Subscribe;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DustFragment extends Fragment implements DustConstants, View.OnClickListener {

    private static final String TAG = DustFragment.class.getSimpleName();

    private TextView mTvResult;

    private AlarmManager alarmManager;

    private GridView mGdImages;
    private ImageAdapter mAdapter;
    private ArrayList<String> mUrlList;

    private PendingIntent pending;

    private LinearLayout mLlIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DustLog.i(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_dust, container, false);
        mTvResult = (TextView) view.findViewById(R.id.tv_result);

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getActivity(), WebParserService.class);
        pending = PendingIntent.getService(getActivity(), 10002, intent, 0);

        mGdImages = (GridView) view.findViewById(R.id.gv_map);
        mUrlList = new ArrayList<String>();
        mAdapter = new ImageAdapter(getActivity(), mUrlList);
        mGdImages.setAdapter(mAdapter);

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
            setDataToView();
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

    }

    private void getImageUrls() {

        mUrlList.clear();
        mAdapter.notifyDataSetChanged();

        String url = null;
        for (String key : KEY_PREFS_IMAGES) {
            url = DustSharedPreferences.getInstance().getString(key, null);
            if (TextUtils.isEmpty(url)) break;

            mUrlList.add(url);
        }

        if (mUrlList.size() == 4) {
            mAdapter.notifyDataSetChanged();
            return;
        }

        mUrlList.clear();

        AsyncTask<Void, Void, ArrayList<String>> task = new AsyncTask<Void, Void, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Void... params) {
                // TODO http://www.kaq.or.kr/main.asp parsing 필요
                final String url = "http://www.kaq.or.kr/main.asp";
                Source source = null;
                try {
                    source = new Source(new URL(url));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                List<Element> imgList = source.getAllElements(HTMLElementName.IMG);
                ArrayList<String> urls = new ArrayList<String>();
                for (Element e : imgList) {
                    String str = e.getAttributeValue("src").toString();
                    if (str.startsWith("http://"))
                        urls.add(str);
                }

                for (int i = 0, j = urls.size(); i < j; i++) {
                    DustSharedPreferences.getInstance().putString(KEY_PREFS_IMAGES[i], urls.get(i));
                }

                return urls;
            }

            @Override
            protected void onPostExecute(ArrayList<String> strings) {
                super.onPostExecute(strings);

                if (strings != null && strings.size() > 0) {
                    mUrlList.clear();
                    mUrlList.addAll(strings);
                    mAdapter.notifyDataSetChanged();
                }
            }
        };

        task.execute();
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
                refreshData();
                return true;

            case R.id.action_settings:
                if (BuildConfig.DEBUG) {
                    getActivity().getFragmentManager().beginTransaction()
                            .replace(R.id.container, new SettingFragment())
                            .addToBackStack(null).commit();
                } else {
                    Toast.makeText(getActivity(), R.string.on_construction,
                            Toast.LENGTH_SHORT).show();
                }
                return true;

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        DustLog.i(TAG, "onCreateOptionsMenu()");
        menu.clear();
        inflater.inflate(R.menu.dust, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        DustApplication.bus.register(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        DustApplication.bus.unregister(this);
    }

    @Subscribe
    public void receiveOttoEventEntity(OttoEventEntity entity) {
        DustLog.i(TAG, "receiveOttoEventEntity()");
        switch (entity.command) {
            case GET_DATA:
                setDataToView(entity.measureTime, entity.rawString);

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

    private void refreshData() {
        Intent intent = new Intent(getActivity(), WebParserService.class);
        getActivity().startService(intent);
    }

    private void setDataToView() {
        String measuredTime = DustSharedPreferences.getInstance().getString(KEY_PREFS_MEASURE_TIME, null);
        String rawString = DustSharedPreferences.getInstance().getString(KEY_PREFS_RAW_STRING, null);

        if (TextUtils.isEmpty(measuredTime) || TextUtils.isEmpty(rawString)) return;

        setDataToView(measuredTime, rawString);

    }

    private void setDataToView(String measureTime, String rawString) {
        STATUS[] statuses = DustUtils.analyzeMicroDust(rawString);

        String[] data = rawString.split(" ");
        SpannableStringBuilder ssb = new SpannableStringBuilder();

        Resources res = getResources();

        if (BuildConfig.DEBUG)
            ssb.append(DustUtils.convertString(res, "측정시각 : " + measureTime + "\n", null));
        else
            ssb.append(DustUtils.convertString(res, "측정시각 : " + measureTime + "\n", STATUS.NONE));
        ssb.append(DustUtils.convertString(res, "지역 : " + data[0] + "\n", STATUS.NONE));
        ssb.append(DustUtils.convertString(res, "미세먼지 : " + data[1] + "\n", statuses[0]));
        ssb.append(DustUtils.convertString(res, "초미세먼지 : " + data[2] + "\n", statuses[1]));
        ssb.append(DustUtils.convertString(res, "오존 : " + data[3] + "\n", statuses[2]));
        ssb.append(DustUtils.convertString(res, "이산화질소 : " + data[4] + "\n", statuses[3]));
        ssb.append(DustUtils.convertString(res, "일산화탄소 : " + data[5] + "\n", statuses[4]));
        ssb.append(DustUtils.convertString(res, "아황산가스 : " + data[6] + "\n", statuses[5]));
        ssb.append(DustUtils.convertString(res, "등급 : " + data[7] + "\n", STATUS.NONE));
        ssb.append(DustUtils.convertString(res, "통합지수 : " + data[8] + "\n", statuses[6]));
        ssb.append(DustUtils.convertString(res, "지수결정물질 : " + data[9], STATUS.NONE));

        mTvResult.setText(ssb);

        mLlIndicator.setVisibility(View.VISIBLE);
        getImageUrls();
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
}
