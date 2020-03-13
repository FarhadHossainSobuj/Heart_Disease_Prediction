package com.example.tensortest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String uid;
    private DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        },1300);

    }

    private void getData() {
        try {
            uid = mAuth.getCurrentUser().getUid();
            if(uid != null){
                transitionToActivity();
                finish();
            } else {
                startActivity(new Intent(getApplicationContext(), SigninActivity.class));
                finish();
            }
        } catch (Exception e){
            startActivity(new Intent(getApplicationContext(), SigninActivity.class));
            finish();
        }
    }
    private void transitionToActivity() {
        startActivity(new Intent(SplashScreen.this, HomeActivity.class));
        finish();
    }


}