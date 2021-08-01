package com.scrappers.dbtraining.mainScreens.prefaceScreen;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.jme3.app.jmeSurfaceView.JmeSurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MultiGestureGLView extends JmeSurfaceView {

    public MultiGestureGLView(@NonNull Context context) {
        super(context);
    }

    public MultiGestureGLView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiGestureGLView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_POINTER_DOWN:
                //get the pointers using IDs
                int pointer1 = event.getPointerId(0);
                int pointer2 = event.getPointerId(1);
                //fetch the current active 2 active pointers data
                getActiveGestures(event);
                break;
            case MotionEvent.ACTION_MOVE:
                getActiveGestures(event);
                break;
        }
        return true;
    }

    /**
     * Get the 1st 2 active pointers
     * @param event the touch event
     */
    private void getActiveGestures(MotionEvent event){
        float x0 = event.getX(0);
        float y0 = event.getY(0);

        float x1 = event.getX(1);
        float y1 = event.getY(1);
        System.out.println("(x0, y0) = "+ x0+ " "+y0+"\n"+
                "(x1, y1) = "+x1+" "+y1+"\n");
    }
}
