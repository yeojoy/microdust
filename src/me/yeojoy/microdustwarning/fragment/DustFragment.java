
package me.yeojoy.microdustwarning.fragment;

import me.yeojoy.microdustwarning.R;
import me.yeojoy.microdustwarning.service.WebParserIntentService;
import my.lib.MyLog;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class DustFragment extends Fragment implements OnClickListener {

    private static final String TAG = DustFragment.class.getSimpleName();

    private TextView mTvResult;
    
    private AlarmManager alarmManager;

    public DustFragment() {
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_dust, container, false);
        mTvResult = (TextView) view.findViewById(R.id.tv_result);

        view.findViewById(R.id.btn_on).setOnClickListener(this);
        view.findViewById(R.id.btn_off).setOnClickListener(this);
        
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        
        Intent intent = new Intent(getActivity(), WebParserIntentService.class);
        pending = PendingIntent.getService(getActivity(), 10002, intent, 0);
        
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
    }

    private PendingIntent pending;
    @Override
    public void onClick(View v) {
        MyLog.i(TAG, "onClick()");
        if (v.getId() == R.id.btn_on) {
            alarmManager.setInexactRepeating(AlarmManager.RTC, 
                    System.currentTimeMillis() + 1000, 1000 * 30, pending);
            mTvResult.append("\nAlarmService is on.\n");
        } else if (v.getId() == R.id.btn_off) {
            alarmManager.cancel(pending);
            mTvResult.append("\nAlarmService is off.\n");
        }
    }
}
