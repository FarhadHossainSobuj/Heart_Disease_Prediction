package com.example.tensortest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ResultActivity extends AppCompatActivity {
    private static final String SHARED_PRE = "user";
    private TextView tvHeartRate, tvPrediction;
    private Button btnHome;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    float[] inputVal = new float[13];

    private TextView result, tvData;
    Interpreter tflite;
    private String heartRate = "";
    private String age, gender, cp, bps, chol, fbs, ecg, exang, oldpeak, slope, ca, thal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle("Heart Disease Prediction");

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("Your_Heart_Rate");


        tvData = findViewById(R.id.tvData);
        tvHeartRate = findViewById(R.id.tvHearRate);
        tvPrediction = findViewById(R.id.tvPrediction);
        btnHome = findViewById(R.id.btnHome);
        getData();
        getPrevValues();
        try {
            tflite = new Interpreter(loadModelFile());
        } catch (Exception e){
            e.printStackTrace();
        }

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                finish();
            }
        });

    }

    private void getPrevValues() {
        try {
            SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PRE,MODE_PRIVATE);
            age=sharedPreferences.getString("age",null).toString();
            gender=sharedPreferences.getString("gender",null).toString();
            cp=sharedPreferences.getString("chest_pains",null).toString();
            bps=sharedPreferences.getString("bp",null).toString();
            chol=sharedPreferences.getString("chol",null).toString();
            fbs=sharedPreferences.getString("fbs",null).toString();
            ecg=sharedPreferences.getString("ecg",null).toString();
            exang=sharedPreferences.getString("exang",null).toString();
            oldpeak=sharedPreferences.getString("oldpeak",null).toString();
            slope=sharedPreferences.getString("slope",null).toString();
            ca=sharedPreferences.getString("ca",null).toString();
            thal=sharedPreferences.getString("thal",null).toString();
        }catch (Exception e){

        }

    }

    private void getData() {
        try {
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    heartRate = dataSnapshot.getValue(String.class);
                    inputVal[7] = Float.parseFloat(heartRate);
                    int prediction = (int)(doInference() * 100);
//                    tvHeartRate.setText("" + heartRate + "\nPrediction is : "+ doInference());
                    tvHeartRate.setText("" + heartRate);
                    tvPrediction.setText("" + prediction+"%");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e){

        }
    }
    private void getValues(){
        inputVal[0] = Float.parseFloat(age);
        inputVal[1] = Float.parseFloat(gender);
        inputVal[2] = Float.parseFloat(cp);
        inputVal[3] = Float.parseFloat(bps);
        inputVal[4] = Float.parseFloat(chol);
        inputVal[5] = Float.parseFloat(fbs);
        inputVal[6] = Float.parseFloat(ecg);
        inputVal[7] = Float.parseFloat(heartRate);
        inputVal[8] = Float.parseFloat(exang);
        inputVal[9] = Float.parseFloat(oldpeak);
        inputVal[10] = Float.parseFloat(slope);
        inputVal[11] = Float.parseFloat(ca);
        inputVal[12] = Float.parseFloat(thal);
    }
    public float doInference(){
        //float[] inputVal = {34,1,3,118,182,0,0,174,0,0,2,0,2};
        //inputVal[0] = 54;


        float[][] outputval = new float[1][1];

        tflite.run(inputVal, outputval);

        float inferredValue = outputval[0][0];
        return inferredValue;
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("linear.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}
