package com.nikhil.EnPass.initialsetupfrag;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nikhil.EnPass.PassActivity;
import com.nikhil.EnPass.R;


public class WelcomefinalFragment extends Fragment {
    Button btn;
    TextView msg;
    public WelcomefinalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_welcomefinal, container, false);
        msg = view.findViewById(R.id.welcomemessage);
        btn = view.findViewById(R.id.continuebtn);
        Bundle b = this.getArguments();
        String name = b.getString("fname");
        msg.setText("Welcome "+name+" to Password Protect Family.\nWe are here for your data's security!");

        btn.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), PassActivity.class);
            startActivity(i);
            getActivity().finish();
        });
        return view;
    }
}
