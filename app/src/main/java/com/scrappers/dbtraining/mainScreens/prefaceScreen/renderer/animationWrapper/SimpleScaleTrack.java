package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper;

import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.AnimTrack;
import com.jme3.anim.ArmatureMask;
import com.jme3.anim.TransformTrack;
import com.jme3.anim.tween.action.BaseAction;
import com.jme3.anim.tween.action.ClipAction;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * @author pavl_g.
 */
public class SimpleScaleTrack extends BaseAppState {
    private final Spatial dataBaseStack;
    private  AnimComposer animComposer;
    private final TransformTrack transformTrack = new TransformTrack();
    private float counter=0f;
    private int keyFrameIndex=1;
    private AnimationEvents animationEvents;
    /*
     * Create new AnimClip
     * Add the animClip to new AnimControl System(AnimComposer)
     */
    final AnimClip clip = new AnimClip("StackAnimation");

    public SimpleScaleTrack(String id, Spatial dataBaseStack){
        super(id);
        this.dataBaseStack=dataBaseStack;
    }
    @Override
    protected void initialize(Application app) {
        setEnabled(false);
        animComposer = dataBaseStack.getControl(AnimComposer.class);
        //1-Basic implementation of the animation tracks , using transformation matrices tracks(Scaling,Rotation,Translation).
        final Spatial stackOne = ((Node) dataBaseStack).getChild("Cylinder.002");

        animComposer.addAnimClip(clip);

        /*set the bone or the spatial*/
        transformTrack.setTarget(stackOne);

        //the frame tracks are in-sequence or in parallel with each transform track , notice it must be in a pattern form , in which the proceeding value
        //is delayed by (n) * the preceding value , n is scaleFactor of delayFactor , is determinant by the coder , here its simply 2.
        transformTrack.setTimes(new float[]{5,10,15,20,25,30,35,40,45,50,55});
        //Zoom in - Zoom out Scales keyFrames
        transformTrack.setKeyframesScale(new Vector3f[]{
                new Vector3f(stackOne.getLocalScale().clone().divide(1.2f)),
                new Vector3f(stackOne.getLocalScale().clone().divide(3f)),
                new Vector3f(stackOne.getLocalScale().clone().divide(4f)),
                new Vector3f(stackOne.getLocalScale().clone().divide(5f)),
                new Vector3f(stackOne.getLocalScale().clone().divide(6f)),
                new Vector3f(stackOne.getLocalScale().clone().divide(7f)),
                new Vector3f(stackOne.getLocalScale().clone().divide(8f)),
                new Vector3f(stackOne.getLocalScale().clone().divide(9f)),
                new Vector3f(stackOne.getLocalScale().clone().divide(10f)),
        });

        transformTrack.setKeyframesTranslation(new Vector3f[]{
                stackOne.getLocalTranslation(),
                stackOne.getLocalTranslation(),
                stackOne.getLocalTranslation(),
                stackOne.getLocalTranslation(),
                stackOne.getLocalTranslation(),
                stackOne.getLocalTranslation(),
                stackOne.getLocalTranslation(),
                stackOne.getLocalTranslation(),
                stackOne.getLocalTranslation(),
                stackOne.getLocalTranslation(),
                stackOne.getLocalTranslation()

        });
        // a Bottle cap rotation Simulation keyFrames
        //in order to do Quaternion keyFrames , you need to know first :
        //1)what is the total angle of rotation that you want to rotate with.
        //2)how much keyFrames you wanna have
        //3)according to the former givens : thetaKeyFrame=totalAngleOfRotation/numberOfKeyFrames.
        transformTrack.setKeyframesRotation(new Quaternion[]{
                //start from angle 0
                new Quaternion().fromAngleAxis(0f,Vector3f.UNIT_Y),
                //do theta=360 on 5 keyFrames , so each keyFrameAngle=2*pi/5
                stackOne.getLocalRotation().fromAngleAxis(2*FastMath.PI/5,Vector3f.UNIT_Y),
                stackOne.getLocalRotation().fromAngleAxis(2*FastMath.PI/5,Vector3f.UNIT_Y),
                stackOne.getLocalRotation().fromAngleAxis(2*FastMath.PI/5,Vector3f.UNIT_Y),
                stackOne.getLocalRotation().fromAngleAxis(2*FastMath.PI/5,Vector3f.UNIT_Y),
                stackOne.getLocalRotation().fromAngleAxis(2*FastMath.PI/5,Vector3f.UNIT_Y),
                //now reverse theta=-360 on 5 keyFrames , so each keyFrameAngle=-2*pi/5
                stackOne.getLocalRotation().fromAngleAxis(-2*FastMath.PI/5,Vector3f.UNIT_Y),
                stackOne.getLocalRotation().fromAngleAxis(-2*FastMath.PI/5,Vector3f.UNIT_Y),
                stackOne.getLocalRotation().fromAngleAxis(-2*FastMath.PI/5,Vector3f.UNIT_Y),
                stackOne.getLocalRotation().fromAngleAxis(-2*FastMath.PI/5,Vector3f.UNIT_Y),
                stackOne.getLocalRotation().fromAngleAxis(-2*FastMath.PI/5,Vector3f.UNIT_Y),
        });
        /*sets the tracks of this animClip*/
        clip.setTracks(new AnimTrack[]{transformTrack});
        animComposer.addAction(clip.getName(), new ClipAction(clip));
        animComposer.makeLayer(LayerBuilder.LAYER_SIMPLE_TRACK, new ArmatureMask());
    }

    @Override
    protected void cleanup(Application app) {}

    @Override
    protected void onEnable() {
        if(animComposer != null){
            /*bind the animation Composer(animation Manager) to the AppState currentState*/
            animComposer.setEnabled(true);
            /*
             * start the animation
             */
            animComposer.setCurrentAction("StackAnimation");
        }
    }

    @Override
    public void update(float tpf) {
        if(animComposer.isEnabled() && animComposer != null){
            if (counter == 0){
                //fire an event on the start of the Animation
                if ( animationEvents != null ){
                    animationEvents.onAnimationStart(animComposer, transformTrack);
                }
            }
            //start the frame timer by the time for the first keyFrame
            counter += tpf;
            keyFrameIndex += 1;
            if ( (keyFrameIndex < transformTrack.getTimes().length-1) && (counter  >= FastMath.interpolateLinear(1, transformTrack.getTimes()[keyFrameIndex-1], transformTrack.getTimes()[keyFrameIndex])) ){
                //fire an event between the current interpolated frames
                if ( animationEvents != null ){
                    animationEvents.onAnimationShuffle(animComposer ,transformTrack, keyFrameIndex-1, keyFrameIndex);
                }
            }else if (counter >= transformTrack.getLength()){
                //fire an end event
                if ( animationEvents != null ){
                    animationEvents.onAnimationEnd(animComposer, transformTrack);
                }
                //reset everything
                counter = 0;
                keyFrameIndex = 1;
            }

        }
    }

    @Override
    protected void onDisable() {
        if(animComposer != null){
            /*
             * start the animation
             */
            animComposer.removeCurrentAction();
        }
    }
    public interface AnimationEvents{
        void onAnimationStart(AnimComposer animComposer, TransformTrack transformationStart);
        void onAnimationEnd(AnimComposer animComposer, TransformTrack transformEnd);
        void onAnimationShuffle(AnimComposer animComposer, TransformTrack transformTrack, int i, int keyFrameIndex);
    }

    public void setAnimationEvents(AnimationEvents animationEvents) {
        this.animationEvents = animationEvents;
    }
}
