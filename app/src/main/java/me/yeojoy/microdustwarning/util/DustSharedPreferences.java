package me.yeojoy.microdustwarning.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by yeojoy on 2014. 6. 11..
 */
public class DustSharedPreferences {

    private static DustSharedPreferences instance;
    private SharedPreferences mPrefs;

    public static DustSharedPreferences getInstance(Context context) {
        if (instance == null)
            instance = new DustSharedPreferences(context);
        return instance;
    }

    public DustSharedPreferences(Context context) {
        if (mPrefs == null)
            mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    public void putString(String key, String value) {
        mPrefs.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return mPrefs.getString(key, defaultValue);
    }

    public String getString(String key) {
        return mPrefs.getString(key, null);
    }

    public void putBoolean(String key, boolean value) {
        mPrefs.edit().putBoolean(key, value).apply();
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
    public void clear() {
        mPrefs.edit().clear().apply();
    }
}
