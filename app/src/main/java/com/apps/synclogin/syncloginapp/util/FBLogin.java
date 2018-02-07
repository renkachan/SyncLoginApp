package com.apps.synclogin.syncloginapp.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;

import com.apps.synclogin.syncloginapp.LoginActivity;
import com.apps.synclogin.syncloginapp.ProfileActivity;
import com.apps.synclogin.syncloginapp.db.SQLiteHelper;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.ui.auth.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.Arrays;

/**
 * Created by robert.arifin on 29/01/2018.
 */

public class FBLogin {
    private ProfileTracker mProfileTracker;
    private Context context;
    String id, name;

    public FBLogin(Context context) {
        this.context = context;
    }

    public void loginAndFetchData (CallbackManager callbackManager) {
        LoginManager.getInstance().logInWithReadPermissions(
                (Activity) context, Arrays.asList("public_profile", "email", "user_friends")

        );


        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            Intent i = new Intent(context.getApplicationContext(), ProfileActivity.class);

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("access token", loginResult.getAccessToken().getToken());
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            id = object.getString("id");
                            if(object.has("name"))
                                name = object.getString("name");
                            Log.d("name", object.toString(4));

                            i.putExtra("name", name);
                            i.putExtra("id", id);
                            i.putExtra("loginType", SQLiteHelper.COLUMN_FB_ID);

                            context.startActivity(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(context.getApplicationContext(),"Sign In To Facebook " +
                        "Is Being Cancelled", Toast.LENGTH_LONG).show();;

            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
            }
        });
    }

    public String getFBId (CallbackManager callbackManager) {
//        LoginManager.getInstance().logInWithReadPermissions(
//                (Activity) context, Arrays.asList("public_profile", "email", "user_friends")
//        );
        final UserProfile user = new UserProfile();
        final Profile profile;

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {
                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            mProfileTracker.stopTracking();
                            Profile.setCurrentProfile(currentProfile);
                        }
                    };

                    mProfileTracker.startTracking();
                } else {
                    Profile profile = Profile.getCurrentProfile();
                    String testId = profile.getId();
                }
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

        return Profile.getCurrentProfile().getId();
    }
}
