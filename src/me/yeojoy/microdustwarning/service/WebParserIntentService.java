package me.yeojoy.microdustwarning.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import my.lib.MyLog;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import android.app.IntentService;
import android.content.Intent;

public class WebParserIntentService extends IntentService {

    private static final String TAG = WebParserIntentService.class.getSimpleName();
    
    public WebParserIntentService() {
        super("WebParserIntentService");
    }
    
    public WebParserIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MyLog.i(TAG, "onHandleIntent()");
        
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
            MyLog.i(TAG, "result is null");
            return;
        }
        
        List<Element> table = source.getAllElements(HTMLElementName.TR);
        
        for (Element e : table) {
            MyLog.d(TAG, "String : " + e.getTextExtractor().toString());
        }
    }

}
