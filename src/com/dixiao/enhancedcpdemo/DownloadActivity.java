package com.dixiao.enhancedcpdemo;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class DownloadActivity extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private final static String TAG = "DownloadActivity"; // TAG for logging

	private EditText mEditText; // URL input
	private String mURL; // store the mURL entered by the user
	private ProgressDialog mDialog; // ProgressDialog
	private ImageView mImageView; // default imageview
	private ListView mList; // listview
	private Context mContext;

	// The callbacks through which we interact with the LoaderManager
	private LoaderManager.LoaderCallbacks<Cursor> mCallbacks = this;
	// The loader's unique id
	private static final int LOADER_ID = 0;
	// The adapter that binds our data to the ListView
	private SimpleCursorAdapter mAdapter;

	private MyContentObserver mConetenObserver;

	// used of simplecursoradapter
	final String[] from = new String[] { DownloadContentProvider._URI,
			DownloadContentProvider._NAME, DownloadContentProvider._CREATETIME };
	// used of simplecursoradapter
	final int[] to = new int[] { R.id.imagedisplay, R.id.name, R.id.time };

	// These are the Images column projection
	static final String[] PROJECTION = new String[] { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_URI,
			MySQLiteHelper.COLUMN_DATE };

	private DownloadRequest asyncService; // handle to Remote Service
	private boolean boundAsync; // indicator of binding state

	// Remote Service callback methods
	private ServiceConnection asyncConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName name, IBinder iservice) {

			Log.v(TAG, "onAsyncServiceConnected");
			asyncService = DownloadRequest.Stub.asInterface(iservice);
			boundAsync = true;
		}

		public void onServiceDisconnected(ComponentName name) {

			Log.v(TAG, "onAsyncServiceDisconnected");
			asyncService = null;
			boundAsync = false;
		}

	};

	// DownloadCallback proxy which is called when Async Aidl returns
	// when finish downloading
	private final DownloadCallback.Stub callback = new DownloadCallback.Stub() {

		public void sendPath(final String imageFilePath) throws RemoteException {

			// when sendFileName was called, run on the UI thread to display the
			// image with given filename
			runOnUiThread(new Runnable() {
				public void run() {
					if (imageFilePath != null) {
						// displayBitmap(imageFilePath);
						mDialog.dismiss();
						Toast.makeText(getApplicationContext(),
								"File Uri is " + imageFilePath,
								Toast.LENGTH_LONG).show();
					} else {
						// mImageView.setImageResource(R.drawable.ic_launcher);
						mDialog.dismiss();
						Toast.makeText(getApplicationContext(),
								"Please check the mURL and try again",
								Toast.LENGTH_LONG).show();
					}
				}
			});
		}
	};

	SyncAdapterService mSyncAdapterService;
	private boolean mBoundSyncAdapter = false;

	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mSyncAdapterConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			Log.v(TAG, "SyncAdapterConnection");
			mBoundSyncAdapter = true;
		}

		public void onServiceDisconnected(ComponentName arg0) {
			mBoundSyncAdapter = false;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);

		// get the UI elements by ID
		Log.v(TAG, "Activity onCreate Executed");
		mEditText = (EditText) findViewById(R.id.editText1);
		mImageView = (ImageView) findViewById(R.id.imageView);
		mList = (ListView) findViewById(android.R.id.list);
		// create a progress dialog
		mDialog = new ProgressDialog(this);
		mDialog.setTitle("Downloading");
		mDialog.setIndeterminate(true);
		mContext = getApplicationContext();

		mConetenObserver = new MyContentObserver(new Handler());

		// this.getContentResolver().registerContentObserver(DownloadContentProvider.CONTENT_URI,
		// true, mConetenObserver);

		new ListeningThread().start();

	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.v(TAG, "onStart Executed");
		// connecting to the Async AIDL service
		connectToService(
				"com.dixiao.enhancedcpdemo.DownloadContentProvider.Async",
				this.asyncConnection);

		connectToService("android.content.SyncAdapter",
				this.mSyncAdapterConnection);
	}

	protected void onStop() {
		// unbind from all the Service
		if (boundAsync)
			unbindService(this.asyncConnection);
		super.onStop();
	}

	public void runDownloadFile(View view) {
		Log.v(TAG, "runDownloadFile Executed");
		mURL = mEditText.getText().toString();

		if (check(mURL)) {
			mDialog.setMessage("Downloading via runDownloadFile");
			mDialog.show();

			try {
				// call the remote method to download the file and store it in
				// the internal storage
				if (boundAsync) {
					asyncService.downloadImage(Uri.parse(mURL), callback);
				} else {
					Toast.makeText(
							this,
							"The service is not available now, please try later",
							Toast.LENGTH_SHORT).show();
					mDialog.dismiss();
				}
			} catch (RemoteException e) {

				e.printStackTrace();
			}
		}
	}

	public void runCursorLoader(View view) {
		Log.v(TAG, "runCursorLoader Executed");
		mImageView.setVisibility(View.INVISIBLE);
		// Create an empty adapter we will use to display the loaded data.
		mAdapter = new SimpleCursorAdapter(getApplicationContext(),
				R.layout.rowlayout, null, from, to, 0);
		setListAdapter(mAdapter);

		// Initialize the Loader with id & callbacks "mCallbacks¡±.
		getLoaderManager().initLoader(LOADER_ID, null, mCallbacks);
	}

	public void runAsyncQueryHandler(View view) {
		Log.v(TAG, "runAsyncQueryHandler Executed");
		mImageView.setVisibility(View.INVISIBLE);
		new DeleteQueryHandler(getContentResolver(), mContext).execute();
	}

	public void runSync(View view) {
		Log.v(TAG, "runSync Executed");
		Toast.makeText(this, "Performing Sync Operation", Toast.LENGTH_LONG).show();
		
		Account account = new Account(Constants.DEFAULT_USER,
				Constants.ACCOUNT_TYPE);
		AccountManager accountManager = AccountManager.get(this);

		accountManager.addAccountExplicitly(account,
				Constants.DEFAULT_PASSWORD, null);

		ContentResolver.setIsSyncable(account,
				DownloadContentProvider.AUTHORITY, 1);

		Bundle bundle = new Bundle();
		bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
		bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

		Log.d(TAG, "Sync Request");
		ContentResolver.requestSync(account, DownloadContentProvider.AUTHORITY,
				bundle);
	}

	// reset image
	public void resetImage(View view) {
		// set the adapter to null and show the default imageview
		mAdapter = null;
		setListAdapter(mAdapter);
		mList.setEmptyView(mImageView);
	}

	protected void connectToService(String IntentFilter,
			ServiceConnection connection) {
		Intent intent = new Intent(IntentFilter);
		// bind to Service
		bindService(intent, connection, Context.BIND_AUTO_CREATE);
		Log.d(TAG,
				"The Service will be connected soon (" + connection.toString()
						+ " call)!");
	}

	// check if the string is legal
	public boolean check(String url) {
		// return false if input is null or is not a valid mURL
		if (url == null || (!URLUtil.isValidUrl(url))) {
			Toast.makeText(this, "Please Enter a valid URL", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else {
			return true;
		}
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		// Create a new CursorLoader with query parameters.
		return new CursorLoader(DownloadActivity.this,
				DownloadContentProvider.CONTENT_URI, null, null, null, null);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		Log.v(TAG, "processing " + cursor.getCount() + " rows");
		// swap the returned cursor with cursor in the adapter
		mAdapter.swapCursor(cursor);
	}

	public void onLoaderReset(Loader<Cursor> arg0) {
	}

	public class ListeningThread extends Thread {

		private final static String TAG = "ListeningThread"; // TAG for logging

		private ArrayList<String> localUrls = new ArrayList<String>();
		private ArrayList<String> receivedUrls = new ArrayList<String>();
		private ArrayList<String> deleteUrls = new ArrayList<String>();
		private ArrayList<String> downloadUrls = new ArrayList<String>();

		@Override
		public void run() {
			try {
				ServerSocket serverSocket = new ServerSocket(7777);
				while (true) {
					Socket client = serverSocket.accept();
					Log.v(TAG, "Connection established");
					
					//Toast.makeText(mContext, "Performing Sync Process", Toast.LENGTH_SHORT).show();

					String json = Utilities.convertStreamToString(client
							.getInputStream());

					/* ****************************************************** */
					// Construct JsonObject from inputstream and parse it
					JSONObject objReceived = new JSONObject(json);
					Log.v(TAG, "Json Object received " + objReceived.toString());

					for (int i = 0; i < objReceived.length(); i++) {
						receivedUrls.add(objReceived.getString("item" + i));
						Log.v(TAG,
								"add item to receivedUrls "
										+ objReceived.getString("item" + i));
					}

					/* ****************************************************** */
					// query local content provider and store it in localurls
					ContentResolver cr = getContentResolver();
					// get cursor of the returned result
					Cursor cursor = cr.query(
							DownloadContentProvider.CONTENT_URI, null, null,
							null, null);
					Log.v(TAG, "processing " + cursor.getCount() + " rows");

					if (cursor != null) {
						for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
								.moveToNext()) {
							String item_url;
							item_url = cursor
									.getString(cursor
											.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_URL));
							Log.v(TAG,
									"add item to local urls "
											+ cursor.getPosition()
											+ " with url = " + item_url);
							localUrls.add(item_url);
						}
						cursor.close();
					}

					downloadUrls = Utilities.getDownloadUrls(localUrls,
							receivedUrls);

					
					/* ****************************************************** */
					Log.v(TAG, "the size of downloadUrls is" + downloadUrls.size());

					// download all urls contained in the received url
					if (!downloadUrls.isEmpty()) {

						for (int i = 0; i < downloadUrls.size(); i++) {

							String downloadUrl = downloadUrls.get(i);
							String imageUri = Utilities.downloadFile(mContext,
									downloadUrl);
							if (null != imageUri) {
								// get the name of the file from the URL
								int slashIndex = imageUri.lastIndexOf('/');
								String filename = imageUri
										.substring(slashIndex + 1);
								ContentValues cv = new ContentValues();
								cv.put(MySQLiteHelper.COLUMN_NAME, filename);
								cv.put(MySQLiteHelper.COLUMN_URI, imageUri);
								cv.put(MySQLiteHelper.COLUMN_URL, downloadUrl);
								cv.put(MySQLiteHelper.COLUMN_DATE,
										Utilities.getCurrentTime());
								getContentResolver()
										.insert(DownloadContentProvider.CONTENT_URI,
												cv);
								Log.v(TAG, "insert item into database, item name = " + filename);
							}
						}
					}
					
					/* ****************************************************** */
					Log.v(TAG, "delete items that does not exist on another device");
					deleteUrls = Utilities.getDeleteUrls(localUrls,
							receivedUrls);
					for (int i = 0; i < deleteUrls.size(); i++) {
						String deletefile = Utilities.getFilenameFromUrl(deleteUrls.get(i));
						getContentResolver().delete(DownloadContentProvider.CONTENT_URI,
								MySQLiteHelper.COLUMN_NAME + "= '"+deletefile+"'", null);
						Utilities.deleteFile(mContext, deletefile);
					}
					
					Log.v(TAG, "Sync process finished");
					
					downloadUrls.clear();
					localUrls.clear();
					receivedUrls.clear();			
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
}
