package lab2_203_13.uwaterloo.ca.lab2_203_13;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import java.util.ArrayList;


public class AccelerometerSensorEventListener implements SensorEventListener {

    private TextView output;
    LineGraphView graph;
    ReadingsBuffer histValues;
    private GestureDetector xDetector;
    private GestureDetector yDetector;

    float[] newValues = new float[3];
    float[] filteredValues = new float[3];

    float[] recordValues = {0, 0, 0};

    static float ATTENUATION_CONSTANT = 10;


    //

    public AccelerometerSensorEventListener(TextView outputView, LineGraphView outputGraph, ReadingsBuffer inputBuffer) {
        output = outputView;
        graph = outputGraph;
        histValues = inputBuffer;
        xDetector = new GestureDetector(true, outputView);
        yDetector = new GestureDetector(false, outputView);
    }

    @Override
    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

            System.arraycopy(se.values, 0, newValues, 0, 3);

            // filteredReading += (newReading - filteredReading) / ATTENUATION_CONSTANT

            filteredValues[0] += (newValues[0] - histValues.getBuffer('X').get(0)) / ATTENUATION_CONSTANT;
            filteredValues[1] += (newValues[1] - histValues.getBuffer('Y').get(0)) / ATTENUATION_CONSTANT;
            filteredValues[2] += (newValues[2] - histValues.getBuffer('Z').get(0)) / ATTENUATION_CONSTANT;


            graph.addPoint(filteredValues);
            histValues.update(filteredValues);

            xDetector.onValuesChanged(filteredValues[0]);
            yDetector.onValuesChanged(filteredValues[1]);

            String outputString = String.format("Accelerometer Values: x: %.2f, y: %.2f, z: %.2f", filteredValues[0], filteredValues[1], filteredValues[2]);
            //output.setText(outputString);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor s, int accuracy) {

    }

}