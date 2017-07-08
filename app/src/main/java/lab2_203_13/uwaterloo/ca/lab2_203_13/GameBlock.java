package lab2_203_13.uwaterloo.ca.lab2_203_13;

import android.content.Context;
import android.view.ViewGroup;

/**
 * Created by desmond on 6/24/17.
 */

public class GameBlock extends GameBlockTemplate{

    private static final float IMAGE_SCALE = 0.66F;

    private float targetX;
    private float targetY;
    private float velocity;

    public GameBlock(Context myContext,float coordX, float coordY){
        super(myContext);
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

    public boolean isStopped(){
        return targetX==getX() && targetY==getY();
    }


    @Override
    public void setDestination(float x, float y) {
        setTargetX(x);
        setTargetY(y);
    }

    @Override
    public void move() {
        if(getX()!=targetX){
            //Moving to the right
            if(getX()<targetX){
                if(targetX<getX()+velocity){
                    setX(targetX);
                }else{
                    setX(getX()+velocity);
                }
            }else{  //Moving to the left
                if(targetX>getX()-velocity){
                    setX(targetX);
                }else{
                    setX(getX()-velocity);
                }
            }
        }else if(getY()!=targetY){
            //Moving to the bottom
            if(getY()<targetY){
                if(targetY<getY()+velocity){
                    setY(targetY);
                }else{
                    setY(getY()+velocity);
                }
            }else {  //Moving to the top
                if(targetY>getY()-velocity){
                    setY(targetY);
                }else{
                    setY(getY()-velocity);
                }
            }
        }else{  //Blocks stopped
            velocity = 0;
            return;
        }

        velocity += ACCELERATION;
    }
}
