package com.nikhil.SettingModules;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.nikhil.EnPass.R;

public class FaqActivity extends AppCompatActivity {
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        findViewById(R.id.q1).setOnTouchListener((v, event) -> {
            viewgone();
            findViewById(R.id.a1).setVisibility(View.VISIBLE);
            return true;
        });

        findViewById(R.id.q2).setOnTouchListener((v, event) -> {
            viewgone();
            findViewById(R.id.a2).setVisibility(View.VISIBLE);
            return true;
        });

        findViewById(R.id.q3).setOnTouchListener((v, event) -> {
            viewgone();
            findViewById(R.id.a3).setVisibility(View.VISIBLE);
            return true;
        });

        findViewById(R.id.q4).setOnTouchListener((v, event) -> {
            viewgone();
            findViewById(R.id.a4).setVisibility(View.VISIBLE);
            return true;
        });

        findViewById(R.id.q5).setOnTouchListener((v, event) -> {
            viewgone();
            findViewById(R.id.a5).setVisibility(View.VISIBLE);
            return true;
        });

        findViewById(R.id.q6).setOnTouchListener((v, event) -> {
            viewgone();
            findViewById(R.id.a6).setVisibility(View.VISIBLE);
            return true;
        });

        findViewById(R.id.q7).setOnTouchListener((v, event) -> {
            viewgone();
            findViewById(R.id.a7).setVisibility(View.VISIBLE);
            return true;
        });

        findViewById(R.id.q8).setOnTouchListener((v, event) -> {
            viewgone();
            findViewById(R.id.a8).setVisibility(View.VISIBLE);
            return true;
        });

        findViewById(R.id.q9).setOnTouchListener((v, event) -> {
            viewgone();
            findViewById(R.id.a9).setVisibility(View.VISIBLE);
            return true;
        });

        findViewById(R.id.q10).setOnTouchListener((v, event) -> {
            viewgone();
            findViewById(R.id.a10).setVisibility(View.VISIBLE);
            return true;
        });

        findViewById(R.id.q11).setOnTouchListener((v, event) -> {
            viewgone();
            findViewById(R.id.a11).setVisibility(View.VISIBLE);
            return true;
        });
    }

    private void viewgone() {
        findViewById(R.id.a1).setVisibility(View.GONE);
        findViewById(R.id.a2).setVisibility(View.GONE);
        findViewById(R.id.a3).setVisibility(View.GONE);
        findViewById(R.id.a4).setVisibility(View.GONE);
        findViewById(R.id.a5).setVisibility(View.GONE);
        findViewById(R.id.a6).setVisibility(View.GONE);
        findViewById(R.id.a7).setVisibility(View.GONE);
        findViewById(R.id.a8).setVisibility(View.GONE);
        findViewById(R.id.a9).setVisibility(View.GONE);
        findViewById(R.id.a10).setVisibility(View.GONE);
        findViewById(R.id.a11).setVisibility(View.GONE);

    }
}
