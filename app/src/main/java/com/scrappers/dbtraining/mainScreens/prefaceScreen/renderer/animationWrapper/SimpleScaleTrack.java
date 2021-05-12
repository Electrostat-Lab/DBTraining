package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper;

import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.AnimTrack;
import com.jme3.anim.ArmatureMask;
import com.jme3.anim.TransformTrack;
import com.jme3.anim.tween.action.ClipAction;
import com.jme3.app.Application;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.builders.AnimEventEntity;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.builders.LayerBuilder;

/**
 * @author pavl_g.
 */
public class SimpleScaleTrack extends AnimEventEntity {
    private final Spatial dataBaseStack;
    private  AnimComposer animComposer;
    private final TransformTrack transformTrack = new TransformTrack();
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
        /*set the animComposer*/
        setAnimComposer(animComposer);
        //1-Basic implementation of the animation tracks , using transformation matrices tracks(Scaling,Rotation,Translation).
        final Spatial stackOne = ((Node) dataBaseStack).getChild("Cylinder.002");

        animComposer.addAnimClip(clip);

        /*set the bone or the spatial*/
        transformTrack.setTarget(stackOne);
        /*set the transformTrack to be used by the animationEvents*/
        setTransformTrack(transformTrack);

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
        /*
         * start the animation
         */
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
            animComposer.setCurrentAction("StackAnimation");
        }
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
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

}
