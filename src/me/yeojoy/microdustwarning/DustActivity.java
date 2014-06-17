package me.yeojoy.microdustwarning;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import me.yeojoy.microdustwarning.fragment.DustFragment;
import me.yeojoy.microdustwarning.fragment.StartFragment;
import me.yeojoy.microdustwarning.util.DustLog;
import me.yeojoy.microdustwarning.util.DustSharedPreferences;

public class DustActivity extends Activity implements DustConstants {

    private static final String TAG = DustActivity.class.getSimpleName();

    private Fragment mFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dust);
        // SharedPreferences init
        DustSharedPreferences.getInstance().init(this);

        mFragmentManager = getFragmentManager();

        boolean isSwitchOff = DustSharedPreferences.getInstance().getBoolean(KEY_PREFS_SWITCH_OFF, false);

        if (isSwitchOff) {
            DustLog.d(TAG, "onCreate(), Service is OFF.");
            mFragment = new StartFragment();
        } else {
            DustLog.d(TAG, "onCreate(), Service is ON.");
            mFragment = new DustFragment();
        }

        mFragmentManager.beginTransaction().add(R.id.container, mFragment).commit();
    }

}
