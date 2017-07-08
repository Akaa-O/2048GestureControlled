package lab2_203_13.uwaterloo.ca.lab2_203_13;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Created by desmond on 6/24/17.
 */

public class GameLoopTask extends TimerTask{

    private Activity myActivity;
    private RelativeLayout relativeLayout;
    private Context context;
    public Direction currentDirection;


    private ArrayList<GameBlockTemplate> myBlocks;
    private static final float X_MIN = -58F;
    private static final float Y_MIN = -58F;
    private static final float X_MAX = 747F;
    private static final float Y_MAX = 747F;


    public GameLoopTask(Activity activity, Context context, RelativeLayout relativeLayout){
        myActivity = activity;
        myBlocks = new ArrayList<>();
        this.relativeLayout = relativeLayout;
        this.context = context;
        currentDirection = Direction.STOPPED;
    }

    @Override
    public void run() {
        //Update Screen

        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                move();
            }
        });

    }

    public void createBlock(){
        GameBlockTemplate block = new GameBlock(context, X_MIN, Y_MIN);
        relativeLayout.addView(block);
        myBlocks.add(block);
    }

    public void move(){
        boolean directionStopped = true;
        for(GameBlockTemplate block : myBlocks){
            block.move();
            if(!block.isStopped()) {
                directionStopped = false;
            }
        }
        if (directionStopped){
            currentDirection = Direction.STOPPED;
        }
    }

    public void setDirection(Direction direction){
        if(!currentDirection.equals(Direction.STOPPED)){
            return;
        }
        currentDirection = direction;
        for(GameBlockTemplate block : myBlocks){
            switch(direction){
                case LEFT:
                    block.setDestination(X_MIN, block.getY());
                    break;
                case RIGHT:
                    block.setDestination(X_MAX, block.getY());
                    break;
                case UP:
                    block.setDestination(block.getX(), Y_MIN);
                    break;
                case DOWN:
                    block.setDestination(block.getX(), Y_MAX);
                    break;
                default:
                    break;
            }
        }
    }

    public enum Direction{
        LEFT, RIGHT, UP, DOWN, STOPPED
    }
}
