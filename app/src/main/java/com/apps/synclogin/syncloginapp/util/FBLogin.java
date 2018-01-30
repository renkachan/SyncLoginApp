package com.apps.synclogin.syncloginapp.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.widget.Toast;

import com.apps.synclogin.syncloginapp.LoginActivity;
import com.apps.synclogin.syncloginapp.ProfileActivity;
import com.apps.synclogin.syncloginapp.db.SQLiteHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

/**
 * Created by robert.arifin on 29/01/2018.
 */

public class FBLogin {
    private Context context;

    public FBLogin(Context context) {
        this.context = context;
    }

    public void loginAndFetchData (CallbackManager callbackManager) {
        LoginManager.getInstance().logInWithReadPermissions(
                (Activity) context, Arrays.asList("public_profile", "email", "user_friends")
        );

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            private ProfileTracker mProfileTracker;
            Intent i = new Intent(context.getApplicationContext(), ProfileActivity.class);

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
                }

                context.startActivity(i);
            }

            @Override
            public void onCancel() {
                Toast.makeText(context.getApplicationContext(),"Sign In To Facebook " +
                        "Is Being Cancelled", Toast.LENGTH_LONG).show();;

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }
}
