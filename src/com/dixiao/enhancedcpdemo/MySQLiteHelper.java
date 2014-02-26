package com.dixiao.enhancedcpdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	
	  private final static String TAG = "MySQLiteHelper"; // TAG for logging  
	  // specify the database table and column name
	  public static final String TABLE_NAME = "images";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_NAME = "_name";
	  public static final String COLUMN_URI = "_uri";
	  public static final String COLUMN_URL = "_url";
	  public static final String COLUMN_DATE = "_createtime";

	  private static final String DATABASE_NAME = "images.db";
	  private static final int DATABASE_VERSION = 1;
	    
	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE_NAME + "(" + COLUMN_ID
	      + " integer primary key autoincrement, " + COLUMN_NAME
	      + " text not null,  " + COLUMN_URI
	      + " text not null,  " + COLUMN_URL
	      + " text not null, " + COLUMN_DATE
	      + " text not null);";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
	    Log.d(TAG, "Upgrading database from version " + oldVersion + " to "
	                + newVersion + ", which will destroy all old data");
	    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	        onCreate(database);
	}

}
