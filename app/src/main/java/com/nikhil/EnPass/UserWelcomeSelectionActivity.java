package com.nikhil.EnPass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.nikhil.EnPass.R;

public class UserWelcomeSelectionActivity extends AppCompatActivity {
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_welcome_selection);
        linearLayout = findViewById(R.id.welcomescreenbuttongrp);
        //shared preference
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if(!(firstStart)){
            Intent i = new Intent(UserWelcomeSelectionActivity.this,PassActivity.class);
            startActivity(i);
            finish();
        }

        Animation aniSlidebtn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slideup);
        linearLayout.startAnimation(aniSlidebtn);
    }

    public void opensignup(View view) {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slideup,R.anim.slidedown);
    }

    public void opensignin(View view) {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slideup,R.anim.slidedown);
    }

}
