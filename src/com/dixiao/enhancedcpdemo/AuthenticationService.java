/*
 * Copyright (C) 2010 The Android Open Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.dixiao.enhancedcpdemo;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class AuthenticationService extends Service {
    private static final String TAG = "AuthenticationService";

    private static AccountAuthenticatorImpl accountAuthenticator = null;

    public AuthenticationService() {
        super();
        Log.d(TAG, "AuthenticationService()");
    }

    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");

        IBinder ret = null;
        if (intent.getAction().equals(
                android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT))
            ret = getAuthenticator().getIBinder();
        return ret;
    }

    private AccountAuthenticatorImpl getAuthenticator() {
        if (accountAuthenticator == null)
            accountAuthenticator = new AccountAuthenticatorImpl(this);
        return accountAuthenticator;
    }

    private static class AccountAuthenticatorImpl extends
            AbstractAccountAuthenticator {
        private static final String TAG = "AccountAuthenticatorImpl";

        // Authentication Service context
        private final Context mContext;

        public AccountAuthenticatorImpl(Context context) {
            super(context);
            Log.d(TAG, "AccountAuthenticatorImpl()");
            mContext = context;
        }

        @Override
        public Bundle addAccount(AccountAuthenticatorResponse response,
                String accountType, String authTokenType,
                String[] requiredFeatures, Bundle options)
                throws NetworkErrorException {
            Log.d(TAG, "addAccount()");
            
        	final Intent intent = new Intent(mContext, DownloadActivity.class);
        	intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        	final Bundle bundle = new Bundle();
        	bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        	
        	return bundle;         
        }

        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse response,
                Account account, Bundle options) throws NetworkErrorException {
            Log.d(TAG, "confirmCredentials()");
            return null;
        }

        @Override
        public Bundle editProperties(AccountAuthenticatorResponse response,
                String accountType) {
            Log.d(TAG, "editProperties()");
            return null;
        }

        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse response,
                Account account, String authTokenType, Bundle options)
                throws NetworkErrorException {
            Log.d(TAG, "getAuthToken()");
            return null;
        }

        @Override
        public String getAuthTokenLabel(String authTokenType) {
            Log.d(TAG, "getAuthTokenLabel()");
            return null;
        }

        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse response,
                Account account, String[] features)
                throws NetworkErrorException {
            Log.d(TAG, "hasFeatures()");
            return null;
        }

        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse response,
                Account account, String authTokenType, Bundle options)
                throws NetworkErrorException {
            Log.d(TAG, "updateCredentials()");
            return null;
        }
    }
}
