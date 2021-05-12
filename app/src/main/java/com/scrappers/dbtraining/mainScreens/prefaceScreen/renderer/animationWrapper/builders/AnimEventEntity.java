package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.builders;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.TransformTrack;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.FastMath;

public class AnimEventEntity extends BaseAppState {
    protected AnimationEvents animationEvents;
    private float counter=0f;
    private int keyFrameIndex=1;
    private AnimComposer animComposer;
    private TransformTrack transformTrack;
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
                        animationEvents.onAnimationStart(animComposer, transformTrack);
                    }
                    delayStartCounter = 0f;
                }
            }
            //start the frame timer by the time for the first keyFrame
            counter += tpf;
            keyFrameIndex += 1;

            if ( (keyFrameIndex < transformTrack.getTimes().length-1) && (counter  >= FastMath.interpolateLinear(1, transformTrack.getTimes()[keyFrameIndex-1], transformTrack.getTimes()[keyFrameIndex])) ){
                //fire an event between the current interpolated frames --Cannot figure this out--WIP--
                if ( animationEvents != null ){
                    animationEvents.onAnimationShuffle(animComposer ,transformTrack, keyFrameIndex-1, keyFrameIndex);
                }
            }else if (counter >= transformTrack.getLength()/6f){
                if ( delayEndCounter >= delayEnd ){
                    //fire an end event --works fine--
                    if ( animationEvents != null ){
                        animationEvents.onAnimationEnd(animComposer, transformTrack);
                    }
                    //reset everything
                    counter = 0;
                    keyFrameIndex = 1;
                    delayEndCounter = 0f;
                }
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

    public void setTransformTrack(TransformTrack transformTrack) {
        this.transformTrack = transformTrack;
    }
    public interface AnimationEvents{
        void onAnimationStart(AnimComposer animComposer, TransformTrack transformationStart);
        void onAnimationEnd(AnimComposer animComposer, TransformTrack transformEnd);
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
