package me.yeojoy.microdustwarning;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import me.yeojoy.microdustwarning.util.DustUtils;

public class AboutActivity extends Activity implements DustConstants, View.OnClickListener {

    private static final String TAG = AboutActivity.class.getSimpleName();
    private static final String VIEW_NAME = "about activity";
    
    private TextView mTvAppVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mTvAppVersion = (TextView) findViewById(R.id.tv_app_version);
        mTvAppVersion.setText("v" + DustUtils.getAppVersion(this));

        if (BuildConfig.DEBUG) mTvAppVersion.setTextColor(Color.RED);

        findViewById(R.id.tv_infomation).setOnClickListener(this);
        findViewById(R.id.tv_thanks_to).setOnClickListener(this);
        findViewById(R.id.tv_send_feedback).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get tracker.
        Tracker t = ((DustApplication) getApplication())
                .getTracker(DustApplication.TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName(VIEW_NAME);

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_infomation:
                intent = new Intent(this, InfoActivity.class);
                intent.putExtra("title", INFORMATION_TITLE);
                startActivity(intent);
                break;

            case R.id.tv_thanks_to:
                intent = new Intent(this, InfoActivity.class);
                intent.putExtra("title", THANKS_TO_TITLE);
                startActivity(intent);

                break;

            case R.id.tv_send_feedback:
//                Intent emailIntent = new Intent(Intent.ACTION_SEND);
//                emailIntent.setType(HTTP.PLAIN_TEXT_TYPE);
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { EMAIL_ADDR });
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);
//                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
//
//                startActivity(emailIntent);

                intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", EMAIL_ADDR, null));
                intent.putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);

                startActivity(Intent.createChooser(intent, "Send Email"));
                break;
        }
    }
}
