package com.apps.synclogin.syncloginapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.apps.synclogin.syncloginapp.db.SQLiteHelper;
import com.apps.synclogin.syncloginapp.util.FBLogin;
import com.apps.synclogin.syncloginapp.util.GoogleLogin;
import com.apps.synclogin.syncloginapp.util.UserProfile;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private Button googleSignIn, fbSignIn, manualSignIn, manualSignUp;
    private EditText emailField, passwordField;
    private FrameLayout errorMessageField;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private static final int REQ_CODE = 400;
    private static final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.loginUsername);
        passwordField = findViewById(R.id.loginPassword);
        manualSignUp = findViewById(R.id.manualSignUpBtn);
        googleSignIn = findViewById(R.id.googleLoginBtn);
        fbSignIn = findViewById(R.id.fbLoginBtn);
        manualSignIn = findViewById(R.id.manualSignInBtn);
        errorMessageField = findViewById(R.id.error_message_container);

        fbSignIn.setOnClickListener(this);
        googleSignIn.setOnClickListener(this);
        manualSignIn.setOnClickListener(this);
        manualSignUp.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.googleLoginBtn:
                signInWithGoogle();
                break;
            case R.id.fbLoginBtn:
                signInWithFB();
                break;
            case R.id.manualSignInBtn:
                signInManual();
                break;
            case R.id.manualSignUpBtn:
                signUpManual();
                break;
        }
    }

    private void signUpManual() {
       Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
       startActivity(intent);
    }

    private void signInManual() {
        UserProfile result ;

        errorMessageField.setVisibility(View.INVISIBLE);

        if (!emailField.getText().toString().equals("") ||
                !passwordField.getText().toString().equals("")) {
            errorMessageField.setVisibility(View.INVISIBLE);

            Intent intent;

            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();

            UserProfile user = new UserProfile();
            user.setEmail(email);
            user.setPassword(password);

            SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
            result = sqLiteHelper.checkRecord(user, SQLiteHelper.COLUMN_EMAIL);

            if (result != null) {
                if (result.getPassword().equals(user.getPassword())) {
                    intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    intent.putExtra("loginType", SQLiteHelper.COLUMN_EMAIL);
                    startActivity(intent);
                } else {
                    errorMessageField.setVisibility(View.VISIBLE);
                }
            } else {
                errorMessageField.setVisibility(View.VISIBLE);
            }
        }
    }

    private void signInWithFB() {
        FBLogin fbLogin = new FBLogin(this);
        fbLogin.loginAndFetchData(callbackManager);
    }

    public void signInWithGoogle() {
       // Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        GoogleLogin signIn = new GoogleLogin(this);
        Intent signInIntent = signIn.loginToGoogle(this, this);
        startActivityForResult(signInIntent, REQ_CODE);
    }

    public void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String id = account.getId();

            Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
            i.putExtra("name", name);
            i.putExtra("id", id);
            i.putExtra("loginType", SQLiteHelper.COLUMN_GOOGLE_ID);
            startActivity(i);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
