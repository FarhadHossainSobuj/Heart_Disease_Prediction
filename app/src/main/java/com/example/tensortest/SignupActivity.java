package com.example.tensortest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText edtFullName, edtUsername, edtPassword, edtMobile, edtEmail;
    private Button btnSignup;
    private TextView tvGoToSignin;
    private String fullName, userName, userPassword,  userPhone, userEmail;
    private static final String user = "users";

    private static final String SHARED_PRE = "user";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        initializeInput();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid()){
                    signUp();
                }

            }
        });
        tvGoToSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(intent);
            }
        });
    }
    private void signUp() {
        getInputValues();
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Toast.makeText(SignupActivity.this, "Signing up Successfull", Toast.LENGTH_SHORT).show();
                    DatabaseReference fbDB = FirebaseDatabase.getInstance().getReference()
                            .child("users").child(task.getResult()
                                    .getUser().getUid());
                    fbDB.child("full_name").setValue(edtFullName.getText().toString());
                    fbDB.child("username").setValue(edtUsername.getText().toString());
                    fbDB.child("password").setValue(edtPassword.getText().toString());
                    fbDB.child("mobile").setValue(edtMobile.getText().toString());
                    fbDB.child("email").setValue(edtEmail.getText().toString());
                    transitionToActivity();
                } else {
                    Toast.makeText(SignupActivity.this, "Sign up Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void transitionToActivity() {
        startActivity(new Intent(SignupActivity.this, HomeActivity.class));
        finish();
    }

    private void getInputValues() {
        fullName = edtFullName.getText().toString().trim();
        userName = edtUsername.getText().toString().trim();
        userPassword = edtPassword.getText().toString().trim();
        userEmail = edtEmail.getText().toString().trim();
        userPhone = edtMobile.getText().toString().trim();
    }

    private void initializeInput() {
        edtFullName = findViewById(R.id.edtSignupFullName);
        edtUsername = findViewById(R.id.edtSignupUsername);
        edtPassword = findViewById(R.id.edtSignupPassword);
        edtMobile = findViewById(R.id.edtSignupMobile);
        edtEmail = findViewById(R.id.edtSignupEmail);

        btnSignup = findViewById(R.id.btnSignup);

        tvGoToSignin = findViewById(R.id.tvGoToSignin);
    }
    private boolean isValid() {
        boolean flag = true;
        getInputValues();
        if(TextUtils.isEmpty(fullName)){
            edtFullName.setError("Full name is empty");
            flag = false;
        }
        if(TextUtils.isEmpty(userName)){
            edtUsername.setError("Username is empty");
            flag = false;
        }
        if(TextUtils.isEmpty(userPassword)){
            edtPassword.setError("Password is empty");
            flag = false;
        }

        if(TextUtils.isEmpty(userPhone)){
            edtMobile.setError("Mobile is empty");
            flag = false;
        }
        if(TextUtils.isEmpty(userEmail)){
            edtEmail.setError("Email is empty");
            flag = false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            edtEmail.setError("Email is not valid");
            flag = false;
        }
        if(userPassword.length() < 6){
            edtPassword.setError("Password must be above 6 character");
            flag = false;
        }
        return flag;
    }
}
