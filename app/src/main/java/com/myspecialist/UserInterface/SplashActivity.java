package com.myspecialist.UserInterface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myspecialist.Common.Common;
import com.myspecialist.R;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        if (Common.isConnectedToInternet(SplashActivity.this)){
            if (mAuth.getCurrentUser() !=null){
                onAuthSuccess(mAuth.getCurrentUser());
            }
        }
        else {
            Toast.makeText(SplashActivity.this, "Check your internet connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Button btn = (Button)findViewById(R.id.btnsignin);
        Button btn1 = (Button)findViewById(R.id.btnsignup);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, SignupActivity.class);
                startActivity(intent);
            }

        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onAuthSuccess(FirebaseUser currentUser) {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}