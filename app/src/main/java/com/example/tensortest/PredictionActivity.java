package com.example.tensortest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PredictionActivity extends AppCompatActivity {

    private TextView result;
    private Button btnPredict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        float prediction = Float.parseFloat(getIntent().getStringExtra("prediction"));


        result = findViewById(R.id.result);
        if(prediction > 0.5){
            result.setText("You have a risk of heart disease :" + (prediction*100) + "%");
        } else {
            result.setText("You are safe from risk of heart disease :" + (prediction*100) + "%");
        }

        btnPredict = findViewById(R.id.btnPredict);
        btnPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
