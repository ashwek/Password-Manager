package com.nikhil.EnPass;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.nikhil.EnPass.R;
import com.nikhil.EnPass.initialsetupfrag.WelcomeInitialFragment;



public class InitialSetupActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setup);
        getSupportFragmentManager().beginTransaction().add(R.id.content_frame,new WelcomeInitialFragment()).commit();
    }

}
