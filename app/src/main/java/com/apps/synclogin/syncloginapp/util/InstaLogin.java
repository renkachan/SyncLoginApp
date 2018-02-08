package com.apps.synclogin.syncloginapp.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Renka on 2/8/2018.
 */

public class InstaLogin {
    private Context context;
    private static final String API_INSTAGRAM = "api.instagram.com";
    private static final String CONNECTION = "oauth";
    private static final String ACTION = "authorize";
    private static final String CLIENT_ID = "client_id";
    private static final String REDIRECT_URI = "redirect_uri";
    private static final String RESPONSE_TYPE = "response_type";
    private String client_id, redirect_uri, response_type;

    public InstaLogin(Context context, String client_id, String redirect_uri
            , String response_type) {
        this.client_id = client_id;
        this.redirect_uri = redirect_uri;
        this.response_type = response_type;
        this.context = context;
    }

    public  void loginToInstagram() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https")
                .authority(API_INSTAGRAM)
                .appendPath(CONNECTION)
                .appendPath(ACTION)
                .appendQueryParameter(CLIENT_ID, client_id)
                .appendQueryParameter(REDIRECT_URI, redirect_uri)
                .appendQueryParameter(RESPONSE_TYPE, "token");

        final Intent browser = new Intent(Intent.ACTION_VIEW, uriBuilder.build());
        context.startActivity(browser);
    }
}
