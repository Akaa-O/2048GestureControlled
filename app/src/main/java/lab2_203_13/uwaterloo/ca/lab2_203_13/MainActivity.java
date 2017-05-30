package lab2_203_13.uwaterloo.ca.lab2_203_13;

import android.app.Activity;
import android.graphics.Color;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity {

    LineGraphView graph;

    SensorManager sensorManager;

    // Light Sensor

    Sensor lightSensor;
    LightSensorEventListener lightSensorEventListener;
    TextView lightSensorLabel;
    TextView lightSensorRecordLabel;

    // Accelerometer Sensor

    Sensor accelerometerSensor;
    AccelerometerSensorEventListener accelerometerSensorEventListener;
    TextView accelerometerSensorLabel;
    TextView accelerometerSensorRecordLabel;

    // Magnetic Field Sensor

    Sensor magnetometerSensor;
    MagnetometerSensorEventListener magnetometerSensorEventListener;
    TextView magnetometerSensorLabel;
    TextView magnetometerSensorRecordLabel;

    // Rotation Sensor

    Sensor rotationSensor;
    RotationSensorEventListener rotationSensorEventListener;
    TextView rotationSensorLabel;
    TextView rotationSensorRecordLabel;

    Button samplingButton;
    Button clearHistoryButton;

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

        // TYPE_LIGHT Sensor

        // Labels

        lightSensorLabel = new TextView(getApplicationContext());
        lightSensorLabel.setText("Light Sensor Value: ");
        lightSensorLabel.setTextColor(Color.parseColor("#000000"));
        mainLayout.addView(lightSensorLabel);

        lightSensorRecordLabel = new TextView(getApplicationContext());
        lightSensorRecordLabel.setText("Light Sensor Record Value: ");
        lightSensorRecordLabel.setTextColor(Color.parseColor("#000000"));
        mainLayout.addView(lightSensorRecordLabel);

        // Sensor

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorEventListener = new LightSensorEventListener(lightSensorLabel, lightSensorRecordLabel);

        // TYPE_ACCELEROMETER Sensor

        // Labels

        accelerometerSensorLabel = new TextView(getApplicationContext());
        accelerometerSensorLabel.setText("Accelerometer \n Value: ");
        accelerometerSensorLabel.setTextColor(Color.parseColor("#000000"));
        mainLayout.addView(accelerometerSensorLabel);

        accelerometerSensorRecordLabel = new TextView(getApplicationContext());
        accelerometerSensorRecordLabel.setText("Accelerometer Record Values: ");
        accelerometerSensorRecordLabel.setTextColor(Color.parseColor("#000000"));
        mainLayout.addView(accelerometerSensorRecordLabel);

        // Sensor

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelerometerSensorEventListener = new AccelerometerSensorEventListener(accelerometerSensorLabel, accelerometerSensorRecordLabel, graph, accelerometerValues);


        // TYPE_MAGNETIC_FIELD

        // Labels

        magnetometerSensorLabel = new TextView(getApplicationContext());
        magnetometerSensorLabel.setText("Magnetometer Value: ");
        magnetometerSensorLabel.setTextColor(Color.parseColor("#000000"));
        mainLayout.addView(magnetometerSensorLabel);

        magnetometerSensorRecordLabel = new TextView(getApplicationContext());
        magnetometerSensorRecordLabel.setText("Magnetometer Record Value: ");
        magnetometerSensorRecordLabel.setTextColor(Color.parseColor("#000000"));
        mainLayout.addView(magnetometerSensorRecordLabel);


        // Sensor

        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        magnetometerSensorEventListener = new MagnetometerSensorEventListener(magnetometerSensorLabel, magnetometerSensorRecordLabel);


        // TYPE_ROTATION

        // Labels

        rotationSensorLabel = new TextView(getApplicationContext());
        rotationSensorLabel.setText("Rotation Value: ");
        rotationSensorLabel.setTextColor(Color.parseColor("#000000"));
        mainLayout.addView(rotationSensorLabel);

        rotationSensorRecordLabel = new TextView(getApplicationContext());
        rotationSensorRecordLabel.setText("Rotation Record Value: ");
        rotationSensorRecordLabel.setTextColor(Color.parseColor("#000000"));
        mainLayout.addView(rotationSensorRecordLabel);


        // Sensor

        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        rotationSensorEventListener = new RotationSensorEventListener(rotationSensorLabel, rotationSensorRecordLabel);

        // Clear all time high Button

        clearHistoryButton = new Button(getApplicationContext());
        clearHistoryButton.setText("Clear All Time Highs");

        clearHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lightSensorEventListener.recordValue = 0;

                for (int i = 0; i < 3; i++) {
                    accelerometerSensorEventListener.recordValues[i] = 0;
                    magnetometerSensorEventListener.recordValues[i] = 0;
                    rotationSensorEventListener.recordValues[i] = 0;

                }
            }
        });

        mainLayout.addView(clearHistoryButton);
    }


    @Override
    protected void onResume() {
        super.onResume();


        if (lightSensor != null) {
            sensorManager.registerListener(
                    lightSensorEventListener,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            lightSensorLabel.setText("Light sensor not available.");
        }
        if (accelerometerSensor != null) {
            sensorManager.registerListener(
                    accelerometerSensorEventListener,
                    accelerometerSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        } else {
            accelerometerSensorLabel.setText("Accelerometer not available.");
        }
        if (magnetometerSensor != null) {
            sensorManager.registerListener(

                    magnetometerSensorEventListener,
                    magnetometerSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        } else {
            magnetometerSensorLabel.setText("Magnetometer not available.");
        }
        if (rotationSensor != null) {
            sensorManager.registerListener(
                    rotationSensorEventListener,
                    rotationSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        } else {
            rotationSensorLabel.setText("Rotation sensor not available.");
        }


    }

    @Override
    protected void onPause() {
        super.onPause();

        if (lightSensor != null) {
            sensorManager.unregisterListener(lightSensorEventListener, lightSensor);
        }
        if (accelerometerSensor != null) {
            sensorManager.unregisterListener(accelerometerSensorEventListener, accelerometerSensor);
        }
        if (magnetometerSensor != null) {
            sensorManager.unregisterListener(magnetometerSensorEventListener, magnetometerSensor);
        }
        if (rotationSensor != null) {
            sensorManager.unregisterListener(rotationSensorEventListener, rotationSensor);
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