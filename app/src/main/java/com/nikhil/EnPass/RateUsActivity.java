package com.nikhil.EnPass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nikhil.FirebaseFunction.Rateus;
import com.nikhil.EnPass.R;

public class RateUsActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reff;
    Rateus rate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);
        RatingBar ratingRatingBar = findViewById(R.id.rateus);
        Button submitButton = findViewById(R.id.submit_button);
        rate = new Rateus();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reff= FirebaseDatabase.getInstance().getReference().child("User Rating");

        submitButton.setOnClickListener(v -> {
            float rating = ratingRatingBar.getRating();
            rate.setRatenum(rating);
            reff.child(user.getUid()).setValue(rate);
            Intent i = new Intent(RateUsActivity.this,SelectionDashboardActivity.class);
            startActivity(i);
            finish();
        });
    }
}
