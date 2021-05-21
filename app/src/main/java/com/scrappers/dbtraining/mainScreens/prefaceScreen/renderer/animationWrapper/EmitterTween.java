package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper;

import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.AnimTrack;
import com.jme3.anim.ArmatureMask;
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
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.builders.AnimEventEntity;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.builders.LayerBuilder;

/**
 *
 * @author pavl_g.
 */
public class EmitterTween extends AnimEventEntity {
    private final Spatial dataBaseStack;
    private AnimComposer animComposer;
    private AudioNode shockThunder;
    private BaseAction shockMyScreen;
    public ParticleEmitter electricWaves1;
    public ParticleEmitter electricWaves2;
    public ParticleEmitter electricWaves3;
    public ParticleEmitter electricWaves4;
    public ParticleEmitter electricWaves5;
    public ParticleEmitter electricWaves6;
    public ParticleEmitter electricWaves7;
    public ParticleEmitter electricWaves8;
    public ParticleEmitter electricWaves9;
    public ParticleEmitter electricWaves10;
    public EmitterTween(final String id, final Spatial dataBaseStack){
        super(id);
        this.dataBaseStack=dataBaseStack;
    }
    @Override
    protected void initialize(Application app) {
        setEnabled(false);
        animComposer = dataBaseStack.getControl(AnimComposer.class);
        setAnimComposer(animComposer);
        setDelayStart(0.55f);
        setDelayEnd(0f);
        shockThunder = loadElectricWaves();
        //initialize the electricWaves particle Emitters instances w/ their properties.
        electricWaves1 = loadElectricWaves(0);
        electricWaves2 = loadElectricWaves(1);
        electricWaves3 = loadElectricWaves(2);
        electricWaves4 = loadElectricWaves(3);
        electricWaves5 = loadElectricWaves(4);
        electricWaves6 = loadElectricWaves(5);
        electricWaves7 = loadElectricWaves(6);
        electricWaves8 = loadElectricWaves(7);
        electricWaves9 = loadElectricWaves(8);
        electricWaves10 = loadElectricWaves(9);

        //set the local translation of them.
        electricWaves1.setLocalTranslation(dataBaseStack.getLocalTranslation().getX()-1f
                ,dataBaseStack.getLocalTranslation().getY()+1f,dataBaseStack.getLocalTranslation().getZ());

        electricWaves2.setLocalTranslation(dataBaseStack.getLocalTranslation().getX()-1f
                ,dataBaseStack.getLocalTranslation().getY()-1f,dataBaseStack.getLocalTranslation().getZ());

        electricWaves3.setLocalTranslation(dataBaseStack.getLocalTranslation().getX()+1f
                ,dataBaseStack.getLocalTranslation().getY()+1f,dataBaseStack.getLocalTranslation().getZ());

        electricWaves4.setLocalTranslation(dataBaseStack.getLocalTranslation().getX()+1f
                ,dataBaseStack.getLocalTranslation().getY()-1f,dataBaseStack.getLocalTranslation().getZ());

        electricWaves5.setLocalTranslation(dataBaseStack.getLocalTranslation().getX(),
                dataBaseStack.getLocalTranslation().getY()+1f,dataBaseStack.getLocalTranslation().getZ());

        electricWaves6.setLocalTranslation(dataBaseStack.getLocalTranslation().getX(),
                dataBaseStack.getLocalTranslation().getY()-1f,dataBaseStack.getLocalTranslation().getZ());

        electricWaves7.setLocalTranslation(dataBaseStack.getLocalTranslation().getX(),
                dataBaseStack.getLocalTranslation().getY(),dataBaseStack.getLocalTranslation().getZ()+1f);

        electricWaves8.setLocalTranslation(dataBaseStack.getLocalTranslation().getX(),
                dataBaseStack.getLocalTranslation().getY(),dataBaseStack.getLocalTranslation().getZ()-1f);

        electricWaves9.setLocalTranslation(dataBaseStack.getLocalTranslation().getX()+1f,
                dataBaseStack.getLocalTranslation().getY(),dataBaseStack.getLocalTranslation().getZ());

        electricWaves10.setLocalTranslation(dataBaseStack.getLocalTranslation().getX()-1f,
                dataBaseStack.getLocalTranslation().getY(),dataBaseStack.getLocalTranslation().getZ());

        shockThunder.setLocalTranslation(dataBaseStack.getLocalTranslation().mult(2));

        if(!dataBaseStack.getParent().hasChild(electricWaves1)){
            //add them to the scene.
            dataBaseStack.getParent().attachChild(electricWaves1);
            dataBaseStack.getParent().attachChild(electricWaves2);
            dataBaseStack.getParent().attachChild(electricWaves3);
            dataBaseStack.getParent().attachChild(electricWaves4);
            dataBaseStack.getParent().attachChild(electricWaves5);
            dataBaseStack.getParent().attachChild(electricWaves6);
            dataBaseStack.getParent().attachChild(electricWaves7);
            dataBaseStack.getParent().attachChild(electricWaves8);
            dataBaseStack.getParent().attachChild(electricWaves9);
            dataBaseStack.getParent().attachChild(electricWaves10);
            dataBaseStack.getParent().attachChild(shockThunder);
        }

        //create 4 TransformTracks that would run in parallel
        final TransformTrack electricWavesTrack1=new TransformTrack();
        final TransformTrack electricWavesTrack2=new TransformTrack();
        final TransformTrack electricWavesTrack3=new TransformTrack();
        final TransformTrack electricWavesTrack4=new TransformTrack();
        final TransformTrack electricWavesTrack5=new TransformTrack();
        final TransformTrack electricWavesTrack6=new TransformTrack();
        final TransformTrack electricWavesTrack7=new TransformTrack();
        final TransformTrack electricWavesTrack8=new TransformTrack();
        final TransformTrack electricWavesTrack9=new TransformTrack();
        final TransformTrack electricWavesTrack10=new TransformTrack();
        final TransformTrack shockThunderTrack=new TransformTrack();
        //create 3 AnimClips , each of which would hold one track
        final AnimClip waves1Clip=new AnimClip(electricWaves1.getName());
        final AnimClip waves2Clip=new AnimClip(electricWaves2.getName());
        final AnimClip waves3Clip=new AnimClip(electricWaves3.getName());
        final AnimClip waves4Clip=new AnimClip(electricWaves4.getName());
        final AnimClip waves5Clip=new AnimClip(electricWaves5.getName());
        final AnimClip waves6Clip=new AnimClip(electricWaves6.getName());
        final AnimClip waves7Clip=new AnimClip(electricWaves7.getName());
        final AnimClip waves8Clip=new AnimClip(electricWaves8.getName());
        final AnimClip waves9Clip=new AnimClip(electricWaves9.getName());
        final AnimClip waves10Clip=new AnimClip(electricWaves10.getName());
        final AnimClip thunderClip=new AnimClip(shockThunder.getName());

        //set the Targets
        electricWavesTrack1.setTarget(electricWaves1);
        electricWavesTrack2.setTarget(electricWaves2);
        electricWavesTrack3.setTarget(electricWaves3);
        electricWavesTrack4.setTarget(electricWaves4);
        electricWavesTrack5.setTarget(electricWaves5);
        electricWavesTrack6.setTarget(electricWaves6);
        electricWavesTrack7.setTarget(electricWaves7);
        electricWavesTrack8.setTarget(electricWaves8);
        electricWavesTrack9.setTarget(electricWaves9);
        electricWavesTrack10.setTarget(electricWaves10);
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

        //5th track
        electricWavesTrack5.setTimes(new float[]{2,4,8,16});
        electricWavesTrack5.setKeyframesTranslation(new Vector3f[]{
                new Vector3f().interpolateLocal(electricWaves5.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0),
                new Vector3f().interpolateLocal(electricWaves5.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
                new Vector3f().interpolateLocal(electricWaves5.getLocalTranslation(),dataBaseStack.getLocalTranslation(),1f),
                new Vector3f().interpolateLocal(electricWaves5.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
        });

        //6th track
        electricWavesTrack6.setTimes(new float[]{2,4,8,16});
        electricWavesTrack6.setKeyframesTranslation(new Vector3f[]{
                new Vector3f().interpolateLocal(electricWaves6.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0),
                new Vector3f().interpolateLocal(electricWaves6.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
                new Vector3f().interpolateLocal(electricWaves6.getLocalTranslation(),dataBaseStack.getLocalTranslation(),1f),
                new Vector3f().interpolateLocal(electricWaves6.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
        });

        //7th track
        electricWavesTrack7.setTimes(new float[]{2,4,8,16});
        electricWavesTrack7.setKeyframesTranslation(new Vector3f[]{
                new Vector3f().interpolateLocal(electricWaves7.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0),
                new Vector3f().interpolateLocal(electricWaves7.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
                new Vector3f().interpolateLocal(electricWaves7.getLocalTranslation(),dataBaseStack.getLocalTranslation(),1f),
                new Vector3f().interpolateLocal(electricWaves7.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
        });
        //8th track
        electricWavesTrack8.setTimes(new float[]{2,4,8,16});
        electricWavesTrack8.setKeyframesTranslation(new Vector3f[]{
                new Vector3f().interpolateLocal(electricWaves8.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0),
                new Vector3f().interpolateLocal(electricWaves8.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
                new Vector3f().interpolateLocal(electricWaves8.getLocalTranslation(),dataBaseStack.getLocalTranslation(),1f),
                new Vector3f().interpolateLocal(electricWaves8.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
        });

        //9th track
        electricWavesTrack9.setTimes(new float[]{2,4,8,16});
        electricWavesTrack9.setKeyframesTranslation(new Vector3f[]{
                new Vector3f().interpolateLocal(electricWaves9.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0),
                new Vector3f().interpolateLocal(electricWaves9.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
                new Vector3f().interpolateLocal(electricWaves9.getLocalTranslation(),dataBaseStack.getLocalTranslation(),1f),
                new Vector3f().interpolateLocal(electricWaves9.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
        });

        //10th track
        electricWavesTrack10.setTimes(new float[]{2,4,8,16});
        electricWavesTrack10.setKeyframesTranslation(new Vector3f[]{
                new Vector3f().interpolateLocal(electricWaves10.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0),
                new Vector3f().interpolateLocal(electricWaves10.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
                new Vector3f().interpolateLocal(electricWaves10.getLocalTranslation(),dataBaseStack.getLocalTranslation(),1f),
                new Vector3f().interpolateLocal(electricWaves10.getLocalTranslation(),dataBaseStack.getLocalTranslation(),0.5f),
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
        waves5Clip.setTracks(new TransformTrack[]{electricWavesTrack5});
        waves6Clip.setTracks(new TransformTrack[]{electricWavesTrack6});
        waves7Clip.setTracks(new TransformTrack[]{electricWavesTrack7});
        waves8Clip.setTracks(new TransformTrack[]{electricWavesTrack8});
        waves9Clip.setTracks(new TransformTrack[]{electricWavesTrack9});
        waves10Clip.setTracks(new TransformTrack[]{electricWavesTrack10});
        thunderClip.setTracks(new AnimTrack[]{shockThunderTrack});

        animComposer.addAnimClip(waves1Clip);
        animComposer.addAnimClip(waves2Clip);
        animComposer.addAnimClip(waves3Clip);
        animComposer.addAnimClip(waves4Clip);
        animComposer.addAnimClip(waves5Clip);
        animComposer.addAnimClip(waves6Clip);
        animComposer.addAnimClip(waves7Clip);
        animComposer.addAnimClip(waves8Clip);
        animComposer.addAnimClip(waves9Clip);
        animComposer.addAnimClip(waves10Clip);
        animComposer.addAnimClip(thunderClip);

        //the ClipAction part before attaching to a Tween thread
        ClipAction waves1ClipAction=new ClipAction(waves1Clip);
        ClipAction waves2ClipAction=new ClipAction(waves2Clip);
        ClipAction waves3ClipAction=new ClipAction(waves3Clip);
        ClipAction waves4ClipAction=new ClipAction(waves4Clip);
        ClipAction waves5ClipAction=new ClipAction(waves5Clip);
        ClipAction waves6ClipAction=new ClipAction(waves6Clip);
        ClipAction waves7ClipAction=new ClipAction(waves7Clip);
        ClipAction waves8ClipAction=new ClipAction(waves8Clip);
        ClipAction waves9ClipAction=new ClipAction(waves9Clip);
        ClipAction waves10ClipAction=new ClipAction(waves10Clip);
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
        shockMyScreen=new BaseAction(Tweens.parallel(
                waves1ClipAction,waves2ClipAction,
                waves3ClipAction,waves4ClipAction,
                waves5ClipAction,waves6ClipAction,
                waves7ClipAction,waves8ClipAction,
                waves9ClipAction,waves10ClipAction,
                Tweens.parallel(modelClipAction,thunderClipAction)
        ));
        //the speed
        shockMyScreen.setLength(20f);
        shockMyScreen.setSpeed(10f);
        //add the base actions to our idle actions stack
        animComposer.addAction("ShockMyScreen", shockMyScreen);
        //make a new Animation Layer that would hold a one current running action.....
        animComposer.makeLayer(LayerBuilder.LAYER_EMITTER_TWEEN, new ArmatureMask());

        setAction(shockMyScreen);

    }

    @Override
    protected void cleanup(Application app) {
        if(animComposer != null){
            animComposer.setEnabled(false);
        }
        if(shockThunder!=null && shockThunder.getStatus()== AudioSource.Status.Playing){
            getApplication().enqueue(()-> shockThunder.stop());
            for(int pos=0; pos<10; pos++) {
                dataBaseStack.getParent().
                        detachChild(dataBaseStack.getParent().getChild("ElectricShocks " + pos));
            }
        }
    }

    @Override
    protected void onEnable() {
        if(animComposer != null){
            animComposer.setEnabled(true);
            //run shockMyScreen action
            animComposer.setCurrentAction("ShockMyScreen", LayerBuilder.LAYER_EMITTER_TWEEN);

        }
        if(shockThunder!=null &&
                (shockThunder.getStatus()== AudioSource.Status.Paused || shockThunder.getStatus()== AudioSource.Status.Stopped)){
            getApplication().enqueue(()-> shockThunder.play());
        }
    }

    @Override
    protected void onDisable() {
        if(animComposer != null){
            /*removing the current Action from this playing layer, NB this doesn't remove the Action from the whole animComposer,
             so no need to add the action with each enable state*/
            animComposer.removeCurrentAction(LayerBuilder.LAYER_EMITTER_TWEEN);
        }
        if(shockThunder!=null && shockThunder.getStatus()== AudioSource.Status.Playing){
            getApplication().enqueue(()-> shockThunder.stop());
        }
    }
    private ParticleEmitter loadElectricWaves(int id){
            ParticleEmitter electricWaves = new ParticleEmitter("ElectricShocks "+id, ParticleMesh.Type.Triangle, 500);
            electricWaves.setParticlesPerSec(FastMath.pow(5,3));
            Material electrics = new Material(getApplication().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
            electrics.setBoolean("PointSprite",true);
            electrics.setTexture("Texture", getApplication().getAssetManager().loadTexture("AssetsForRenderer/Textures/Fire.png"));
            electrics.setTexture("GlowMap", getApplication().getAssetManager().loadTexture("AssetsForRenderer/Textures/Fire.png"));
            electrics.selectTechnique("Glow",getApplication().getRenderManager());
            electrics.setColor("GlowColor",ColorRGBA.Cyan.mult(50f));
            electricWaves.setMaterial(electrics);
            electricWaves.getParticleInfluencer().setInitialVelocity(Vector3f.ZERO.negate().mult(5.5f));
            electricWaves.setImagesX(1);
            electricWaves.setImagesY(1);
            electricWaves.setStartColor(ColorRGBA.White.mult(ColorRGBA.Blue));
            electricWaves.setEndColor(ColorRGBA.Blue);
            electricWaves.setStartSize(0.05f);
            electricWaves.setEndSize(0.025f);
            electricWaves.setGravity(electricWaves.getParticleInfluencer().getInitialVelocity().mult(20f));
            electricWaves.setLowLife(0.2f);
            electricWaves.setHighLife(1f);
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


    @Override
    public void update(float tpf) {
        super.update(tpf);
    }
}
