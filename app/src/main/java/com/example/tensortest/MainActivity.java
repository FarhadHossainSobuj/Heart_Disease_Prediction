package com.example.tensortest;

import androidx.appcompat.app.AppCompatActivity;
import org.tensorflow.lite.Interpreter;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {
    private TextView result;
    private Button btnInfer;
    Interpreter tflite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Heart Disease Prediction");

        result = findViewById(R.id.result);
        btnInfer = findViewById(R.id.infer);

        try {
            tflite = new Interpreter(loadModelFile());
        } catch (Exception e){
            e.printStackTrace();
        }
        btnInfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float prediction = doInference();
                result.setText("Prediction is : " + prediction);
            }
        });
    }

    public float doInference(){
        float[] inputVal = {34,1,3,118,182,0,0,174,0,0,2,0,2};
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
