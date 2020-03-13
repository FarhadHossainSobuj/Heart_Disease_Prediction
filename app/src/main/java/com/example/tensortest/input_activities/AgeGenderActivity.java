package com.example.tensortest.input_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tensortest.R;

public class AgeGenderActivity extends AppCompatActivity {
    private static final String SHARED_PRE = "user";
    private EditText edtAge;
    private Spinner spGender;
    private Button btnNext, btnPrev;
    private String[] gender = {"Male", "Female"};

    private String age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_gender);
        setTitle("Enter your age, gender");


        initializeViews();
        getPrevValues();

        if(age == null){
            btnPrev.setVisibility(View.GONE);
        } else {
            edtAge.setText(age);
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    setValues();
                    startActivity(new Intent(AgeGenderActivity.this, ChestPainBPSActivity.class));
                }
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtAge.setText("" + age);
            }
        });

    }

    private boolean isValid(){
        boolean flag = true;
        if(edtAge.getText().toString().equals("")){
            edtAge.setError("Fields can be empty");
            flag = false;
        }
        return flag;
    }

    private void getPrevValues() {
        try {
            SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PRE,MODE_PRIVATE);
            age=sharedPreferences.getString("age",null).toString();
        }catch (Exception e){

        }

    }

    private void initializeViews() {
        btnNext = findViewById(R.id.btnNext1);
        btnPrev = findViewById(R.id.btnSetPrev1);

        edtAge = findViewById(R.id.edtAge);
        spGender = findViewById(R.id.spGender);

        ArrayAdapter genderAdaper = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, gender);
        genderAdaper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(genderAdaper);

    }

    private void setValues() {
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PRE,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("age",edtAge.getText().toString());
//        editor.putString("gender",spGender.getSelectedItem().toString());
        editor.putString("gender","" + spGender.getSelectedItemPosition());
        edtAge.setText("");
        editor.commit();
        Toast.makeText(AgeGenderActivity.this, "" + "Age Gender Data saved", Toast.LENGTH_SHORT).show();

    }
}
