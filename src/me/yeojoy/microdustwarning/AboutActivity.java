package me.yeojoy.microdustwarning;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import me.yeojoy.microdustwarning.fragment.DustFragment;
import me.yeojoy.microdustwarning.fragment.StartFragment;
import me.yeojoy.microdustwarning.util.DustLog;
import me.yeojoy.microdustwarning.util.DustSharedPreferences;

public class AboutActivity extends Activity implements DustConstants {

    private static final String TAG = AboutActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

}
