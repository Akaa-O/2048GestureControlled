package lab2_203_13.uwaterloo.ca.lab2_203_13;

import android.util.Log;
import android.widget.TextView;

import static java.lang.String.format;

public class GestureDetector {

    private enum AccelState{
        WAIT, RISE_A, FALL_A, FALL_B, RISE_B;
    }

    private AccelState state;
    private float positiveThreshold = 1f;
    private float negativeThreshold = -1f;
    private float lastValue;
    private boolean detectsX;
    private int sampleCount;
    private int waitThreshold;

    private TextView mTextView;

    public GestureDetector(boolean detectsX, TextView detection){
        this.state = AccelState.WAIT;
        this.lastValue = 0;
        this.detectsX = detectsX;
        this.sampleCount = 0;
        this.waitThreshold = 0;

        mTextView = detection;
        mTextView.setText("Waiting");
    }

    public void onValuesChanged(float newReading) {
        float slope = newReading - lastValue;

        switch (state) {

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
                if (sampleCount < 30) {
                    sampleCount++;
                }
                else
                {
                    state = AccelState.WAIT;
                }
                if (slope <= -0.2) {
                    state = AccelState.FALL_A;

                }
                break;
            case FALL_A:
                sampleCount++;
                if (slope >= 0.2) {
                    if(sampleCount<=30) {
                        Log.d(this.getClass().getSimpleName(), "Detected " + (detectsX ? "right" : "up"));
                        mTextView.setText((detectsX ? "Right" : "Up"));
                    }
                    waitThreshold = 0;
                    state = AccelState.WAIT;
                    Log.d(this.getClass().getSimpleName(), format("%.2f", slope));
                }
                break;
            case FALL_B:
                if (sampleCount < 30) {
                    sampleCount++;
                } else {
                    state = AccelState.WAIT;
                }

                if (slope >= 0.2) {
                    state = AccelState.RISE_B;
                }
                break;
            case RISE_B:
                sampleCount++;
                if (slope <= -0.2) {
                    if(sampleCount<=30) {
                        Log.d(this.getClass().getSimpleName(), "Detected " + (detectsX ? "left" : "down"));
                        mTextView.setText((detectsX ? "Left" : "Down"));
                    }
                    waitThreshold = 0;
                    state = AccelState.WAIT;
                    Log.d(this.getClass().getSimpleName(), format("%.2f", slope));
                }
                break;
            default:
                state = AccelState.WAIT;

        }

        lastValue = newReading;
    }
}
