package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper;

import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.AnimTrack;
import com.jme3.anim.ArmatureMask;
import com.jme3.anim.Joint;
import com.jme3.anim.SkinningControl;
import com.jme3.anim.TransformTrack;
import com.jme3.anim.tween.action.ClipAction;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.builders.LayerBuilder;

/**
 * @author pavl_g.
 */
public class StackLoops extends BaseAppState {
    private final Spatial dataBaseStack;
    //Animation Composer control instance
    private AnimComposer animComposer;
    //AnimationClip
    final AnimClip animClip=new AnimClip("StackLoops");

    public StackLoops(String id,Spatial dataBaseStack){
        super(id);
        this.dataBaseStack=dataBaseStack;
    }
    @Override
    protected void initialize(Application app) {
        setEnabled(false);
        animComposer = dataBaseStack.getControl(AnimComposer.class);
        Spatial stackOne= ((Node)dataBaseStack).getChild("Cylinder.001");
        Spatial stackTwo= ((Node)dataBaseStack).getChild("Cylinder.002");


        SkinningControl skinningControl=dataBaseStack.getControl(SkinningControl.class);
        skinningControl.setHardwareSkinningPreferred(true);
        /*add models(or nodes or spatial) to the given Joints*/
        skinningControl.getAttachmentsNode("StackOne").attachChild(stackOne);
        skinningControl.getAttachmentsNode("StackTwo").attachChild(stackTwo);

        //Parallel Transform tracks
        final TransformTrack stackOneTrack=new TransformTrack();
        final TransformTrack stackTwoTrack=new TransformTrack();

        final Joint jointOne = skinningControl.getArmature().getJoint("StackOne");
        final Joint jointTwo = skinningControl.getArmature().getJoint("StackTwo");

        stackOneTrack.setTarget(jointOne);
        stackOneTrack.setTimes(new float[]{8,16,32,64});
        stackOneTrack.setKeyframesTranslation(new Vector3f[]{
                jointOne.getLocalTranslation(),
                jointOne.getLocalTranslation().subtract(new Vector3f(stackOne.getLocalScale().x+1f,0,0)),
                jointOne.getLocalTranslation().subtract(new Vector3f(0,stackOne.getLocalScale().y+0.2f,0)),
                jointOne.getLocalTranslation().add(new Vector3f(stackOne.getLocalScale().x,0,0)),
        });

        stackTwoTrack.setTarget(jointTwo);
        stackTwoTrack.setTimes(new float[]{8,16,32,64});
        stackTwoTrack.setKeyframesTranslation(new Vector3f[]{
                stackTwo.getLocalTranslation(),
                stackTwo.getLocalTranslation().add(new Vector3f(stackTwo.getLocalScale().x+1f,0,0)),
                stackTwo.getLocalTranslation().add(new Vector3f(0,stackTwo.getLocalScale().y+0.2f,0)),
                stackTwo.getLocalTranslation().add(new Vector3f(-stackTwo.getLocalScale().x,0,0)),
        });
        animClip.setTracks(new AnimTrack[]{stackOneTrack, stackTwoTrack});
        animComposer.addAnimClip(animClip);
        //start the animation composer in the default layer.
        animComposer.addAction(animClip.getName(), new ClipAction(animClip));
        animComposer.makeLayer(LayerBuilder.LAYER_STACKS, new ArmatureMask());

    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        if(animComposer != null){
            animComposer.setEnabled(true);
            animComposer.setCurrentAction("StackLoops", LayerBuilder.LAYER_STACKS);
        }
    }

    @Override
    protected void onDisable() {
        if(animComposer != null){
            animComposer.removeCurrentAction(LayerBuilder.LAYER_STACKS);
        }
    }
}
