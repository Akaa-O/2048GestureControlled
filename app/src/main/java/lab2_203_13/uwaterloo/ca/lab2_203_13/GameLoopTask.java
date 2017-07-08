package lab2_203_13.uwaterloo.ca.lab2_203_13;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Random;
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
    private static final float X_MAX = 746.99F;
    private static final float Y_MAX = 746.99F;

    private static final float[] XPositions = {X_MIN, 210.33f, 478.66f, X_MAX};
    private static final float[] YPositions = {Y_MIN, 210.33f, 478.66f, Y_MAX};
    private static final float SLOT_ISOLATION = 268.33f;

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

        if(myBlocks.size()==16){
            return;
        }
        // Generate new coordinates for game block
        Random randomNum = new Random();
        int newX;
        int newY;

        do{
            newX = randomNum.nextInt(4);
            newY = randomNum.nextInt(4);
        }while(isOccupied(newX, newY));

        GameBlockTemplate block = new GameBlock(context, XPositions[newX], YPositions[newY], relativeLayout);
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
        int offset;
        for(GameBlockTemplate block : myBlocks){
            switch(direction){
                case LEFT:
                    offset = inBetween(block, Direction.LEFT, true);
                    block.setDestination(X_MIN+offset*SLOT_ISOLATION, block.getY());
                    break;
                case RIGHT:
                    offset = inBetween(block, Direction.RIGHT, true);
                    block.setDestination(X_MAX-offset*SLOT_ISOLATION, block.getY());
                    break;
                case UP:
                    offset = inBetween(block, Direction.UP, false);
                    block.setDestination(block.getX(), Y_MIN+offset*SLOT_ISOLATION);
                    break;
                case DOWN:
                    offset = inBetween(block, Direction.DOWN, false);
                    block.setDestination(block.getX(), Y_MAX-offset*SLOT_ISOLATION);
                    break;
                default:
                    break;
            }
        }
        createBlock();
    }


    // isOccupied()
    // XYCoords is a coordinate pair (x,y) obtained from each block in the myBlocks array

    public boolean isOccupied(float x, float y) {
        for (GameBlockTemplate block : myBlocks) {
            if (Math.abs(x-block.getTargetX())<1 && Math.abs(y-block.getTargetY())<1) {
                return true;
            }
        }
        return false;
    }

    public boolean isCurrentlyOccupied(float x, float y) {
        for (GameBlockTemplate block : myBlocks) {
            if (Math.abs(x-block.getX())<1 && Math.abs(y-block.getY())<1) {
                return true;
            }
        }
        return false;
    }


    public enum Direction{
        LEFT, RIGHT, UP, DOWN, STOPPED
    }

    private int inBetween(GameBlockTemplate currentBlock, Direction dir, boolean checksX){
        int num = 0;
        float currentlyChecking;
        if(checksX){
            if(dir == Direction.LEFT){
                currentlyChecking = X_MIN;
                while(currentlyChecking-currentBlock.getX()<-1){
                    if(isCurrentlyOccupied(currentlyChecking, currentBlock.getY())){
                        System.out.println(currentlyChecking);
                        System.out.println(currentBlock.getY());
                        num++;
                    }
                    currentlyChecking+=SLOT_ISOLATION;
                }
                System.out.println(num);
                return num;
            }else if(dir == Direction.RIGHT){
                currentlyChecking = X_MAX;
                while(currentlyChecking-currentBlock.getX()>1){
                    if(isCurrentlyOccupied(currentlyChecking, currentBlock.getY())){
                        System.out.println(currentlyChecking);
                        System.out.println(currentBlock.getY());
                        num++;
                    }
                    currentlyChecking-=SLOT_ISOLATION;
                }
                System.out.println(num);
                return num;
            }
        }else{
            if(dir == Direction.UP){
                currentlyChecking = Y_MIN;
                while(currentlyChecking-currentBlock.getY()<-1){
                    if(isCurrentlyOccupied(currentBlock.getX(), currentlyChecking)){
                        System.out.println(currentBlock.getX());
                        System.out.println(currentlyChecking);
                        num++;
                    }
                    currentlyChecking+=SLOT_ISOLATION;
                }
                System.out.println(num);
                return num;
            }else if(dir == Direction.DOWN){
                currentlyChecking = Y_MAX;
                while(currentlyChecking-currentBlock.getX()>1){
                    if(isCurrentlyOccupied(currentBlock.getX(), currentlyChecking)){
                        System.out.println(currentBlock.getX());
                        System.out.println(currentlyChecking);
                        num++;
                    }
                    currentlyChecking-=SLOT_ISOLATION;
                }
                System.out.println(num);
                return num;
            }
        }
        return num;
    }
}
