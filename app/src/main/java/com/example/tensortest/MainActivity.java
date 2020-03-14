package com.example.tensortest;

import androidx.appcompat.app.AppCompatActivity;
import org.tensorflow.lite.Interpreter;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String[] chestPains = {"Mid", "Moderate", "Severe", "Very Severe"};
    private String[] EcgAndSlope = {"0", "1", "2"};
    private String[] exang = { "No", "Yes"};
    private String[] ca = {"0", "1", "2","3"};
    private String[] thal = {"Normal", "Fixed Defect", "Reversable Defect"};
    private String[] gend = {"Female", "Male"};

    private Spinner spChest, spEcg, spExang, spSlope, spCa, spThal, spGender, spFBS;
    private EditText edtAge, edtBP, edtChol, edtHeartRate, edtOldPeak;

    float[] inputVal = new float[13];

    private TextView result;
    private Button btnInfer;
    Interpreter tflite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Heart Disease Prediction");

        spChest = findViewById(R.id.spChestPain);
        spEcg = findViewById(R.id.spECGLevel);
        spExang = findViewById(R.id.spExang);
        spSlope = findViewById(R.id.spSlope);
        spCa = findViewById(R.id.spCa);
        spThal = findViewById(R.id.spThal);
        spGender = findViewById(R.id.spGender);
        spFBS = findViewById(R.id.spFBS);
        initializeSpinners();

        edtAge = findViewById(R.id.edtAge);
        edtBP = findViewById(R.id.edtBP);
        edtChol = findViewById(R.id.edtChol);
        edtHeartRate = findViewById(R.id.edtThalach);
        edtOldPeak = findViewById(R.id.edtOldPeak);

        //result = findViewById(R.id.result);
        btnInfer = findViewById(R.id.infer);

        try {
            tflite = new Interpreter(loadModelFile());
        } catch (Exception e){
            e.printStackTrace();
        }

        getSpinnerValues();

        spChest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputVal[2] = Float.parseFloat(""+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                inputVal[2] = Float.parseFloat(""+0);
            }
        });
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputVal[1] = Float.parseFloat(""+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                inputVal[1] = Float.parseFloat(""+0);
            }
        });
        spFBS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputVal[5] = Float.parseFloat(""+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                inputVal[5] = Float.parseFloat(""+0);
            }
        });
        spEcg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputVal[6] = Float.parseFloat(""+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                inputVal[6] = Float.parseFloat(""+0);
            }
        });
        spExang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputVal[8] = Float.parseFloat(""+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                inputVal[8] = Float.parseFloat(""+0);
            }
        });
        spSlope.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputVal[10] = Float.parseFloat(""+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                inputVal[10] = Float.parseFloat(""+0);
            }
        });

        spCa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputVal[11] = Float.parseFloat(""+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                inputVal[11] = Float.parseFloat(""+0);
            }
        });
        spThal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputVal[12] = Float.parseFloat(""+(i+1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                inputVal[12] = Float.parseFloat(""+1);
            }
        });
        btnInfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputVal[0] = Float.parseFloat(edtAge.getText().toString());
                inputVal[3] = Float.parseFloat(edtBP.getText().toString());
                inputVal[4] = Float.parseFloat(edtChol.getText().toString());
                inputVal[7] = Float.parseFloat(edtHeartRate.getText().toString());
                inputVal[9] = Float.parseFloat(edtOldPeak.getText().toString());

                float prediction = doInference();
                Intent intent = new Intent(getApplicationContext(), PredictionActivity.class);
                intent.putExtra("prediction", "" + prediction);
                startActivity(intent);
                finish();
            }
        });


    }

    private void getSpinnerValues() {

        spChest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputVal[2] = Float.parseFloat(""+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                inputVal[2] = Float.parseFloat(""+0);
            }
        });
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputVal[1] = Float.parseFloat(""+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                inputVal[1] = Float.parseFloat(""+0);
            }
        });
        spFBS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputVal[5] = Float.parseFloat(""+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                inputVal[5] = Float.parseFloat(""+0);
            }
        });
        spEcg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputVal[6] = Float.parseFloat(""+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                inputVal[6] = Float.parseFloat(""+0);
            }
        });
        spExang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputVal[8] = Float.parseFloat(""+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                inputVal[8] = Float.parseFloat(""+0);
            }
        });
        spSlope.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputVal[10] = Float.parseFloat(""+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                inputVal[10] = Float.parseFloat(""+0);
            }
        });

        spCa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputVal[11] = Float.parseFloat(""+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                inputVal[11] = Float.parseFloat(""+0);
            }
        });
        spThal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputVal[12] = Float.parseFloat(""+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                inputVal[12] = Float.parseFloat(""+0);
            }
        });

    }

    private void initializeSpinners() {
        //Creating the ArrayAdapter instance having the chest pain list
        ArrayAdapter chest = new ArrayAdapter(this,android.R.layout.simple_spinner_item,chestPains);
        chest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spChest.setAdapter(chest);

        ArrayAdapter<String> ecg = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, EcgAndSlope);
        ecg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEcg.setAdapter(ecg);

        ArrayAdapter<String> exanAdap = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exang);
        exanAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExang.setAdapter(exanAdap);

        ArrayAdapter<String> caAdapt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ca);
        caAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCa.setAdapter(caAdapt);

        ArrayAdapter<String> thalAdap = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, thal);
        thalAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spThal.setAdapter(thalAdap);

        ArrayAdapter<String> slopeAdap = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, EcgAndSlope);
        slopeAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSlope.setAdapter(slopeAdap);

        ArrayAdapter<String> genderAdap = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gend);
        genderAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(genderAdap);

        ArrayAdapter<String> fbsAdap = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exang);
        fbsAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFBS.setAdapter(fbsAdap);

    }

    public float doInference(){
        //float[] inputVal = {34,1,3,118,182,0,0,174,0,0,2,0,2};
        //inputVal[0] = 54;
        // v1


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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id = view.getId();
        switch (id){
            case R.id.spChestPain:
                inputVal[2] = i;
                Toast.makeText(this, "" + i, Toast.LENGTH_SHORT).show();
                break;
            case R.id.spCa:
                inputVal[11] = i;
                Toast.makeText(this, "" + i, Toast.LENGTH_SHORT).show();
                break;
            case R.id.spExang:
                inputVal[8] = i;
                Toast.makeText(this, "" + i, Toast.LENGTH_SHORT).show();
                break;
            case R.id.spFBS:
                inputVal[5] = i;
                Toast.makeText(this, "" + i, Toast.LENGTH_SHORT).show();
                break;
            case R.id.spECGLevel:
                inputVal[6] = i;
                Toast.makeText(this, "" + i, Toast.LENGTH_SHORT).show();
                break;
            case R.id.spGender:
                inputVal[1] = i;
                Toast.makeText(this, "" + i, Toast.LENGTH_SHORT).show();
                break;
            case R.id.spSlope:
                inputVal[10] = i;
                Toast.makeText(this, "" + i, Toast.LENGTH_SHORT).show();
                break;
            case R.id.spThal:
                inputVal[12] = i;
                Toast.makeText(this, "" + i, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
//        inputVal[2] = 0;
//        inputVal[11] = 0;
//        inputVal[8] = 0;
//        inputVal[5] = 0;
//        inputVal[6] = 0;
//        inputVal[1] = 0;
//        inputVal[10] = 0;
//        inputVal[12] = 0;
    }
}
