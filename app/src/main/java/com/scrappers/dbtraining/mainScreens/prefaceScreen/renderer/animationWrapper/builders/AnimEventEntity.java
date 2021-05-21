package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.builders;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.TransformTrack;
import com.jme3.anim.tween.action.Action;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;

public class AnimEventEntity extends BaseAppState {
    protected AnimationEvents animationEvents;
    private float counter=0f;
    private int keyFrameIndex=1;
    private AnimComposer animComposer;
    private Action action;
    private float delayStartCounter=0;
    private float delayEndCounter=0;
    private float delayStart = 0.2f;
    private float delayEnd = 0.2f;
    public AnimEventEntity(String id){
        super(id);
    }
    @Override
    protected void initialize(Application app) {
        //default configuration goes here
        setEnabled(false);
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        if(animComposer != null){
            animComposer.setEnabled(true);
        }
    }

    @Override
    public void update(float tpf) {
        if(animComposer.isEnabled() && animComposer != null){
            delayStartCounter += tpf;
            delayEndCounter += tpf;
            if (counter == 0){
                if(delayStartCounter >= delayStart ){
                    //fire an event on the start of the Animation --works fine--
                    if ( animationEvents != null ){
                        animationEvents.onAnimationStart(animComposer, action);
                    }
                    delayStartCounter = 0f;
                }
            }
            //start the frame timer by the time for the first keyFrame
            counter += tpf;
            keyFrameIndex += 1;

         if (counter > action.getLength()/6f){
                    //fire an end event --works fine--
                    if ( animationEvents != null ){
                        animationEvents.onAnimationEnd(animComposer, action);
                    }
                    //reset everything
                    counter = 0;
                    keyFrameIndex = 1;
                    delayEndCounter = 0f;

            }

        }
    }

    @Override
    protected void onDisable() {
        if(animComposer != null){
            animComposer.setEnabled(false);
        }
    }
    public void setAnimComposer(AnimComposer animComposer) {
        this.animComposer = animComposer;
    }

    public int getKeyFrameIndex() {
        return keyFrameIndex;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public interface AnimationEvents{
        void onAnimationStart(AnimComposer animComposer, Action transformationStart);
        void onAnimationEnd(AnimComposer animComposer, Action transformEnd);
        void onAnimationShuffle(AnimComposer animComposer, TransformTrack transformTrack, int i, int keyFrameIndex);
    }

    public void setDelayEnd(float delayEnd) {
        this.delayEnd = delayEnd;
    }

    public void setDelayStart(float delayStart) {
        this.delayStart = delayStart;
    }

    public float getDelayStart() {
        return delayStart;
    }

    public float getDelayEnd() {
        return delayEnd;
    }

    public void setAnimationEvents(AnimationEvents animationEvents) {
        this.animationEvents = animationEvents;
    }

}
