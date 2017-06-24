package lab2_203_13.uwaterloo.ca.lab2_203_13;

import android.content.Context;
import android.view.ViewGroup;

/**
 * Created by desmond on 6/24/17.
 */

public class GameBlock extends android.support.v7.widget.AppCompatImageView{

    private static final float IMAGE_SCALE = 0.66F;
    private GameLoopTask.Direction myDirection;

    private float targetX;
    private float targetY;
    private float velocity;

    public GameBlock(Context myContext,float coordX, float coordY){
        super(myContext);
        myDirection = GameLoopTask.Direction.STOPPED;
        setX(coordX);
        setY(coordY);
        this.setImageResource(R.drawable.gameblock);
        this.setScaleType(ScaleType.FIT_XY);
        this.setScaleX(IMAGE_SCALE);
        this.setScaleY(IMAGE_SCALE);
        velocity = 0;
        targetX = coordX;
        targetY = coordY;
    }

    public void setTargetX(float targetX){
        this.targetX = targetX;
    }

    public void setTargetY(float targetY){
        this.targetY = targetY;
    }

    public float getTargetX(){
        return targetX;
    }

    public float getTargetY(){
        return targetY;
    }

    public float getVelocity(){
        return velocity;
    }
    public void setVelocity(float velocity){
        this.velocity = velocity;
    }

    public GameLoopTask.Direction getDirection(){
        return myDirection;
    }

    public void setDirection(GameLoopTask.Direction dir){
        myDirection = dir;
    }
}
