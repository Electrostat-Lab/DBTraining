package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper;

import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.AnimTrack;
import com.jme3.anim.TransformTrack;
import com.jme3.anim.tween.action.Action;
import com.jme3.anim.tween.action.BlendAction;
import com.jme3.anim.tween.action.BlendSpace;
import com.jme3.anim.tween.action.BlendableAction;
import com.jme3.anim.tween.action.ClipAction;
import com.jme3.anim.util.HasLocalTransform;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * <b>A class demonstrating the usage of BlendableActions #{@link BlendableAction} & BlendActions #{@link BlendAction}</b>
 * <ul>
 * <li>---Attach the AnimClips#{@link AnimClip#setTracks(AnimTrack[])} instances (holding your TransformTracks#{@link TransformTrack}) to the AnimComposer#{@link AnimComposer#addAnimClip(AnimClip)}---</li>
 * <li>---Feed the AnimClips#{@link AnimClip}(Concrete class of the abstract BlendableAction) into a Single BlendAction#{@link BlendAction#BlendAction(BlendSpace, BlendableAction...)}---</li>
 * <li>---Add that Single BlendAction#{@link BlendAction} to the AnimComposer#{@link AnimComposer} using {@link AnimComposer#addAction(String, Action)}---</li>
 * <li>---Run that BlendAction using the name as id using {@link AnimComposer#setCurrentAction(String)} in the default layer---</li>
 * </ul>
 * @author pavl_g
 */
public class BlendableAnimation extends BaseAppState implements BlendSpace {
    private final Spatial dataBaseStack;
    //create a global AnimComposer Control instance
    private final AnimComposer animComposer=new AnimComposer();
    public BlendableAnimation(final String id, final Spatial dataBaseStack){
        super(id);
        this.dataBaseStack=dataBaseStack;
    }
    @Override
    protected void initialize(Application app) {
        //disabling the animation at the Start-up
        setEnabled(false);
        //let's simulate a bottle glass fall on a floor..LoL

        //1) collect the objects , you want to animate
        //2)create a TransformTrack
        final TransformTrack capRotation=new TransformTrack();
        final TransformTrack bottleTraction=new TransformTrack();
        //3)initialize settings for TransformTracks
        //set a target object that implements HasLocalTransform(eg: Spatial , node , emitters , AudioNode , etc) for your track
        capRotation.setTarget(dataBaseStack);
        capRotation.setTimes(new float[]{2,4,8,16});
        //specify the totalAngleOfRotation(theta) then divide that by number of keyFrames , to get the angle for each frame
        capRotation.setKeyframesRotation(new Quaternion[]{
                new Quaternion().fromAngleAxis(0f,Vector3f.UNIT_Y),
                dataBaseStack.getLocalRotation().fromAngleAxis((float)Math.toRadians(120), Vector3f.UNIT_Y),
                dataBaseStack.getLocalRotation().fromAngleAxis((float)Math.toRadians(120), Vector3f.UNIT_Y),
                dataBaseStack.getLocalRotation().fromAngleAxis((float)Math.toRadians(120), Vector3f.UNIT_Y)
        });
        //set a target object that implements HasLocalTransform(eg: Spatial , node , emitters , AudioNode , etc) for your track
        bottleTraction.setTarget(dataBaseStack);
        bottleTraction.setTimes(new float[]{2,4,8,16});
        bottleTraction.setKeyframesRotation(new Quaternion[]{
                new Quaternion().fromAngleAxis(0f,Vector3f.UNIT_X),
                dataBaseStack.getLocalRotation().fromAngleAxis((float)Math.toRadians(30), Vector3f.UNIT_X),
                dataBaseStack.getLocalRotation().fromAngleAxis((float)Math.toRadians(30), Vector3f.UNIT_X),
                dataBaseStack.getLocalRotation().fromAngleAxis((float)Math.toRadians(30), Vector3f.UNIT_X)
        });
        //4)create AnimClips instances & instantiate them to use the TransformTracks
        final AnimClip capRotationAnimClip=new AnimClip("CapRotation");
        final AnimClip bottleTractionAnimClip=new AnimClip("BottleTraction");
        //5)set the tracks to the AnimClip
        capRotationAnimClip.setTracks(new TransformTrack[]{capRotation});
        bottleTractionAnimClip.setTracks(new TransformTrack[]{bottleTraction});
        //6)add the AnimClips to the AnimComposer , the adds part
        animComposer.addAnimClip(capRotationAnimClip);
        animComposer.addAnimClip(bottleTractionAnimClip);
        //7)add the AnimComposer Control to the HasLocalTransform
        dataBaseStack.getParent().addControl(animComposer);
        //8)Create ClipAction instances for the AnimClips (BlendableActions)
        ClipAction capRotationClip=new ClipAction(capRotationAnimClip);
        ClipAction bottleTractionClip=new ClipAction(bottleTractionAnimClip);
        capRotationClip.setTransitionLength(200d);
        bottleTractionClip.setTransitionLength(150d);
        //9)feed the BlendableActions to a single BlendAction
        BlendAction blendAction=new BlendAction(this,bottleTractionClip,capRotationClip);

        //10)add that BlendAction to the AnimComposer using addAction(...)
        animComposer.addAction("SimulateBottleFall",blendAction);
        //11)run this BlendAction in the default layer
        animComposer.setCurrentAction("SimulateBottleFall");
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        //bind the AnimComposer to the appState status.
        animComposer.setEnabled(true);
    }

    @Override
    protected void onDisable() {
        //bind the AnimComposer to the appState status.
        animComposer.setEnabled(false);
    }

    /**
     * additional method for additional settings to set during the instantiation of #{@link BlendAction} instance
     * this method gets called inside the Constructor of #{@link BlendAction#BlendAction(BlendSpace, BlendableAction...)} when the BlendAction instance gets instantiated\.
     * @param action your blendAction instance.
     */
    @Override
    public void setBlendAction(BlendAction action) {
        /*sets the length of transitions between keyFrames-delay time between keyFrames*/
        action.setTransitionLength(200);
        action.setSpeed(1);
    }

    /**
     * Returns the value of the weight of the transformations interpolation(ie delta or scaleFactor).
     * #{@link BlendableAction#interpolate(double)} , #{@link BlendableAction#getWeight()} ,#{BlendAction#blendWeight}.
     * #{@link BlendAction#collectTransform(HasLocalTransform, Transform, float, BlendableAction)} :
     * basically collects the transforms of that model & outputs the result of interpolation between them using the scaleDeltaFactor TransitionWeight or blendWeight.
     * @return the value of the weight of the transformations interpolation(ie delta or scaleFactor)
     * @apiNote Notice :
     * <ol>
     * <li>
     * If you set the delta(weight) of the interpolation to 1 :
     * that means the next Transform would be equal to the next keyFrame that you have specified using #{@link TransformTrack#setKeyframes(float[], Vector3f[], Quaternion[], Vector3f[])}
     * </li>
     * <li>
     * If you set the delta(weight) of the interpolation to values less than 1 :
     * that means the next Transform would be less than the next keyFrame that you have specified using #{@link TransformTrack#setKeyframes(float[], Vector3f[], Quaternion[], Vector3f[])}
     * by (1-getWeight()) value , due to the linear interpolation.
     * </li>
     * <li>
     * If you set the delta(weight) of the interpolation to values higher than 1:
     * that means the next Transform (of the 2nd active Action only) would be more greater than your local next keyFrame by (getWeight()*getLength()) times ,
     * because #{@link Transform#interpolateTransforms(Transform, Transform, float)} isn't protected against values bigger than 1 , so values bigger than 1 would extrapolate it
     * that means even if you didn't setScaleKeyFrames() , the BlendAction would interpolate between the tr1.getScales() & tr2.getScales() by a delta of 2 ,
     * that would lead to scaling the object by 2 times it's initial value.
     * </li>
     * <li>
     * If you set the delta(weight) of the interpolation to values Zero:
     * that means the next Transform would be equal to the previous while iterating over your TransformTrack keyFrames(would lead to a very smooth motion)  ,
     *  #{@link Transform#interpolateTransforms(Transform, Transform, float)}.
     * </li>
     * </ol>
     *
     * <b>NOTICE2(WIP) : i have also noticed that the 2nd , 3rd , 4th , .... blendable actions for the single blendAction run only if the getWeight() is > 1 , in the 2nd loop
     * of the keyFrames
     * --->because this #{@link BlendAction#setCollectTransformDelegate(BlendableAction)} gets settled to null after the first run of the #{@link BlendAction#doInterpolate(double)} &
     * the secondary blendActions are dependant upon #{@link BlendAction#collect(HasLocalTransform, Transform)} to provide their interpolation which basically
     * interpolates only if(getWeight() > 1) or less than 1.
     *</b>
     */
    @Override
    public float getWeight() {
        return 0.8f;
    }

    /**
     * this doesn't actually get called or used by anything in the universe
     * @param value DON'T USE
     */
    @Deprecated
    @Override
    public void setValue(float value) {}
}
