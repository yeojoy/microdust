package me.yeojoy.microdustwarning.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import me.yeojoy.microdustwarning.DustApplication;
import me.yeojoy.microdustwarning.DustConstants;
import me.yeojoy.microdustwarning.entity.DustInfoDto;
import me.yeojoy.microdustwarning.entity.OttoEventEntity;
import me.yeojoy.microdustwarning.util.DustFileLogger;
import me.yeojoy.microdustwarning.util.DustLog;
import me.yeojoy.microdustwarning.util.DustSharedPreferences;
import me.yeojoy.microdustwarning.util.DustUtils;

public class WebParserService extends Service implements DustConstants {

    private static final String TAG = WebParserService.class.getSimpleName();

    private Context mContext;

    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        DustLog.i(TAG, "onCreate()");
        mContext = this;

        DustFileLogger.getInstance().init(mContext);
        DustFileLogger.getInstance().writeLogToFile("Service starts.");

        if (!DustSharedPreferences.getInstance().hasPrefs())
            DustSharedPreferences.getInstance().init(mContext);

        mHandler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DustLog.i(TAG, "onStartCommand()");
        getMicrodustInfo();
        
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void getMicrodustInfo() {
        DustLog.i(TAG, "getMicrodustInfo()");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(CLEAN_AIR_API_ADDRESS).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                DustLog.i(TAG, "onFailure()");
                Toast.makeText(mContext, "데이터를 가져오는 데 실패했습니다.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                DustLog.i(TAG, "onResponse()");

                if (response.body() == null) {
                    Toast.makeText(mContext, "데이터 내용이 없습니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                sendMeasuredData(DustUtils.parseRawXmlString(response.body().string()));
            }
        });
    }

    /** Otto를 사용해서 DustFragment로 데이터를 보내줌 */
    private void sendMeasuredData(final List<DustInfoDto> dto) {
        DustLog.i(TAG, "sendMeasuredData()");
        DustFileLogger.getInstance().writeLogToFile("Send List<DustInfoDto> to Fragment.");

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                OttoEventEntity entity = new OttoEventEntity(OttoEventEntity.COMMAND.GET_DATA_WITH_DTO);
                entity.setData(dto);
                DustApplication.bus.post(entity);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DustLog.i(TAG, "onDestroy()");
        DustFileLogger.getInstance().writeLogToFile("Service destroies.");
    }
}
