package me.yeojoy.microdustwarning;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.apache.http.protocol.HTTP;

import me.yeojoy.microdustwarning.util.DustUtils;

public class AboutActivity extends Activity implements DustConstants, View.OnClickListener {

    private static final String TAG = AboutActivity.class.getSimpleName();

    private TextView mTvAppVersion, mTvAppTag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mTvAppVersion = (TextView) findViewById(R.id.tv_app_version);
        mTvAppTag = (TextView) findViewById(R.id.tv_app_version_tag);

        mTvAppVersion.setText(DustUtils.getAppVersion(this));
        mTvAppTag.setText(BuildConfig.DEBUG ? DEBUGGABLE : RELEASE);

        findViewById(R.id.tv_infomation).setOnClickListener(this);
        findViewById(R.id.tv_thanks_to).setOnClickListener(this);
        findViewById(R.id.tv_send_feedback).setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_infomation:
                intent = new Intent(this, InfoActivity.class);
                intent.putExtra("title", INFOMATION_TITLE);
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
