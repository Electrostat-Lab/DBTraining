package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jme3.app.Application;
import com.jme3.app.jmeSurfaceView.JmeSurfaceView;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Spatial;
import com.scrappers.dbtraining.R;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.AnimLayers;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.BasicArmature;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.BasicTween;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.BlendableAnimation;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.BlenderTween;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.EmitterTween;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.SimpleScaleTrack;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.StackLoops;
import com.scrappers.superiorExtendedEngine.menuStates.UiStateManager;
import com.scrappers.superiorExtendedEngine.menuStates.UiStatesLooper;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * @author pavl_g
 */
public class AnimationFactory extends BaseAppState implements View.OnClickListener {
    private final Spatial dataBaseStack;
    private final JmeSurfaceView jmeSurfaceView;
    private RelativeLayout menu;
    private UiStateManager uiStateManager;
    private static final float REANIMATE_TIME=2f;
    private float timer=0.0f;
    public AnimationFactory(Spatial dataBaseStack, JmeSurfaceView jmeSurfaceView) {
        this.dataBaseStack=dataBaseStack;
        this.jmeSurfaceView=jmeSurfaceView;
    }
    @Override
    protected void initialize(Application app) {
        ((AppCompatActivity)jmeSurfaceView.getContext()).runOnUiThread(()->{
            uiStateManager=new UiStateManager(((RelativeLayout)jmeSurfaceView.getParent()));
            ((RelativeLayout)jmeSurfaceView.getParent()).findViewById(R.id.animationSettings).setOnClickListener(this);
            ((RelativeLayout)jmeSurfaceView.getParent()).findViewById(R.id.reset).setOnClickListener(this);

                menu = (RelativeLayout) uiStateManager.attachUiState(uiStateManager.fromXML(R.layout.animation_selection_menu));
                menu.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                menu.setTranslationY(1200);

                menu.findViewById(R.id.closeMenu).setOnClickListener(this);
                menu.findViewById(R.id.simpleTrack).setOnClickListener(this);
                menu.findViewById(R.id.stackLoops).setOnClickListener(this);
                menu.findViewById(R.id.armatureTwist).setOnClickListener(this);
                menu.findViewById(R.id.basicTween).setOnClickListener(this);
                menu.findViewById(R.id.particleEmitter).setOnClickListener(this);
                menu.findViewById(R.id.bottleFall).setOnClickListener(this);
                menu.findViewById(R.id.blenderAnimations).setOnClickListener(this);
                menu.findViewById(R.id.armatureMask).setOnClickListener(this);

        });
        getStateManager().attach(new SimpleScaleTrack("SimpleScaleTrack",dataBaseStack));
        getStateManager().attach(new StackLoops("StackLoopsTrack",dataBaseStack));
        getStateManager().attach(new BasicArmature("BasicArmatureTrack",dataBaseStack));
        getStateManager().attach(new BasicTween("BasicTweenTrack",dataBaseStack));
        getStateManager().attach(new EmitterTween("EmitterTween",dataBaseStack));
        getStateManager().attach(new BlendableAnimation("SimulateBottleFall",dataBaseStack));
        getStateManager().attach(new BlenderTween("BlenderImport",dataBaseStack));
        getStateManager().attach(new AnimLayers("MultipleAnimLayers",dataBaseStack));
    }

