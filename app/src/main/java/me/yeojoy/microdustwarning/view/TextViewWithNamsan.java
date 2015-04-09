package me.yeojoy.microdustwarning.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import me.yeojoy.microdustwarning.R;

/**
 * Created by yeojoy on 15. 3. 25..
 * Refs
 * http://www.techrepublic.com/article/pro-tip-extend-androids-textview-to-use-custom-fontsTex
 *
 * <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
 *     xmlns:cfont="http://schemas.android.com/apk/res-auto"
 *     android:layout_width="match_parent"
 *     android:layout_height="match_parent"
 *     android:orientation="vertical"
 *     android:gravity="center">
 *
 *     <TextView
 *     android:layout_width="wrap_content"
 *     android:layout_height="wrap_content"
 *     android:textSize="24sp"
 *     android:padding="12dp"
 *     android:text="Standard Android Font" />
 *
 *     <com.authorwjf.cfont.MyTextView
 *     android:layout_width="wrap_content"
 *     android:layout_height="wrap_content"
 *     android:textSize="32sp"
 *     android:padding="12dp"
 *     cfont:fontName="pipe_dream.ttf"
 *     android:text="Custom Android Font" />
 *
 * </LinearLayout>
 *
 */
public class TextViewWithNamsan extends TextView {

    private static final String TAG = TextViewWithNamsan.class.getSimpleName();

    private Context mContext;

    public TextViewWithNamsan(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public TextViewWithNamsan(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TextViewWithNamsan(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                    R.styleable.CustomTextView);
            String fontName
                    = typedArray.getString(R.styleable.CustomTextView_fontName);

            if (fontName != null) {
                Typeface myTypeface
                        = Typeface.createFromAsset(context.getAssets(),
                        "fonts/" + fontName);
                setTypeface(myTypeface);
            }
            typedArray.recycle();
        }
    }
}
