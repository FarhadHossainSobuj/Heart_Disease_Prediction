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

public class EcgExangOldpeakActivity extends AppCompatActivity {
    private static final String SHARED_PRE = "user";
    private EditText edtOldPeak;
    private Spinner spECGLevel, spExang;
    private Button btnNext, btnPrev;
    private String[] exang = { "No", "Yes"};
    private String[] EcgAndSlope = {"0", "1", "2"};

    private String oldpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecg_exang_oldpeak);
        setTitle("Enter your ecg, exang, oldpeak");

        initializeViews();
        getPrevValues();

        if(oldpeak == null){
            btnPrev.setVisibility(View.GONE);
        } else {
            edtOldPeak.setText(oldpeak);
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    setValues();
                    startActivity(new Intent(EcgExangOldpeakActivity.this, SlopeThalScanActivity.class));
                }
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtOldPeak.setText("" + oldpeak);
            }
        });

    }
    private boolean isValid(){
        boolean flag = true;
        if(edtOldPeak.getText().toString().equals("")){
            edtOldPeak.setError("Fields can be empty");
            flag = false;
        }
        return flag;
    }

    private void getPrevValues() {
        try {
            SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PRE,MODE_PRIVATE);
            oldpeak =sharedPreferences.getString("oldpeak",null).toString();
        }catch (Exception e){

        }

    }

    private void initializeViews() {
        btnNext = findViewById(R.id.btnNext4);
        btnPrev = findViewById(R.id.btnSetPrev4);

        edtOldPeak = findViewById(R.id.edtOldPeak);
        spECGLevel = findViewById(R.id.spECGLevel);
        spExang = findViewById(R.id.spExang);

        ArrayAdapter adaper = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, exang);
        adaper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExang.setAdapter(adaper);

        ArrayAdapter adaper2 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, EcgAndSlope);
        adaper2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spECGLevel.setAdapter(adaper2);
    }

    private void setValues() {
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PRE,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("oldpeak", edtOldPeak.getText().toString());
        editor.putString("exang", "" + spExang.getSelectedItemPosition());
        editor.putString("ecg", "" + spECGLevel.getSelectedItemPosition());
        edtOldPeak.setText("");
        editor.commit();
        Toast.makeText(EcgExangOldpeakActivity.this, "" + "Data saved", Toast.LENGTH_SHORT).show();

    }
}