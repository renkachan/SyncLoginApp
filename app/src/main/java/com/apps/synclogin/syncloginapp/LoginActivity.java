package com.apps.synclogin.syncloginapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.apps.synclogin.syncloginapp.db.SQLiteHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements  View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private Button googleSignIn, fbSignIn;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Button signOut;
    private SignInButton SignIn;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 400;
    private static final String EMAIL = "email";

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        googleSignIn = findViewById(R.id.googleLoginBtn);
        fbSignIn = findViewById(R.id.fbLoginBtn);
        fbSignIn.setOnClickListener(this);
        googleSignIn.setOnClickListener(this);
        settingUpFBLogin();
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.
                DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).
                enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.googleLoginBtn:
                signInWithGoogle();
            case R.id.fbLoginBtn:
                LoginManager.getInstance().logInWithReadPermissions(
                        this, Arrays.asList("public_profile", "email", "user_friends")
                );
                break;
        }
    }
    private void settingUpFBLogin() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (loginResult != null) {
                    Profile profile = Profile.getCurrentProfile();
                    String name = profile.getName();
                    String id = profile.getId();

                    Intent i = new  Intent(getApplicationContext(), ProfileActivity.class);
                    i.putExtra("name", name);
                    i.putExtra("id", id);
                    i.putExtra("loginType", SQLiteHelper.COLUMN_FB_ID);
                    startActivity(i);
                }

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }
    public void signInWithGoogle() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    //For sign out later
//    public void signInWithTwitter() {
//      if(googleApiClient.isConnected()) {
//          googleApiClient.disconnect();
//        });
//    }


    public void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String id = account.getId();
            Intent i = new  Intent(getApplicationContext(), ProfileActivity.class);
            i.putExtra("name", name);
            i.putExtra("id", id);
            i.putExtra("loginType", SQLiteHelper.COLUMN_GOOGLE_ID);
            startActivity(i);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
