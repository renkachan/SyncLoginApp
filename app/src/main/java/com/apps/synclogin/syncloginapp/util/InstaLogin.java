package com.apps.synclogin.syncloginapp.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

import com.apps.synclogin.syncloginapp.R;

/**
 * Created by Renka on 2/8/2018.
 */

public class InstaLogin extends AppCompatActivity {
    private Context context;
    private  final String API_INSTAGRAM = "api.instagram.com";
    private  final String CONNECTION = "oauth";
    private  final String ACTION = "authorize";
    private  final String CLIENT_ID = "client_id";
    private  final String REDIRECT_URI = "redirect_uri";
    private  final String RESPONSE_TYPE = "response_type";
    private String client_id, redirect_uri, response_type;
    private View view;

    public InstaLogin(Context context, String client_id, String redirect_uri
            , String response_type) {
        this.client_id = client_id;
        this.redirect_uri = redirect_uri;
        this.response_type = response_type;
        this.context = context;
    }

    public  String  buildWebViewUri() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https")
                .authority(API_INSTAGRAM)
                .appendPath(CONNECTION)
                .appendPath(ACTION)
                .appendQueryParameter(CLIENT_ID, client_id)
                .appendQueryParameter(REDIRECT_URI, redirect_uri)
                .appendQueryParameter(RESPONSE_TYPE, "token");

       return uriBuilder.build().toString();
//        final Intent browser = new Intent(Intent.ACTION_VIEW, uriBuilder.build());
//        context.startActivity(browser);
    }
}
