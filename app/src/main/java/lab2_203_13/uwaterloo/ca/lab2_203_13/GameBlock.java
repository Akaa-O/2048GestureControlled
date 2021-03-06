package lab2_203_13.uwaterloo.ca.lab2_203_13;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by desmond on 6/24/17.
 */

public class GameBlock extends GameBlockTemplate{

    private static final float IMAGE_SCALE = 0.66F;
    private static final float yTEXT_OFFSET = 120f;
    private static final float xTEXT_OFFSET = 105f;

    private float targetX;
    private float targetY;
    private float velocity;
    private int value;

    public TextView mTextView;


    public GameBlock(Context myContext,float coordX, float coordY, RelativeLayout layout){
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
        layout.addView(this);

        mTextView = new TextView(myContext);
        Random randNum = new Random();
        value = (randNum.nextInt(2)+1)*2;
        mTextView.setText(String.valueOf(value));
        mTextView.setTextSize(38);
        mTextView.setX(coordX+xTEXT_OFFSET);
        mTextView.setY(coordY+yTEXT_OFFSET);
        layout.addView(mTextView);
        mTextView.bringToFront();
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

    public boolean isStopped(){
        return targetX==getX() && targetY==getY();
    }

    public int getValue(){
        return value;
    }

    public void setValue(){
        value*=2;
        mTextView.setText(String.valueOf(value));
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
                    mTextView.setX(targetX+xTEXT_OFFSET);
                }else{
                    setX(getX()+velocity);
                    mTextView.setX(getX()+velocity+xTEXT_OFFSET);
                }
            }else{  //Moving to the left
                if(targetX>getX()-velocity){
                    setX(targetX);
                    mTextView.setX(targetX+xTEXT_OFFSET);
                }else{
                    setX(getX()-velocity);
                    mTextView.setX(getX()-velocity+xTEXT_OFFSET);
                }
            }
        }else if(getY()!=targetY){
            //Moving to the bottom
            if(getY()<targetY){
                if(targetY<getY()+velocity){
                    setY(targetY);
                    mTextView.setY(targetY+yTEXT_OFFSET);
                }else{
                    setY(getY()+velocity);
                    mTextView.setY(targetY+velocity+yTEXT_OFFSET);
                }
            }else {  //Moving to the top
                if(targetY>getY()-velocity){
                    setY(targetY);
                    mTextView.setY(targetY+yTEXT_OFFSET);
                }else{
                    setY(getY()-velocity);
                    mTextView.setY(targetY-velocity+yTEXT_OFFSET);
                }
            }
        }else{  //Blocks stopped
            velocity = 0;
            return;
        }

        velocity += ACCELERATION;
    }
}
