package com.pickupapp.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.pickupapp.R;

public class Splash extends AppCompatActivity {
    private static final int SPLASH_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splash.this, DrawerLocador.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
