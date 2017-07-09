package lab2_203_13.uwaterloo.ca.lab2_203_13;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashSet;
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
    public boolean endGameW = false;
    public boolean endGameL = false;

    private ArrayList<GameBlockTemplate> myBlocks;

    private HashSet<GameBlockTemplate> mergedBlocks;

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
        mergedBlocks = new HashSet<>();
    }

    @Override
    public void run() {
        //Update Screen

        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (myBlocks.size() == 16) {
                    ArrayList<GameBlockTemplate> ordered = new ArrayList<GameBlockTemplate>();
                    for(float posY : YPositions){
                        for(float posX : XPositions){
                            ordered.add(isCurrentlyOccupied(posX, posY));
                        }
                    }

                    int indexUp, indexRight, indexDown, indexLeft;
                    boolean stillPlayable = false;

                    for (int i = 0; i < 16; i++) {

                        GameBlockTemplate currentBlock = ordered.get(i);

                        if (i + 4 < 16) {
                            indexDown = i + 4;
                            if(ordered.get(indexDown)!=null && ordered.get(indexDown).getValue()==currentBlock.getValue()){
                                stillPlayable = true;
                                break;
                            }
                        }
                        if (i - 4 >= 0) {
                            indexUp = i - 4;
                            if(ordered.get(indexUp)!=null && ordered.get(indexUp).getValue()==currentBlock.getValue() ){
                                stillPlayable = true;
                                break;
                            }
                        }
                        if (i + 1 < 16) {
                            indexRight = i + 1;
                            if(ordered.get(indexRight)!=null && ordered.get(indexRight).getValue()==currentBlock.getValue() ){
                                stillPlayable = true;
                                break;
                            }
                        }
                        if (i - 1 >= 0) {
                            indexLeft = i - 1;
                            if(ordered.get(indexLeft)!=null && ordered.get(indexLeft).getValue()==currentBlock.getValue() ){
                                stillPlayable = true;
                                break;
                            }
                        }
                    }

                    Log.d("Playable", String.valueOf(stillPlayable));
                    if (!stillPlayable) {
                        endGameL= true;
                    }
                    if(endGameW){
                        Log.d("Game Status:", "You Win");
                    }else if (endGameL){
                        Log.d("Game Status:", "You Lose");
                    }

                }

                move();
            }
        });

    }

    public void createBlock(){

        if (myBlocks.size() == 16) {
            return;
        }

        // Generate new coordinates for game block
        Random randomNum = new Random();
        int newX;
        int newY;

        do{
            newX = randomNum.nextInt(4);
            newY = randomNum.nextInt(4);
        }while(isOccupied(XPositions[newX], YPositions[newY]));

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
        if(!currentDirection.equals(Direction.STOPPED) || endGameW == true || endGameL == true){
//            if(endGameW){
//                Log.d("Game Status:", "You Win");
//            }else if (endGameL){
//                Log.d("Game Status:", "You Lose");
//            }
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

        // Clear collected merged blocks

        for (GameBlockTemplate block : mergedBlocks) {
            myBlocks.remove(block);
            relativeLayout.removeView(block);
            relativeLayout.removeView(((GameBlock)block).mTextView);
        }

        mergedBlocks.clear();

        //createBlock();

        for(int i=0; i< myBlocks.size() ; ++i){
            if (myBlocks.get(i).getValue() == 256){
                endGameW = true;
            }
        }
        if(!endGameW && !endGameL) {
            createBlock();
        }
    }


    // isOccupied()
    // XYCoords is a coordinate pair (x,y) obtained from each block in the myBlocks array

    public boolean isOccupied(float x, float y) {
        for (GameBlockTemplate block : myBlocks) {
            if (Math.abs(x-block.getTargetX())<10 && Math.abs(y-block.getTargetY())<10) {
                return true;
            }
        }
        return false;
    }

    public GameBlockTemplate isCurrentlyOccupied(float x, float y) {
        for (GameBlockTemplate block : myBlocks) {
            if (Math.abs(x-block.getX())<10 && Math.abs(y-block.getY())<10) {
                return block;
            }
        }
        return null;
    }


    public enum Direction{
        LEFT, RIGHT, UP, DOWN, STOPPED
    }

    private int inBetween(GameBlockTemplate currentBlock, Direction dir, boolean checksX){
        int num = 0;
        float currentlyChecking;
        GameBlockTemplate occupant;
        ArrayList<GameBlockTemplate> occupants = new ArrayList<>();
        if(checksX){
            if(dir == Direction.LEFT){
                currentlyChecking = X_MIN;
                while(currentlyChecking-currentBlock.getX()<-10){
                    occupant = isCurrentlyOccupied(currentlyChecking, currentBlock.getY());
                    if(occupant!=null){
                        System.out.println(currentlyChecking);
                        System.out.println(currentBlock.getY());
                        occupants.add(occupant);
                        num++;
                    }
                    currentlyChecking+=SLOT_ISOLATION;
                }
                System.out.println(num);
                return findPosition(num, occupants, currentBlock);
            }else if(dir == Direction.RIGHT){
                currentlyChecking = X_MAX;
                while(currentlyChecking-currentBlock.getX()>10){
                    occupant = isCurrentlyOccupied(currentlyChecking, currentBlock.getY());
                    if(occupant!=null){
                        System.out.println(currentlyChecking);
                        System.out.println(currentBlock.getY());
                        occupants.add(occupant);
                        num++;
                    }
                    currentlyChecking-=SLOT_ISOLATION;
                }
                System.out.println(num);
                return findPosition(num, occupants, currentBlock);
            }
        }else{
            if(dir == Direction.UP){
                currentlyChecking = Y_MIN;
                while(currentlyChecking-currentBlock.getY()<-10){
                    occupant = isCurrentlyOccupied(currentBlock.getX(), currentlyChecking);
                    if(occupant!=null){
                        System.out.println(currentBlock.getX());
                        System.out.println(currentlyChecking);
                        occupants.add(occupant);
                        num++;
                    }
                    currentlyChecking+=SLOT_ISOLATION;
                }
                System.out.println(num);
                return findPosition(num, occupants, currentBlock);
            }else if(dir == Direction.DOWN){
                currentlyChecking = Y_MAX;
                while(currentlyChecking-currentBlock.getY()>10){
                    occupant = isCurrentlyOccupied(currentBlock.getX(), currentlyChecking);
                    if(occupant!=null){
                        System.out.println(currentBlock.getX());
                        System.out.println(currentlyChecking);
                        occupants.add(occupant);
                        num++;
                    }
                    currentlyChecking-=SLOT_ISOLATION;
                }
                System.out.println(num);
                return findPosition(num, occupants, currentBlock);
            }
        }
        return num;
    }

    private int findPosition(int num, ArrayList<GameBlockTemplate> occupants, GameBlockTemplate currentBlock){
        switch(num){
            case 0:
                break;
            case 1:
                if(occupants.get(0).getValue()==currentBlock.getValue()){
                    occupants.get(0).setValue();
                    mergedBlocks.add(currentBlock);
                    //myBlocks.remove(currentBlock);
                    //currentBlock = null;
                    num--;
                }
                break;
            case 2:
                if(occupants.get(0).getValue()==occupants.get(1).getValue()){
                    occupants.get(0).setValue();
                    mergedBlocks.add(occupants.get(1));
                    occupants.set(1, null);
                    num--;
                }else if(occupants.get(1).getValue()==currentBlock.getValue()){
                    occupants.get(1).setValue();
                    mergedBlocks.add(currentBlock);
                    //myBlocks.remove(currentBlock);
                    //currentBlock = null;
                    num--;
                }
                break;
            case 3:
                if(occupants.get(0).getValue()==occupants.get(1).getValue()){
                    occupants.get(0).setValue();
                    mergedBlocks.add(occupants.get(1));
                    //myBlocks.remove(occupants.get(1));
                    occupants.set(1,null);
                    num--;
                    if(occupants.get(2).getValue()==currentBlock.getValue()){
                        occupants.get(2).setValue();
                        mergedBlocks.add(currentBlock);
                        //myBlocks.remove(currentBlock);
                        //currentBlock = null;
                        num--;
                    }
                }else if(occupants.get(1).getValue()==occupants.get(2).getValue()){
                    occupants.get(1).setValue();
                    mergedBlocks.add(occupants.get(2));
                    // myBlocks.remove(occupants.get(2));
                    occupants.set(2,null);
                    num--;
                }else if(occupants.get(2).getValue()==currentBlock.getValue()){
                    occupants.get(2).setValue();
                    mergedBlocks.add(currentBlock);
                    // myBlocks.remove(currentBlock);
                    //currentBlock = null;
                    num--;
                }
                break;
            default:
                break;
        }
        return num;
    }
}
