package me.yeojoy.microdustwarning;

import me.yeojoy.microdustwarning.fragment.DustFragment;
import me.yeojoy.microdustwarning.util.DustSharedPreferences;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class DustActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dust);
        // SharedPreferences init
        DustSharedPreferences.getInstance().init(this);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new DustFragment())
                    .commit();
        }
    }

}
