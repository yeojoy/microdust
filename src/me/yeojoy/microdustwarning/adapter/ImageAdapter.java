package me.yeojoy.microdustwarning.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by yeojoy on 2014. 6. 11..
 */
public class ImageAdapter extends BaseAdapter {
    private final Context mContext;
    private ArrayList<String> mUrls;
    public ImageAdapter(Context context, ArrayList<String> urls) {
        super();
        mContext = context;
        mUrls = urls;
    }

    @Override
    public int getCount() {
        return mUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return mUrls.get(position);
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
        setImage(imageView, mUrls.get(position));

        return imageView;
    }

    private void setImage(ImageView view, String url) {
        if (view == null || TextUtils.isEmpty(url)) return;

        // TODO url에 이미지 가져오기
//        06-12 00:03:56.962  21011-21039/me.yeojoy.microdustwarning I/DustFragment﹕ http://www.webairwatch.com/kaq/modelimg/PM10_24H_AVG.09KM.Day1.gif
//        06-12 00:03:56.962  21011-21039/me.yeojoy.microdustwarning I/DustFragment﹕ http://www.webairwatch.com/kaq/modelimg/PM10_24H_AVG.09KM.Day2.gif
//        06-12 00:03:56.962  21011-21039/me.yeojoy.microdustwarning I/DustFragment﹕ http://www.webairwatch.com/kaq/modelimg/PM2_5_24H_AVG.09KM.Day1.gif
//        06-12 00:03:56.962  21011-21039/me.yeojoy.microdustwarning I/DustFragment﹕ http://www.webairwatch.com/kaq/modelimg/PM2_5_24H_AVG.09KM.Day2.gif
        // 사용할 URL
    }
}