    @Override
    protected void cleanup(Application app) {
        ((Activity)menu.getContext()).runOnUiThread(()->{
            menu.animate().translationY(1200).setDuration(500).start();
            ((RelativeLayout)jmeSurfaceView.getParent()).findViewById(R.id.animationSettings).animate().setDuration(600).rotation(-45).start();
            uiStateManager.forEachUiState((UiStatesLooper.Modifiable.Looper) (currentView, position) -> {
                deActivateButton(currentView);
                uiStateManager.detachUiState(currentView);
                if(position == uiStateManager.getLastStateIndex()){
                    uiStateManager.detachUiManager();
                }
            });
        });
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    @Override
    public void update(float tpf) {
        timer+=tpf;
        if(timer>REANIMATE_TIME){
            timer=0;
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.animationSettings){
            ((RelativeLayout)jmeSurfaceView.getParent()).findViewById(R.id.animationSettings).animate().setDuration(300).rotation(45).start();
            menu.animate().translationY(-menu.getLayoutParams().height).setDuration(500).start();
        }
        if(v.getId()==R.id.reset){
            getStateManager().detach(getStateManager().getState(BasicArmature.class));
            getStateManager().detach(getStateManager().getState(BasicTween.class));
            getStateManager().detach(getStateManager().getState(BlendableAnimation.class));
            getStateManager().detach(getStateManager().getState(BlenderTween.class));
            getStateManager().detach(getStateManager().getState(EmitterTween.class));
            getStateManager().detach(getStateManager().getState(SimpleScaleTrack.class));
            getStateManager().detach(getStateManager().getState(StackLoops.class));
            getStateManager().detach(getStateManager().getState(Environment.class));
            getStateManager().detach(getStateManager().getState(AnimationFactory.class));
            getStateManager().detach(getStateManager().getState(Scene.class));
            getStateManager().attach(new Scene(dataBaseStack.getParent(),jmeSurfaceView));
            if(!dataBaseStack.getParent().hasChild(dataBaseStack)){
                dataBaseStack.getParent().detachChild(dataBaseStack);
            }
            Toast.makeText(jmeSurfaceView.getContext(),"Resettled the Scene !",Toast.LENGTH_LONG).show();
        }else if(v.getId()==R.id.closeMenu){
            menu.animate().translationY(1200).setDuration(500).start();
            ((RelativeLayout)jmeSurfaceView.getParent()).findViewById(R.id.animationSettings).animate().setDuration(600).rotation(-45).start();
        }else if(v.getId()==R.id.simpleTrack){
            //getStateManager().detach(getStateManager().getState("SimpleAnimation",SimpleScaleTrack.class)); -> not working ??!
            if(!getStateManager().getState(SimpleScaleTrack.class).isEnabled()){
                getStateManager().getState(SimpleScaleTrack.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(SimpleScaleTrack.class).setEnabled(false);
                deActivateButton(v);
            }
        }else if(v.getId()==R.id.stackLoops){
            if(!getStateManager().getState(StackLoops.class).isEnabled()){
                getStateManager().getState(StackLoops.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(StackLoops.class).setEnabled(false);
                deActivateButton(v);
            }
        }else if(v.getId()==R.id.armatureTwist){
            if(!getStateManager().getState(BasicArmature.class).isEnabled()){
                getStateManager().getState(BasicArmature.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(BasicArmature.class).setEnabled(false);
                deActivateButton(v);
            }
        }else if(v.getId()==R.id.basicTween){
            if(!getStateManager().getState(BasicTween.class).isEnabled()){
                getStateManager().getState(BasicTween.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(BasicTween.class).setEnabled(false);
                deActivateButton(v);
            }
        }else if(v.getId()==R.id.particleEmitter){
            if(!getStateManager().getState(EmitterTween.class).isEnabled()){
                getStateManager().getState(EmitterTween.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(EmitterTween.class).setEnabled(false);
                deActivateButton(v);
            }
        }else if(v.getId()==R.id.bottleFall){
            if(!getStateManager().getState(BlendableAnimation.class).isEnabled()){
                getStateManager().getState(BlendableAnimation.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(BlendableAnimation.class).setEnabled(false);
                deActivateButton(v);
            }
        }else if(v.getId()==R.id.blenderAnimations){
            if(!getStateManager().getState(BlenderTween.class).isEnabled()){
                getStateManager().getState(BlenderTween.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(BlenderTween.class).setEnabled(false);
                deActivateButton(v);
            }
        }else if(v.getId()==R.id.armatureMask){
            if(!getStateManager().getState(AnimLayers.class).isEnabled()){
                getStateManager().getState(AnimLayers.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(AnimLayers.class).setEnabled(false);
                deActivateButton(v);
            }
        }
    }
    /**
     * Changes the UI settings of the activated animation
     * @param v the android view to change it's UI.
     */
    protected void activateButton(View v){
        ((GradientDrawable) Objects.requireNonNull(
                ContextCompat.getDrawable(v.getContext(), R.drawable.toolbar_background))).setColors(new int[]{
                ContextCompat.getColor(v.getContext(),R.color.greenGold),ContextCompat.getColor(v.getContext(),
                R.color.greenGold),ContextCompat.getColor(v.getContext(),R.color.greenGold)});
        v.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.toolbar_background));
        v.invalidate();
        Toast.makeText(jmeSurfaceView.getContext(),"Animation Started",Toast.LENGTH_LONG).show();
    }
    /**
     * Changes the UI settings of the deactivated animation
     * @param v the android view to change it's UI.
     */
    protected void deActivateButton(View v){
        ((GradientDrawable) Objects.requireNonNull(
                ContextCompat.getDrawable(v.getContext(), R.drawable.toolbar_background))).setColors(new int[]{
                ContextCompat.getColor(v.getContext(),R.color.lightBlack),ContextCompat.getColor(v.getContext(),
                R.color.lightBlack),ContextCompat.getColor(v.getContext(),R.color.lightBlack)});
        v.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.toolbar_background));
        v.invalidate();
        Toast.makeText(jmeSurfaceView.getContext(),"Animation Stopped",Toast.LENGTH_LONG).show();
    }
}
