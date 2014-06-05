package me.yeojoy.microdustwarning.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import me.yeojoy.microdustwarning.db.SqliteManager;
import me.yeojoy.microdustwarning.util.DustUtils;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

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
        Log.i(TAG, "onHandleIntent()");
        
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
            Log.i(TAG, "result is null");
            return;
        }
        
        SqliteManager manager = SqliteManager.getInstance();
        if (!manager.isDoneInit())
            manager.init(getApplicationContext());
        
        String mesureTime = null;
        
        List<Element> table = source.getAllElements(HTMLElementName.EM);
        for (Element e : table) {
            if (e.getTextExtractor().toString().endsWith("시")) {
                mesureTime = e.getTextExtractor().toString();
                break;
            }
        }
        
        table.clear();
        table = source.getAllElements(HTMLElementName.TR);
        
        for (Element e : table) {
            String parsedString = e.getTextExtractor().toString();
            int firstIndex = parsedString.indexOf(" ");
            parsedString = parsedString.substring(firstIndex + 1);
            Log.d(TAG, "String : " + parsedString);
            
            if (parsedString.startsWith("동작구")) {
                manager.saveData(mesureTime, parsedString);
                DustUtils.analyzeMicroDust(parsedString);
            }
        }
    }
}
