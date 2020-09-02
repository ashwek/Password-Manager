package com.nikhil.EnPass.initialsetupfrag;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nikhil.EnPass.R;


public class WelcomeInitialFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_initial, container, false);
        Button start = view.findViewById(R.id.start);
        start.setOnClickListener(v -> {
            UserImgFragment fragment = new UserImgFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slideleft,R.anim.slideright);
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        });
        return view;
    }
}
