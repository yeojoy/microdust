package me.yeojoy.microdustwarning.network;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import me.yeojoy.microdustwarning.DustApplication;
import me.yeojoy.microdustwarning.entity.DustInfoDto;
import me.yeojoy.microdustwarning.entity.OttoEventEntity;
import me.yeojoy.microdustwarning.util.DustFileLogger;
import me.yeojoy.microdustwarning.util.DustLog;
import me.yeojoy.microdustwarning.util.DustSharedPreferences;

/**
 * Created by yeojoy on 14. 12. 22..
 */
public class NetworkManager {
    private static final String TAG = NetworkManager.class.getSimpleName();
    private static NetworkManager mNetworkManager;
    
    private Context mContext;
    
    public NetworkManager getInstance(Context context) {
        if (mNetworkManager == null)
            mNetworkManager = new NetworkManager();
        
        init(context);
        return mNetworkManager;
    }
    
    private void init(Context context) {
        mContext = context;

        if (!DustSharedPreferences.getInstance().hasPrefs())
            DustSharedPreferences.getInstance().init(mContext);
    }

    private void getMicrodustInfo() {
        DustLog.i(TAG, "getMicrodustInfo()");

        final String url = "http://cleanair.seoul.go.kr/air_city.htm?method=airPollutantInfoMeasureXml&msrntwCode=A";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
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
                    Toast.makeText(mContext, "데이터 body가 없습니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                parseRawXmlString(response.body().string());
            }
        });
    }
    
    private void parseRawXmlString(String str) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            DustLog.i(TAG, "Respons String : " + str);
            DustFileLogger.getInstance().writeLogToFile("Respons Body String.");
            DustFileLogger.getInstance().writeLogToFile(str);

            xpp.setInput(new StringReader(str));

            int eventType = xpp.getEventType();
            List<DustInfoDto> list = new ArrayList<DustInfoDto>();
            String startTagName = null;
            String text = null;
            DustInfoDto dto = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    startTagName = xpp.getName();
                } else if (eventType == XmlPullParser.TEXT) {
                    text = xpp.getText().trim();
                    if (startTagName != null && text != null
                            && !TextUtils.isEmpty(text)) {

                        if (startTagName.equals("msrdate")) {
                            dto = new DustInfoDto();
                            dto.setDate(text);
                        } else if (startTagName.equals("msrstename")) {
                            dto.setLocality(text);
                        } else if (startTagName.equals("maxindex")) {
                            dto.setMaxIndex(text);
                        } else if (startTagName.equals("pm10")) {
                            dto.setPm10(text);
                        } else if (startTagName.equals("pm10index")) {
                            dto.setPm10Index(text);
                        } else if (startTagName.equals("pm24")) {
                            dto.setPm24(text);
                        } else if (startTagName.equals("pm24index")) {
                            dto.setPm24Index(text);
                        } else if (startTagName.equals("pm25")) {
                            dto.setPm25(text);
                        } else if (startTagName.equals("pm25index")) {
                            dto.setPm25Index(text);
                        } else if (startTagName.equals("ozone")) {
                            dto.setOzone(text);
                        } else if (startTagName.equals("ozoneindex")) {
                            dto.setOzoneIndex(text);
                        } else if (startTagName.equals("nitrogen")) {
                            dto.setNitrogen(text);
                        } else if (startTagName.equals("nitrogenindex")) {
                            dto.setNitrogenIndex(text);
                        } else if (startTagName.equals("carbon")) {
                            dto.setCarbon(text);
                        } else if (startTagName.equals("carbonindex")) {
                            dto.setCarbonIndex(text);
                        } else if (startTagName.equals("sulfurous")) {
                            dto.setSulfurous(text);
                        } else if (startTagName.equals("sulfurousindex")) {
                            dto.setSulfurousIndex(text);
                            list.add(dto);
                        }
                    }
                }
                eventType = xpp.next();
            }

            DustLog.i(TAG, "DustInfoDto length : " + list.size());
            for (DustInfoDto d : list) {
                DustLog.d("TAG", "DTO : " + d);
            }

            sendMeasuredData(list);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    /** Otto를 사용해서 DustFragment로 데이터를 보내줌 */
    private void sendMeasuredData(final List<DustInfoDto> dto) {
        DustLog.i(TAG, "sendMeasuredData()");
        DustFileLogger.getInstance().writeLogToFile("Send List<DustInfoDto> to Fragment.");
        
        OttoEventEntity entity = new OttoEventEntity(OttoEventEntity.COMMAND.GET_DATA_WITH_DTO);
        entity.setData(dto);
        DustApplication.bus.post(entity);
        
    }
}
