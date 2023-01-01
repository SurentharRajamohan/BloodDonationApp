package com.blooddonationapp.startactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               Intent homePageIntent = new Intent(SplashScreen.this, LoginActivity.class);
               startActivity(homePageIntent);
               finish();
           }
       }, 2000);

    }
}