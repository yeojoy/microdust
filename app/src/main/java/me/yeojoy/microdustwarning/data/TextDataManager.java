package me.yeojoy.microdustwarning.data;

import android.content.Context;

/**
 * Created by yeojoy on 14. 12. 29..
 */
public class TextDataManager {

    private static final String TAG = TextDataManager.class.getSimpleName();

    private static TextDataManager mManager;
    private static Context mContext;
    
    public static TextDataManager getInstance(Context context) {
        if (mManager == null)
            mManager = new TextDataManager();
        
        mContext = context;
        
        return mManager;
    }
}
