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

public class CholBloodActivity extends AppCompatActivity {
    private static final String SHARED_PRE = "user";
    private EditText edtChol;
    private Spinner spFBSS;
    private Button btnNext, btnPrev;
    private String[] exang = { "No", "Yes"};

    private String chol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chol_blood);
        setTitle("Enter your cholesterol, FBS");

        initializeViews();
        getPrevValues();

        if(chol == null){
            btnPrev.setVisibility(View.GONE);
        } else {
            edtChol.setText(chol);
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    setValues();
                    startActivity(new Intent(CholBloodActivity.this, EcgExangOldpeakActivity.class));
                }
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtChol.setText("" + chol);
            }
        });

    }
    private boolean isValid(){
        boolean flag = true;
        if(edtChol.getText().toString().equals("")){
            edtChol.setError("Fields can be empty");
            flag = false;
        }
        return flag;
    }

    private void getPrevValues() {
        try {
            SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PRE,MODE_PRIVATE);
            chol =sharedPreferences.getString("chol",null).toString();
        }catch (Exception e){

        }

    }

    private void initializeViews() {
        btnNext = findViewById(R.id.btnNext3);
        btnPrev = findViewById(R.id.btnSetPrev3);

        edtChol = findViewById(R.id.edtSerumChol);
        spFBSS = findViewById(R.id.spFBS);

        ArrayAdapter adaper = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, exang);
        adaper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFBSS.setAdapter(adaper);
    }

    private void setValues() {
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PRE,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("chol", edtChol.getText().toString());
        editor.putString("fbs", "" + spFBSS.getSelectedItemPosition());
        edtChol.setText("");
        editor.commit();
        Toast.makeText(CholBloodActivity.this, "" + "Data saved", Toast.LENGTH_SHORT).show();

    }
}
