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

public class ChestPainBPSActivity extends AppCompatActivity {
    private static final String SHARED_PRE = "user";
    private EditText edtChest;
    private Spinner spChest;
    private Button btnNext, btnPrev;
    private String[] chest_pains = {"Mid", "Moderate", "Severe", "Very Severe"};

    private String bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chest_pain_bps);
        setTitle("Enter your chest pain, BP");

        initializeViews();
        getPrevValues();

        if(bp == null){
            btnPrev.setVisibility(View.GONE);
        } else {
            edtChest.setText(bp);
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    setValues();
                    startActivity(new Intent(ChestPainBPSActivity.this, CholBloodActivity.class));
                }
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtChest.setText("" + bp);
            }
        });

    }
    private boolean isValid(){
        boolean flag = true;
        if(edtChest.getText().toString().equals("")){
            edtChest.setError("Fields can be empty");
            flag = false;
        }
        return flag;
    }

    private void getPrevValues() {
        try {
            SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PRE,MODE_PRIVATE);
            bp =sharedPreferences.getString("bp",null).toString();
        }catch (Exception e){

        }

    }

    private void initializeViews() {
        btnNext = findViewById(R.id.btnNext2);
        btnPrev = findViewById(R.id.btnSetPrev2);

        edtChest = findViewById(R.id.edtBPR);
        spChest = findViewById(R.id.spChest);

        ArrayAdapter genderAdaper = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, chest_pains);
        genderAdaper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spChest.setAdapter(genderAdaper);

    }

    private void setValues() {
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PRE,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("bp", edtChest.getText().toString());
        editor.putString("chest_pains", "" + spChest.getSelectedItemPosition());
        edtChest.setText("");
        editor.commit();
        Toast.makeText(ChestPainBPSActivity.this, "" + "Data saved", Toast.LENGTH_SHORT).show();

    }
}
