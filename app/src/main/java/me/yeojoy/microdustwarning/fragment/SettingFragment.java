package me.yeojoy.microdustwarning.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import me.yeojoy.microdustwarning.DustApplication;
import me.yeojoy.microdustwarning.DustConstants;
import me.yeojoy.microdustwarning.R;
import me.yeojoy.microdustwarning.network.DustNetworkManager;

/**
 * Created by yeojoy on 2014. 6. 13..
 */
public class SettingFragment extends PreferenceFragment implements DustConstants,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String VIEW_NAME = "setting fragment";
    
    private Context mContext;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        
        mContext = getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        getActivity().getActionBar().setTitle(R.string.setting);

        Tracker t = ((DustApplication) getActivity().getApplication())
                .getTracker(DustApplication.TrackerName.APP_TRACKER);
        // Set screen name.
        t.setScreenName(VIEW_NAME);

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_PREFS_NOTICE_VIBRATE)) {
            Preference noticePref = findPreference(key);
            if (sharedPreferences.getBoolean(key, true))
                noticePref.setSummary(getString(R.string.pref_summary_noti_on));
            else
                noticePref.setSummary(getString(R.string.pref_summary_noti_off));
        } else if (key.equals(KEY_PREFS_IS_ENABLED_DO_NOT_BOTHER)) {
            if (sharedPreferences.getBoolean(key, true)) {
                DustApplication.mIsEnabledDoNotBother = true;
            } else {
                DustApplication.mIsEnabledDoNotBother = false;
            }
        } else if (key.equals(KEY_PREFS_IS_ON_GOING)) {
            if (sharedPreferences.getBoolean(key, true)) {
                DustApplication.mIsOnGoing = true;
            } else {
                DustApplication.mIsOnGoing = false;
            }

            DustNetworkManager.getInstance().getMicrodustInfo(mContext);
        }
    }
}
