package com.nikhil.EnPass;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.nikhil.EnPass.R;
import com.ybs.passwordstrengthmeter.PasswordStrength;

public class MainActivity extends AppCompatActivity implements TextWatcher {
    TextInputEditText emailId, password;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.emailtext);
        password = findViewById(R.id.password);
        btnSignUp = findViewById(R.id.signup);
        tvSignIn = findViewById(R.id.textView);
        ProgressBar spinner = findViewById(R.id.progressBar1);

        spinner.setVisibility(View.GONE);

        //password bar
        password.addTextChangedListener( this);



        //signup button on click
        btnSignUp.setOnClickListener(v -> {
            final String email = emailId.getText().toString();
            final String pwd = password.getText().toString();
            if(email.isEmpty()){
                emailId.setError("Please enter email id");
                emailId.requestFocus();
            }
            else  if(pwd.isEmpty()){
                password.setError("Please enter your password");
                password.requestFocus();
            }
            else if(!networkconnectivity()){
                Toast.makeText(MainActivity.this,"No Internet Connection!!!",Toast.LENGTH_LONG).show();
            }
            else{
                add();
            }
        });

        //login intent
        tvSignIn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        });
    }

    // email validator
    private boolean validemail(){
        String email = emailId.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.matches(emailPattern))
        {
            return true;
        }
        else
        {
            Toast.makeText(MainActivity.this,"Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //network connection
    public Boolean networkconnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else
            return false;
    }
    //signup function
    private void add(){
        if(validemail()){
            final String email = emailId.getText().toString();
            final String pwd = password.getText().toString();
            mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, task -> {
                if(!task.isSuccessful()){
                    try
                    {
                        throw task.getException();
                    }
                    // if user enters weak password.
                    catch (FirebaseAuthWeakPasswordException weakPassword)
                    {
                        password.setError("Weak Password. Please enter strong Password!!");
                    }
                    // if user enters invalid email.
                    catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                    {
                        emailId.setError("Invalid Email!");
                    }
                    // if user enters existing email
                    catch (FirebaseAuthUserCollisionException existEmail)
                    {
                        Toast.makeText(MainActivity.this,"Given email is already registered!\n Try login",Toast.LENGTH_SHORT).show();

                    }
                    catch (Exception e)
                    {
                        Log.d("Signup Error:", "onComplete: " + e.getMessage());
                    }
                }

                else {
                    mFirebaseAuth.getCurrentUser().sendEmailVerification()
                            .addOnCompleteListener(MainActivity.this, task1 -> {
                                if (task1.isSuccessful()) {
                                    //Alert dialog
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                                    builder1.setMessage("Signup sucessful!\nCheck your email for validation link!");
                                    builder1.setCancelable(true);

                                    builder1.setPositiveButton(
                                            "OK",
                                            (dialog, id) -> {
                                                dialog.cancel();
                                                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                                                startActivity(i);
                                                finish();
                                            });

                                    AlertDialog alert11 = builder1.create();
                                    alert11.show();
                                }
                            });
                }
            });
            return;
        }
        emailId.setError("Please enter credentials!!");
    }



//password strength progress bar
    @Override
    public void afterTextChanged(Editable s) {
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updatePasswordStrengthView(s.toString());
    }

    private void updatePasswordStrengthView(String password) {

        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView strengthView = findViewById(R.id.password_strength);
        if (TextView.VISIBLE != strengthView.getVisibility())
            return;

        if (password.isEmpty()) {
            strengthView.setText("");
            progressBar.setProgress(0);
            return;
        }

        PasswordStrength str = PasswordStrength.calculateStrength(password);
        strengthView.setText(str.getText(this));
        strengthView.setTextColor(str.getColor());

        progressBar.getProgressDrawable().setColorFilter(str.getColor(), android.graphics.PorterDuff.Mode.SRC_IN);
        if (str.getText(this).equals("Weak")) {
            progressBar.setProgress(25);
        } else if (str.getText(this).equals("Medium")) {
            progressBar.setProgress(50);
        } else if (str.getText(this).equals("Strong")) {
            progressBar.setProgress(75);
        } else {
            progressBar.setProgress(100);
        }
    }
}

