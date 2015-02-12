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
     * 받은 data로 미세먼지 확인 후 Notification으로 알려준다.
     * @param data
     */
    public static STATUS[] analyzeMicroDust(DustInfoDto data) {
        DustLog.i(TAG, "analyzeMicroDust()");
        // TODO Index로 하면 하나의 method로 처리할 수 있다. 수정 필요.
        // TEST DATA
        // 동네 미세먼지 초미세먼지 오존 이산화질소 일산화탄소 아황산가스 등급 지수 지수결정물질
        // 관악구 60 39 0.012 0.051 0.6 0.005 보통 85 NO2
        STATUS[] status = new STATUS[7];
        status[0] = getMicroDustDegree(data.getPm10());
        status[1] = getNanoDustDegree(data.getPm25());
        status[2] = getOzonDegree(data.getOzone());
        status[3] = getNO2Degree(data.getNitrogen());
        status[4] = getCODegree(data.getCarbon());
        status[5] = getSO2Degree(data.getSulfurous());
        status[6] = getTotalDegree(data.getMaxIndex());

        return status;
    }

    /**
     * 미세먼지 수치가 괜찮은지
     * @param value
     * @return
     */
    private static String getTotalAirQuality(String value) {
        float microDustValue = Float.parseFloat(value);

        if (microDustValue > TOTAL_DEGREE_WORSE)
            return AIR_QUALITY_INDEX[4];
        else if (microDustValue > TOTAL_DEGREE_BAD)
            return AIR_QUALITY_INDEX[3];
        else if (microDustValue > TOTAL_DEGREE_NORMAL)
            return AIR_QUALITY_INDEX[2];
        else if (microDustValue > TOTAL_DEGREE_GOOD)
            return AIR_QUALITY_INDEX[1];

        return AIR_QUALITY_INDEX[0];
    }

    /**
     * 미세먼지 수치가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getMicroDustDegree(String value) {
        if (TextUtils.isEmpty(value) || "-".equals(value) || "점검중".equals(value))
            return STATUS.NONE;

        float microDustValue = Float.parseFloat(value);

        if (microDustValue > MICRO_DUST_WORSE)
            return STATUS.WORST;
        else if (microDustValue > MICRO_DUST_BAD)
            return STATUS.WORSE;
        else if (microDustValue > MICRO_DUST_NORMAL)
            return STATUS.BAD;
        else if (microDustValue > MICRO_DUST_GOOD)
            return STATUS.NORMAL;

        return STATUS.GOOD;
    }

    /**
     * 초미세먼지 수치가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getNanoDustDegree(String value) {
        if (TextUtils.isEmpty(value) || "-".equals(value) || "점검중".equals(value))
            return STATUS.NONE;

        float microDustValue = Float.parseFloat(value);

        if (microDustValue > NANO_DUST_WORSE)
            return STATUS.WORST;
        else if (microDustValue > NANO_DUST_BAD)
            return STATUS.WORSE;
        else if (microDustValue > NANO_DUST_NORMAL)
            return STATUS.BAD;
        else if (microDustValue > NANO_DUST_GOOD)
            return STATUS.NORMAL;

        return STATUS.GOOD;
    }

    /**
     * 오존 수치가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getOzonDegree(String value) {
        if (TextUtils.isEmpty(value) || "-".equals(value) || "점검중".equals(value))
            return STATUS.NONE;

        float ozonValue = Float.parseFloat(value);

        if (ozonValue > O3_WORSE)
            return STATUS.WORST;
        else if (ozonValue > O3_BAD)
            return STATUS.WORSE;
        else if (ozonValue > O3_NORMAL)
            return STATUS.BAD;
        else if (ozonValue > O3_GOOD)
            return STATUS.NORMAL;

        return STATUS.GOOD;
    }

    /**
     * 이산화질소 수치가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getNO2Degree(String value) {
        if (TextUtils.isEmpty(value) || "-".equals(value) || "점검중".equals(value))
            return STATUS.NONE;

        float no2Value = Float.parseFloat(value);

        if (no2Value > NO2_WORSE)
            return STATUS.WORST;
        else if (no2Value > NO2_BAD)
            return STATUS.WORSE;
        else if (no2Value > NO2_NORMAL)
            return STATUS.BAD;
        else if (no2Value > NO2_GOOD)
            return STATUS.NORMAL;

        return STATUS.GOOD;
    }

    /**
     * 일산화탄소 수치가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getCODegree(String value) {
        if (TextUtils.isEmpty(value) || "-".equals(value) || "점검중".equals(value))
            return STATUS.NONE;

        float coValue = Float.parseFloat(value);

        if (coValue > CO_WORSE)
            return STATUS.WORST;
        else if (coValue > CO_BAD)
            return STATUS.WORSE;
        else if (coValue > CO_NORMAL)
            return STATUS.BAD;
        else if (coValue > CO_GOOD)
            return STATUS.NORMAL;

        return STATUS.GOOD;
    }

    /**
     * 아황산가스 수치가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getSO2Degree(String value) {
        if (TextUtils.isEmpty(value) || "-".equals(value) || "점검중".equals(value))
            return STATUS.NONE;

        float so2Value = Float.parseFloat(value);

        if (so2Value > SO2_WORSE)
            return STATUS.WORST;
        else if (so2Value > SO2_BAD)
            return STATUS.WORSE;
        else if (so2Value > SO2_NORMAL)
            return STATUS.BAD;
        else if (so2Value > SO2_GOOD)
            return STATUS.NORMAL;

        return STATUS.GOOD;
    }

    /**
     * 통합대기지수가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getTotalDegree(String value) {
        if (TextUtils.isEmpty(value) || "-".equals(value) || "점검중".equals(value))
            return STATUS.NONE;

        float totalValue = Float.parseFloat(value);

        if (totalValue > TOTAL_DEGREE_WORSE)
            return STATUS.WORST;
        else if (totalValue > TOTAL_DEGREE_BAD)
            return STATUS.WORSE;
        else if (totalValue > TOTAL_DEGREE_NORMAL)
            return STATUS.BAD;
        else if (totalValue > TOTAL_DEGREE_GOOD)
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

    public static String getStatusToString(STATUS status) {
        switch (status) {
            case GOOD:
                return "좋음 :-)";
            case BAD:
                return "약간 안 좋음";
            case WORSE:
                return "나쁨";
            case WORST:
                return "위험!!!";
            default:
                return "보통";
        }
    }

    public static List<DustInfoDto> parseRawXmlString(Context context, String str) {
        DustLog.i(TAG, "parseRawXmlString()");
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(str));

            int eventType = xpp.getEventType();
            List<DustInfoDto> list = new ArrayList<DustInfoDto>();
            String startTagName = null;
            String text = null;
            DustInfoDto dto = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    startTagName = xpp.getName();
                } else if (eventType == XmlPullParser.TEXT) {
                    text = xpp.getText().trim();
                    if (startTagName != null && text != null
                            && !TextUtils.isEmpty(text)) {
                        if (startTagName.equals("msrdate")) {
                            dto = new DustInfoDto();
                            dto.setMesuredDate(splitDateString(text));
                        } else if (startTagName.equals("msrstename")) {
                            dto.setLocality(text);
                        } else if (startTagName.equals("maxindex")) {
                            dto.setMaxIndex(text);
                        } else if (startTagName.equals("pm10")) {
                            dto.setPm10(text);
                        } else if (startTagName.equals("pm10index")) {
                            dto.setPm10Index(text);
                        } else if (startTagName.equals("pm24")) {
                            dto.setPm24(text);
                        } else if (startTagName.equals("pm24index")) {
                            dto.setPm24Index(text);
                        } else if (startTagName.equals("pm25")) {
                            dto.setPm25(text);
                        } else if (startTagName.equals("pm25index")) {
                            dto.setPm25Index(text);
                        } else if (startTagName.equals("ozone")) {
                            dto.setOzone(text);
                        } else if (startTagName.equals("ozoneindex")) {
                            dto.setOzoneIndex(text);
                        } else if (startTagName.equals("nitrogen")) {
                            dto.setNitrogen(text);
                        } else if (startTagName.equals("nitrogenindex")) {
                            dto.setNitrogenIndex(text);
                        } else if (startTagName.equals("carbon")) {
                            dto.setCarbon(text);
                        } else if (startTagName.equals("carbonindex")) {
                            dto.setCarbonIndex(text);
                        } else if (startTagName.equals("sulfurous")) {
                            dto.setSulfurous(text);
                        } else if (startTagName.equals("sulfurousindex")) {
                            dto.setSulfurousIndex(text);
                            list.add(setDegreeAndMaterial(dto));
                        }
                    }
                }
                eventType = xpp.next();
            }

            DustLog.i(TAG, "DustInfoDto length : " + list.size());
            // 지역이름(Locality) 오름차순 정렬
            Collections.sort(list, new DustInfoDtoLocalityAscComparer());

            DustUtils.sendNotification(context, 
                    getTotalDegree(dto.getMaxIndex()),
                    new String[] {dto.getMaterial(), dto.getMaxIndex()});

            return list;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /** 지수등급 및 결정물질 추가 */
    private static DustInfoDto setDegreeAndMaterial(DustInfoDto d) {
        DustInfoDto dto = verifyDtoValues(d);

        if (dto.getMaxIndex() == null || dto.getMaxIndex().equals("null")
                || TextUtils.isEmpty(dto.getMaxIndex())) {
            dto.setMaxIndex(NO_VALUE_TOTAL);
            dto.setDegree(NO_VALUE_TOTAL);
            dto.setMaterial(NO_VALUE_TOTAL);
        } else {
            dto.setDegree(getTotalAirQuality(dto.getMaxIndex()));

            if (dto.getMaxIndex().equals(dto.getPm10Index()) ||
                    dto.getMaxIndex().equals(dto.getPm24Index())) {
                dto.setMaterial(MATERIALS[0]);
            } else if (dto.getMaxIndex().equals(dto.getOzoneIndex())) {
                dto.setMaterial(MATERIALS[1]);
            } else if (dto.getMaxIndex().equals(dto.getNitrogenIndex())) {
                dto.setMaterial(MATERIALS[2]);
            } else if (dto.getMaxIndex().equals(dto.getCarbonIndex())) {
                dto.setMaterial(MATERIALS[3]);
            } else if (dto.getMaxIndex().equals(dto.getSulfurousIndex())) {
                dto.setMaterial(MATERIALS[4]);
            }


        }
        return dto;
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
        dto.setSulfurous(dto.getSulfurous());

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
