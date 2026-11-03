package com.itismob.s03.groupiseight.adapted;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.itismob.s03.groupiseight.adapted.activities.HomeActivity;
import com.itismob.s03.groupiseight.adapted.activities.LoginActivity;
import com.itismob.s03.groupiseight.adapted.helpers.AuthManager;

/**
 * Launcher activity with no screen of its own. It only decides where to go:
 * Home if a user is already logged in, otherwise Login.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean loggedIn = new AuthManager(this).isLoggedIn();
        Class<?> destination = loggedIn ? HomeActivity.class : LoginActivity.class;
        startActivity(new Intent(this, destination));
        finish();
    }
}
