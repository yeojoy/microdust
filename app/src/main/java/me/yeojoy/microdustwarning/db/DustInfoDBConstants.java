package me.yeojoy.microdustwarning.db;

import android.net.Uri;
import android.provider.BaseColumns;

import me.yeojoy.microdustwarning.BuildConfig;

public interface DustInfoDBConstants {
    public static final String DB_NAME = "dust_info.db";
    public static final int DB_VERSION = 8;

    public static final String TABLE_NAME = "dust_info";
    
    public static final String ID = BaseColumns._ID;
    public static final String SAVE_TIME = "save_time";
    public static final String MEASURE_TIME = "measure_time";
    public static final String MEASURE_LOCALITY = "locality";
    public static final String MICRO_DUST = "micro_dust";
    public static final String MICRO_DUST_INDEX = "micro_dust_index";
    public static final String NANO_DUST = "nano_dust";
    public static final String OZON = "ozon";
    public static final String OZON_INDEX = "ozon_index";
    public static final String NO2 = "no2";
    public static final String NO2_INDEX = "no2_index";
    public static final String CO = "co";
    public static final String CO_INDEX = "co_index";
    public static final String SO2 = "so2";
    public static final String SO2_INDEX = "so2_index";
    public static final String DEGREE = "degree";
    public static final String AIR_QUAL_INDEX = "air_index";
    public static final String MATERIAL = "material";

    // CursorLoader
    public static final int AIR_QUALITY_INDEX_CURSOR_LOADER = 1212;

    public static final String[] PROJECTION = {
            ID,
            SAVE_TIME,
            MEASURE_TIME,
            MEASURE_LOCALITY,
            MICRO_DUST,
            MICRO_DUST_INDEX,
            NANO_DUST,
            OZON,
            OZON_INDEX,
            NO2,
            NO2_INDEX,
            CO,
            CO_INDEX,
            SO2,
            SO2_INDEX,
            DEGREE,
            AIR_QUAL_INDEX,
            MATERIAL
    };

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
