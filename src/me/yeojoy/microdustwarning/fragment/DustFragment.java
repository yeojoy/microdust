
package me.yeojoy.microdustwarning.fragment;

import me.yeojoy.microdustwarning.DustConstants;
import me.yeojoy.microdustwarning.R;
import me.yeojoy.microdustwarning.adapter.ImageAdapter;
import me.yeojoy.microdustwarning.service.WebParserService;
import me.yeojoy.microdustwarning.util.DustSharedPreferences;
import me.yeojoy.microdustwarning.util.DustUtils;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ToggleButton;

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
    private ToggleButton mTbOnOff;

    private AlarmManager alarmManager;

    private GridView mGdImages;
    private ImageAdapter mAdapter;
    private ArrayList<String> mUrlList;

    private PendingIntent pending;
    
    public DustFragment() { }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_dust, container, false);
        mTvResult = (TextView) view.findViewById(R.id.tv_result);

        mTbOnOff = (ToggleButton) view.findViewById(R.id.tb_on_off);
        mTbOnOff.setChecked(DustSharedPreferences.getInstance().getBoolean("switch"));
        mTbOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    alarmManager.setInexactRepeating(AlarmManager.RTC,
                            System.currentTimeMillis() + 1000, NOTI_TIME, pending);
                    setDataToView();
                } else {
                    alarmManager.cancel(pending);
                    DustSharedPreferences.getInstance().clear();
                }
                DustSharedPreferences.getInstance().putBoolean("switch", isChecked);
            }
        });
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getActivity(), WebParserService.class);
        pending = PendingIntent.getService(getActivity(), 10002, intent, 0);

        mGdImages = (GridView) view.findViewById(R.id.gv_map);
        mUrlList = new ArrayList<String>();
        mAdapter = new ImageAdapter(getActivity(), mUrlList);
        mGdImages.setAdapter(mAdapter);

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
                    for (String s : strings) {
                        Log.d(TAG, "Image URL : " + s);
                    }

                    mUrlList.clear();
                    mUrlList.addAll(strings);
                    mAdapter.notifyDataSetChanged();
                }
            }
        };

        task.execute();
    }

    /**
     * 수집한 데이터를 View에 보여줌
     * SpannableStringBuilder를 사용해서 색깔을 달리함.
     */
    private void setDataToView() {
        String string = DustSharedPreferences.getInstance().getString("str", null);
        String time = DustSharedPreferences.getInstance().getString("measureTime", null);

        if (TextUtils.isEmpty(string)) return;

        DustUtils.STATUS[] statuses = DustUtils.analyzeMicroDust(string);

        String[] data = string.split(" ");
        SpannableStringBuilder ssb = new SpannableStringBuilder();

        // 일반 정보
        if (!TextUtils.isEmpty(time))
            ssb.append(DustUtils.convertString("측정시각 : " + time, DustUtils.STATUS.NONE));
        ssb.append(DustUtils.convertString("지역 : " + data[0], DustUtils.STATUS.NONE));
        ssb.append(DustUtils.convertString("미세먼지 : " + data[1], statuses[0]));
        ssb.append(DustUtils.convertString("초미세먼지 : " + data[2], statuses[1]));
        ssb.append(DustUtils.convertString("오존 : " + data[3], statuses[2]));
        ssb.append(DustUtils.convertString("이산화질소 : " + data[4], statuses[3]));
        ssb.append(DustUtils.convertString("일산화탄소 : " + data[5], statuses[4]));
        ssb.append(DustUtils.convertString("아황산가스 : " + data[6], statuses[5]));
        ssb.append(DustUtils.convertString("등급 : " + data[7], DustUtils.STATUS.NONE));
        ssb.append(DustUtils.convertString("지수 : " + data[8], statuses[6]));
        ssb.append(DustUtils.convertString("지수결정물질 : " + data[9], DustUtils.STATUS.NONE));

        mTvResult.setText(ssb);

        getImageUrls();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
