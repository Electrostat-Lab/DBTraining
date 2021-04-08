package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.ArmatureMask;
import com.jme3.anim.SkinningControl;
import com.jme3.anim.tween.Tweens;
import com.jme3.anim.tween.action.BaseAction;
import com.jme3.anim.tween.action.ClipAction;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
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

        BaseAction baseAction1=new BaseAction(Tweens.sequence(new ClipAction(animComposer.getAnimClip("StackLoops"))));
        animComposer.addAction("CrazyLooping",baseAction1);
        //make a layer using ArmatureMask
        animComposer.makeLayer("Looper",armatureMask);
        animComposer.setCurrentAction("CrazyLooping","Looper");

    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        if(animComposer !=null ){
            animComposer.setEnabled(true);
        }
    }

    @Override
    protected void onDisable() {
        if(animComposer !=null ){
            animComposer.setEnabled(false);
        }
    }
}





