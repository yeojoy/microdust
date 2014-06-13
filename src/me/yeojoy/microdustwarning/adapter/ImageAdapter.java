package me.yeojoy.microdustwarning.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import me.yeojoy.microdustwarning.R;

/**
 * Created by yeojoy on 2014. 6. 11..
 */
public class ImageAdapter extends BaseAdapter {

    private static final String TAG = ImageAdapter.class.getSimpleName();

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
        ViewHolder holder;
        if (convertView == null) { // if it's not recycled, initialize some attributes
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.grid_item, null);
            holder.mIvMapImage = (ImageView) convertView.findViewById(R.id.iv_map_image);
            holder.mTvDesc = (TextView) convertView.findViewById(R.id.tv_desc);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        imageView.setImageResource(mUrls[position]); // Load image into ImageView
        if (!TextUtils.isEmpty(mUrls.get(position))) {
            holder.mIvMapImage.setVisibility(View.VISIBLE);
            setImage(holder.mIvMapImage, mUrls.get(position));
        } else
            holder.mIvMapImage.setVisibility(View.INVISIBLE);

        String desc = null;
        switch (position) {
            case 0:
                desc = "오늘 / 미세먼지";
                break;
            case 1:
                desc = "내일 / 미세먼지";
                break;
            case 2:
                desc = "오늘 / 초미세먼지";
                break;
            default:
                desc = "내일 / 초미세먼지";
        }
        holder.mTvDesc.setText(desc);
        return convertView;
    }

    private void setImage(final ImageView view, String url) {
        if (view == null || TextUtils.isEmpty(url)) return;

        // TODO url에 이미지 가져오기
//        06-12 00:03:56.962  21011-21039/me.yeojoy.microdustwarning I/DustFragment﹕ http://www.webairwatch.com/kaq/modelimg/PM10_24H_AVG.09KM.Day1.gif
//        06-12 00:03:56.962  21011-21039/me.yeojoy.microdustwarning I/DustFragment﹕ http://www.webairwatch.com/kaq/modelimg/PM10_24H_AVG.09KM.Day2.gif
//        06-12 00:03:56.962  21011-21039/me.yeojoy.microdustwarning I/DustFragment﹕ http://www.webairwatch.com/kaq/modelimg/PM2_5_24H_AVG.09KM.Day1.gif
//        06-12 00:03:56.962  21011-21039/me.yeojoy.microdustwarning I/DustFragment﹕ http://www.webairwatch.com/kaq/modelimg/PM2_5_24H_AVG.09KM.Day2.gif
        // 사용할 URL

        AsyncTask<String, Void, Bitmap> task = new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap image = null;
                try {
                    InputStream is = new URL(params[0]).openStream();

                    image = BitmapFactory.decodeStream(is);
                    if (is != null)
                        is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return image;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                view.setImageBitmap(bitmap);
            }
        };

        task.execute(url);
    }

    class ViewHolder {
        public TextView mTvDesc;
        public ImageView mIvMapImage;
    }
}
