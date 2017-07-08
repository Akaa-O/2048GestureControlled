package lab2_203_13.uwaterloo.ca.lab2_203_13;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;

/**
 * Created by Frank on 2017-07-08.
 */

public abstract class GameBlockTemplate extends AppCompatImageView{

    protected static final float ACCELERATION = 30;

    public GameBlockTemplate(Context context) {
        super(context);
    }

    public abstract void setDestination(float x, float y);

    public abstract boolean isStopped();

    public abstract void move();

    public abstract float getTargetX();

    public abstract float getTargetY();

    public abstract int getValue();

    public abstract void setValue();
}
