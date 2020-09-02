package com.nikhil.EnPass.initialsetupfrag;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nikhil.EnPass.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MpinFragment extends Fragment {
    EditText mpin,cmpin;
    FloatingActionButton submit;

    public MpinFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mpin, container, false);
        mpin = view.findViewById(R.id.mpin);
        cmpin = view.findViewById(R.id.cmpin);
        submit = view.findViewById(R.id.submit);
        Bundle bundle = this.getArguments();
        String fname = bundle.getString("firstname");
        String mname = bundle.getString("middlename");
        String lname = bundle.getString("lastname");
        submit.setOnClickListener(v -> {
            if(validpin()){
                MasterPassFragment fragment = new MasterPassFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("firstnamemp",fname);
                Log.d(TAG, "onCreateView: firstname in mpin"+fname);
                bundle1.putString("middlenamemp",mname);
                Log.d(TAG, "onCreateView: middlename in mpin"+mname);
                bundle1.putString("lastnamemp",lname);
                Log.d(TAG, "onCreateView: lastname in mpin"+lname);
                bundle1.putString("mpin",mpin.getText().toString());
                fragment.setArguments(bundle1);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            else{
                mpin.setError("Please enter Pin");
            }
        });
        return view;
    }

    private boolean validpin(){
        if(mpin.getText().toString().length() != 4 )
        {
            mpin.setError("enter 4 digit pin");
        }else if(cmpin.getText().toString().length() != 4){
            cmpin.setError("Re-enter 4 digit pin");
        }
        else if(!mpin.getText().toString().equals(cmpin.getText().toString())) {
            cmpin.setError("M-Pin not matched!!");
        }
        else{
            return true;
        }
        return false;
    }
}
