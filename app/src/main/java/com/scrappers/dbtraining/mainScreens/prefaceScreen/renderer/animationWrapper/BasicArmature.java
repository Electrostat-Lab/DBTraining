package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper;

import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.AnimTrack;
import com.jme3.anim.Armature;
import com.jme3.anim.Joint;
import com.jme3.anim.SkinningControl;
import com.jme3.anim.TransformTrack;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * <b>A State class that wraps an example for a basic Armature Structure DataBaseStack 3D object with different Joints(Bones) that have some discs(cylinders)
 * from the database 3d object attached to their nodes.</b>
 * <ul>
 * <li>An Armature#{@link Armature#Armature(Joint[])} has a joint list.</li>
 * <li>Each Joint#{@link Joint#Joint(String)} has got an empty node#{@link SkinningControl#getAttachmentsNode(String)}</li>
 * <li>A SkinningControl#{@link SkinningControl#SkinningControl(Armature)} performs the Transformations on this mesh joints</li>
 * <li>To attach a Node to a Joint use #{@link SkinningControl#getAttachmentsNode(String)}</li>
 * <li>Every Joint implements HasLocalTransform#{@link com.jme3.anim.util.HasLocalTransform} ,So it can be used inside keyFrames as any Nodes or Spatial or Emitters</li>
 * </ul>
 * @author pavl_g.
 */
public class BasicArmature extends BaseAppState {
    private final Spatial dataBaseStack;
    private final AnimComposer animComposer=new AnimComposer();
    public BasicArmature(String id,Spatial dataBaseStack){
        super(id);
        this.dataBaseStack=dataBaseStack;
    }
    @Override
    protected void initialize(Application app) {
        //setting the state to be disabled after being attached to the stateManager by the AnimationFactory UI class.
        setEnabled(false);
        /*create empty joints that hold empty nodes for the latter attachments*/
        Joint stackOne=new Joint("StackOne");
        Joint stackTwo=new Joint("StackTwo");
        /*create an armature , ie body to hold those joints*/
        Armature armature=new Armature(new Joint[]{stackOne,stackTwo});
        SkinningControl skinningControl=new SkinningControl(armature);
        skinningControl.setHardwareSkinningPreferred(true);
        /*notify this spatial of the incoming updates from the skinning control*/
        skinningControl.setSpatial(dataBaseStack);
        /*add models(or nodes or spatial) to the given Joints*/
        skinningControl.getAttachmentsNode(stackOne.getName()).attachChild(((Node)dataBaseStack).getChild("Cylinder.001"));
        skinningControl.getAttachmentsNode(stackOne.getName()).attachChild(((Node)dataBaseStack).getChild("Cylinder.003"));

        //AnimationClip
        final AnimClip animClip=new AnimClip("StackLoops");
        //Parallel Transform tracks
        final TransformTrack stackOneTrack=new TransformTrack();
        final TransformTrack stackTwoTrack=new TransformTrack();

        stackOneTrack.setTarget(stackOne);
        stackOneTrack.setTimes(new float[]{8,16,32,64});
        stackOneTrack.setKeyframesTranslation(new Vector3f[]{
                stackOne.getLocalTranslation(),
                stackOne.getLocalTranslation().subtract(new Vector3f(stackOne.getLocalScale().x+1f,0,0)),
                stackOne.getLocalTranslation().subtract(new Vector3f(0,stackOne.getLocalScale().y+0.2f,0)),
                stackOne.getLocalTranslation().add(new Vector3f(stackOne.getLocalScale().x,0,0)),
        });

        stackTwoTrack.setTarget(stackTwo);
        stackTwoTrack.setTimes(new float[]{8,16,32,64});
        stackTwoTrack.setKeyframesTranslation(new Vector3f[]{
                stackTwo.getLocalTranslation(),
                stackTwo.getLocalTranslation().add(new Vector3f(stackTwo.getLocalScale().x+1f,0,0)),
                stackTwo.getLocalTranslation().add(new Vector3f(0,stackTwo.getLocalScale().y+0.2f,0)),
                stackTwo.getLocalTranslation().add(new Vector3f(-stackTwo.getLocalScale().x,0,0)),
        });
        animClip.setTracks(new AnimTrack[]{stackOneTrack,stackTwoTrack});

        animComposer.addAnimClip(animClip);
        dataBaseStack.addControl(skinningControl);
        dataBaseStack.addControl(animComposer);
        //start the animation composer in the default layer.
        animComposer.setCurrentAction(animClip.getName());
        animComposer.setEnabled(false);
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        animComposer.setEnabled(true);
    }

    @Override
    protected void onDisable() {
        animComposer.setEnabled(false);
    }
}
