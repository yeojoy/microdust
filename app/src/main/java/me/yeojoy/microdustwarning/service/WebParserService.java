package me.yeojoy.microdustwarning.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.util.List;

import me.yeojoy.microdustwarning.DustApplication;
import me.yeojoy.microdustwarning.DustConstants;
import me.yeojoy.microdustwarning.entity.DtoList;
import me.yeojoy.microdustwarning.entity.DustInfoDto;
import me.yeojoy.microdustwarning.entity.OttoEventEntity;
import me.yeojoy.microdustwarning.network.DustNetworkManager;
import me.yeojoy.microdustwarning.util.DustLog;
import me.yeojoy.microdustwarning.util.DustSharedPreferences;

public class WebParserService extends Service implements DustConstants,
        DustNetworkManager.OnReceiveDataListener {

    private static final String TAG = WebParserService.class.getSimpleName();

    private Context mContext;

    private Handler mHandler;

    private DustNetworkManager mNetworkManager;
    
    @Override
    public void onCreate() {
        super.onCreate();
        DustLog.i(TAG, "onCreate()");
        mContext = this;

        mHandler = new Handler(getMainLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DustLog.i(TAG, "onStartCommand()");
        if (mNetworkManager == null)
            mNetworkManager = DustNetworkManager.getInstance();

        mNetworkManager.setOnReceiveDataListener(this);
        mNetworkManager.getMicrodustInfo(mContext);
        
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /** Otto를 사용해서 DustFragment로 데이터를 보내줌 */
    private void sendMeasuredData(final DtoList dto) {
        DustLog.i(TAG, "sendMeasuredData()");

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                OttoEventEntity entity = new OttoEventEntity(
                        OttoEventEntity.COMMAND.GET_DATA_WITH_DTO);
                entity.setData(dto);
                DustApplication.bus.post(entity);

                stopSelf();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DustLog.i(TAG, "onDestroy()");
    }

    @Override
    public void onReceiveData(DtoList data) {
        DustLog.i(TAG, "onReceiveData()");
        if (data == null) {
            DustLog.i(TAG, "onReceiveData(), data is null.");
            return;
        }
        
        if (data.getList().size() < 1) {
            DustLog.i(TAG, "onReceiveData(), data is null. or data's size is under 1");
            return;
        }
        sendMeasuredData(data);
    }
}
