package me.yeojoy.microdustwarning;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.squareup.otto.Bus;

import java.util.HashMap;

import me.yeojoy.microdustwarning.service.WebParserService;
import me.yeojoy.microdustwarning.util.DustSharedPreferences;

/**
 * Created by yeojoy on 2014. 6. 12..
 */
public class DustApplication extends Application implements DustConstants {
    public static Bus bus;
    
    public static String locality;

    public static boolean mIsEnabledDoNotBother;
    
    public static boolean mIsOnGoing;
    
    /* GA */
    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     *
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
//                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker)
//                    : analytics.newTracker(R.xml.ecommerce_tracker);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(getString(R.string.ga_property_id))
                    : analytics.newTracker(R.xml.global_tracker);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        bus = new Bus();

        mIsEnabledDoNotBother = DustSharedPreferences.getInstance(this)
                .getBoolean(KEY_PREFS_IS_ENABLED_DO_NOT_BOTHER, true);
        mIsOnGoing = DustSharedPreferences.getInstance(this)
                .getBoolean(KEY_PREFS_IS_ON_GOING, true);
    }
}
