package me.yeojoy.microdustwarning;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;

import me.yeojoy.microdustwarning.fragment.DustFragment;
import me.yeojoy.microdustwarning.fragment.StartFragment;
import me.yeojoy.microdustwarning.util.DustLog;
import me.yeojoy.microdustwarning.util.DustSharedPreferences;

public class StartActivity extends Activity implements DustConstants {

    private static final String TAG = StartActivity.class.getSimpleName();

    private Fragment mFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dust);

        DustLog.i(TAG, "onCreate()");

        mFragmentManager = getFragmentManager();

        if (savedInstanceState == null) {
            mFragment = new StartFragment();
            mFragmentManager.beginTransaction().add(R.id.container, mFragment);
        }
    }

}
