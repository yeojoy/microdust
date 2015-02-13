package me.yeojoy.microdustwarning.db;

import android.net.Uri;
import android.provider.BaseColumns;

import me.yeojoy.microdustwarning.BuildConfig;

public interface DustInfoDBConstants {
    public static final String DB_NAME = "dust_info.db";
    public static final int DB_VERSION = 8;

    public static final String TABLE_NAME = "DUST_INFO";
    
    public static final String ID = BaseColumns._ID;
    public static final String SAVE_TIME = "SAVE_TIME";
    public static final String MEASURE_TIME = "MEASURE_TIME";
    public static final String MEASURE_LOCALITY = "LOCALITY";
    public static final String MICRO_DUST = "PM10";
    public static final String MICRO_DUST_INDEX = "PM10_INDEX";
    public static final String MICRO_DUST_PM24 = "PM24";
    public static final String MICRO_DUST_PM24_INDEX = "PM24_INDEX";
    public static final String NANO_DUST = "PM25";
    public static final String OZON = "OZON";
    public static final String OZON_INDEX = "OZON_INDEX";
    public static final String NO2 = "NO2";
    public static final String NO2_INDEX = "NO2_INDEX";
    public static final String CO = "CO";
    public static final String CO_INDEX = "CO_INDEX";
    public static final String SO2 = "SO2";
    public static final String SO2_INDEX = "SO2_INDEX";
    public static final String DEGREE = "DEGREE";
    public static final String AIR_QUAL_INDEX = "AIR_INDEX";
    public static final String MATERIAL = "MATERIAL";

    public static final int INDEX_ID                        = 0;
    public static final int INDEX_SAVE_TIME                 = 1;
    public static final int INDEX_MEASURE_TIME              = 2;
    public static final int INDEX_MEASURE_LOCALITY          = 3;
    public static final int INDEX_MICRO_DUST                = 4;
    public static final int INDEX_MICRO_DUST_INDEX          = 5;
    public static final int INDEX_MICRO_DUST_PM24           = 6;
    public static final int INDEX_MICRO_DUST_PM24_INDEX     = 7;
    public static final int INDEX_NANO_DUST                 = 8;
    public static final int INDEX_OZON                      = 9;
    public static final int INDEX_OZON_INDEX                = 10;
    public static final int INDEX_NO2                       = 11;
    public static final int INDEX_NO2_INDEX                 = 12;
    public static final int INDEX_CO                        = 13;
    public static final int INDEX_CO_INDEX                  = 14;
    public static final int INDEX_SO2                       = 15;
    public static final int INDEX_SO2_INDEX                 = 16;
    public static final int INDEX_DEGREE                    = 17;
    public static final int INDEX_AIR_QUAL_INDEX            = 18;
    public static final int INDEX_MATERIAL                  = 19;

    // CursorLoader
    public static final int AIR_QUALITY_INDEX_CURSOR_LOADER = 1212;

    public static final String[] PROJECTION = {
            ID,
            SAVE_TIME,
            MEASURE_TIME,
            MEASURE_LOCALITY,
            MICRO_DUST,
            MICRO_DUST_INDEX,
            MICRO_DUST_PM24,
            MICRO_DUST_PM24_INDEX,
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

    public static final String SELECTION = MEASURE_LOCALITY + " = ?";

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
