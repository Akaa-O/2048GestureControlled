package lab2_203_13.uwaterloo.ca.lab2_203_13;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import java.util.ArrayList;


public class AccelerometerSensorEventListener implements SensorEventListener {
    ReadingsBuffer histValues;
    private GestureDetector xDetector;
    private GestureDetector yDetector;

    float[] newValues = new float[3];
    float[] filteredValues = new float[3];

    static float ATTENUATION_CONSTANT = 10;


    //

    public AccelerometerSensorEventListener(TextView outputView, ReadingsBuffer inputBuffer, GameLoopTask gameLoopTask) {
        histValues = inputBuffer;
        xDetector = new GestureDetector(true, outputView, gameLoopTask);
        yDetector = new GestureDetector(false, outputView, gameLoopTask);
    }

    @Override
    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

            System.arraycopy(se.values, 0, newValues, 0, 3);

            // filteredReading += (newReading - filteredReading) / ATTENUATION_CONSTANT

            filteredValues[0] += (newValues[0] - histValues.getBuffer('X').get(0)) / ATTENUATION_CONSTANT;
            filteredValues[1] += (newValues[1] - histValues.getBuffer('Y').get(0)) / ATTENUATION_CONSTANT;
            filteredValues[2] += (newValues[2] - histValues.getBuffer('Z').get(0)) / ATTENUATION_CONSTANT;

            histValues.update(filteredValues);

            xDetector.onValuesChanged(filteredValues[0]);
            yDetector.onValuesChanged(filteredValues[1]);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor s, int accuracy) {

    }

}