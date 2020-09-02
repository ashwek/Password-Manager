package com.nikhil.EnPass.initialsetupfrag;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nikhil.FirebaseFunction.Detail;
import com.nikhil.EnPass.R;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class MasterPassFragment extends Fragment {
    private EditText mpass,cmpass;
    private DatabaseReference reff;
    private Detail detail;

    public MasterPassFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_master_pass, container, false);
        mpass = view.findViewById(R.id.mpass);
        cmpass = view.findViewById(R.id.cmpass);
        FloatingActionButton submit = view.findViewById(R.id.submit);

        Bundle bundle1 = this.getArguments();
        String fname = bundle1.getString("firstnamemp");
        Log.d(TAG, "onCreateView: firstname in mpass"+fname);
        String mname = bundle1.getString("middlenamemp");
        String lname = bundle1.getString("lastnamemp");
        String mpin = bundle1.getString("mpin");

        detail=new Detail();
        reff= FirebaseDatabase.getInstance().getReference().child("Detail");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        submit.setOnClickListener(v -> {
            if(validpass()){
                submit(fname,mname,lname,mpin,mpass.getText().toString());
                WelcomefinalFragment fragment = new WelcomefinalFragment();
                Bundle bun = new Bundle();
                bun.putString("fname",fname);
                fragment.setArguments(bun);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
            }
        });
        return view;
    }
    private boolean validpass(){
        String pass1 = mpass.getText().toString();
        String pass2 = cmpass.getText().toString();
        if(pass1.length() <8){
            mpass.setError("Minimum 8 length password required");
            return false;
        }
        else if(pass2.length() < 8){
            cmpass.setError("Minimum 8 length password required");
            return false;
        }
        else if (!(pass1.equals(pass2))){
            cmpass.setError("both password not matched!!");
            return false;
        }
        else {
            mpass.setError(null);
            cmpass.setError(null);
            return true;
        }
    }

    //random string generator for IV
    public static class RandomString {
        static String getAlphaNumericString() {
            String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz" + "@#$%_/-+*";
            StringBuilder sb = new StringBuilder(16);
            for (int i = 0; i < 16; i++) {
                int index = (int) (AlphaNumericString.length() * Math.random());
                sb.append(AlphaNumericString.charAt(index));
            }
            return new String(Base64.encode(sb.toString().getBytes() ,Base64.DEFAULT), StandardCharsets.UTF_8);
        }
    }

    //code for firebase submit
    private void submit(String fname, String mname, String lname, String mpin, String mpass){
            SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();

            String ran = RandomString.getAlphaNumericString();
            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            String userid =user.getUid();
            String email =user.getEmail();
            detail.setUid(userid);
            detail.setEmail(email);
            detail.setFname(fname);
            detail.setMname(mname);
            detail.setLname(lname);
            String encmpass = new String(Base64.encode(mpass.getBytes() ,Base64.DEFAULT), StandardCharsets.UTF_8);
            detail.setMpass(encmpass);
            detail.setMpin(Integer.valueOf(mpin));
            detail.setRan(ran);
            reff.child(userid).setValue(detail);
            Toast.makeText(getActivity(),"data inserted successfully",Toast.LENGTH_LONG).show();
    }
}
