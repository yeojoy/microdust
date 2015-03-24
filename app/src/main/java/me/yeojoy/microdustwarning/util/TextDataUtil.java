package me.yeojoy.microdustwarning.util;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.yeojoy.microdustwarning.BuildConfig;
import me.yeojoy.microdustwarning.DustApplication;
import me.yeojoy.microdustwarning.DustConstants;
import me.yeojoy.microdustwarning.R;
import me.yeojoy.microdustwarning.entity.DustInfoDto;
import me.yeojoy.microdustwarning.entity.DustInfoDtoLocalityAscComparer;
import me.yeojoy.microdustwarning.entity.STATUS;

/**
 * Created by yeojoy on 14. 12. 30..
 */
public class TextDataUtil implements DustConstants {

    private static final String TAG = TextDataUtil.class.getSimpleName();

    /**
     * 통합대기지수가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getTotalDegree(String value) {
        DustLog.i(TAG, "getTotalDegree()");
        if (TextUtils.isEmpty(value) || "-".equals(value) || "점검중".equals(value))
            return STATUS.NONE;

        float totalValue = Float.parseFloat(value);
        DustLog.d(TAG, "getTotalDegree(), Total Value : " + totalValue);

        if (totalValue > TOTAL_DEGREE_WORST)
            return STATUS.WORST;
        else if (totalValue > TOTAL_DEGREE_WORSE)
            return STATUS.WORSE;
        else if (totalValue > TOTAL_DEGREE_BAD)
            return STATUS.BAD;
        else if (totalValue > TOTAL_DEGREE_NORMAL)
            return STATUS.NORMAL;

        return STATUS.GOOD;
    }

    public static int getTextColor(Resources res, STATUS status) {
        if (status == null) return res.getColor(R.color.font_debuggable_color);

        switch (status) {
            case GOOD:
                // 파란색
                return res.getColor(R.color.font_blue);
            case NORMAL:
                // 연두색
                return res.getColor(R.color.font_light_green);
            case BAD:
                // 노란색
                return res.getColor(R.color.font_yellow);
            case WORSE:
                // 주황색
                return res.getColor(R.color.font_orange);
            case WORST:
                // 빨간색
                return res.getColor(R.color.font_red);
        }
        // 희무끄리한 색
        return res.getColor(R.color.font_default);
    }

    /**
     * status를 확인해서 해당라인의 색을 바꿔준다.
     * @param str
     * @param status
     * @return
     */
    public static SpannableString convertString(Resources res, String str, STATUS status) {
        SpannableString spannableString = new SpannableString(str);

        spannableString.setSpan(new ForegroundColorSpan(getTextColor(res, status)),
                0, spannableString.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);

        return spannableString;
    }

    public static String getMaxValueToString(String maxValue) {
        DustLog.i(TAG, "getMaxValueToString()");
        float max = Float.parseFloat(maxValue);
        DustLog.i(TAG, "getMaxValueToString(), maxValue : " + max);

        if (max > TOTAL_DEGREE_WORST)
            return "위험!!!";
        else if (max > TOTAL_DEGREE_WORSE)
            return "약간 안 좋음";
        else if (max > TOTAL_DEGREE_BAD)
            return "나쁨";
        else if (max > TOTAL_DEGREE_NORMAL)
            return "보통";

        return "좋음 :-)";
    }

    /**
     * 각 물질의 값에 xml로 파싱해서 저장한 값이 유효한 값인지 확인
     * @param dto
     * @return
     */
    private static DustInfoDto verifyDtoValues(DustInfoDto dto) {
        dto.setPm10(checkValue(dto.getPm10()));
        dto.setPm25(checkValue(dto.getPm25()));
        dto.setOzone(checkValue(dto.getOzone()));
        dto.setNitrogen(checkValue(dto.getNitrogen()));
        dto.setCarbon(checkValue(dto.getCarbon()));
        dto.setSulfurous(checkValue(dto.getSulfurous()));

        return dto;
    }

    /**
     * 각 value를 확인해서 null이거나 "null" string이거나 비어있을 때
     * NO_VALUE 값으로 설정해 준다.
     * @param value
     * @return
     */
    private static String checkValue(String value) {
        if (value == null || TextUtils.isEmpty(value) || "null".equals(value)) {
            return NO_VALUE;
        }
        return value;
    }

    /**
     * 201412231000 형식으로 오는 date String을 2014년 12월 23일 10:00 으로 변경
     * @param date
     * @return
     */
    private static String splitDateString(String date) {
        // Example 201412231000
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);
        String hour = date.substring(8, 10);
        if (BuildConfig.DEBUG)
            return String.format(MEASURED_TIME_FORMAT_D, year, month, day, hour);
        return String.format(MEASURED_TIME_FORMAT, year, month, day, hour);
    }
}
