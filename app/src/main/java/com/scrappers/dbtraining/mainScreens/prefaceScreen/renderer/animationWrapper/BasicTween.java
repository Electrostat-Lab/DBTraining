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
import com.jme3.anim.tween.action.ClipAction;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
/**
 * This class wraps multiple simple example for the new Animation System of Jme using multiple #{@link AnimClip}s , #{@link BaseAction}s , #{@link Tweens}.
 * , So this would be a brief preface on HOW the animation System works:
 *
 * So, basically the design is as follows :
 * <ul>
 * <li>-AnimComposer(AnimationManager class)</li>
 * <li>BaseActions(General Actions classes of AnimComposer , each of which holds one Tween thread)
 * A Tween Thread(Wraps the AnimClip instances into an Array of actions(ClipActions) & creates a relationShip between them ,
 * for example a Tween thread , holding 2 ClipActions that gets ran in Parallel or Sequence or As a Sine wave , this basically
 * would differ in the timing of the Tween ClipActions meaning which one will have to run before the other)</li>
 *<li>ClipAction(this class wraps the AnimClip into an Action executable Tween)</li>
 *<li>AnimClip(this class wraps sets of Transformations(Scales,Rotations,Translations) in keyFrames(like blender) with keyFrames timings)</li>
 *<li>AnimComposer class adds the AnimClips to the animation stacks</li>
 *<li>AnimComposer extends AbstractControl class , means that it gets added to a Spatial or Node in a SafeArrayList that executes it frequently in the update(tpf:Float) entry point)</li>
 *<li>AnimComposer class can set a CurrentAction to occur , which is the same as saying current one Tween that holds one ClipAction or multiple running in Parallel or Sequence or Sine....etc.</li>
 *<li>AnimComposer can have multiple layers that has multiple baseActions running around.</li>
 *</ul>
 * @apiNote  other examples on Jme/examples repo :
 * <ul>
 * <li><a href="https://github.com/jMonkeyEngine/jmonkeyengine/blob/master/jme3-examples/src/main/java/jme3test/model/anim/TestOgreAnim.java">See JMonkeyEngine other Animation Examples(WIP)</a></li>
 * <li><a href="https://github.com/jMonkeyEngine/jmonkeyengine/blob/master/jme3-examples/src/main/java/jme3test/model/anim/TestOgreComplexAnim.java">See JMonkeyEngine other Animation Examples(WIP)</a></li>
 * </ul>
 *
 * @author pavl_g
 *
 */
public class BasicTween extends BaseAppState {
    private final Spatial dataBaseStack;
    private AnimComposer animComposer;
    private BaseAction baseAction;

    public BasicTween(String id,Spatial dataBaseStack){
        super(id);
        this.dataBaseStack=dataBaseStack;
    }
    @Override
    protected void initialize(Application app) {
        setEnabled(false);
        animComposer = dataBaseStack.getControl(AnimComposer.class);
        Spatial stackOne= ((Node)dataBaseStack).getChild("Cylinder.001");
        Spatial stackTwo= ((Node)dataBaseStack).getChild("Cylinder.003");

        //AnimationClip
        final AnimClip stackOneClip=new AnimClip("BasicTweenOne");
        final AnimClip stackTwoClip=new AnimClip("BasicTweenTwo");
        //Transform tracks for each AnimClip
        final TransformTrack stackOneTrack=new TransformTrack();
        final TransformTrack stackTwoTrack=new TransformTrack();
        //the 1st TransformTrack settings.
        stackOneTrack.setTarget(stackOne);
        //add the times for each KeyFrame , ie this array must be of the same length as the keyFrames array
        stackOneTrack.setTimes(new float[]{8,16,32,64});
        stackOneTrack.setKeyframesTranslation(new Vector3f[]{
                stackOne.getLocalTranslation(),
                stackOne.getLocalTranslation().subtract(new Vector3f(stackOne.getLocalScale().x+1f,0,0)),
                stackOne.getLocalTranslation().subtract(new Vector3f(0,stackOne.getLocalScale().y+0.2f,0)),
                stackOne.getLocalTranslation().add(new Vector3f(stackOne.getLocalScale().x,0,0)),
        });
        //set the TransFormTracks to the stackOneClip AnimClip so we could use them in a Tween thread(as a ClipAction Thread).
        stackOneClip.setTracks(new AnimTrack[]{stackOneTrack});

        //the 2nd TransformTrack settings.
        stackTwoTrack.setTarget(stackTwo);
        //add the times for each KeyFrame , ie this array must be of the same length as the keyFrames array
        stackTwoTrack.setTimes(new float[]{8,16,32,64});
        stackTwoTrack.setKeyframesTranslation(new Vector3f[]{
                stackTwo.getLocalTranslation(),
                stackTwo.getLocalTranslation().add(new Vector3f(stackTwo.getLocalScale().x+1f,0,0)),
                stackTwo.getLocalTranslation().add(new Vector3f(0,stackTwo.getLocalScale().y+0.2f,0)),
                stackTwo.getLocalTranslation().add(new Vector3f(-stackTwo.getLocalScale().x,0,0)),
        });
        //set the TransFormTracks to the stackOneClip AnimClip so we could use them in a Tween thread(as a ClipAction Thread).
        stackTwoClip.setTracks(new AnimTrack[]{stackTwoTrack});

        //the adds part
        //add the animation clips to the animation composer(manager)
        animComposer.addAnimClip(stackOneClip);
        animComposer.addAnimClip(stackTwoClip);

        /*NB : new ClipAction(stackOneClip) is the same as animComposer.makeAction(stackTwoClip.getName()) , just an extra confusing data-Structure on the collection though , LoL*/
        ClipAction stackOneClipAction=new ClipAction(stackOneClip);
        Action stackTwoClipAction=animComposer.makeAction(stackTwoClip.getName());
        //base actions that can be triggered to run via makeAction() or action() from AnimComposer(Animation Manager)
        baseAction=new BaseAction(Tweens.parallel(stackOneClipAction,
                stackTwoClipAction));
        /* add the baseAction that holds the Tween thread actions , into a HashMap of General actions for each AnimComposer*/
        /*btw,this is still an idle action*/

        //change some BaseAction,ClipActions settings
        //set the General speed for this whole BaseAction Tween based thread, ie it's applied on all of its child Tween & their ClipActions.
        baseAction.setSpeed(5f);
        /*those has no effect , since those 2 ClipActions are constrained to a single BaseAction , if you need separate speeds , then separate BaseActions as well*/
        stackOneClipAction.setSpeed(-1f);
        stackTwoClipAction.setSpeed(-40);
        /*run this baseAction that has a Tween of actions , each of which has it's own AnimClip ,
         each AnimClip has it's own TransformTrack with Time Frames & Transformation keyFrames*/
        /*notice that we ran an existed(added before) Abstract Action to the AnimComposer*/
        animComposer.makeLayer(LayerBuilder.LAYER_BASIC_TWEEN, new ArmatureMask());

    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        if(animComposer != null){
            animComposer.setEnabled(true);
            animComposer.addAction("BasicTween",baseAction);
            animComposer.setCurrentAction("BasicTween", LayerBuilder.LAYER_BASIC_TWEEN);
        }
    }

    @Override
    protected void onDisable() {
        if(animComposer != null){
            animComposer.removeCurrentAction(LayerBuilder.LAYER_BASIC_TWEEN);
        }
    }
}
