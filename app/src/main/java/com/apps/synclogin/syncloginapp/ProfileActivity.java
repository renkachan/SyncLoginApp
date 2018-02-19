package com.apps.synclogin.syncloginapp;

import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.media.MediaCas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.synclogin.syncloginapp.db.SQLiteHelper;
import com.apps.synclogin.syncloginapp.util.FBLogin;
import com.apps.synclogin.syncloginapp.util.GoogleLogin;
import com.apps.synclogin.syncloginapp.util.UserProfile;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.firebase.ui.auth.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by Renka on 1/24/2018.
 */

public class ProfileActivity extends AppCompatActivity implements  View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener  {
    private Button googleSyncBtn, twitterSyncBtn, githubSyncBtn, fbSyncBtn, instaSyncBtn,
            signOutBtn;
    private TextView firstNameField, lastNameField, emailField;
    private CallbackManager callbackManager;

    GoogleSignInOptions signInOptions;
    GoogleApiClient googleApiClient;

    String name, email, id, loginType, password, loginResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        googleSyncBtn = findViewById(R.id.googleSyncBtn);
        twitterSyncBtn = findViewById(R.id.twitterSyncBtn);
        githubSyncBtn = findViewById(R.id.githubSyncBtn);
        fbSyncBtn = findViewById(R.id.fbSyncBtn);
        instaSyncBtn = findViewById(R.id.instaSyncBtn);
        firstNameField = findViewById(R.id.firstNameValue);
        lastNameField = findViewById(R.id.lastNameValue);
        emailField = findViewById(R.id.emailValue);
        signOutBtn = findViewById(R.id.signOutBtn);

        signOutBtn.setOnClickListener(this);
        googleSyncBtn.setOnClickListener(this);
        fbSyncBtn.setOnClickListener(this);

        name = getIntent().getStringExtra("name");
        id  = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        loginType = getIntent().getStringExtra("loginType");

        switch (loginType) {
            case SQLiteHelper.COLUMN_FB_ID:
                fbSyncBtn.setEnabled(false);
                break;
            case SQLiteHelper.COLUMN_GOOGLE_ID:
                googleSyncBtn.setEnabled(false);
                break;
        }

        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.
             DEFAULT_SIGN_IN).requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).
                enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
        googleApiClient.connect();

        callbackManager = CallbackManager.Factory.create();
        checkUserExistOrNot();
    }



    private void disableSyncButtons() {

    }

    private void checkUserExistOrNot() {
        UserProfile user = new UserProfile();
        UserProfile result;
        user.setEmail(email);
        user.setPassword(password);

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

        if (name.contains(" ")) {
            firstName = name.substring(0, name.indexOf(' '));
            lastName =  name.substring(name.indexOf(' ') + 1);
        } else {
            firstName = name;
            lastName = "";
        }

        UserProfile user = new UserProfile();
        user.setID(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setEmail(email);

        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        sqLiteHelper.insertRecord(user, loginType);

        return user;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.googleSyncBtn:
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
            case R.id.fbSyncBtn:
                syncWithFB();
//                break;
//            case R.id.instaSyncBtn:
//                sycnWithInsta();
//                break;
        }
    }

    private void syncWithFB() {
        UserProfile user = new UserProfile();
        String  fbID = "";

        if (loginType.equals(SQLiteHelper.COLUMN_EMAIL)) {
            user.setID(email);
        } else {
            user.setID(id);
        }

        FBLogin fbLogin = new FBLogin(this);
        fbID = fbLogin.getFBId(callbackManager);

        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        sqLiteHelper.updateRecord(user, loginType, SQLiteHelper.COLUMN_FB_ID, fbID);

        fbSyncBtn.setEnabled(false);
    }

    private void signOut() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
//        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
//            @Override
//            public void onConnected(Bundle bundle) {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });
//                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
//                        @Override
//                        public void onResult(@NonNull Status status) {
//                        }
//                    });
//                }
//
        Auth.GoogleSignInApi.revokeAccess(googleApiClient)
                .setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                    }
                });

        if (accessToken != null) {
            LoginManager.getInstance().logOut();
        }

        Toast.makeText(getApplicationContext(), "Logged out",
                Toast.LENGTH_LONG).show();
        startActivity(i);
    }

    private  void syncWithGoogle() {


    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    }
