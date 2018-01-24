package com.apps.synclogin.syncloginapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Renka on 1/24/2018.
 */

public class ProfileActivity extends AppCompatActivity implements  View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener  {
    private Button googleSyncBtn, twitterSyncBtn, githubSyncBtn, fbSyncBtn, instaSyncBtn;
    GoogleSignInOptions signInOptions;
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        googleSyncBtn = findViewById(R.id.googleLoginBtn);
        twitterSyncBtn = findViewById(R.id.twitterLoginBtn);
        githubSyncBtn = findViewById(R.id.githubLoginBtn);
        fbSyncBtn = findViewById(R.id.fbLoginBtn);
        instaSyncBtn = findViewById(R.id.instaLoginBtn);

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String id = getIntent().getStringExtra("id");
        checkUserExistOrNot(id);

        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.
                DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).
                enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
    }

    private void checkUserExistOrNot(String id) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.googleLoginBtn:
                syncWithGoogle();
                break;
            case R.id.twitterLoginBtn:
                syncWithGoogle();
                break;
            case R.id.githubLoginBtn:
                syncWithGoogle();
                break;
            case R.id.fbLoginBtn:
                syncWithGoogle();
                break;
            case R.id.instaSyncBtn:
                syncWithGoogle();
                break;
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    }
