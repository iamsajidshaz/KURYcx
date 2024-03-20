package com.shaztech.kurycx;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class MainLoadingScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_loading_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // init
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // check if user already logged in?
        if (currentUser != null) {
            // goto home page
            startHomePageThread();

        } else {
            // goto login page
            startLoginThread();
        }


    }
    private void startHomePageThread() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i=new Intent(getBaseContext(),HomePageActivity.class);
                startActivity(i);
                finish();
            }
        }, 5000);
    }

    private void startLoginThread() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i=new Intent(getBaseContext(),LoginPageActivity.class);
                startActivity(i);
                finish();
            }
        }, 5000);
    }
}