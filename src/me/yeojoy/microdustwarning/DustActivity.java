package me.yeojoy.microdustwarning;

import me.yeojoy.microdustwarning.entity.OttoEventEntity;
import me.yeojoy.microdustwarning.fragment.DustFragment;
import me.yeojoy.microdustwarning.fragment.SettingFragment;
import me.yeojoy.microdustwarning.fragment.StartFragment;
import me.yeojoy.microdustwarning.util.DustLog;
import me.yeojoy.microdustwarning.util.DustSharedPreferences;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

        if (DustSharedPreferences.getInstance().getBoolean(KEY_PREFS_FIRST_LAUNCH, true)) {
            mFragment = new StartFragment();
            DustSharedPreferences.getInstance().putBoolean(KEY_PREFS_FIRST_LAUNCH, false);
        } else {
            mFragment = new DustFragment();
        }

        mFragmentManager.beginTransaction().add(R.id.container, mFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DustLog.i(TAG, "onOptionsItemSelected()");
        switch (item.getItemId()) {
            case R.id.action_on_off:
                OttoEventEntity entity = new OttoEventEntity(OttoEventEntity.COMMAND.ON_OFF);
                entity.setBoolean(!item.isChecked());
                DustApplication.bus.post(entity);

                return true;

            case R.id.action_refresh:
                DustApplication.bus.post(new OttoEventEntity(OttoEventEntity.COMMAND.REFRESH));
                return true;

            case R.id.action_settings:
                mFragmentManager.beginTransaction()
                        .add(R.id.container, new SettingFragment())
                        .addToBackStack(null).commit();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        DustLog.i(TAG, "onPrepareOptionsMenu()");
        MenuItem item = menu.findItem(R.id.action_on_off);
        boolean serviceSwitchStatus = !DustSharedPreferences.getInstance().getBoolean("switch", true);

        item.setChecked(serviceSwitchStatus);

        if (serviceSwitchStatus) {
            item.setTitle(R.string.action_swtich_on);
        } else {
            item.setTitle(R.string.action_swtich_off);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        DustLog.i(TAG, "onCreateOptionsMenu()");
        getMenuInflater().inflate(R.menu.dust, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
