package com.nikhil.EnPass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PassActivity extends AppCompatActivity {

    private String TRUE_CODE;
    FirebaseAuth auth;
    FirebaseUser user;
    public static String firebasemasterpassword;
    public static String repeatedmsterpass;
    public static String firebaserandomnumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstlogin = prefs.getBoolean("firstlogin", true);
        if(!firstlogin){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);}
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Detail").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TRUE_CODE = dataSnapshot.child("mpin").getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        String TAG = LoginActivity.class.getSimpleName();


        PinLockView mPinLockView = findViewById(R.id.pin_lock_view);
        IndicatorDots mIndicatorDots = findViewById(R.id.indicator_dots);

        mPinLockView.attachIndicatorDots(mIndicatorDots);

        mPinLockView.setPinLength(4);
        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                if (pin.equals(TRUE_CODE)) {
                    SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("firstlogin", false);
                    editor.apply();
                    fetchusercred();

                    Intent intent = new Intent(PassActivity.this, SelectionDashboardActivity.class);
                    intent.putExtra("code", pin);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(PassActivity.this, "Wrong Pin, try again!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onEmpty(){
            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {
            }
        });
    }

    private void fetchusercred() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Detail").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String mpass = dataSnapshot.child("mpass").getValue().toString();
                firebasemasterpassword = new String(Base64.decode(mpass.getBytes(), Base64.DEFAULT));
                String random = dataSnapshot.child("ran").getValue().toString();
                firebaserandomnumber = new String(Base64.decode(random.getBytes(), Base64.DEFAULT));
                repeatedmsterpass = RepeatKey.generateKey(32,firebasemasterpassword);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //padding of master pass
    public static class RepeatKey {
        public static String generateKey(int x, String key) {
            StringBuilder keyBuilder = new StringBuilder(key);
            for (int i = 0; ; i++) {
                if (x == i)
                    i = 0;
                if (keyBuilder.length() == x)
                    break;
                keyBuilder.append(keyBuilder.charAt(i));
            }
            key = keyBuilder.toString();
            return key;
        }
    }
}

