package com.example.tensortest.input_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tensortest.R;
import com.example.tensortest.ResultActivity;

public class SlopeThalScanActivity extends AppCompatActivity {
    private static final String SHARED_PRE = "user";

    private Spinner spSlope, spCa, spThal;
    private Button btnNext, btnPrev;
    private String[] ca = {"0", "1", "2","3"};
    private String[] thal = {"Normal", "Fixed Defect", "Reversable Defect"};
    private String[] EcgAndSlope = {"0", "1", "2"};

    private String slope, c, thals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slope_thal_scan);
        setTitle("Enter your slope, c, thal");

        initializeViews();
        getPrevValues();

        if(slope == null){
            btnPrev.setVisibility(View.GONE);
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValues();
                startActivity(new Intent(SlopeThalScanActivity.this, ResultActivity.class));
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SlopeThalScanActivity.this, ResultActivity.class));
            }
        });

    }


    private void getPrevValues() {
        try {
            SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PRE,MODE_PRIVATE);
            slope =sharedPreferences.getString("slope",null).toString();
            c =sharedPreferences.getString("ca",null).toString();
            thals =sharedPreferences.getString("thal",null).toString();
        }catch (Exception e){

        }

    }

    private void initializeViews() {
        btnNext = findViewById(R.id.btnNext5);
        btnPrev = findViewById(R.id.btnSetPrev5);

        spSlope = findViewById(R.id.spSlope);
        spCa = findViewById(R.id.spCa);
        spThal = findViewById(R.id.spThal);

        ArrayAdapter adaper = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, ca);
        adaper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCa.setAdapter(adaper);

        ArrayAdapter adaper2 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, EcgAndSlope);
        adaper2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSlope.setAdapter(adaper2);

        ArrayAdapter adaper3 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, thal);
        adaper3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spThal.setAdapter(adaper3);
    }

    private void setValues() {
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PRE,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("slope", "" + spSlope.getSelectedItemPosition());
        editor.putString("ca", "" + spCa.getSelectedItemPosition());
        editor.putString("thal", "" + spThal.getSelectedItemPosition());
        editor.commit();
        Toast.makeText(SlopeThalScanActivity.this, "" + "Data saved", Toast.LENGTH_SHORT).show();

    }
}