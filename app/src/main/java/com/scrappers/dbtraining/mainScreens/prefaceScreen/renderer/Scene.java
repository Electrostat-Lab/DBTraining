package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer;

import com.jme3.app.Application;
import com.jme3.app.jmeSurfaceView.JmeSurfaceView;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.ChaseCamera;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

public class Scene extends BaseAppState {
    private final Node rootNode;
    private Spatial dataBaseStack;
    private final JmeSurfaceView jmeSurfaceView;
    private final float ENV_TIME=2.5f;
    private float timer=0.0f;
    public Scene(Node rootNode, JmeSurfaceView jmeSurfaceView){
        this.rootNode=rootNode;
        this.jmeSurfaceView=jmeSurfaceView;
    }
    @Override
    protected void initialize(Application app) {
        dataBaseStack = app.getAssetManager().loadModel("AssetsForRenderer/Models/DatabaseAnimCombinedArmature.gltf");
        dataBaseStack.setLocalScale(0.6f);
        dataBaseStack.setName("DataBaseStackModel");

        Material material = new Material(app.getAssetManager(),"Common/MatDefs/Light/PBRLighting.j3md");
        /*metalness , max is 1*/
        material.setFloat("Metallic", 0.5f);
        /*Roughness , 1 is the max roughnesss*/
        material.setFloat("Roughness", 0.5f);
        material.setFloat("EmissivePower",1.0f);
        material.setFloat("EmissiveIntensity",2.0f);
        material.setBoolean("HorizonFade",true);
        material.setVector3("LightDir",new Vector3f(-0.5f,-0.5f,-0.5f).normalize());
        material.setBoolean("BackfaceShadows",true);
        /*Reflection color*/
        material.setColor("Specular", ColorRGBA.Cyan.mult(3.5f));
        Texture texture=app.getAssetManager().loadTexture("AssetsForRenderer/Textures/dataBaseTexture.jpg");
        material.setTexture("BaseColorMap",texture);
        material.setReceivesShadows(true);
        dataBaseStack.setMaterial(material);

        app.getCamera().setFrustumNear(0.7f);
        ChaseCamera chaseCamera = new ChaseCamera(app.getCamera(), dataBaseStack, app.getInputManager());
        chaseCamera.setDragToRotate(true);
        chaseCamera.setSmoothMotion(true);
        chaseCamera.setDefaultDistance(-30f);
        chaseCamera.setMaxDistance(-10f);
        chaseCamera.setMinDistance(-5f);
        chaseCamera.setDefaultVerticalRotation(-FastMath.QUARTER_PI/2);
        chaseCamera.setDefaultHorizontalRotation(-FastMath.HALF_PI);
        chaseCamera.setHideCursorOnRotate(true);
        rootNode.attachChild(dataBaseStack);


    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    @Override
    public void update(float tpf) {
        //synchronized component addition to minimize scene tear
        synchronized(getApplication().getStateManager()) {
            timer+=tpf;
            if ( timer >= ENV_TIME ){
                if(!getStateManager().hasState(getStateManager().getState(Environment.class))){
                    getApplication().getStateManager().attach(new Environment(dataBaseStack));
                }
                if(!getStateManager().hasState(getStateManager().getState(AnimationFactory.class))){
                    getApplication().getStateManager().attach(new AnimationFactory(dataBaseStack, jmeSurfaceView));
                }
                getApplication().getStateManager().detach(this);
            }
        }
    }
}
