package me.yeojoy.microdustwarning.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yeojoy on 2014. 6. 11..
 */
public class DustSharedPreferences {

    private static final String SHARED_PREFS_NAME = "dust";

    private static DustSharedPreferences instance;
    private SharedPreferences mPrefs;

    public static DustSharedPreferences getInstance() {
        if (instance == null)
            instance = new DustSharedPreferences();
        return instance;
    }

    public boolean hasPrefs() {
        return mPrefs != null;
    }

    public void init(Context context) {
        if (mPrefs == null)
            mPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean putString(String key, String value) {
        return mPrefs.edit().putString(key, value).commit();
    }

    public String getString(String key, String defaultValue) {
        return mPrefs.getString(key, defaultValue);
    }

    public boolean putBoolean(String key, boolean value) {
        return mPrefs.edit().putBoolean(key, value).commit();
    }

    /**
     * boolean 값 가져오기
     *
     * @param key
     * @return default false
     */
    public boolean getBoolean(String key) {
        return mPrefs.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mPrefs.getBoolean(key, defaultValue);
    }

    /**
     * SharedPreferences clear
     * @return
     */
    public boolean clear() {
        return mPrefs.edit().clear().commit();
    }
}
