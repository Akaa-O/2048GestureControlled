package lab2_203_13.uwaterloo.ca.lab2_203_13;

import android.app.Activity;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;

public class MainActivity extends Activity {

    LineGraphView graph;

    SensorManager sensorManager;

    // Accelerometer Sensor

    Sensor accelerometerSensor;
    AccelerometerEventListener  accelerometerEventListener;

    TextView accelerometerSensorLabel;

    //ReadingsBuffer accelerometerValues = new ReadingsBuffer(100);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.llabel);
        ImageView board = new ImageView(this);
        board.setImageResource(R.drawable.gameboard);
        mainLayout.getLayoutParams().width = 1080;
        mainLayout.getLayoutParams().height = 1080;

        mainLayout.setBackgroundResource(R.drawable.gameboard);

        // SINGLETON (creation) pattern
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // TYPE_ACCELEROMETER Sensor

        accelerometerSensorLabel = new TextView(getApplicationContext());
        accelerometerSensorLabel.setX(1080/2+10);
        accelerometerSensorLabel.setY(20);
        mainLayout.addView(accelerometerSensorLabel);

        //GameLoopTask
        Timer timer = new Timer();
        GameLoopTask gameLoopTask = new GameLoopTask(this, getBaseContext(), mainLayout);

        // Add first two blocks to the game
        gameLoopTask.createBlock();
        gameLoopTask.createBlock();

        timer.schedule(gameLoopTask, 16, 16);

        // Sensor

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        accelerometerEventListener = new AccelerometerEventListener(accelerometerSensorLabel, gameLoopTask);


    }

    @Override
    protected void onResume() {
        super.onResume();


        if (accelerometerSensor != null) {
            sensorManager.registerListener(
                    accelerometerEventListener,
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
            sensorManager.unregisterListener(accelerometerEventListener, accelerometerSensor);
        }
    }

}