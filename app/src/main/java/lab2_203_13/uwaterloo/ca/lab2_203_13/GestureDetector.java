package lab2_203_13.uwaterloo.ca.lab2_203_13;

import android.util.Log;
import android.widget.TextView;


import static java.lang.String.format;

public class GestureDetector {

    private enum AccelState{
        WAIT, RISE_A, FALL_A, FALL_B, RISE_B, PAUSE;
    }

    final private int sampleCounts = 20;

    private AccelState state;
    private float positiveThreshold = 3.5f;
    private float negativeThreshold = -3.5f;
    private float lastValue;
    private boolean detectsX;
    private int sampleCount;
    private int waitThreshold;
    private int pauseSamples;

    private TextView mTextView;
    private GameLoopTask gameLoopTask;

    public GestureDetector(boolean detectsX, TextView detection, GameLoopTask gameLoopTask){
        this.state = AccelState.WAIT;
        this.lastValue = 0;
        this.detectsX = detectsX;
        this.sampleCount = 0;
        this.waitThreshold = 0;
        this.gameLoopTask = gameLoopTask;
        this.pauseSamples = 0;

        mTextView = detection;
        mTextView.setText("Waiting");
    }

    public void onValuesChanged(float newReading) {
        float slope = newReading - lastValue;

        switch (state) {
            case PAUSE:
                if (pauseSamples < 50) {
                    pauseSamples++;
                }
                else {
                    state = AccelState.WAIT;
                }

            case WAIT:
                waitThreshold++;
                if (waitThreshold >= 90) {
                    waitThreshold = 0;
                    String outputText = "Waiting";
                    mTextView.setText(outputText);
                }
                sampleCount = 0;
                if (slope > positiveThreshold) {
                    state = AccelState.RISE_A;
                } else if (slope < negativeThreshold) {
                    state = AccelState.FALL_B;
                }
                break;
            case RISE_A:
                if (sampleCount < sampleCounts) {
                    sampleCount++;
                }
                else
                {
                    state = AccelState.PAUSE;
                }
                if (slope <= -0.20) {
                    state = AccelState.FALL_A;

                }
                break;
            case FALL_A:
                sampleCount++;
                if (slope >= 0.20) {
                    if(sampleCount<= sampleCounts) {
                        Log.d(this.getClass().getSimpleName(), "Detected " + (detectsX ? "right" : "up"));
                        mTextView.setText((detectsX ? "Right" : "Up"));
                        this.gameLoopTask.setDirection((detectsX ? GameLoopTask.Direction.RIGHT : GameLoopTask.Direction.UP));
                    }
                    waitThreshold = 0;
                    state = AccelState.PAUSE;
                    Log.d(this.getClass().getSimpleName(), format("%.2f", slope));
                }
                break;
            case FALL_B:
                if (sampleCount < sampleCounts) {
                    sampleCount++;
                } else {
                    state = AccelState.PAUSE;
                }

                if (slope >= 0.20) {
                    state = AccelState.RISE_B;
                }
                break;
            case RISE_B:
                sampleCount++;
                if (slope <= -0.20) {
                    if(sampleCount<= sampleCounts) {
                        Log.d(this.getClass().getSimpleName(), "Detected " + (detectsX ? "left" : "down"));
                        mTextView.setText((detectsX ? "Left" : "Down"));
                        this.gameLoopTask.setDirection((detectsX ? GameLoopTask.Direction.LEFT : GameLoopTask.Direction.DOWN));
                    }
                    waitThreshold = 0;
                    state = AccelState.PAUSE;
                    Log.d(this.getClass().getSimpleName(), format("%.2f", slope));
                }
                break;
            default:
                state = AccelState.WAIT;

        }

        lastValue = newReading;
    }
}
