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
import android.widget.TextView;

import me.yeojoy.microdustwarning.DustConstants;
import me.yeojoy.microdustwarning.R;
import me.yeojoy.microdustwarning.util.DustUtils;


public class StartFragment extends Fragment implements DustConstants {
    private static final String TAG = StartFragment.class.getSimpleName();

    private TextView mTvMainDesc;

    private CheckBox mCbAutoStart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_start, container, false);

        mTvMainDesc = (TextView) layout.findViewById(R.id.tv_main_desc);
        mCbAutoStart = (CheckBox) layout.findViewById(R.id.cb_auto_start);

        layout.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment dustFragment = new DustFragment();
                Bundle args = new Bundle();
                args.putBoolean(KEY_CHECKBOX_AUTO_START, mCbAutoStart.isChecked());
                dustFragment.setArguments(args);

                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.container, dustFragment, null).commit();

            }
        });
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        setDesc();
    }

    private void setDesc() {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        SpannableString ss = new SpannableString(getActivity().getString(R.string.desc_blue));
        ss.setSpan(new ForegroundColorSpan(DustUtils.getTextColor(DustUtils.STATUS.GOOD)), 0, ss.length(),
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        ssb.append(ss);

        ss = new SpannableString(getActivity().getString(R.string.desc_ligth_green));
        ss.setSpan(new ForegroundColorSpan(DustUtils.getTextColor(DustUtils.STATUS.NORMAL)), 0, ss.length(),
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        ssb.append(ss);

        ss = new SpannableString(getActivity().getString(R.string.desc_yellow));
        ss.setSpan(new ForegroundColorSpan(DustUtils.getTextColor(DustUtils.STATUS.BAD)), 0, ss.length(),
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        ssb.append(ss);

        ss = new SpannableString(getActivity().getString(R.string.desc_orange));
        ss.setSpan(new ForegroundColorSpan(DustUtils.getTextColor(DustUtils.STATUS.WORSE)), 0, ss.length(),
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        ssb.append(ss);

        ss = new SpannableString(getActivity().getString(R.string.desc_red));
        ss.setSpan(new ForegroundColorSpan(DustUtils.getTextColor(DustUtils.STATUS.WORST)), 0, ss.length(),
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        ssb.append(ss);

        mTvMainDesc.append(ssb);
    }
}
