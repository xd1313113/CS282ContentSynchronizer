package com.dixiao.enhancedcpdemo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.app.Service;
import android.content.Context;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;

public abstract class DownloadBoundServiceBase extends Service {

	// DownloadManagement actually implement the download process.
	private final String LOG_TAG = "DownloadManagement";

	public String downloadFile(Uri uri) {

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		Log.v(LOG_TAG, "downloadFile Executed");
		String filename = null;
		final String url = uri.toString();
		try {

			InputStream is = new URL(url).openConnection().getInputStream();
			byte[] buffer = new byte[1024];

			if (is != null) {
				FileOutputStream fos;
				// get the name of the file from the URL
				int slashIndex = url.toString().lastIndexOf('/');
				filename = url.toString().substring(slashIndex + 1);
				// output the filestream into the filesystem
				fos = openFileOutput(filename, Context.MODE_PRIVATE);
				for (int bytesRead; (bytesRead = is.read(buffer, 0,
						buffer.length)) != -1;) {
					fos.write(buffer, 0, bytesRead);
				}
				fos.flush();
				fos.close();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return filename;
	}
}
