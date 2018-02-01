package com.apps.synclogin.syncloginapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.apps.synclogin.syncloginapp.db.SQLiteHelper;
import com.apps.synclogin.syncloginapp.util.UserProfile;
import com.firebase.ui.auth.User;

/**
 * Created by robert.arifin on 31/01/2018.
 */

public class SignUpActivity extends AppCompatActivity {
    EditText name, email, password, confirmPassword;
    FrameLayout errorEmail, errorPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.Username);
        email = findViewById(R.id.emailInput);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        errorEmail = findViewById(R.id.error_email_container);
        errorPassword = findViewById(R.id.error_password_container);
    }

    public void signUpComplete (View v) {
        errorPassword.setVisibility(View.INVISIBLE);
        errorEmail.setVisibility(View.INVISIBLE);

        UserProfile result;
        UserProfile user = new UserProfile();
        user.setEmail(email.getText().toString());

        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        result = sqLiteHelper.checkRecord(user, SQLiteHelper.COLUMN_EMAIL);

        if (result == null) {
            if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("email", email.getText().toString());
                intent.putExtra("password", password.getText().toString());
                intent.putExtra("loginType", SQLiteHelper.COLUMN_EMAIL);

                startActivity(intent);
            } else {
                errorPassword.setVisibility(View.VISIBLE);
            }
        } else {
            errorEmail.setVisibility(View.VISIBLE);
        }
    }
}
