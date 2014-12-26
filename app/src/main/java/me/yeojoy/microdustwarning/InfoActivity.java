package me.yeojoy.microdustwarning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import me.yeojoy.microdustwarning.util.DustLog;

public class InfoActivity extends Activity implements DustConstants {

    private static final String TAG = InfoActivity.class.getSimpleName();
    private static final String VIEW_NAME = "informain activity";
    
    private TextView mTvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        DustLog.i(TAG, "onCreate()");
        setContentView(R.layout.activity_info);

        mTvInfo = (TextView) findViewById(R.id.tv_info);

        Intent intent = getIntent();
        if (intent == null || TextUtils.isEmpty(intent.getStringExtra("title"))) return;

        String title = intent.getStringExtra("title");
        getActionBar().setTitle(title);

        if (title.equals(INFORMATION_TITLE)) {
            mTvInfo.setText(R.string.info_infomation);
        } else {
            mTvInfo.setText(R.string.info_thanks_to);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        Tracker t = ((DustApplication) getApplication())
                .getTracker(DustApplication.TrackerName.APP_TRACKER);
        // Set screen name.
        t.setScreenName(VIEW_NAME);

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }
}
