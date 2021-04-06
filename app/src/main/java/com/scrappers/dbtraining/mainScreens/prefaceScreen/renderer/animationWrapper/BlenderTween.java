package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper;

import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.SkinningControl;
import com.jme3.anim.tween.Tweens;
import com.jme3.anim.tween.action.BaseAction;
import com.jme3.anim.tween.action.ClipAction;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * <b>A Simple Class to demonstrate how to load an AnimComposer#{@link AnimComposer} from a blender model using #{@link Spatial#getControl(Class)} </b>
 *
 * @author pavl_g.
 */
public class BlenderTween extends BaseAppState {
    private final Spatial dataBaseStack;
    private AnimComposer animComposer;
    public BlenderTween(final String id, final Spatial dataBaseStack){
        super(id);
        this.dataBaseStack=dataBaseStack;
    }
    @Override
    protected void initialize(Application app) {
        animComposer=((Node)dataBaseStack).getChild("Cylinder.001").getControl(AnimComposer.class);
        setEnabled(false);
        final AnimClip animClip=animComposer.getAnimClip("Cylinder.001Action");
        final ClipAction clipAction=new ClipAction(animClip);
        final BaseAction baseAction=new BaseAction(Tweens.sineStep(clipAction,clipAction));
        animComposer.addAction("Composite Animation",baseAction);
        animComposer.setCurrentAction("Composite Animation");
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        if ( animComposer != null ){
            animComposer.setEnabled(true);
        }
    }

    @Override
    protected void onDisable() {
        if ( animComposer != null ){
            animComposer.setEnabled(false);
        }
    }
}
