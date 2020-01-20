package com.example.tensortest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;

public class UserLoginActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private EditText usernameText;
    private EditText passwordText;
    private Button signUpButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);


        usernameText = (EditText) findViewById(R.id.username);
        passwordText = (EditText) findViewById(R.id.password);
        signUpButton = (Button) findViewById(R.id.signup);
        loginButton = (Button) findViewById(R.id.login);

        userViewModel = ViewModelProviders.of(this, new UserViewModel.Factory(getApplicationContext())).get(UserViewModel.class);

        Boolean signup = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("signup", true);


        if(signup)
        {
            signUpButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            setTitle("Please Sign Up First");
        }
        else
        {
            signUpButton.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
            setTitle("Please Login");
        }
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userViewModel.createUser(usernameText.getText().toString(), passwordText.getText().toString());
                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                        .putBoolean("signup", false).commit();
                Toast.makeText(getBaseContext(), "Successfully Created An Account!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();

                try {
                    boolean isValid = userViewModel.checkValidLogin(""+username,""+ password);
                    if(isValid)
                    {
                        Toast.makeText(getBaseContext(), "Successfully Logged In!", Toast.LENGTH_LONG).show();
                        Log.i("Successful_Login", "Login was successful");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "Invalid Login!", Toast.LENGTH_SHORT).show();
                        Log.i("Unsuccessful_Login", "Login was not successful");
                    }
                } catch (Exception e){
                    Toast.makeText(UserLoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
