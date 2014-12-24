package me.yeojoy.microdustwarning.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import me.yeojoy.microdustwarning.util.DustLog;

/**
 * Created by yeojoy on 14. 12. 24..
 */
public class DustContentProvider extends ContentProvider implements 
        DustInfoDBConstants {
    private static final String TAG = DustContentProvider.class.getSimpleName();
    // Indicates that the incoming query is for a URL air quality index
    public static final int URL_DATA_QUERY = 1;

    // Defines a helper object that matches content URIs to table-specific parameters
    private static final UriMatcher sUriMatcher;

    // Defines an helper object for the backing database
    private DustInfoDBHelper mHelper;
    private SQLiteDatabase mDb;

    static {
        // Creates an object that associates content URIs with numeric codes
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, URL_DATA_QUERY);
    }
    
    @Override
    public boolean onCreate() {
        DustLog.i(TAG, "onCreate()");
        mHelper = new DustInfoDBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        DustLog.i(TAG, "query()");
        mDb = mHelper.getWritableDatabase();
        return mDb.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        DustLog.i(TAG, "getType()");
        if (sUriMatcher.match(uri) == URL_DATA_QUERY) {
            return CONTENT_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        DustLog.i(TAG, "insert()");
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        DustLog.i(TAG, "delete()");
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        DustLog.i(TAG, "update()");
        return 0;
    }
}
