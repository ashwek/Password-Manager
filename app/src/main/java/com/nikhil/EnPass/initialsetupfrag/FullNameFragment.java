package com.nikhil.EnPass.initialsetupfrag;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nikhil.EnPass.R;


public class FullNameFragment extends Fragment {
    EditText fname,mname,lname;
    FloatingActionButton submit;

    public FullNameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_full_name, container, false);
        fname = view.findViewById(R.id.fname);
        fname.setText("");
        mname = view.findViewById(R.id.mname);
        lname = view.findViewById(R.id.lname);
        submit = view.findViewById(R.id.submit);

        submit.setOnClickListener(v -> {
            if(fname.getText().toString().equals("")){
                fname.setError("First name field can't be empty");
            }
            else if(lname.getText().toString().equals("")){
                lname.setError("Last name Required!");
            }
            else{
                MpinFragment fragment = new MpinFragment();
                Bundle bundle = new Bundle();
                bundle.putString("firstname",fname.getText().toString());
                bundle.putString("middlename",mname.getText().toString());
                bundle.putString("lastname",lname.getText().toString());
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }
}
