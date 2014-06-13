package me.yeojoy.microdustwarning.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import me.yeojoy.microdustwarning.DustApplication;
import me.yeojoy.microdustwarning.DustConstants;
import me.yeojoy.microdustwarning.R;
import me.yeojoy.microdustwarning.db.SqliteManager;
import me.yeojoy.microdustwarning.entity.OttoEventEntity;
import me.yeojoy.microdustwarning.util.DustLog;
import me.yeojoy.microdustwarning.util.DustSharedPreferences;
import me.yeojoy.microdustwarning.util.DustUtils;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class WebParserService extends Service implements LocationListener, DustConstants {

    private static final String TAG = WebParserService.class.getSimpleName();

    private LocationManager mLocationManager;

    private Context mContext;

    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        DustLog.i(TAG, "onCreate()");
        mContext = this;

        if (!DustSharedPreferences.getInstance().hasPrefs())
            DustSharedPreferences.getInstance().init(mContext);

        mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        String bestProvider = mLocationManager.getBestProvider(criteria, false);

        if (TextUtils.isEmpty(bestProvider)) {
            Toast.makeText(mContext, R.string.str_no_location, Toast.LENGTH_LONG).show();
            stopSelf();
        }
        mLocationManager.requestLocationUpdates(bestProvider, 0, 0, this);

        mHandler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DustLog.i(TAG, "onStartCommand()");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void parseString(Location location) {
        DustLog.i(TAG, "parseString()");
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> fromLocation = null;
        try {
            fromLocation = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String ADMIN = fromLocation.get(0).getAdminArea();
        if (!"서울특별시".equals(ADMIN)) {
            Toast.makeText(mContext, R.string.can_only_use_in_seoul, Toast.LENGTH_SHORT).show();
            finishService();
            return;
        }

        final String LOCALITY = fromLocation.get(0).getLocality();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final String url = "http://cleanair.seoul.go.kr/air_city.htm?method=measure";
                Source source = null;
                try {
                    source = new Source(new URL(url));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (source == null) {
                    DustLog.e(TAG, "result is null");
                    finishService();
                }

                SqliteManager manager = SqliteManager.getInstance();
                if (!manager.isDoneInit())
                    manager.init(mContext);

                String measureTime = null;

                List<Element> table = source.getAllElements(HTMLElementName.EM);
                for (Element e : table) {
                    if (e.getTextExtractor().toString().endsWith("시")) {
                        measureTime = e.getTextExtractor().toString();
                        break;
                    }
                }

                table.clear();
                table = source.getAllElements(HTMLElementName.TR);

                for (Element e : table) {
                    String rawString = e.getTextExtractor().toString();
                    int firstIndex = rawString.indexOf(" ");
                    rawString = rawString.substring(firstIndex + 1);

                    if (rawString.startsWith(LOCALITY)) {
                        manager.saveData(measureTime, rawString);
                        DustUtils.STATUS[] status = DustUtils.analyzeMicroDust(rawString);
                        DustUtils.sendNotification(mContext, status);

                        // TODO refactoring!
                        DustSharedPreferences.getInstance().putString(KEY_PREFS_MEASURE_TIME, measureTime);
                        DustSharedPreferences.getInstance().putString(KEY_PREFS_RAW_STRING, rawString);

                        sendString(measureTime, rawString);
                        break;
                    }
                }
                finishService();
            }
        };

        new Thread(runnable).start();

    }

    private void sendString(final String measureTime, final String rawString) {
        DustLog.i(TAG, "sendString()");

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                OttoEventEntity entity = new OttoEventEntity(OttoEventEntity.COMMAND.GET_DATA);
                entity.setData(measureTime, rawString);
                DustApplication.bus.post(entity);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DustLog.i(TAG, "onDestroy()");
    }

    @Override
    public void onLocationChanged(Location location) {
        DustLog.i(TAG, "onLocationChanged()");
        parseString(location);
    }

    @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override public void onProviderEnabled(String provider) {}
    @Override public void onProviderDisabled(String provider) {}


    private void finishService() {
        mLocationManager.removeUpdates(this);
        stopSelf();
    }
}
