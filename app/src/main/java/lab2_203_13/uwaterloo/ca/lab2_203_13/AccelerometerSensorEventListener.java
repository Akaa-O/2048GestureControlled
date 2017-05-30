package lab2_203_13.uwaterloo.ca.lab2_203_13;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import java.util.EventListener;
import java.util.LinkedList;


public class AccelerometerSensorEventListener implements SensorEventListener {

    private TextView output;
    private TextView recordOutput;
    LineGraphView graph;
    ReadingsBuffer histValues;

    float[] values = new float[3];

    float[] recordValues = {0, 0, 0};


    //

    public AccelerometerSensorEventListener(TextView outputView, TextView outputRecordView, LineGraphView outputGraph, ReadingsBuffer inputBuffer) {
        output = outputView;
        recordOutput = outputRecordView;
        graph = outputGraph;
        histValues = inputBuffer;
    }

    @Override
    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            System.arraycopy(se.values, 0, values, 0, 3);
            // values[0] = se.values[0];

            graph.addPoint(values);
            histValues.update(values);


            String outputString = String.format("Accelerometer Values: x: %.2f, y: %.2f, z: %.2f", values[0], values[1], values[2]);
            output.setText(outputString);

            updateRecordValues();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor s, int accuracy) {

    }

    private void updateRecordValues() {
        for (int i = 0; i < values.length; i++) {
            if (Math.abs(values[i]) > Math.abs(recordValues[i])) {
                recordValues[i] = values[i];
            }
        }

        String recordOutputString = String.format("Accelerometer Record Values: x: %.2f, y: %.2f, z: %.2f", recordValues[0], recordValues[1], recordValues[2]);
        recordOutput.setText(recordOutputString);

    }
}