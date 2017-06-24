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
import android.widget.RelativeLayout;
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

    ReadingsBuffer accelerometerValues = new ReadingsBuffer(100);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // tell OS to run it's own pre-defined initialization sequence before running ours
        super.onCreate(savedInstanceState);

        //my activity is focusing on working with the Activity_Main layout.
        setContentView(R.layout.activity_main);


        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.llabel);

        // SINGLETON (creation) pattern
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // TYPE_ACCELEROMETER Sensor

        accelerometerSensorLabel = new TextView(getApplicationContext());
        mainLayout.addView(accelerometerSensorLabel);

        // Sensor

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        accelerometerSensorEventListener = new AccelerometerSensorEventListener(accelerometerSensorLabel, graph, accelerometerValues);




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

}