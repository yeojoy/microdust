package me.yeojoy.microdustwarning.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import me.yeojoy.microdustwarning.DustConstants;
import me.yeojoy.microdustwarning.R;

/**
 * Created by yeojoy on 2014. 6. 13..
 */
public class SettingFragment extends PreferenceFragment implements DustConstants,
        SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
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
        }
    }
}
