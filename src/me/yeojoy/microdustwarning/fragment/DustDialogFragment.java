package me.yeojoy.microdustwarning.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.yeojoy.microdustwarning.R;

/**
 * Created by yeojoy on 2014. 6. 15..
 */
public class DustDialogFragment extends DialogFragment {

    private static final String TAG = DustDialogFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dust, container, false);
        return view;
    }
}
