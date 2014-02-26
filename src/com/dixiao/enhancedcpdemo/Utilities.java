package com.dixiao.enhancedcpdemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

public class Utilities {

	private final static String TAG = "Utilities";
	/**
     *
     */
	public static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	/**
    *
    */
	public static ArrayList<String> getDownloadUrls(ArrayList<String> local, ArrayList<String> received) {

        ArrayList<String> downloadUrls = new ArrayList<String>();
        
        for(String str:received)
        {
            if(!local.contains(str))
            {
            	downloadUrls.add(str);
            }
        }

        return downloadUrls;
    }

	/**
        *
        */
	public static ArrayList<String> getDeleteUrls(ArrayList<String> local, ArrayList<String> received) {

		ArrayList<String> sendUrls = new ArrayList<String>();

		for (String str : local) {
			if (!received.contains(str)) {
				sendUrls.add(str);
			}
		}
		return sendUrls;
	}
	
	public static String downloadFile(Context ctx, String url) {

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		Log.v(TAG, "downloadFile Executed in the Utilites");
		String imageUri = null;
		
		// get the name of the file from the URL
		int slashIndex = url.lastIndexOf('/');
		String tempName = url.substring(slashIndex + 1);
		// create the image URI
		String image_Uri = Environment.getDataDirectory().toString()+ "/data/"
					+ ctx.getPackageName() + "/files/" + tempName;
		try {

			InputStream is = new URL(url).openConnection().getInputStream();
			byte[] buffer = new byte[1024];

			if (is != null) {
				FileOutputStream fos;
				// get the name of the file from the URL

				// output the filestream into the filesystem
				fos = ctx.openFileOutput(tempName, Context.MODE_PRIVATE);
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
		return image_Uri;
	}
	
	// get the system time
	public static String getCurrentTime() {
		Date date = new Date();
		return date.toString();
	}
	
	
	public static String getFilenameFromUrl(String url) {
		int slashIndex = url.lastIndexOf('/');
		String filename = url
				.substring(slashIndex + 1);
		return filename;
	}
	
	public static void deleteFile(Context mContext, String filename){
		String basePath = Environment.getDataDirectory().toString() + "/data/"
				+ mContext.getPackageName() + "/files/";
		File file = new File(basePath+filename);
		file.delete();
	}
	
	

}
