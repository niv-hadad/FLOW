package com.niv.flow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.niv.flow.models.HelperDB;

public class SplashActivity extends AppCompatActivity {

    HelperDB helperDB;
    Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initComponent();
        SharedPreferences sharedPreferences = getSharedPreferences("com.niv.flow.SharedPreferences", MODE_PRIVATE);
        String currentUserId = sharedPreferences.getString("current_user", "");
        if (helperDB.hasAnyUsers()) {
            if (currentUserId.isEmpty()) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                        startActivity(intent);
                    }
                }, 2000);
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, 2000);
            }
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, OnboardingActivity.class);
                    startActivity(intent);
                }
            }, 2000);
        }
    }
    private void initComponent() {
        helperDB = new HelperDB(this);
    }
}