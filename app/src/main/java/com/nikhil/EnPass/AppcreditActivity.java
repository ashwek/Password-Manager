package com.nikhil.EnPass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;

import com.nikhil.EnPass.R;

public class AppcreditActivity extends AppCompatActivity {
    ScrollView scrollView;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appcredit);
        scrollView = findViewById(R.id.creditlinearlayout);
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animcredit);
        scrollView.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                animation.setDuration(5);
                Intent i = new Intent(AppcreditActivity.this,SelectionDashboardActivity.class);
                startActivity(i);
                finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
