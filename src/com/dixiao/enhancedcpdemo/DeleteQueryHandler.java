package com.dixiao.enhancedcpdemo;

import java.io.File;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

//The query handler which implements the AsyncQueryHandler class
// provide an execute method which query the database and return all records which meet the URI
public class DeleteQueryHandler extends AsyncQueryHandler {

	private final static String TAG = "DeleteQueryHandler"; // TAG for logging
	Context mContext;

	public DeleteQueryHandler(ContentResolver cr, Context m) {
		super(cr);
		mContext = m;
	}

	public void execute() {
		String basePath = Environment.getDataDirectory().toString() + "/data/"
				+ mContext.getPackageName() + "/files/";

		File dir = new File(basePath);
		File delfile[] = dir.listFiles();
		Log.v(TAG, ""+delfile.length);
		for (int i = 0; i < delfile.length; i++) {
			delfile[i].delete();
		}
		startDelete(0, null, DownloadContentProvider.CONTENT_URI, null, null);
	}

	// called when query finished
	@Override
	protected void onDeleteComplete(int token, Object cookie, int result) {
		// TODO Auto-generated method stub
		super.onDeleteComplete(token, cookie, result);
		Toast.makeText(mContext,
				DownloadContentProvider.CONTENT_URI.toString(),
				Toast.LENGTH_LONG).show();
	}

}
