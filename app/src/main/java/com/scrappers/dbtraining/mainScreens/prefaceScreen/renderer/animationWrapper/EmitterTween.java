package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper;

import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.AnimTrack;
import com.jme3.anim.TransformTrack;
import com.jme3.anim.tween.Tweens;
import com.jme3.anim.tween.action.BaseAction;
import com.jme3.anim.tween.action.ClipAction;
import com.jme3.anim.util.HasLocalTransform;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author pavl_g.
 */
public class EmitterTween extends BaseAppState {
    private final Spatial dataBaseStack;
    private final AnimComposer animComposer=new AnimComposer();
    private AudioNode shockThunder;
    public EmitterTween(final String id, final Spatial dataBaseStack){
        super(id);
        this.dataBaseStack=dataBaseStack;
    }
    @Override
    protected void initialize(Application app) {
        setEnabled(false);
        shockThunder=loadElectricWaves();
        //initialize the electricWaves particle Emitters instances w/ their properties.
        ParticleEmitter electricWaves1=loadElectricWaves(0);
        ParticleEmitter electricWaves2=loadElectricWaves(1);
        ParticleEmitter electricWaves3=loadElectricWaves(2);
        ParticleEmitter electricWaves4=loadElectricWaves(3);
        //set the local translation of them.
        electricWaves1.setLocalTranslation(dataBaseStack.getLocalTranslation().getX()-1f
                ,dataBaseStack.getLocalTranslation().getY()+1f,dataBaseStack.getLocalTranslation().getZ());

        electricWaves2.setLocalTranslation(dataBaseStack.getLocalTranslation().getX()-1f
                ,dataBaseStack.getLocalTranslation().getY()-1f,dataBaseStack.getLocalTranslation().getZ());

        electricWaves3.setLocalTranslation(dataBaseStack.getLocalTranslation().getX()+1f
                ,dataBaseStack.getLocalTranslation().getY()+1f,dataBaseStack.getLocalTranslation().getZ());

        electricWaves4.setLocalTranslation(dataBaseStack.getLocalTranslation().getX()+1f
                ,dataBaseStack.getLocalTranslation().getY()-1f,dataBaseStack.getLocalTranslation().getZ());
        shockThunder.setLocalTranslation(dataBaseStack.getLocalTranslation().mult(2));

        if(!dataBaseStack.getParent().hasChild(electricWaves1)){
            //add them to the scene.
            dataBaseStack.getParent().attachChild(electricWaves1);
            dataBaseStack.getParent().attachChild(electricWaves2);
            dataBaseStack.getParent().attachChild(electricWaves3);
            dataBaseStack.getParent().attachChild(electricWaves4);
            dataBaseStack.getParent().attachChild(shockThunder);
        }

        //create 4 TransformTracks that would run in parallel
        final TransformTrack electricWavesTrack1=new TransformTrack();
        final TransformTrack electricWavesTrack2=new TransformTrack();
        final TransformTrack electricWavesTrack3=new TransformTrack();
        final TransformTrack electricWavesTrack4=new TransformTrack();
        final TransformTrack shockThunderTrack=new TransformTrack();
        //create 3 AnimClips , each of which would hold one track
        final AnimClip waves1Clip=new AnimClip(electricWaves1.getName());
        final AnimClip waves2Clip=new AnimClip(electricWaves2.getName());
        final AnimClip waves3Clip=new AnimClip(electricWaves3.getName());
        final AnimClip waves4Clip=new AnimClip(electricWaves4.getName());
        final AnimClip thunderClip=new AnimClip(shockThunder.getName());

        //set the Targets
        electricWavesTrack1.setTarget(electricWaves1);
        electricWavesTrack2.setTarget(electricWaves2);
        electricWavesTrack3.setTarget(electricWaves3);
        electricWavesTrack4.setTarget(electricWaves4);
        shockThunderTrack.setTarget(shockThunder);

        //1st track
        electricWavesTrack1.setTimes(new float[]{2,4,8,16});
        electricWavesTrack1.setKeyframesTranslation(new Vector3f[]{
                new Vector3f().interpolateLocal(electricWaves1.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0),
                new Vector3f().interpolateLocal(electricWaves1.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
                new Vector3f().interpolateLocal(electricWaves1.getLocalTranslation(),dataBaseStack.getLocalTranslation(),1f),
                new Vector3f().interpolateLocal(electricWaves1.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
        });

        //2nd track
        electricWavesTrack2.setTimes(new float[]{2,4,8,16});
        electricWavesTrack2.setKeyframesTranslation(new Vector3f[]{
                new Vector3f().interpolateLocal(electricWaves2.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0),
                new Vector3f().interpolateLocal(electricWaves2.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
                new Vector3f().interpolateLocal(electricWaves2.getLocalTranslation(),dataBaseStack.getLocalTranslation(),1f),
                new Vector3f().interpolateLocal(electricWaves2.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
        });

        //3rd track
        electricWavesTrack3.setTimes(new float[]{2,4,8,16});
        electricWavesTrack3.setKeyframesTranslation(new Vector3f[]{
                new Vector3f().interpolateLocal(electricWaves3.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0),
                new Vector3f().interpolateLocal(electricWaves3.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
                new Vector3f().interpolateLocal(electricWaves3.getLocalTranslation(),dataBaseStack.getLocalTranslation(),1f),
                new Vector3f().interpolateLocal(electricWaves3.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
        });

        //4th track
        electricWavesTrack4.setTimes(new float[]{2,4,8,16});
        electricWavesTrack4.setKeyframesTranslation(new Vector3f[]{
                new Vector3f().interpolateLocal(electricWaves4.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0),
                new Vector3f().interpolateLocal(electricWaves4.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
                new Vector3f().interpolateLocal(electricWaves4.getLocalTranslation(),dataBaseStack.getLocalTranslation(),1f),
                new Vector3f().interpolateLocal(electricWaves4.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
        });


        shockThunderTrack.setTimes(new float[]{2,4,8,16});
        shockThunderTrack.setKeyframesScale(new Vector3f[]{
                new Vector3f().interpolateLocal(shockThunder.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0),
                new Vector3f().interpolateLocal(shockThunder.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
                new Vector3f().interpolateLocal(shockThunder.getLocalTranslation(),dataBaseStack.getLocalTranslation(),1f),
                new Vector3f().interpolateLocal(shockThunder.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
        });

        //the adds part
        waves1Clip.setTracks(new TransformTrack[]{electricWavesTrack1});
        waves2Clip.setTracks(new TransformTrack[]{electricWavesTrack2});
        waves3Clip.setTracks(new TransformTrack[]{electricWavesTrack3});
        waves4Clip.setTracks(new TransformTrack[]{electricWavesTrack4});
        thunderClip.setTracks(new AnimTrack[]{shockThunderTrack});

        animComposer.addAnimClip(waves1Clip);
        animComposer.addAnimClip(waves2Clip);
        animComposer.addAnimClip(waves3Clip);
        animComposer.addAnimClip(waves4Clip);
        animComposer.addAnimClip(thunderClip);
        dataBaseStack.getParent().addControl(animComposer);

        //the ClipAction part before attaching to a Tween thread
        ClipAction waves1ClipAction=new ClipAction(waves1Clip);
        ClipAction waves2ClipAction=new ClipAction(waves2Clip);
        ClipAction waves3ClipAction=new ClipAction(waves3Clip);
        ClipAction waves4ClipAction=new ClipAction(waves4Clip);
        ClipAction thunderClipAction=new ClipAction(thunderClip);

        //Bind the ParticleEmitter to the model through the tween , baseAction & the animComposer
        final TransformTrack modelTrack=new TransformTrack();
        final AnimClip modelAnimClip=new AnimClip(dataBaseStack.getName());

        modelTrack.setTarget(dataBaseStack);

        modelTrack.setTimes(new float[]{2,4,8,16,});
        /*dividing 2PI by 4 = PI/2 , so 4 Quaternion keyFrames with (theta+90) for each of them , where theta is the former angle*/
        modelTrack.setKeyframesRotation(circuitalRotation(dataBaseStack,Quaternion.IDENTITY,Vector3f.UNIT_Y,FastMath.TWO_PI,modelTrack.getTimes().length));
        // the adds part
        modelAnimClip.setTracks(new TransformTrack[]{modelTrack});
        animComposer.addAnimClip(modelAnimClip);
        //create a ClipAction for that AnimClip to be attached to the BaseAction & Handled by the tween thread
        ClipAction modelClipAction=new ClipAction(modelAnimClip);

        //Create a BaseAction that would hold a Tween thread to run these actions in parallel
        //bind electric sound to the tween of actions
        BaseAction shockMyScreen=new BaseAction(Tweens.parallel(waves1ClipAction,waves2ClipAction,waves3ClipAction,waves4ClipAction,
                Tweens.parallel(modelClipAction,thunderClipAction)));
        //the speed
        shockMyScreen.setSpeed(2f);
        //add the base actions to our idle actions stack
        animComposer.addAction("ShockMyScreen",shockMyScreen);

        //run shockMyScreen action
        animComposer.setCurrentAction("ShockMyScreen");
    }

    @Override
    protected void cleanup(Application app) {
        animComposer.setEnabled(false);
        if(shockThunder!=null && shockThunder.getStatus()== AudioSource.Status.Playing){
            getApplication().enqueue(()-> shockThunder.stop());
        }
    }

    @Override
    protected void onEnable() {
        animComposer.setEnabled(true);
        if(shockThunder!=null &&
                (shockThunder.getStatus()== AudioSource.Status.Paused || shockThunder.getStatus()== AudioSource.Status.Stopped)){
            getApplication().enqueue(()-> shockThunder.play());
        }
    }

    @Override
    protected void onDisable() {
        animComposer.setEnabled(false);
        if(shockThunder!=null && shockThunder.getStatus()== AudioSource.Status.Playing){
            getApplication().enqueue(()-> shockThunder.stop());
        }
    }
    private ParticleEmitter loadElectricWaves(int id){
            ParticleEmitter electricWaves = new ParticleEmitter("ElectricShocks "+id, ParticleMesh.Type.Triangle, 300);
            electricWaves.setParticlesPerSec(FastMath.pow(5,5));
            Material electrics = new Material(getApplication().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
            electrics.setTexture("Texture", getApplication().getAssetManager().loadTexture("AssetsForRenderer/Textures/Fire.png"));
            electrics.setTexture("GlowMap", getApplication().getAssetManager().loadTexture("AssetsForRenderer/Textures/Fire.png"));
            electrics.selectTechnique("Glow",getApplication().getRenderManager());
            electrics.setColor("GlowColor",ColorRGBA.Cyan);
            electricWaves.setMaterial(electrics);
            electricWaves.getParticleInfluencer().setInitialVelocity(Vector3f.ZERO.negate().mult(3.5f));
            electricWaves.getParticleInfluencer().setVelocityVariation(3f);
            electricWaves.setImagesX(1);
            electricWaves.setImagesY(1);
            electricWaves.setStartColor(ColorRGBA.White.mult(ColorRGBA.Cyan));
            electricWaves.setEndColor(ColorRGBA.Blue);
            electricWaves.setStartSize(0.05f);
            electricWaves.setEndSize(0.02f);
            electricWaves.setGravity(Vector3f.ZERO);
            electricWaves.setLowLife(0.5f);
            electricWaves.setHighLife(1f);
            electricWaves.center();
        return electricWaves;
    }
    private AudioNode loadElectricWaves(){
            AudioNode electricWaves=new AudioNode(getApplication().getAssetManager(),"AssetsForRenderer/Audio/shocks.wav",AudioData.DataType.Stream);
            electricWaves.stop();
            electricWaves.setPositional(false);
            electricWaves.setLooping(true);
        return electricWaves;
    }

    /**
     * rotates a #{@link HasLocalTransform} object with a startAngle , around rotation axes.
     * @param model
     * @param startAngle
     * @param rotationAxis
     * @param totalAngleOfRotation
     * @param numberOfKeyFrames
     * @return
     */
    private Quaternion[] circuitalRotation(HasLocalTransform model,Quaternion startAngle,Vector3f rotationAxis,float totalAngleOfRotation,int numberOfKeyFrames){
        Quaternion[] rotations=new Quaternion[numberOfKeyFrames];
        rotations[0] = startAngle;
        float anglePerKeyFrame = totalAngleOfRotation / numberOfKeyFrames ;
        for(int pos=1; pos<rotations.length; pos++){
            rotations[pos] = model.getLocalTransform().getRotation().fromAngleAxis(anglePerKeyFrame,rotationAxis);
        }
        return rotations;
    }
}
