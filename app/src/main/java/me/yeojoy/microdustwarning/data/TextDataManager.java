package me.yeojoy.microdustwarning.data;

import android.content.Context;

/**
 * Created by yeojoy on 14. 12. 29..
 */
public class TextDataManager {
    
    private TextDataManager mManager;
    private Context mContext;
    
    public TextDataManager getInstance(Context context) {
        if (mManager == null)
            mManager = new TextDataManager();
        
        mContext = context;
        
        return mManager;
    }
}
