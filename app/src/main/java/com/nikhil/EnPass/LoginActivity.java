package com.nikhil.EnPass;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText emailId, password;
    Button btnSignIn;
    TextView tvSignUp,verificationtv;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ProgressBar spinner;


    @Override
    protected void onStart() {
        super.onStart();

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        btnSignIn = findViewById(R.id.button2);
        tvSignUp = findViewById(R.id.textView);
        spinner = findViewById(R.id.progressBar1);
        verificationtv = findViewById(R.id.resendverificationtext);
        verificationtv.setOnClickListener(v -> {
            spinner.setVisibility(View.VISIBLE);
            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                    builder1.setMessage("Re-Sent Verification e-Mail \nCheck your email for link!");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "OK",
                            (dialog, id) -> {
                                dialog.cancel();
                                verificationtv.setEnabled(false);
                                verificationtv.setText("");
                                spinner.setVisibility(View.GONE);
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
            });
        });

        spinner.setVisibility(View.GONE);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if(!firstStart){
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        mAuthStateListener = firebaseAuth -> {
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
            if( mFirebaseUser != null ){
                Toast.makeText(LoginActivity.this,"You are logged in",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, PassActivity.class);
                startActivity(i);
                finish();
            }
            else{
                Toast.makeText(LoginActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
            }
        };}

        if(!networkconnectivity()){
            Toast.makeText(LoginActivity.this,"No Internet Connection!!!",Toast.LENGTH_LONG).show();
        }
       //login button code
        btnSignIn.setOnClickListener(v -> {
            String email = emailId.getText().toString();
            String pwd = password.getText().toString();
            //if email field is empty
            if(email.isEmpty()){
                emailId.setError("Please enter email id");
                emailId.requestFocus();
            }
            //if password field is empty
            else  if(pwd.isEmpty()){
                password.setError("Please enter your password");
                password.requestFocus();
            }
            //if both field is empty
            else  if(email.isEmpty() && pwd.isEmpty()){
                Toast.makeText(LoginActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
            }
            //if both field is filled
            else  if(!(email.isEmpty() && pwd.isEmpty())){
                mFirebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(LoginActivity.this, task -> {
                    if (!task.isSuccessful()) {
                        try {
                            throw task.getException();
                        }
                        // if user enters wrong email.
                        catch (FirebaseAuthInvalidUserException invalidEmail) {
                            emailId.setError("Please enter your valid email!");

                        }
                        // if user enters wrong password.
                        catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                            password.setError("Invalid Password\nEnter correct password!");

                        } catch (Exception e) {
                            Log.d("Login exception:", "onComplete: " + e.getMessage());
                        }
                    }
                    else{
                        spinner.setVisibility(View.VISIBLE);
                        //code if email is verified
                        if (mFirebaseAuth.getCurrentUser().isEmailVerified()){
                            //if email is verified but initial setup is remaining
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Detail").child(user.getUid());
                            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() == null) {
                                        Intent i = new Intent(LoginActivity.this, InitialSetupActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        SharedPreferences prefs1 = getSharedPreferences("prefs", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = prefs1.edit();
                                        editor.putBoolean("firstStart", false);
                                        editor.apply();
                                        Intent i = new Intent(LoginActivity.this, PassActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                        else{
                            spinner.setVisibility(View.GONE);
                            //alert dialog of email not verified
                            verificationtv.setEnabled(true);
                            verificationtv.setText("Click here to re-send VERIFICATION LINK!");
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                            builder1.setMessage("Email not Verified.\ncheck your email for verification and Login again!!");
                            builder1.setCancelable(true);
                            builder1.setPositiveButton(
                                    "OK",
                                    (dialog, id) -> dialog.cancel());
                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                    }

                });
            }
            else{
                Toast.makeText(LoginActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
            }
        });


        //change to signup page code
        tvSignUp.setOnClickListener(v -> {
            Intent intSignUp = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intSignUp);
            finish();
        });
    }

    //code for forgot pass activity
    public void forgotpass(View view) {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final String email = emailId.getText().toString();
        if(email.length() == 0){
            Toast.makeText(LoginActivity.this,"Please enter your valid email",Toast.LENGTH_LONG).show();
            emailId.setFocusable(true);
        }
        else{

            auth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(task -> {
                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                        if (isNewUser) {
                            emailId.setError("Please enter valid email");
                        } else {
                            auth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this, "Password reset mail sent succesfully on your registered email!!", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Error occured! \n Please Try again!!", Toast.LENGTH_LONG).show();
                                        }
                                    });
                            }
                    });
        }
    }

    //network connection
    public Boolean networkconnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }
}
