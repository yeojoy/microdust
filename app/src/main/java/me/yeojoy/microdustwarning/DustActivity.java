package me.yeojoy.microdustwarning;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import me.yeojoy.microdustwarning.fragment.DustFragment;
import me.yeojoy.microdustwarning.fragment.StartFragment;
import me.yeojoy.microdustwarning.util.DustLog;
import me.yeojoy.microdustwarning.util.DustSharedPreferences;

public class DustActivity extends Activity implements DustConstants {

    private static final String TAG = DustActivity.class.getSimpleName();

    private Fragment mFragment;
    private FragmentManager mFragmentManager;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dust);
        // SharedPreferences init

        mContext = this;
        mFragmentManager = getFragmentManager();

        boolean isSwitchOff = DustSharedPreferences.getInstance(this).getBoolean(KEY_PREFS_SWITCH_OFF, true);

        if (isSwitchOff) {
            Intent intent = new Intent(mContext, StartActivity.class);
            startActivity(intent);
        }

        if (savedInstanceState == null) {
            mFragment = new DustFragment();
            mFragmentManager.beginTransaction().add(R.id.container, mFragment).commit();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        
    }
}
