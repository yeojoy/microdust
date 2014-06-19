package me.yeojoy.microdustwarning;

public interface DustConstants {

    /** 서울외 지역의 경우 airkorea 정보로 보여준다. */
    public static final String AIR_KOREA = "http://m.airkorea.or.kr/getAddr.do?dmX=%.6f&dmY=%.6f";

    /** 자동으로 NOTI를 알려주는 시간 간격 */
    public static final int NOTI_TIME_TEST = 1000 * 60 * 2;     // 2분
    public static final int NOTI_TIME_REAL = 1000 * 60 * 60;    // 1시간

    /** Bundle key */
    public static final String KEY_CHECKBOX_AUTO_START = "auto_start";

    /** SharedPreferences key */
    public static final String KEY_PREFS_SWITCH_OFF = "switch";

    public static final String KEY_PREFS_RAW_STRING = "raw_string";
    public static final String KEY_PREFS_MEASURE_TIME = "measure_time";

    public static final String KEY_PREFS_NOTICE_VIBRATE = "notice_vibrate";

    public static final String[] KEY_PREFS_IMAGES = {
        "image01", "image02", "image03", "image04"
    };

    /** About */
    public static final String DEBUGGABLE = "D";
    public static final String RELEASE = "R";

    /** Developer's Email */
    public static final String EMAIL_ADDR = "kim.yeojoy@gmail.com";
    public static final String EMAIL_SUBJECT = "이건 어떨까요?";

    /** Title */
    public static final String INFOMATION_TITLE = "Infomation";
    public static final String THANKS_TO_TITLE = "Thanks to...";

    // 미세먼지 수치
    /** 0 ~ 30 까지 좋음 */
    public static final float MICRO_DUST_GOOD = 30f;
    /** 31 ~ 80 까지 보통 */
    public static final float MICRO_DUST_NORMAL = 80f;
    /** 81 ~ 120 까지 약간나쁨 */
    public static final float MICRO_DUST_BAD = 120f;
    /** 121 ~ 200 까지 나쁨, 201이상 매우나쁨 */
    public static final float MICRO_DUST_WORSE = 200f;
    
    // 초미세먼지 수치
    /** 0 ~ 20 까지 좋음 */
    public static final float NANO_DUST_GOOD = 20f;
    /** 21 ~ 40 까지 보통 */
    public static final float NANO_DUST_NORMAL = 40f;
    /** 41 ~ 60 까지 약간나쁨 */
    public static final float NANO_DUST_BAD = 60f;
    /** 61 ~ 80 까지 나쁨, 81이상 매우나쁨 */
    public static final float NANO_DUST_WORSE = 80f;
    
    // 오존 수치
    /** 0 ~ 0.04 까지 좋음 */
    public static final float O3_GOOD = 0.04f;
    /** 0.041 ~ 0.08 까지 보통 */
    public static final float O3_NORMAL = 0.08f;
    /** 0.081 ~ 0.12 까지 약간나쁨 */
    public static final float O3_BAD = 0.12f;
    /** 0.121 ~ 0.3 까지 나쁨, 0.301이상 매우나쁨 */
    public static final float O3_WORSE = 0.3f;
    
    // 이산화질소 수치
    /** 0 ~ 0.03 까지 좋음 */
    public static final float NO2_GOOD = 0.03f;
    /** 0.031 ~ 0.06 까지 보통 */
    public static final float NO2_NORMAL = 0.06f;
    /** 0.061 ~ 0.15 까지 약간나쁨 */
    public static final float NO2_BAD = 0.15f;
    /** 0.151 ~ 0.2 까지 나쁨, 0.201이상 매우나쁨 */
    public static final float NO2_WORSE = 0.2f;
    
    // 일산화탄소 수치
    /** 0 ~ 2 까지 좋음 */
    public static final float CO_GOOD = 2f;
    /** 2.01 ~ 9 까지 보통 */
    public static final float CO_NORMAL = 9f;
    /** 9.01 ~ 12 까지 약간나쁨 */
    public static final float CO_BAD = 12f;
    /** 12.01 ~ 15 까지 나쁨, 15.01이상 매우나쁨 */
    public static final float CO_WORSE = 15f;
    
    // 아황산가스 수치
    /** 0 ~ 0.02 까지 좋음 */
    public static final float SO2_GOOD = 0.02f;
    /** 0.021 ~ 0.05 까지 보통 */
    public static final float SO2_NORMAL = 0.05f;
    /** 0.051 ~ 0.1 까지 약간나쁨 */
    public static final float SO2_BAD = 0.1f;
    /** 0.101 ~ 0.15 까지 나쁨, 0.151이상 매우나쁨 */
    public static final float SO2_WORSE = 0.15f;
    
    // 통합대기지수
    /** 0 ~ 50 까지 좋음 */
    public static final float TOTAL_DEGREE_GOOD = 50f;
    /** 51 ~ 100 까지 보통 */
    public static final float TOTAL_DEGREE_NORMAL = 100f;
    /** 100 ~ 150 까지 약간나쁨 */
    public static final float TOTAL_DEGREE_BAD = 150f;
    /** 151 ~ 250 까지 나쁨, 251이상 매우나쁨 */
    public static final float TOTAL_DEGREE_WORSE = 250f;
}
