package me.yeojoy.microdustwarning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

public class InfoActivity extends Activity implements DustConstants {

    private static final String TAG = InfoActivity.class.getSimpleName();

    private TextView mTvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

}
