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


    private ArrayList<GameBlock> myBlocks;
    private static final float X_MIN = -58F;
    private static final float Y_MIN = -58F;
    private static final float X_MAX = 747F;
    private static final float Y_MAX = 747F;
    private static final float ACCELERATION = 30;


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
        final GameBlock block = new GameBlock(context, X_MIN, Y_MIN);
        relativeLayout.addView(block);
        myBlocks.add(block);
    }

    public void move(){
        for(GameBlock block : myBlocks){
            switch(currentDirection){
                case LEFT:
                    block.setTargetX(X_MIN);
                    if(block.getX()>block.getTargetX()){
                        block.setX(block.getX()-block.getVelocity());
                        block.setVelocity(block.getVelocity()+ACCELERATION);
                    }else{
                        block.setX(block.getTargetX());
                        block.setVelocity(0);
                        block.setDirection(Direction.STOPPED);
                        currentDirection = Direction.STOPPED;
                    }
                    break;
                case RIGHT:
                    block.setTargetX(X_MAX);
                    if(block.getX()<block.getTargetX()){
                        block.setX(block.getX()+block.getVelocity());
                        block.setVelocity(block.getVelocity()+ACCELERATION);
                    }else{
                        block.setX(block.getTargetX());
                        block.setVelocity(0);
                        block.setDirection(Direction.STOPPED);
                        currentDirection = Direction.STOPPED;
                    }
                    break;
                case UP:
                    block.setTargetY(Y_MIN);
                    if(block.getY()>block.getTargetY()){
                        block.setY(block.getY()-block.getVelocity());
                        block.setVelocity(block.getVelocity()+ACCELERATION);
                    }else{
                        block.setY(block.getTargetY());
                        block.setVelocity(0);
                        block.setDirection(Direction.STOPPED);
                        currentDirection = Direction.STOPPED;
                    }
                    break;
                case DOWN:
                    block.setTargetY(Y_MAX);
                    if(block.getY()<block.getTargetY()){
                        block.setY(block.getY()+block.getVelocity());
                        block.setVelocity(block.getVelocity()+ACCELERATION);
                    }else{
                        block.setY(block.getTargetY());
                        block.setVelocity(0);
                        block.setDirection(Direction.STOPPED);
                        currentDirection = Direction.STOPPED;
                    }
                    break;
                default:
                    block.setDirection(Direction.STOPPED);
                    currentDirection = Direction.STOPPED;
                    break;
            }
        }
    }

    public void setDirection(Direction direction){
        if(!currentDirection.equals(Direction.STOPPED)){
            return;
        }
        currentDirection = direction;
        for(GameBlock block : myBlocks){
            block.setDirection(direction);
        }
    }

    public enum Direction{
        LEFT, RIGHT, UP, DOWN, STOPPED
    }
}
