package com.myspecialist.UserInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myspecialist.Common.Common;
import com.myspecialist.R;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    TextInputEditText name, email, password, confirmPassword;
    TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword, inputLayoutCpassword;

    TextView termsOfService;
    Button button;
    CheckBox checkBox;

    FirebaseAuth mAuth;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        button = findViewById(R.id.signUp);
        progressBar = findViewById(R.id.progressBar);
        termsOfService = findViewById(R.id.terms_of_service);
        inputLayoutName = findViewById(R.id.inputLayoutName);
        inputLayoutEmail = findViewById(R.id.inputLayoutEmail);
        inputLayoutPassword = findViewById(R.id.inputLayoutPassword);
        inputLayoutCpassword = findViewById(R.id.inputLayoutCPassword);
        checkBox = findViewById(R.id.checkbox);
        relativeLayout = findViewById(R.id.relative_layout);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    button.setEnabled(true);
                }
                else {
                    button.setEnabled(false);
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.user_name);
        email = findViewById(R.id.user_email);
        password = findViewById(R.id.user_password);
        confirmPassword = findViewById(R.id.user_confirm_password);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isConnectedToInternet(SignupActivity.this)){
                    SignUp();
                }
                else {
                    Toast.makeText(SignupActivity.this, "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        termsOfService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(SignupActivity.this);
                dialog.setContentView(R.layout.terms_of_service);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
                dialog.setCancelable(false);
                Button accept = dialog.findViewById(R.id.accept);
                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(SignupActivity.this, "Thank you", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                Button decline = dialog.findViewById(R.id.decline);
                decline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        System.exit(0);
                    }
                });
            }
        });

    }

    private void SignUp() {

        String Name = name.getText().toString();
        String Email = email.getText().toString();
       // String Phone = phone.getText().toString();
        String Password = password.getText().toString();
        String ConfirmPassword = confirmPassword.getText().toString();

        if (TextUtils.isEmpty(Name)){
            inputLayoutName.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(Email)){
            inputLayoutEmail.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(Password)){
            inputLayoutPassword.setError("Required");
            return;
        }
        if (Password.length()<6){
            inputLayoutPassword.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(ConfirmPassword)){
            inputLayoutCpassword.setError("Required");
            return;
        }
        if (!ConfirmPassword.equals(Password)){
            inputLayoutCpassword.setError("Passwords do not match");
            inputLayoutPassword.setError("Passwords do not match");
            return;
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        onAuthSuccess(task.getResult().getUser());
                        progressBar.setVisibility(View.GONE);
                    }
                    else {

                        Snackbar.make(relativeLayout, "Could not register your account, try again", Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(getResources().getColor(android.R.color.holo_red_dark))
                                .setAction("Action", null).show();
                    }
                }
            });
        }
    }

    private void onAuthSuccess(FirebaseUser user) {
        String Name = name.getText().toString();
        String Email = email.getText().toString();
        //String Phone = phone.getText().toString();

        user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(Name).build();
        user.updateProfile(profileUpdates);

        String user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        Map newUsers = new HashMap();
        newUsers.put("Name", Name);
        newUsers.put("Email", Email);
        //newUsers.put("Phone", Phone);
        databaseReference.setValue(newUsers);

        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

        Toast.makeText(getApplicationContext(), "Welcome "+Name, Toast.LENGTH_SHORT).show();
    }
}