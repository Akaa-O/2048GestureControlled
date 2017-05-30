package lab2_203_13.uwaterloo.ca.lab2_203_13;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;



class LightSensorEventListener implements SensorEventListener {
    private TextView output;
    private TextView outputRecord;

    public float value;
    public float recordValue = 0;


    public LightSensorEventListener(TextView outputView, TextView recordView) {
        output = outputView;
        outputRecord = recordView;
    }

    public void onAccuracyChanged(Sensor s, int i) { }

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_LIGHT) {
            value = se.values[0];

            String outputText = String.format("Light Sensor Value: %.2f", value);
            output.setText(outputText);

            updateRecordValues();

        }
    }

    public void updateRecordValues() {

        if (Math.abs(value) > Math.abs(recordValue)) {
            recordValue = value;
        }

        String outputRecordText = String.format("Light Sensor Record Value: %.2f", recordValue);
        outputRecord.setText(outputRecordText);

    }
}