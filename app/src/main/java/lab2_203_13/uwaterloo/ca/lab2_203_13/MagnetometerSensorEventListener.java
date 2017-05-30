package lab2_203_13.uwaterloo.ca.lab2_203_13;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;



public class MagnetometerSensorEventListener implements SensorEventListener {

    TextView output;
    TextView recordOutput;

    float[] values = new float[3];
    float[] recordValues = {0, 0, 0};

    public MagnetometerSensorEventListener(TextView outputView, TextView recordOutputView) {
        output = outputView;
        recordOutput = recordOutputView;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(se.values, 0, values, 0, 3);
            String outputString = String.format("Magnetometer Values: x: %.2f, y: %.2f, z: %.2f", values[0], values[1], values[2]);
            output.setText(outputString);

            updateRecordValues();
        }


    }

    private void updateRecordValues() {
        for (int i = 0; i < values.length; i++) {
            if (Math.abs(values[i]) > Math.abs(recordValues[i])) {
                recordValues[i] = values[i];
            }
        }

        String recordOutputString = String.format("Magnetometer Record Values: x: %.2f, y: %.2f, z: %.2f", recordValues[0], recordValues[1], recordValues[2]);
        recordOutput.setText(recordOutputString);

    }

}