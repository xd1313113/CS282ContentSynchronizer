package com.dixiao.enhancedcpdemo;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class DownloadContentProvider extends ContentProvider {

	// Used for the UriMacher
	private static final int IMAGES = 10;
	private static final int IMAGES_ID = 20;

	public static final String AUTHORITY = "com.dixiao.enhancedcpdemo.DownloadContentProvider";

	private static final String BASE_PATH = "images";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/images";

	public static final String _ID = "_id", _NAME = "_name", _URI = "_uri",
			_CREATETIME = "_createtime";
	//private static final String[] columns = new String[] { _ID, _NAME, _URI,_CREATETIME };

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, IMAGES);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", IMAGES_ID);
	}

	private MySQLiteHelper mDB;

	@Override
	public boolean onCreate() {
		
		mDB = new MySQLiteHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		
		switch (sURIMatcher.match(uri)) {
		case IMAGES:
			return CONTENT_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MySQLiteHelper.TABLE_NAME);
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
        case IMAGES_ID:
            queryBuilder.appendWhere(MySQLiteHelper.COLUMN_ID + "="
                    + uri.getLastPathSegment());
            break;
        case IMAGES:
            break;
        default:
            throw new IllegalArgumentException("Unknown URI");
        }
        Cursor cursor = queryBuilder.query(mDB.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		
		int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = mDB.getWritableDatabase();
	    int rowsDeleted = 0;
	    switch (uriType) {
	    case IMAGES:
	      rowsDeleted = sqlDB.delete(MySQLiteHelper.TABLE_NAME, selection,
	          selectionArgs);
	      break;
	    case IMAGES_ID:
	      String id = uri.getLastPathSegment();
	      if (TextUtils.isEmpty(selection)) {
	        rowsDeleted = sqlDB.delete(MySQLiteHelper.TABLE_NAME,
	        		MySQLiteHelper.COLUMN_ID + "=" + id, 
	            null);
	      } else {
	        rowsDeleted = sqlDB.delete(MySQLiteHelper.TABLE_NAME,
	        		MySQLiteHelper.COLUMN_ID + "=" + id 
	            + " and " + selection,
	            selectionArgs);
	      }
	      break;
	    default:
	      throw new IllegalArgumentException("Unknown URI: " + uri);
	    }	
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsDeleted;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
        if (sURIMatcher.match(uri) != IMAGES) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = mDB.getWritableDatabase();
        long rowId = db.insert(MySQLiteHelper.TABLE_NAME, null, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		
        SQLiteDatabase sqlDB = mDB.getWritableDatabase();
        int uriType = sURIMatcher.match(uri);
        int rowsAffected = 0;
        switch(uriType)
        {
        case IMAGES:
            rowsAffected = sqlDB.update(MySQLiteHelper.TABLE_NAME, values,
                    selection, selectionArgs);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
	}

}
