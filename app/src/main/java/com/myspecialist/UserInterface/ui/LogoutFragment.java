package com.myspecialist.UserInterface.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.myspecialist.R;
import com.myspecialist.UserInterface.SplashActivity;

public class LogoutFragment extends Fragment {

    FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        mAuth = FirebaseAuth.getInstance();

        mAuth.signOut();

        Intent intent = new Intent(getContext(), SplashActivity.class);
        startActivity(intent);
        getActivity().finish();

        return root;
    }
}