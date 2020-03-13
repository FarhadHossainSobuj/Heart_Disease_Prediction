package com.example.tensortest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SigninActivity extends AppCompatActivity {
    private EditText edtLoginEmail, edtLoginPassword;
    private Button btnLogin;
    private TextView tvGoToSignup;
    private Spinner spLoginRole;

    private String loginEmail, loginPassword, loginRole;
    public String role = "";
    public String uid;

    private static final String SHARED_PRE = "user";
    private String[] roles = {"Athlete", "Coach", "Nutritionist"};

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        getSupportActionBar().hide();

        progressBar = findViewById(R.id.progressBar);

        initializeInput();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("users");
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid()){
                    progressBar.setVisibility(View.VISIBLE);
                    signIn();

                }
            }
        });
        tvGoToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initializeInput() {
        edtLoginEmail = findViewById(R.id.edtLoginUsername);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);

        tvGoToSignup = findViewById(R.id.tvGotoRegister);

    }
    private boolean isValid() {
        boolean flag = true;
        getInputValues();
        if(TextUtils.isEmpty(loginPassword)){
            edtLoginPassword.setError("Password is empty");
            flag = false;
        }
        if(TextUtils.isEmpty(loginEmail)){
            edtLoginEmail.setError("Email is empty");
            flag = false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(loginEmail).matches()){
            edtLoginEmail.setError("Email is not valid");
            flag = false;
        }
        return flag;
    }
    private void getInputValues() {
        loginEmail = edtLoginEmail.getText().toString().trim();
        loginPassword = edtLoginPassword.getText().toString().trim();
    }
    private void signIn(){
        getInputValues();
        mAuth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    transitionToActivity();
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(SigninActivity.this, "Signing in Failed", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    private void logout() {
        mAuth.signOut();
    }

    private void transitionToActivity() {
        startActivity(new Intent(SigninActivity.this, HomeActivity.class));
        finish();
    }

}
