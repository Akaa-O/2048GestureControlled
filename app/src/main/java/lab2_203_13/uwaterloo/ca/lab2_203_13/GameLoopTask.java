package lab2_203_13.uwaterloo.ca.lab2_203_13;

import android.app.Activity;

import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Created by desmond on 6/24/17.
 */

public class GameLoopTask extends TimerTask{

    private Activity myActivity;
    private ArrayList<GameBlock> myBlocks;
    private static final float GRID_WIDTH = 30;
    private static final float GRID_HEIGHT = 30;
    private static final float X_MAX = 200;
    private static final float Y_MAX = 200;

    public GameLoopTask(Activity activity){
        myActivity = activity;
    }

    @Override
    public void run() {

        //Set block new position based on direction

        for (final GameBlock block : myBlocks) {

            switch (block.getDirection()) {

                case LEFT:
                    if (block.getX() > 0) {
                        block.setX(block.getX() - GRID_WIDTH);
                    }
                    break;

                case RIGHT:
                    if (block.getX() < X_MAX) {
                        block.setX(block.getX() + GRID_WIDTH);
                    }
                    break;

                case UP:
                    if (block.getY() > 0) {
                        block.setY(block.getY() - GRID_HEIGHT);
                    }
                    break;

                case DOWN:
                    if (block.getY() < Y_MAX) {
                        block.setY(block.getY() + GRID_HEIGHT);
                    }
                    break;

                default:
            }


            //Stop block

            block.setDirection(GameBlock.Direction.STOPPED);

            //Update Screen

            myActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    block.setX(block.getX());
                    block.setY(block.getY());
                }
            });

        }
    }

    public void createBlock(){
        GameBlock block = new GameBlock(myActivity, 10, 10);
        myBlocks.add(block);
    }

}
