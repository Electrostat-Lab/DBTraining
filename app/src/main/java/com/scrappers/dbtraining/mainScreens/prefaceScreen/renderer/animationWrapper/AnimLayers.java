package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper;

import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.AnimTrack;
import com.jme3.anim.ArmatureMask;
import com.jme3.anim.SkinningControl;
import com.jme3.anim.TransformTrack;
import com.jme3.anim.tween.Tweens;
import com.jme3.anim.tween.action.BaseAction;
import com.jme3.anim.tween.action.ClipAction;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class AnimLayers extends BaseAppState {
    private final Spatial dataBaseStack;
    private AnimComposer animComposer;
     public AnimLayers(final String id, final Spatial dataBaseStack){
        super(id);
        this.dataBaseStack=dataBaseStack;
    }
    @Override
    protected void initialize(Application app) {
        setEnabled(false);
        animComposer=dataBaseStack.getControl(AnimComposer.class);
        //getting the SkinningControl we have used before that got an Armature - Some Nodes attached to it's joints
        SkinningControl skinningControl=dataBaseStack.getControl(SkinningControl.class);
        ArmatureMask armatureMask=new ArmatureMask();
        //add the StackOne Joint to the collection of the masked Joints
        armatureMask.addFromJoint(skinningControl.getArmature(),"StackOne");
        AnimClip animClip=animComposer.getAnimClip("BasicArmature");
        animComposer.addAnimClip(animClip);
        BaseAction baseAction1=new BaseAction(Tweens.parallel(new ClipAction(animClip)));
        baseAction1.setSpeed(8f);
        baseAction1.setLength(2f);
        baseAction1.setMask(armatureMask);
        animComposer.addAction("CrazyLooping",baseAction1);
        //make a layer using ArmatureMask
        animComposer.makeLayer(LayerBuilder.LAYER_ARMATURE_LAYERS, armatureMask);
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        if(animComposer !=null ){
            animComposer.setEnabled(true);
            animComposer.setCurrentAction("CrazyLooping", LayerBuilder.LAYER_ARMATURE_LAYERS);
        }
    }

    @Override
    protected void onDisable() {
        if(animComposer !=null ){
            animComposer.removeCurrentAction(LayerBuilder.LAYER_ARMATURE_LAYERS);
        }
    }
}





