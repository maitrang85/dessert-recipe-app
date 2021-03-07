package fi.group6.dessertrecipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class ActivitySplash extends AppCompatActivity {

    protected static final String SHARED_PREF_FILE = "userTheme";
    protected static final String THEME_KEY = "theme";

    String theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        //Get the shared preferences data and update the switch because onStart already ran once
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_FILE, ActivitySplash.MODE_PRIVATE);
        theme = sharedPreferences.getString(THEME_KEY, "light");

        if(theme.equals("light")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ActivitySplash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }
}