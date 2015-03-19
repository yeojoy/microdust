package me.yeojoy.microdustwarning.network;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import me.yeojoy.microdustwarning.BuildConfig;
import me.yeojoy.microdustwarning.DustApplication;
import me.yeojoy.microdustwarning.DustConstants;
import me.yeojoy.microdustwarning.db.SqliteManager;
import me.yeojoy.microdustwarning.entity.AllStateDustInfoDto;
import me.yeojoy.microdustwarning.entity.DtoList;
import me.yeojoy.microdustwarning.entity.DustInfoDto;
import me.yeojoy.microdustwarning.util.DustLog;
import me.yeojoy.microdustwarning.util.TextDataUtil;

/**
 * Created by yeojoy on 14. 12. 22..
 */
public class DustNetworkManager implements DustConstants {
    private static final String TAG = DustNetworkManager.class.getSimpleName();
    private static DustNetworkManager mDustNetworkManager;
    
    public static DustNetworkManager getInstance() {
        if (mDustNetworkManager == null)
            mDustNetworkManager = new DustNetworkManager();
        
        return mDustNetworkManager;
    }
    
    public void getMicrodustInfo(final Context context) {
        DustLog.i(TAG, "getMicrodustInfo()");

        OkHttpClient client = new OkHttpClient();

        String url;
        if (BuildConfig.DEBUG) {
            url = String.format(API_MICRO_DUST, DustApplication.locality, API_KEY_FOR_DEV);
        } else {
            url = String.format(API_MICRO_DUST, DustApplication.locality, API_KEY_FOR_REAL);
        }

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                DustLog.i(TAG, "onFailure()\n" + e.getMessage());

                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                DustLog.i(TAG, "onResponse()");

                if (response.body() == null) {
                    Toast.makeText(context, "데이터 body가 없습니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Gson gson = new Gson();

//                List<DustInfoDto> dtoList = TextDataUtil.parseRawXmlString(context,
//                        response.body().string());

                DtoList dtoList = gson.fromJson(response.body().string(),
                        DtoList.class);
                // DB에 저장
                SqliteManager.getInstance(context).saveData(dtoList);

                mOnReceiveDataListener.onReceiveData(dtoList);
            }
        });
    }

    public interface OnReceiveDataListener {
        public void onReceiveData(List<DustInfoDto> data);
        public void onReceiveData(DtoList data);
    }

    public void setOnReceiveDataListener(OnReceiveDataListener onReceiveDataListener) {
        this.mOnReceiveDataListener = onReceiveDataListener;
    }

    private OnReceiveDataListener mOnReceiveDataListener;
    
    
}
