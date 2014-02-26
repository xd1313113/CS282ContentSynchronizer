package com.dixiao.enhancedcpdemo;

import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;

public class MyContentObserver extends ContentObserver {
	
	private Cursor cursor = null;
	private Handler mHandler = null;  

	public MyContentObserver(Handler handler) {
		super(handler);
		mHandler = handler ; 
	}

	@Override
	public void onChange(boolean selfChange) {
		
		
		
		
	}
	
	

}
