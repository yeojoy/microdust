package me.yeojoy.microdustwarning.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import me.yeojoy.microdustwarning.DustConstants;
import me.yeojoy.microdustwarning.R;
import me.yeojoy.microdustwarning.entity.STATUS;
import me.yeojoy.microdustwarning.util.DustLog;
import me.yeojoy.microdustwarning.util.DustUtils;


public class StartFragment extends Fragment implements DustConstants {
    private static final String TAG = StartFragment.class.getSimpleName();

    private TextView mTvMainDescription;

    private boolean mWantToStartService = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_start, container, false);

        mTvMainDescription = (TextView) layout.findViewById(R.id.tv_main_desc);
        ((CheckBox) layout.findViewById(R.id.cb_auto_start)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mWantToStartService = isChecked;
                    }
                }
        );
        
        layout.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment dustFragment = new DustFragment();
                Bundle args = new Bundle();
                args.putBoolean(KEY_CHECKBOX_AUTO_START, mWantToStartService);
                dustFragment.setArguments(args);

                DustLog.d(TAG, KEY_CHECKBOX_AUTO_START + " : " + mWantToStartService);

                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.container, dustFragment, null).commit();
            }

        });

        setDescription();
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setTitle(R.string.app_name);
    }

    private void setDescription() {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        SpannableString ss = new SpannableString(getActivity().getString(R.string.desc_blue));
        ss.setSpan(new ForegroundColorSpan(DustUtils.getTextColor(getResources(), STATUS.GOOD)), 0, ss.length(),
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        ssb.append(ss);

        ss = new SpannableString(getActivity().getString(R.string.desc_ligth_green));
        ss.setSpan(new ForegroundColorSpan(DustUtils.getTextColor(getResources(), STATUS.NORMAL)), 0, ss.length(),
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        ssb.append(ss);

        ss = new SpannableString(getActivity().getString(R.string.desc_yellow));
        ss.setSpan(new ForegroundColorSpan(DustUtils.getTextColor(getResources(), STATUS.BAD)), 0, ss.length(),
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        ssb.append(ss);

        ss = new SpannableString(getActivity().getString(R.string.desc_orange));
        ss.setSpan(new ForegroundColorSpan(DustUtils.getTextColor(getResources(), STATUS.WORSE)), 0, ss.length(),
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        ssb.append(ss);

        ss = new SpannableString(getActivity().getString(R.string.desc_red));
        ss.setSpan(new ForegroundColorSpan(DustUtils.getTextColor(getResources(), STATUS.WORST)), 0, ss.length(),
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        ssb.append(ss);

        mTvMainDescription.append(ssb);
    }
}
