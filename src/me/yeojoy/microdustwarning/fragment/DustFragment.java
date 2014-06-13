
package me.yeojoy.microdustwarning.fragment;

import me.yeojoy.microdustwarning.BuildConfig;
import me.yeojoy.microdustwarning.DustApplication;
import me.yeojoy.microdustwarning.DustConstants;
import me.yeojoy.microdustwarning.R;
import me.yeojoy.microdustwarning.adapter.ImageAdapter;
import me.yeojoy.microdustwarning.entity.OttoEventEntity;
import me.yeojoy.microdustwarning.service.WebParserService;
import me.yeojoy.microdustwarning.util.DustLog;
import me.yeojoy.microdustwarning.util.DustSharedPreferences;
import me.yeojoy.microdustwarning.util.DustUtils;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DustFragment extends Fragment implements DustConstants {

    private static final String TAG = DustFragment.class.getSimpleName();

    private TextView mTvResult;
//    private ToggleButton mTbOnOff;

    private AlarmManager alarmManager;

    private GridView mGdImages;
    private ImageAdapter mAdapter;
    private ArrayList<String> mUrlList;

    private PendingIntent pending;
    
    public DustFragment() { }
    
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

        // Start Activity에서 checkbox 상태를 보고 시작시킨다.
        if (getArguments() != null && getArguments().getBoolean(KEY_CHECKBOX_AUTO_START, false)) {
            launchAlarmManager();
            DustSharedPreferences.getInstance().putBoolean("switch", true);
        }

        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
    }
    
    @Override
    public void onStart() {
        super.onStart();
        // TODO 마지막 데이터를 보여줌.

        setDataToView();
    }

    private void getImageUrls() {
        // TODO http://www.kaq.or.kr/main.asp parsing 필요
        AsyncTask<Void, Void, ArrayList<String>> task = new AsyncTask<Void, Void, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Void... params) {
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
                if (entity == null || TextUtils.isEmpty(entity.rawString)) {
                    setDataToView();
                    return;
                }
                setDataToView(entity.measureTime, entity.rawString);

                break;

            case REFRESH:
                Intent intent = new Intent(getActivity(), WebParserService.class);
                getActivity().startService(intent);
                break;

            case ON_OFF:
                if (entity.on_off) {
                    launchAlarmManager();
                } else {
                    cancelAlarmManager();
                }

                DustSharedPreferences.getInstance().putBoolean("switch", !entity.on_off);

                break;
        }
    }

    /**
     * 알람 매니저 종료
     */
    private void cancelAlarmManager() {
        alarmManager.cancel(pending);
        DustSharedPreferences.getInstance().clear();
    }

    /**
     * 알람 매니저 실행
     */
    private void launchAlarmManager() {
        int notiTime = NOTI_TIME_REAL;
        if (BuildConfig.DEBUG) {
            notiTime = NOTI_TIME_TEST;
        }

        alarmManager.setInexactRepeating(AlarmManager.RTC,
                System.currentTimeMillis() + 1000, notiTime, pending);
        setDataToView();
    }

    /**
     * 수집한 데이터를 View에 보여줌
     * SpannableStringBuilder를 사용해서 색깔을 달리함.
     */
    private void setDataToView() {
        String string = DustSharedPreferences.getInstance().getString("str", null);
        String time = DustSharedPreferences.getInstance().getString("measureTime", null);

        if (TextUtils.isEmpty(string)) return;

        setDataToView(time, string);
    }

    private void setDataToView(String measureTime, String rawString) {
        DustUtils.STATUS[] statuses = DustUtils.analyzeMicroDust(rawString);

        String[] data = rawString.split(" ");
        SpannableStringBuilder ssb = new SpannableStringBuilder();

        if (BuildConfig.DEBUG)
            ssb.append(DustUtils.convertString("측정시각 : " + measureTime + "\n", null));
        else
            ssb.append(DustUtils.convertString("측정시각 : " + measureTime + "\n", DustUtils.STATUS.NONE));
        ssb.append(DustUtils.convertString("지역 : " + data[0] + "\n", DustUtils.STATUS.NONE));
        ssb.append(DustUtils.convertString("미세먼지 : " + data[1] + "\n", statuses[0]));
        ssb.append(DustUtils.convertString("초미세먼지 : " + data[2] + "\n", statuses[1]));
        ssb.append(DustUtils.convertString("오존 : " + data[3] + "\n", statuses[2]));
        ssb.append(DustUtils.convertString("이산화질소 : " + data[4] + "\n", statuses[3]));
        ssb.append(DustUtils.convertString("일산화탄소 : " + data[5] + "\n", statuses[4]));
        ssb.append(DustUtils.convertString("아황산가스 : " + data[6] + "\n", statuses[5]));
        ssb.append(DustUtils.convertString("등급 : " + data[7] + "\n", DustUtils.STATUS.NONE));
        ssb.append(DustUtils.convertString("지수 : " + data[8] + "\n", statuses[6]));
        ssb.append(DustUtils.convertString("지수결정물질 : " + data[9], DustUtils.STATUS.NONE));

        mTvResult.setText(ssb);

        getImageUrls();
    }
}
