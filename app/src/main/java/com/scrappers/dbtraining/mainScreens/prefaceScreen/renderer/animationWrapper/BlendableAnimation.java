package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper;

import android.os.Build;

import androidx.annotation.RequiresApi;
import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.AnimTrack;
import com.jme3.anim.ArmatureMask;
import com.jme3.anim.TransformTrack;
import com.jme3.anim.tween.action.Action;
import com.jme3.anim.tween.action.BaseAction;
import com.jme3.anim.tween.action.BlendAction;
import com.jme3.anim.tween.action.BlendSpace;
import com.jme3.anim.tween.action.BlendableAction;
import com.jme3.anim.tween.action.ClipAction;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.misc.LayerBuilder;
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
    private BlendSpace blendSpace;
    private BlendAction blendAction;
    private float count = 0;
    private boolean shuffleFlag = false;
    private final float timeToReboot = 0f;

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
        final TransformTrack capRotation = new TransformTrack(null, null, null, null, null);
        final TransformTrack bottleTraction = new TransformTrack(null, null, null, null, null);

        //3)initialize settings for TransformTracks
        //set a target object that implements HasLocalTransform(eg: Spatial , node , emitters , AudioNode , etc) for your track
        capRotation.setTarget(dataBaseStack);
        capRotation.setTimes(new float[]{2, 4, 8, 16});
        //specify the totalAngleOfRotation(theta) then divide that by number of keyFrames , to get the angle for each frame
        capRotation.setKeyframesRotation(new Quaternion[]{
                new Quaternion().fromAngleAxis(0f, Vector3f.UNIT_Y),
                dataBaseStack.getLocalRotation().fromAngleAxis((float) Math.toRadians(120), Vector3f.UNIT_Y),
                dataBaseStack.getLocalRotation().fromAngleAxis((float) Math.toRadians(120), Vector3f.UNIT_Y),
                dataBaseStack.getLocalRotation().fromAngleAxis((float) Math.toRadians(120), Vector3f.UNIT_Y)
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
        final AnimClip capRotationAnimClip = new AnimClip("CapRotation");
        final AnimClip bottleTractionAnimClip = new AnimClip("BottleTraction");

        //5)set the tracks to the AnimClip
        capRotationAnimClip.setTracks(new TransformTrack[]{capRotation});
        bottleTractionAnimClip.setTracks(new TransformTrack[]{bottleTraction});

        //6)add the AnimClips to the AnimComposer , the adds part
        animComposer.addAnimClip(capRotationAnimClip);
        animComposer.addAnimClip(bottleTractionAnimClip);

        //8)Create ClipAction instances for the AnimClips (BlendableActions)
        final ClipAction capRotationClip = new ClipAction(capRotationAnimClip);
        final ClipAction bottleTractionClip = new ClipAction(bottleTractionAnimClip);


        blendSpace = new CustomBlendAction.PieChartSpace(0.5f, 180f);

        blendAction = new CustomBlendAction(blendSpace, capRotationClip, bottleTractionClip);

        //baseAction supporting Tweens, blendingActions, & regular ClipActions
        final BaseAction baseAction = new BaseAction(blendAction);
        baseAction.setSpeed(5f);

        //10)add that BlendAction to the AnimComposer using addAction(...)
        animComposer.addAction("SimulateBottleFall", baseAction);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void update(float tpf) {
        count += tpf;
        if(count > blendAction.getLength()){
            if (!shuffleFlag) {
//                ((CustomBlendAction.ContinuitySpace)blendSpace).setScaleFactor(0.3222f);
//                ((CustomBlendAction.ContinuitySpace)blendSpace).setValues(new Vector4f[]{
//                        new Vector4f(FastMath.PI * 0.004f, 0.2f ,0.2f ,0.4f),
//                        new Vector4f(FastMath.PI * 0.05f, 0.2f ,0 ,0),
//                });
                ((CustomBlendAction.PieChartSpace) blendSpace).setRadius(0.5f);
                ((CustomBlendAction.PieChartSpace) blendSpace).setAngle(200f);
                setShuffleFlag(true);
            } else {
//                ((CustomBlendAction.ContinuitySpace)blendSpace).setScaleFactor(1 / count *  ((CustomBlendAction.ContinuitySpace)blendSpace).getScaleFactor());
//                ((CustomBlendAction.ContinuitySpace)blendSpace).setValues(new Vector4f[]{
//                        new Vector4f(FastMath.PI, 0.02f ,0.2f ,0.4f),
//                        new Vector4f(FastMath.PI * 0.025f, 0.2f ,0 ,0),
//                });
                ((CustomBlendAction.PieChartSpace) blendSpace).setRadius(1f);
                ((CustomBlendAction.PieChartSpace) blendSpace).setAngle(150f);
                setShuffleFlag(false);
            }
            //reset counter
            count = 0;
        }
    }


    public boolean isShuffleFlag() {
        return shuffleFlag;
    }

    public void setShuffleFlag(boolean shuffleFlag) {
        this.shuffleFlag = shuffleFlag;
    }
}
