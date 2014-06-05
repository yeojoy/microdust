
package me.yeojoy.microdustwarning.fragment;

import me.yeojoy.microdustwarning.DustConstants;
import me.yeojoy.microdustwarning.R;
import me.yeojoy.microdustwarning.db.SqliteManager;
import me.yeojoy.microdustwarning.service.WebParserIntentService;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class DustFragment extends Fragment implements OnClickListener, DustConstants {

    private static final String TAG = DustFragment.class.getSimpleName();

    private TextView mTvResult;
    
    private AlarmManager alarmManager;

    private Context mContext;
    
    private PendingIntent pending;
    
    public DustFragment() { }
    
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
        
        mContext = getActivity();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        
        SqliteManager manager = SqliteManager.getInstance();
        if (!manager.isDoneInit())
            manager.init(mContext);
        
        Cursor cursor = manager.getDBData();
        
        if (cursor == null) return;
        
        // TODO 마지막 데이터를 보여줌.
        
        
    }
    
    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick()");
        if (v.getId() == R.id.btn_on) {
            alarmManager.setInexactRepeating(AlarmManager.RTC, 
                    System.currentTimeMillis() + 1000, NOTI_TIME, pending);
            mTvResult.append("\nAlarmService is on.\n");
        } else if (v.getId() == R.id.btn_off) {
            alarmManager.cancel(pending);
            mTvResult.append("\nAlarmService is off.\n");
        }
    }
}
