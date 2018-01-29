package com.apps.synclogin.syncloginapp.util;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;

import com.apps.synclogin.syncloginapp.ProfileActivity;
import com.apps.synclogin.syncloginapp.db.SQLiteHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

/**
 * Created by robert.arifin on 29/01/2018.
 */

public class FBLogin extends AppCompatActivity {
    public  static CallbackManager callbackManager = CallbackManager.Factory.create();

    public void loginAndFetchData () {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            private ProfileTracker mProfileTracker;
            Intent i = new Intent(getApplicationContext(), ProfileActivity.class);

            @Override
            public void onSuccess(LoginResult loginResult) {
                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            mProfileTracker.stopTracking();
                            Profile.setCurrentProfile(currentProfile);

                            i.putExtra("name", Profile.getCurrentProfile().getName());
                            i.putExtra("id", Profile.getCurrentProfile().getId());
                            i.putExtra("loginType", SQLiteHelper.COLUMN_FB_ID);
                            startActivity(i);
                        }
                    };

                    mProfileTracker.startTracking();
                } else {
                    Profile profile = Profile.getCurrentProfile();
                    String name = profile.getName();
                    String id = profile.getId();

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
}
