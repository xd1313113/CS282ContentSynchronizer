package com.dixiao.enhancedcpdemo;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class SyncAdapterService extends Service {
	
    private static final Object sSyncAdapterLock = new Object();

    private static SyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }
    
	@Override
	public IBinder onBind(Intent intent) {
		 return sSyncAdapter.getSyncAdapterBinder();
	}
	
	public class SyncAdapter extends AbstractThreadedSyncAdapter {

		private static final String TAG = "SyncAdapter";

		private final Context mContext;

		public SyncAdapter(Context context, boolean autoInitialize) {
			super(context, autoInitialize);
			mContext = context;
		}

		@Override
		public void onPerformSync(Account account, Bundle extras, String authority,
				ContentProviderClient provider, SyncResult syncResult) {
			Log.i(TAG, "performSync");
			
			JSONObject objSend  = new JSONObject();
			
			/* ****************************************************** */
			Log.i(TAG, "query local content provider");
			ContentResolver cr = getContentResolver();
			// get cursor of the returned result
			Cursor cursor = cr.query(DownloadContentProvider.CONTENT_URI,null, null, null, null);
			
			Log.v(TAG, "processing " + cursor.getCount() + " rows");
			
			/* ****************************************************** */
			//construct jSONObject from cursor
			Log.i(TAG, "construct jSONObject from cursor");
			if(cursor != null)
			{
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
						String item_url;
						item_url = cursor.getString(cursor.getColumnIndexOrThrow
								(MySQLiteHelper.COLUMN_URL));
						Log.v(TAG, "put item"+cursor.getPosition()+" with url = " + item_url);
						try {
							objSend.put("item"+cursor.getPosition(), item_url);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}			
				}
			}
			
			/* ****************************************************** */
			Log.v(TAG, "Json Object to be send "+objSend.toString());
			try {
				Socket client = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), 7777);
	            OutputStream os = client.getOutputStream();
	            os.write(objSend.toString().getBytes());
	            os.close();
	            client.close();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
	}

}
