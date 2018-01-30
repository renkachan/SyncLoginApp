package com.apps.synclogin.syncloginapp.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by robert.arifin on 30/01/2018.
 */

public class GoogleLogin  {
    private Context context;

    public GoogleLogin(Context context) {
        this.context = context;
    }

    public void loginToGoogle(GoogleApiClient googleApiClient) {
        Activity activity = (Activity) context;
    }
    //   GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.
      //          DEFAULT_SIGN_IN).requestEmail().build();
//        googleApiClient = new GoogleApiClient.Builder(context).
//                enableAutoManage(activity.this activity)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
//        return Auth.GoogleSignInApi.getSignInIntent(googleApiClient);




}
