package com.dixiao.enhancedcpdemo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

public class DownloadBoundServiceAsync extends DownloadBoundServiceBase {
	private final static String LOG_TAG = "DownloadBoundServiceAsync";

	private final DownloadRequest.Stub binder = new DownloadRequest.Stub() {

		public void downloadImage(Uri uri, DownloadCallback callback)
				throws RemoteException {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);

			Log.d(LOG_TAG, uri.toString());
			ArrayList<String> urls = parseUrl(uri.toString());

			if (!urls.isEmpty()) {
				for (int i = 0; i < urls.size(); i++) {
					// get the name of the file from the URL
					int slashIndex = urls.get(i).lastIndexOf('/');
					String tempName = urls.get(i).substring(slashIndex + 1);
					// create the image URI
					String image_Uri = Environment.getDataDirectory()
							.toString()
							+ "/data/"
							+ getPackageName()
							+ "/files/" + tempName;
					File file = new File(image_Uri);
					// check if a file with same name has been created
					if (!file.exists()) {
						String filename = downloadFile(Uri.parse(urls.get(i)));
						Log.v(LOG_TAG, "filename" + filename);
						// insert a record into the database when the same file
						// does
						// not
						// exist
						if (filename != null) {
							ContentValues cv = new ContentValues();
							cv.put(MySQLiteHelper.COLUMN_NAME, filename);
							cv.put(MySQLiteHelper.COLUMN_URI, image_Uri);
							cv.put(MySQLiteHelper.COLUMN_URL, urls.get(i));
							cv.put(MySQLiteHelper.COLUMN_DATE, getCurrentTime());
							getContentResolver().insert(
									DownloadContentProvider.CONTENT_URI, cv);
						} else {
							image_Uri = null;
						}
					}
					callback.sendPath(image_Uri);
				}
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return this.binder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(LOG_TAG, "The DownloadBoundServiceAsync was created.");
	}

	@Override
	public void onDestroy() {
		Log.d(LOG_TAG, "The DownloadBoundServiceAsync was destroyed.");
		super.onDestroy();
	}

	// get the system time
	public String getCurrentTime() {
		Date date = new Date();
		return date.toString();
	}

	public ArrayList<String> parseUrl(String url) {
		if (url == null || (!URLUtil.isValidUrl(url))) {
			Toast.makeText(this, "Please Enter a valid image URL",
					Toast.LENGTH_SHORT).show();
			return null;
		} else {
			Document doc;
			ArrayList<String> urls = new ArrayList<String>();

			try {

				doc = Jsoup.connect(url).get();
				Elements images = doc.select("img");

				for (int i = 0; i < images.size(); i++) {
					urls.add(images.get(i).absUrl("src"));
				}
				return urls;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
}
