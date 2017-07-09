package lab2_203_13.uwaterloo.ca.lab2_203_13;

/**
 * Created by confo on 7/9/2017.
 */

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

public class AccelerometerEventListener implements SensorEventListener {
    private final float FILTER_CONSTANT = 58f;

    private GameLoopTask gameLoopTask1;

    private TextView instanceOutput;
    //private LineGraphView graph;

    private myFSM [] myFSMs = new myFSM[2]; //x, y z
    private int myFSMCounter;
    private final int FSM_COUNTER_DEFAULT = 20;

    private float[][] historyReading = new float[100][3];

    private void insertHistoryReading(float[] values){
        for(int i = 1; i < 100; i++){
            historyReading[i - 1][0] = historyReading[i][0];
            historyReading[i - 1][1] = historyReading[i][1];
            historyReading[i - 1][2] = historyReading[i][2];
        }

        historyReading[99][0] += (values[0] - historyReading[99][0]) / FILTER_CONSTANT;
        historyReading[99][1] += (values[1] - historyReading[99][1]) / FILTER_CONSTANT;
        historyReading[99][2] += (values[2] - historyReading[99][2]) / FILTER_CONSTANT;
    }


    private void determineGesture(){

        myFSM.mySig[] sigs = new myFSM.mySig[2];

        for(int i = 0; i < 2; i++) {
            sigs[i] = myFSMs[i].getSignature();
            myFSMs[i].resetFSM();
        }

        if(sigs[0] == myFSM.mySig.SIG_A && sigs[1] == myFSM.mySig.SIG_X){
            instanceOutput.setText("RIGHT");
            gameLoopTask1.setDirection(GameLoopTask.Direction.RIGHT);
        }
        else if(sigs[0] == myFSM.mySig.SIG_B && sigs[1] == myFSM.mySig.SIG_X){
            instanceOutput.setText("LEFT");
            gameLoopTask1.setDirection(GameLoopTask.Direction.LEFT);
        }
        else if(sigs[0] == myFSM.mySig.SIG_X && sigs[1] == myFSM.mySig.SIG_A){
            instanceOutput.setText("UP");
            gameLoopTask1.setDirection(GameLoopTask.Direction.UP);
        }
        else if(sigs[0] == myFSM.mySig.SIG_X && sigs[1] == myFSM.mySig.SIG_B){
            instanceOutput.setText("DOWN");
            gameLoopTask1.setDirection(GameLoopTask.Direction.DOWN);
        }
        else{
            instanceOutput.setText("N/A");
        }

    }


    public AccelerometerEventListener(TextView outputView, GameLoopTask gameLoopTask) {
        instanceOutput = outputView;
        //graph = graphOutput;
        gameLoopTask1 = gameLoopTask;
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 100; j++)
                historyReading[j][i] = 0.0f;

        myFSMs[0] = new myFSM();
        myFSMs[1] = new myFSM();

        myFSMCounter = FSM_COUNTER_DEFAULT;
    }


    public float[][] getHistoryReading(){
        return historyReading;
    }

    public void onAccuracyChanged(Sensor s, int i) { }

    public void onSensorChanged(SensorEvent se) {

        if (se.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

            insertHistoryReading(se.values);

            if(myFSMCounter > 0) {

                boolean reductionFlag = false;

                for (int i = 0; i < 2; i++) {
                    myFSMs[i].supplyInput(historyReading[99][i]);
                    if(myFSMs[i].getState() != myFSM.FSMState.WAIT)
                        reductionFlag = true;
                }

                if(reductionFlag)
                    myFSMCounter--;
            }
            else if(myFSMCounter <= 0){
                determineGesture();
                myFSMCounter = FSM_COUNTER_DEFAULT;
            }

            if(myFSMs[0].isReady() && myFSMs[1].isReady())
                instanceOutput.setTextColor(Color.GREEN);
            else
                instanceOutput.setTextColor(Color.BLACK);

            // instanceOutput.setText("The Accelerometer Reading is: \n"
            //         + String.format("(%.2f, %.2f, %.2f)", historyReading[99][0],historyReading[99][1], historyReading[99][2]) + "\n");


            //graph.addPoint(historyReading[99]);

        }
    }
}
