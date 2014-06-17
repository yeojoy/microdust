package me.yeojoy.microdustwarning.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import me.yeojoy.microdustwarning.R;

/**
 * Created by yeojoy on 2014. 6. 13..
 */
public class SettingFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
    }
}
