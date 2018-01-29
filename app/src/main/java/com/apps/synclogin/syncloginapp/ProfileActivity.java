package com.apps.synclogin.syncloginapp;

import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.media.MediaCas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apps.synclogin.syncloginapp.db.SQLiteHelper;
import com.apps.synclogin.syncloginapp.util.UserProfile;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.firebase.ui.auth.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by Renka on 1/24/2018.
 */

public class ProfileActivity extends AppCompatActivity implements  View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener  {
    private Button googleSyncBtn, twitterSyncBtn, githubSyncBtn, fbSyncBtn, instaSyncBtn,
            signOutBtn;
    private TextView firstNameField, lastNameField, emailField;
    GoogleSignInOptions signInOptions;
    String name, email, id, loginType;
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
        firstNameField = findViewById(R.id.firstNameValue);
        lastNameField = findViewById(R.id.lastNameValue);
        emailField = findViewById(R.id.emailValue);
        signOutBtn = findViewById(R.id.signOutBtn);

        signOutBtn.setOnClickListener(this);

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        loginType = getIntent().getStringExtra("loginType");
        checkUserExistOrNot();

        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.
                DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).
                enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
    }

    private void checkUserExistOrNot() {
        UserProfile user = new UserProfile();
        UserProfile result;
        user.setID(id);

        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        result = sqLiteHelper.checkRecord(user, loginType);

        if (result == null) {
            result = createNewUser();
        }

        firstNameField.setText(result.getFirstName());
        lastNameField.setText(result.getLastName());
         emailField.setText(result.getEmail());
    }

    private UserProfile createNewUser() {
        String firstName, lastName;
        String[] separated;
        separated = name.split(" ");
        firstName = separated[0];
        lastName = separated[1];

        UserProfile user = new UserProfile();
        user.setID(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        sqLiteHelper.insertRecord(user, loginType);

        return user;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.googleLoginBtn:
                syncWithGoogle();
                break;
            case R.id.signOutBtn:
                signOut();
                break;
//            case R.id.twitterLoginBtn:
//                syncWithTwitter();
//                break;
//            case R.id.githubLoginBtn:
//                syncWithGithub();
//                break;
            case R.id.fbLoginBtn:
                syncWithFB();
//                break;
//            case R.id.instaSyncBtn:
//                sycnWithInsta();
//                break;
        }
    }

    private void syncWithFB() {
        UserProfile user = new UserProfile();
        user.setID(id);

        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        //sqLiteHelper.updateRecord(user, loginType, SQLiteHelper.COLUMN_FB_ID, );

    }

    private void signOut() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);

        if (googleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                }
                });
        }

        if (accessToken != null) {
            LoginManager.getInstance().logOut();
        }

        startActivity(i);
    }

    private  void syncWithGoogle() {


    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    }
