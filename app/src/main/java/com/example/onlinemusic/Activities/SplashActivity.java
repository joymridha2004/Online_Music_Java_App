package com.example.onlinemusic.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.onlinemusic.Fragments.SignInFragment;
import com.example.onlinemusic.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences(SignInFragment.PREFS_NAME, 0);
                boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn", false);
                Intent intent = null;
                if (hasLoggedIn) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    finish();
                } else {
                    intent = new Intent(SplashActivity.this, RegisterActivity.class);
                    finish();
                }
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}