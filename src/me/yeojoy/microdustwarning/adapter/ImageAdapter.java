package me.yeojoy.microdustwarning.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by yeojoy on 2014. 6. 11..
 */
public class ImageAdapter extends BaseAdapter {
    private final Context mContext;
    private String[] mUrls;
    public ImageAdapter(Context context, String[] urls) {
        super();
        mContext = context;
        mUrls = urls;
    }

    @Override
    public int getCount() {
        return mUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return mUrls[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        ImageView imageView;
        if (convertView == null) { // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new GridView.LayoutParams(
                    GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.MATCH_PARENT));
        } else {
            imageView = (ImageView) convertView;
        }
//        imageView.setImageResource(mUrls[position]); // Load image into ImageView

        return imageView;
    }

    private void setImage(ImageView view, String url) {

    }
}
