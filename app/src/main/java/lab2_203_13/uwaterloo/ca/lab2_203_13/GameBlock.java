package lab2_203_13.uwaterloo.ca.lab2_203_13;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by desmond on 6/24/17.
 */

public class GameBlock extends android.support.v7.widget.AppCompatImageView{

    public enum Direction{
        LEFT, RIGHT, UP, DOWN, STOPPED
    }

    private float x;
    private float y;
    private static final float IMAGE_SCALE = 1;
    private Direction myDirection;

    public GameBlock(Context myContext,float coordX, float coordY){
        super(myContext);
        myDirection = Direction.STOPPED;
        x = coordX;
        y = coordY;
        this.setImageResource(R.drawable.gameblock);
        this.setScaleX(IMAGE_SCALE);
        this.setScaleY(IMAGE_SCALE);
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    public Direction getDirection(){
        return myDirection;
    }

    public void setDirection(Direction dir){
        myDirection = dir;
    }
}
