package com.apps.synclogin.syncloginapp.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by robert.arifin on 30/01/2018.
 */

public class GoogleLogin {
    private Context context;
    private GoogleSignInOptions signInOptions;
    private GoogleApiClient googleApiClient;

    public GoogleLogin(Context context) {
        this.context = context;
    }

    public Intent loginToGoogle(FragmentActivity activity,
                                GoogleApiClient.OnConnectionFailedListener listener) {
        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.
                DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(activity).
                enableAutoManage(activity, listener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();

        return Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
    }

//    public void logOutFromGoogle(FragmentActivity activity,
//                                 GoogleApiClient.OnConnectionFailedListener listener) {
//        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.
//                DEFAULT_SIGN_IN).requestEmail().build();
//        googleApiClient = new GoogleApiClient.Builder(context).
//                enableAutoManage(activity, listener)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
//        googleApiClient.connect();
//
//        if (googleApiClient.isConnected()) {
//            Auth.GoogleSignInApi.signOut(googleApiClient);
//        }
//    }
}





