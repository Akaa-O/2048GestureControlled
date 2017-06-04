package lab2_203_13.uwaterloo.ca.lab2_203_13;

import java.util.ArrayList;


public class ReadingsBuffer {

    private ArrayList<Float> bufferX;
    private ArrayList<Float> bufferY;
    private ArrayList<Float> bufferZ;

    private int bufferSize = 0;
    public int currentSize = 0;

    ReadingsBuffer(int size) {
        bufferX = new ArrayList<>(size);
        bufferY = new ArrayList<>(size);
        bufferZ = new ArrayList<>(size);
        bufferSize = size;

        for (int i = 0; i < 100; i++) {
            bufferX.add(0, (float)0.0);
            bufferY.add(0, (float)0.0);
            bufferZ.add(0, (float)0.0);
        }

    }

    public void update(float[] values) {
        if (currentSize >= bufferSize) {
            bufferX.remove(bufferX.size()-1);
            bufferY.remove(bufferY.size()-1);
            bufferZ.remove(bufferZ.size()-1);
            addToIndex(0, values);
            currentSize = bufferSize;

        } else {
            addToIndex(0, values);
            currentSize += 1;
        }
    }

    public ArrayList<Float> getBuffer(char buffer) {
        if (buffer == 'X') {
            return bufferX;
        } else if (buffer =='Y') {
            return bufferY;
        } else if (buffer == 'Z') {
            return bufferZ;
        }
        else {
            return bufferX;
        }
    }

    private void addToIndex(int index, float[] values) {
        bufferX.add(index, values[0]);
        bufferY.add(index, values[1]);
        bufferZ.add(index, values[2]);
    }


}