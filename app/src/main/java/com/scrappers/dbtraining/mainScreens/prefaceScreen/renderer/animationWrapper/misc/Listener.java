package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.misc;

import com.jme3.anim.tween.Tween;
import com.jme3.anim.tween.Tweens;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import java.util.Arrays;

/**
 * A state class to listen for timings points for a {@link Listener.TimeUtilisingAction}, by injecting an action {@link Listener.InterpolationAction} on each turn.
 * @author pavl_g.
 */
public class Listener extends BaseAppState {
    //primitives
    private float time = 0f;
    private float speed = 1f;
    private float length = 0;
    private boolean repeat = true;
    //instances
    private Tween tween;
    private Object[] data = new Object[1];
    private TimeUtilisingAction timeUtilisingAction;
    private InterpolationAction interpolationAction;

    /**
     * Instantiate a listener with a default arg.
     * @param stateManager the state manager.
     * @param length the duration at which execution of {@link InterpolationAction#onInterpolate(Application, float, float, Object[])} stops executing.
     */
    public Listener(final AppStateManager stateManager, final float length){
        this(stateManager, length, new Object[1]);
    }
    /**
     * Instantiate a listener with a custom object arg.
     * @param stateManager the state manager.
     * @param length the duration at which execution of {@link InterpolationAction#onInterpolate(Application, float, float, Object[])} stops executing.
     * @param data the user data to be injected.
     */
    public Listener(final AppStateManager stateManager, final float length, final Object[] data){
        if(stateManager == null){
            throw new IllegalStateException("Cannot create a listener for a null manager !");
        }
        stateManager.attach(this);
        setEnabled(false);
        if(length >= 0) {
            this.length = length;
        }
        //sanity check inputs before using
        if(data != null) {
            this.data = data;
        }
    }

    @Override
    protected void initialize(Application app) {
        //inject a parallel action -- you can remove this and do your code here.
        if(timeUtilisingAction != null){
            setLength(timeUtilisingAction.injectAction(getApplication()));
        }
        tween = Tweens.callTweenMethod(length, this, "onInterpolate", Arrays.toString(data));
    }

    @Override
    protected void cleanup(Application app) {
        //clean-up helps the garbage collector
        tween = null;
        data = null;
        timeUtilisingAction = null;
        interpolationAction = null;
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    @Override
    public void update(float tpf) {
        //advance the timings per frames
        time += tpf * speed;
        //skip if the time isn't appropriate
        if (time < 0f) {
            return;
        }
        final boolean isRunning = tween.interpolate(time);

        //reset time when its over
        if (!isRunning) {
            time = 0f;
            //gain the repetition flag
            if(interpolationAction != null){
                repeat = interpolationAction.isRepetitiveAction();
            }
        }
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Sets the duration at which the execution stops.
     * @param length the duration in seconds.
     */
    public void setLength(float length) {
        this.length = length;
    }

    public float getLength() {
        return length;
    }

    public void injectData(final Object[] data){
        this.data = data;
    }

    public Object[] getData() {
        return data;
    }

    /**
     * Called within each {@link Tween#interpolate(double)}, until the time reaches the length, then it repeats.
     * @param time the current time in seconds.
     * @param data the user injected data.
     */
    private void onInterpolate(final float time, final String data){
        if(!repeat){
            return;
        }
        if(interpolationAction != null){
            interpolationAction.onInterpolate(getApplication(), time, length, this.data);
        }
    }

    public void setInterpolationAction(InterpolationAction interpolationAction) {
        this.interpolationAction = interpolationAction;
    }

    public void setTimeUtilisingAction(TimeUtilisingAction timeUtilisingAction) {
        this.timeUtilisingAction = timeUtilisingAction;
    }

    /**
     * Use this interface to inject a time utilising action into {@link Listener#initialize(Application)}, such as {@link com.jme3.audio.AudioNode}.
     */
    public interface TimeUtilisingAction {
        /**
         * Injects a time utilising action, to run it before listening to its time bands.
         * @param application the app instance.
         * @return length in seconds.
         */
        float injectAction(final Application application);
    }

    /**
     * Injects an action to run it each interpolation turn.
     */
    public interface InterpolationAction {
        /**
         * Injects an action into interpolation turn.
         * @param application the app instance.
         * @param time the current timings with respect to the length (duration).
         * @param data the user injected data.
         */
        void onInterpolate(final Application application, final float time, final float length, final Object[] data);

        /**
         * Checks whether the action is repetitive.
         * @return true if the action is a repetitive one, false otherwise.
         */
        boolean isRepetitiveAction();
    }
}
