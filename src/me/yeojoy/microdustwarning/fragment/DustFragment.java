
package me.yeojoy.microdustwarning.fragment;

import me.yeojoy.microdustwarning.DustConstants;
import me.yeojoy.microdustwarning.R;
import me.yeojoy.microdustwarning.adapter.ImageAdapter;
import me.yeojoy.microdustwarning.db.SqliteManager;
import me.yeojoy.microdustwarning.service.WebParserIntentService;
import me.yeojoy.microdustwarning.util.DustSharedPreferences;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ToggleButton;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
                    mTvResult.append("\nAlarmService is on.\n");
                } else {
                    alarmManager.cancel(pending);
                    mTvResult.append("\nAlarmService is off.\n");

                    DustSharedPreferences.getInstance().clear();
                }
                DustSharedPreferences.getInstance().putBoolean("switch", isChecked);
            }
        });
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getActivity(), WebParserIntentService.class);
        pending = PendingIntent.getService(getActivity(), 10002, intent, 0);

        mGdImages = (GridView) view.findViewById(R.id.gv_map);
        mUrlList = new ArrayList<String>();
        mAdapter = new ImageAdapter(getActivity(), mUrlList);

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
        getImageUrls();
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
                    Log.i(TAG, str);
                    if (str.startsWith("http://"))
                        urls.add(str);
                }

                return urls;
            }

            @Override
            protected void onPostExecute(ArrayList<String> strings) {
                super.onPostExecute(strings);
                if (strings != null && strings.size() > 0) {
                    mUrlList.addAll(strings);
                    mAdapter.notifyDataSetChanged();
                }
            }
        };

        task.execute();
    }

    private void setDataToView() {
        String string = DustSharedPreferences.getInstance().getString("str", null);
        String time = DustSharedPreferences.getInstance().getString("measureTime", null);

        if (TextUtils.isEmpty(string)) return;

        String[] data = string.split(" ");
        StringBuilder sb = new StringBuilder();

        if (!TextUtils.isEmpty(time))
            sb.append("\n측정시각 : ").append(time).append("\n");
        sb.append("지역 : ").append(data[0]).append("\n");
        sb.append("미세먼지 : ").append(data[1]).append("\n");
        sb.append("초미세먼지 : ").append(data[2]).append("\n");
        sb.append("오존 : ").append(data[3]).append("\n");
        sb.append("이산화질소 : ").append(data[4]).append("\n");
        sb.append("일산화탄소 : ").append(data[5]).append("\n");
        sb.append("아황산가스 : ").append(data[6]).append("\n");
        sb.append("등급 : ").append(data[7]).append("\n");
        sb.append("지수 : ").append(data[8]).append("\n");
        sb.append("지수결정물질 : ").append(data[9]).append("\n\n\n");

        mTvResult.append(sb);
    }

}
