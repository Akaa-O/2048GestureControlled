package lab2_203_13.uwaterloo.ca.lab2_203_13;

import android.app.Activity;
import android.graphics.Color;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity {

    LineGraphView graph;

    SensorManager sensorManager;

    // Accelerometer Sensor

    Sensor accelerometerSensor;
    AccelerometerSensorEventListener accelerometerSensorEventListener;
    TextView accelerometerSensorLabel;
    TextView accelerometerSensorRecordLabel;

    Button samplingButton;

    ReadingsBuffer accelerometerValues = new ReadingsBuffer(100);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // tell OS to run it's own pre-defined initialization sequence before running ours
        super.onCreate(savedInstanceState);

        //my activity is focusing on working with the Activity_Main layout.
        setContentView(R.layout.activity_main);


        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.llabel);


        graph = new LineGraphView(getApplicationContext(),
                100,
                Arrays.asList("x", "y", "z"));


        // SINGLETON (creation) pattern
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


        // Graph

        mainLayout.addView(graph);
        graph.setVisibility(View.VISIBLE);

        samplingButton = new Button(getApplicationContext());
        samplingButton.setText("Capture Last 100 Accelerometer Recordings");

        samplingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                writeToFile(accelerometerValues);
            }
        });

        mainLayout.addView(samplingButton);

        // TYPE_ACCELEROMETER Sensor

        // Labels

        accelerometerSensorLabel = new TextView(getApplicationContext());
        accelerometerSensorLabel.setTextColor(Color.parseColor("#000000"));
        mainLayout.addView(accelerometerSensorLabel);

        accelerometerSensorRecordLabel = new TextView(getApplicationContext());
        accelerometerSensorRecordLabel.setTextColor(Color.parseColor("#000000"));
        mainLayout.addView(accelerometerSensorRecordLabel);
        accelerometerSensorLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        // accelerometerSensorLabel.setText("hello");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;

        accelerometerSensorLabel.setLayoutParams(params);

        // Sensor

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        accelerometerSensorEventListener = new AccelerometerSensorEventListener(accelerometerSensorLabel, accelerometerSensorRecordLabel, graph, accelerometerValues);


    }


    @Override
    protected void onResume() {
        super.onResume();


        if (accelerometerSensor != null) {
            sensorManager.registerListener(
                    accelerometerSensorEventListener,
                    accelerometerSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        } else {
            accelerometerSensorLabel.setText("Accelerometer not available.");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (accelerometerSensor != null) {
            sensorManager.unregisterListener(accelerometerSensorEventListener, accelerometerSensor);
        }
    }

    private void writeToFile(ReadingsBuffer accelerometerValues) {
        File myFile = null;
        PrintWriter myPTR = null;

        ArrayList<Float> bufferX = accelerometerValues.getBuffer('X');
        ArrayList<Float> bufferY = accelerometerValues.getBuffer('Y');
        ArrayList<Float> bufferZ = accelerometerValues.getBuffer('Z');

        try {
            myFile = new File(getExternalFilesDir("Recorded Readings"), "readings.csv");
            myPTR = new PrintWriter(myFile);

            myPTR.println("X, Y, Z");
            for (int i = 0; i < accelerometerValues.currentSize - 1; i++) {
                myPTR.println(String.format("%s, %s, %s", bufferX.get(i), bufferY.get(i), bufferZ.get(i)));
            }

        }
        catch (IOException e) {
            Log.d("Lab1_203_13: ", "File access failed!!!");
        }
        finally {

            if (myPTR != null) {

                myPTR.close();

            }

            Log.d("Lab1_203_13: ", "File write ended");
        }
    }

}