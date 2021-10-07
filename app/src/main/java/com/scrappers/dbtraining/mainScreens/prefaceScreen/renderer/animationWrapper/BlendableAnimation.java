package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper;

import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.AnimTrack;
import com.jme3.anim.ArmatureMask;
import com.jme3.anim.TransformTrack;
import com.jme3.anim.tween.Tweens;
import com.jme3.anim.tween.action.Action;
import com.jme3.anim.tween.action.BaseAction;
import com.jme3.anim.tween.action.BlendAction;
import com.jme3.anim.tween.action.BlendSpace;
import com.jme3.anim.tween.action.BlendableAction;
import com.jme3.anim.tween.action.ClipAction;
import com.jme3.anim.tween.action.LinearBlendSpace;
import com.jme3.anim.util.HasLocalTransform;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.events.AnimEvent;
import com.jme3.cinematic.events.AnimationEvent;
import com.jme3.cinematic.events.CinematicEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.builders.LayerBuilder;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.customBlendAction.CustomBlendAction;

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
public class BlendableAnimation extends BaseAppState {
    private final Spatial dataBaseStack;
    //create a global AnimComposer Control instance
    private AnimComposer animComposer;
    private BlendSpace radialBlendSpace;
    private BlendAction blendAction;
    private float count = 0;
    private static final float minValueOfBlendSlider = 3;
    private static final float maxValueOfBlendSlider = 6;
    private boolean shuffleFlag = false;

    public BlendableAnimation(final String id, final Spatial dataBaseStack){
        super(id);
        this.dataBaseStack=dataBaseStack;
    }
    @Override
    protected void initialize(Application app) {
        //disabling the animation at the Start-up
        setEnabled(false);
        animComposer = dataBaseStack.getControl(AnimComposer.class);
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
        //8)Create ClipAction instances for the AnimClips (BlendableActions)
        final ClipAction capRotationClip = new ClipAction(capRotationAnimClip);
        final ClipAction bottleTractionClip=new ClipAction(bottleTractionAnimClip);
        bottleTractionClip.setTransitionLength(10f);
        bottleTractionClip.setLength(10f);
        capRotationClip.setLength(10f);
        capRotationClip.setTransitionLength(10f);
        //9)feed the BlendableActions to a single BlendAction

        radialBlendSpace = new CustomBlendAction.RadialBlendSpace(minValueOfBlendSlider, maxValueOfBlendSlider);

        blendAction = new CustomBlendAction(radialBlendSpace, capRotationClip, bottleTractionClip);
        final BaseAction baseAction = new BaseAction(blendAction);
        baseAction.setLength(10f);
        baseAction.setSpeed(2f);
        //10)add that BlendAction to the AnimComposer using addAction(...)
        animComposer.addAction("SimulateBottleFall", blendAction);
        animComposer.makeLayer(LayerBuilder.LAYER_BLENDABLE_ANIM, new ArmatureMask());

    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        if(animComposer != null){
            //bind the AnimComposer to the appState status.
            animComposer.setEnabled(true);
            //11)run this BlendAction in the default layer
            animComposer.setCurrentAction("SimulateBottleFall", LayerBuilder.LAYER_BLENDABLE_ANIM);
        }
    }

    @Override
    protected void onDisable() {
        if(animComposer != null){
            animComposer.removeCurrentAction(LayerBuilder.LAYER_BLENDABLE_ANIM);
        }
    }

    @Override
    public void update(float tpf) {
        count += tpf;
        if(count > blendAction.getLength()){
            if(!shuffleFlag) {
                radialBlendSpace.setValue(FastMath.extrapolateLinear(80f * count, minValueOfBlendSlider, maxValueOfBlendSlider));
                ((CustomBlendAction.RadialBlendSpace)radialBlendSpace).shuffleActionIndices();
                setShuffleFlag(true);
            }else{
                radialBlendSpace.setValue(FastMath.interpolateLinear(0.8f, minValueOfBlendSlider, maxValueOfBlendSlider));
                setShuffleFlag(false);
            }
            //reset counter
            count = 0;
            //shuffle actions for the regular run.
            ((CustomBlendAction.RadialBlendSpace)radialBlendSpace).shuffleActionIndices();
        }
    }

    public boolean isShuffleFlag() {
        return shuffleFlag;
    }

    public void setShuffleFlag(boolean shuffleFlag) {
        this.shuffleFlag = shuffleFlag;
    }
}
