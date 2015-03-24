package me.yeojoy.microdustwarning.db;

import android.net.Uri;
import android.provider.BaseColumns;

import me.yeojoy.microdustwarning.BuildConfig;

public interface DustInfoDBConstants {
    public static final String DB_NAME = "dust_info.db";
    public static final int DB_VERSION = 10;

    public static final String TABLE_NAME = "DUST_INFO";
    
    public static final String ID               = BaseColumns._ID;
    public static final String SAVE_TIME        = "SAVE_TIME";
    public static final String SIDO_NAME        = "sidoName";
    public static final String CO_GRADE         = "coGrade";
    public static final String NO2_GRADE        = "no2Grade";
    public static final String O3_GRADE         = "o3Grade";
    public static final String PM10_GRADE       = "pm10Grade";
    public static final String SO2_GRADE        = "so2Grade";
    public static final String KHAI_GRADE       = "khaiGrade";
    public static final String CO_VALUE         = "coValue";
    public static final String NO2_VALUE        = "no2Value";
    public static final String O3_VALUE         = "o3Value";
    public static final String PM10_VALUE       = "pm10Value";
    public static final String SO2_VALUE        = "so2Value";
    public static final String KHAI_VALUE       = "khaiValue";
    public static final String NUM_OF_ROWS      = "numOfRows";
    public static final String ROW_NUM          = "rnum";
    public static final String RESULT_CODE      = "resultCode";
    public static final String RESULT_MESSAGE   = "resultMsg";
    public static final String SERVICE_KEY      = "serviceKey";
    public static final String PAGE_NO          = "pageNo";
    public static final String DATA_TERM        = "dataTerm";
    public static final String DATA_TIME        = "dataTime";
    public static final String STATION_CODE     = "stationCode";
    public static final String STATION_NAME     = "stationName";
    public static final String TOTAL_COUNT      = "totalCount";

    public static final int INDEX_ID               = 0;
    public static final int INDEX_SAVE_TIME        = 1;
    public static final int INDEX_SIDO_NAME        = 2;
    public static final int INDEX_CO_GRADE         = 3;
    public static final int INDEX_NO2_GRADE        = 4;
    public static final int INDEX_O3_GRADE         = 5;
    public static final int INDEX_PM10_GRADE       = 6;
    public static final int INDEX_SO2_GRADE        = 7;
    public static final int INDEX_KHAI_GRADE       = 8;
    public static final int INDEX_CO_VALUE         = 9;
    public static final int INDEX_NO2_VALUE        = 10;
    public static final int INDEX_O3_VALUE         = 11;
    public static final int INDEX_PM10_VALUE       = 12;
    public static final int INDEX_SO2_VALUE        = 13;
    public static final int INDEX_KHAI_VALUE       = 14;
    public static final int INDEX_NUM_OF_ROWS      = 15;
    public static final int INDEX_ROW_NUM          = 16;
    public static final int INDEX_RESULT_CODE      = 17;
    public static final int INDEX_RESULT_MESSAGE   = 18;
    public static final int INDEX_SERVICE_KEY      = 19;
    public static final int INDEX_PAGE_NO          = 20;
    public static final int INDEX_DATA_TERM        = 21;
    public static final int INDEX_DATA_TIME        = 22;
    public static final int INDEX_STATION_CODE     = 23;
    public static final int INDEX_STATION_NAME     = 24;
    public static final int INDEX_TOTAL_COUNT      = 25;

    // CursorLoader
    public static final int AIR_QUALITY_INDEX_CURSOR_LOADER = 1212;

    public static final String[] PROJECTION = {

    };

    public static final String SELECTION = INDEX_SIDO_NAME + " = ?";

    // The URI scheme used for content URIs
    public static final String SCHEME = "content";

    // The provider's authority
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID;

    /**
     * The DataProvider content URI
     */
    public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);

    public static final Uri AIR_QUALITY_SELECT_ALL_QUERY_URI =
            Uri.withAppendedPath(CONTENT_URI, TABLE_NAME);

    public static final String CONTENT_TYPE = "vnd.microdustwarning.yeojoy.me.cursor.dir/" + TABLE_NAME;
}
