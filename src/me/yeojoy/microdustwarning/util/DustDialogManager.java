package me.yeojoy.microdustwarning.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import me.yeojoy.microdustwarning.DustConstants;
import me.yeojoy.microdustwarning.R;
import me.yeojoy.microdustwarning.entity.STATUS;

/**
 * Created by yeojoy on 2014. 6. 16..
 */
public class DustDialogManager implements DustConstants {

    private static final String TAG = DustDialogManager.class.getSimpleName();

    private static final int[] GOOD_DESC = { R.string.good };
    private static final int[] NORMAL_DESC = { R.string.normal_o3 };
    private static final int[] BAD_DESC = {
            R.string.bad_micro_dust, R.string.bad_o3, R.string.bad_co, R.string.bad_so2
    };
    private static final int[] WORSE_DESC = {
            R.string.worse_micro_dust, R.string.worse_o3, R.string.worse_co, R.string.worse_so2
    };
    private static final int[] WORST_DESC = {
            R.string.worst_micro_dust, R.string.worst_o3, R.string.worst_no2,
            R.string.worst_co, R.string.worst_so2
    };

    public static void showDialogWarningMessage(Context context, STATUS status) {
        Log.i(TAG, "showDialogTitleAndMessage()");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        int title;
        int[] messageIds;

        switch (status) {
            case GOOD:
                title = R.string.dlg_status_good_title;
                messageIds = GOOD_DESC;
                break;

            case BAD:
                title = R.string.dlg_status_bad_title;
                messageIds = BAD_DESC;
                break;

            case WORSE:
                title = R.string.dlg_status_worse_title;
                messageIds = WORSE_DESC;
                break;

            case WORST:
                title = R.string.dlg_status_worst_title;
                messageIds = WORST_DESC;
                break;

            default:
                title = R.string.dlg_status_normal_title;
                messageIds = NORMAL_DESC;
                break;
        }

        if (title == -1)
            builder.setTitle(context.getString(R.string.dlg_default_title));
        else
            builder.setTitle(title);

        builder.setMessage(getDialogMessage(context.getResources(), status, messageIds));
        builder.setPositiveButton(R.string.dlg_default_positive_button,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );

        builder.create().show();
    }

    private static String getDialogMessage(Resources res, STATUS status, int... messageIds) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        int index;
        // TODO 기본 AlertDialog에 SpannableString 안 먹힘.
        // solution 1. 아래 코드를 그냥 String으로 변경
        // solution 2. CustomDialog를 만듦
        switch (status) {

            case GOOD:
                ssb.append(res.getString(R.string.good));
                break;
            case NORMAL:
                ssb.append(res.getString(R.string.o3)).append("\n");
                ssb.setSpan(new ForegroundColorSpan(DustUtils.getTextColor(res, status)),
                        0, ssb.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ssb.append(res.getString(messageIds[0]));
                break;
            case BAD:
            case WORSE:
                ssb.append(res.getString(R.string.micro_dust)).append("\n");
                ssb.setSpan(new ForegroundColorSpan(DustUtils.getTextColor(res, status)),
                        0, ssb.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ssb.append(res.getString(messageIds[0]));
                index = ssb.length();
                ssb.append("\n").append(res.getString(R.string.o3)).append("\n");
                ssb.setSpan(new ForegroundColorSpan(DustUtils.getTextColor(res, status)),
                        index, ssb.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ssb.append(res.getString(messageIds[1]));
                index = ssb.length();
                ssb.append("\n").append(res.getString(R.string.co)).append("\n");
                ssb.setSpan(new ForegroundColorSpan(DustUtils.getTextColor(res, status)),
                        index, ssb.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ssb.append(res.getString(messageIds[2]));
                index = ssb.length();
                ssb.append("\n").append(res.getString(R.string.so2)).append("\n");
                ssb.setSpan(new ForegroundColorSpan(DustUtils.getTextColor(res, status)),
                        index, ssb.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ssb.append(res.getString(messageIds[3]));

                break;
            case WORST:
                ssb.append(res.getString(R.string.micro_dust)).append("\n");
                ssb.setSpan(new ForegroundColorSpan(DustUtils.getTextColor(res, status)),
                        0, ssb.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ssb.append(res.getString(messageIds[0]));
                index = ssb.length();
                ssb.append("\n").append(res.getString(R.string.o3)).append("\n");
                ssb.setSpan(new ForegroundColorSpan(DustUtils.getTextColor(res, status)),
                        index, ssb.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ssb.append(res.getString(messageIds[1]));
                index = ssb.length();
                ssb.append("\n").append(res.getString(R.string.co)).append("\n");
                ssb.setSpan(new ForegroundColorSpan(DustUtils.getTextColor(res, status)),
                        index, ssb.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ssb.append(res.getString(messageIds[2]));
                index = ssb.length();
                ssb.append("\n").append(res.getString(R.string.co)).append("\n");
                ssb.setSpan(new ForegroundColorSpan(DustUtils.getTextColor(res, status)),
                        index, ssb.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ssb.append(res.getString(messageIds[3]));
                index = ssb.length();
                ssb.append("\n").append(res.getString(R.string.so2)).append("\n");
                ssb.setSpan(new ForegroundColorSpan(DustUtils.getTextColor(res, status)),
                        index, ssb.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ssb.append(res.getString(messageIds[4]));

                break;
        }

        return ssb.toString();
    }

    /**
     * 서울 외 지역인 경우 Airkorea 모바일 사이트로 이동시킴
     * @param context
     * @param location
     */
    public static void showWarningOutsideSeoul(final Context context, final Location location) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dlg_default_title);
        builder.setMessage(R.string.dlg_warn_out_of_seoul);
        builder.setPositiveButton(R.string.dlg_btn_move, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(String.format(AIR_KOREA, location.getLatitude(),
                        location.getLongitude())));
                context.startActivity(intent);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.dlg_btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }
}
