package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper;

import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.AnimTrack;
import com.jme3.anim.TransformTrack;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * @author pavl_g.
 */
public class StackLoops extends BaseAppState {
    private final Spatial dataBaseStack;
    //Animation Composer control instance
    private final AnimComposer animComposer=new AnimComposer();
    public StackLoops(String id,Spatial dataBaseStack){
        super(id);
        this.dataBaseStack=dataBaseStack;
    }
    @Override
    protected void initialize(Application app) {
        setEnabled(false);
        Spatial stackOne= ((Node)dataBaseStack).getChild("Cylinder.001");
        Spatial stackTwo= ((Node)dataBaseStack).getChild("Cylinder.003");

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
    public AnimComposer getAnimComposer() {
        return animComposer;
    }
}
