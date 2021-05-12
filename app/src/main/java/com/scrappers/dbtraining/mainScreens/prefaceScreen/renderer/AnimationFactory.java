package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.TransformTrack;
import com.jme3.app.Application;
import com.jme3.app.jmeSurfaceView.JmeSurfaceView;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
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
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.builders.AnimEventEntity;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.triggersUtils.TriggerID;
import com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.triggersUtils.TriggerModel;
import com.scrappers.superiorExtendedEngine.menuStates.UiStateManager;
import com.scrappers.superiorExtendedEngine.menuStates.UiStatesLooper;
import com.scrappers.superiorExtendedEngine.menuStates.uiPager.UiPager;
import java.util.Objects;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_LONG;
import static com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.Scene.defaultOne;
import static com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.Scene.defaultStack;
import static com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.Scene.defaultThree;
import static com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.Scene.defaultTwo;

/**
 * @author pavl_g
 */
public class AnimationFactory extends BaseAppState implements View.OnClickListener {
    private final Spatial dataBaseStack;
    private final JmeSurfaceView jmeSurfaceView;
    private final DisplayMetrics displayMetrics = new DisplayMetrics();
    private RelativeLayout menu;
    private UiStateManager uiStateManager;
    private static final float REANIMATE_TIME=2f;
    private float timer=0.0f;
    private static final char SCROLLABLE_CONTENT = 'S' + 'C' + 'R' + 'O' + 'L' + 'L';
    private static boolean gridOn = false;
    public AnimationFactory(Spatial dataBaseStack, JmeSurfaceView jmeSurfaceView) {
        this.dataBaseStack=dataBaseStack;
        this.jmeSurfaceView=jmeSurfaceView;
    }
    @Override
    protected void initialize(Application app) {
        ((AppCompatActivity)jmeSurfaceView.getContext()).runOnUiThread(()->{
            //set the display metrics of the current device, fetched from the mainActivity Holder context
            ((AppCompatActivity)jmeSurfaceView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            ((RelativeLayout)jmeSurfaceView.getParent()).findViewById(R.id.animationSettings).setOnClickListener(this);
            ((RelativeLayout)jmeSurfaceView.getParent()).findViewById(R.id.gridSwitcher).setOnClickListener(this);
            ((RelativeLayout)jmeSurfaceView.getParent()).findViewById(R.id.reset).setOnClickListener(this);
            //initialize the UiStateManager instance
            uiStateManager = new UiStateManager(((RelativeLayout)jmeSurfaceView.getParent()));

            menu = (RelativeLayout) uiStateManager.attachUiState(uiStateManager.fromXML(R.layout.animation_selection_menu));
            menu.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1200));
            menu.animate().translationY(displayMetrics.heightPixels + menu.getLayoutParams().height).setDuration(600).start();
            menu.setVisibility(INVISIBLE);
            menu.setHorizontalScrollBarEnabled(true);
            //uiPager starts here......
            UiPager uiPager = new UiPager(menu.getContext());
            uiPager.setLayoutTransition(new LayoutTransition());
            uiPager.setLayoutParams(menu.getLayoutParams());
            ScrollView scrollableContainer = uiPager.initializeScrollableContainer();
            scrollableContainer.setHorizontalScrollBarEnabled(true);
            scrollableContainer.setId(SCROLLABLE_CONTENT);
            menu.addView(scrollableContainer);
            //add-remove-update-integrate-disintegrate views
            invalidateUiPager(gridOn);
        });
        SimpleScaleTrack simpleScaleTrack = new SimpleScaleTrack("SimpleScaleTrack", dataBaseStack);
        getStateManager().attach(simpleScaleTrack);
        getStateManager().attach(new BasicArmature("BasicArmatureTrack", dataBaseStack));
        getStateManager().attach(new StackLoops("StackLoopsTrack", dataBaseStack));
        getStateManager().attach(new BasicTween("BasicTweenTrack", dataBaseStack));
        EmitterTween emitterTween = new EmitterTween("EmitterTween", dataBaseStack);
        emitterTween.setAnimationEvents(new AnimEventEntity.AnimationEvents() {
            @Override
            public void onAnimationStart(AnimComposer animComposer, TransformTrack transformationStart) {

            }

            @Override
            public void onAnimationEnd(AnimComposer animComposer, TransformTrack transformEnd) {
                jmeSurfaceView.getLegacyApplication().enqueue(()->{
//                    emitterTween.electricWaves1.getMaterial().setColor("GlowColor",ColorRGBA.randomColor().mult(ColorRGBA.Blue));
//                    emitterTween.electricWaves2.getMaterial().setColor("GlowColor",ColorRGBA.randomColor().mult(ColorRGBA.Blue));
//                    emitterTween.electricWaves3.getMaterial().setColor("GlowColor",ColorRGBA.randomColor().mult(ColorRGBA.Blue));
//                    emitterTween.electricWaves4.getMaterial().setColor("GlowColor",ColorRGBA.randomColor().mult(ColorRGBA.Blue));
//                    emitterTween.electricWaves5.getMaterial().setColor("GlowColor",ColorRGBA.randomColor().mult(ColorRGBA.Blue));
//                    emitterTween.electricWaves6.getMaterial().setColor("GlowColor",ColorRGBA.randomColor().mult(ColorRGBA.Blue));
//                    emitterTween.electricWaves7.getMaterial().setColor("GlowColor",ColorRGBA.randomColor().mult(ColorRGBA.Blue));
//                    emitterTween.electricWaves8.getMaterial().setColor("GlowColor",ColorRGBA.randomColor().mult(ColorRGBA.Blue));
//                    emitterTween.electricWaves9.getMaterial().setColor("GlowColor",ColorRGBA.randomColor().mult(ColorRGBA.Blue));
//                    emitterTween.electricWaves10.getMaterial().setColor("GlowColor",ColorRGBA.randomColor().mult(ColorRGBA.Blue));
                });
            }

            @Override
            public void onAnimationShuffle(AnimComposer animComposer, TransformTrack transformTrack, int i, int keyFrameIndex) {

            }
        });
        getStateManager().attach(emitterTween);
        getStateManager().attach(new BlendableAnimation("SimulateBottleFall", dataBaseStack));
        getStateManager().attach(new BlenderTween("BlenderImport", dataBaseStack));
        getStateManager().attach(new AnimLayers("MultipleAnimLayers", dataBaseStack));
    }

    @Override
    protected void cleanup(Application app) {
        ((Activity)menu.getContext()).runOnUiThread(()->{
            menu.animate().translationY(displayMetrics.heightPixels + menu.getLayoutParams().height).setDuration(600).start();
            menu.setVisibility(INVISIBLE);
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

            ((RelativeLayout)jmeSurfaceView.getParent()).findViewById(R.id.animationSettings).animate().setDuration(100).rotation(45).start();
            menu.setVisibility(VISIBLE);
            menu.animate().translationY(displayMetrics.heightPixels - menu.getLayoutParams().height).setDuration(600).start();

        }
        if(v.getId() == R.id.reset){
            /*reset button*/
            jmeSurfaceView.getLegacyApplication().enqueue(this::disableAllStates);
            UiPager uiPager = (UiPager) ((ScrollView)uiStateManager.getChildUiStateById(SCROLLABLE_CONTENT)).getChildAt(0);
            uiPager.forEachUiState((UiStatesLooper.Modifiable.Looper) (currentView, position) -> {
                if(((ViewGroup) currentView).getChildAt(0).getId() != TriggerID.closeTrigger){
                    deActivateButton(((ViewGroup) currentView).getChildAt(0));
                }
            });
            Toast.makeText(jmeSurfaceView.getContext(),"Resettled the Scene !", LENGTH_LONG).show();

        }else if(v.getId() == R.id.gridSwitcher){
            if(gridOn){
                gridOn = false;
                ((ImageView)v).setImageDrawable(ContextCompat.getDrawable(jmeSurfaceView.getContext(), R.drawable.ic_baseline_grid_off_24));
            }else{
                gridOn = true;
                ((ImageView)v).setImageDrawable(ContextCompat.getDrawable(jmeSurfaceView.getContext(), R.drawable.ic_baseline_grid_on_24));
            }
            invalidateUiPager(gridOn);
        }else if(v.getId() == TriggerID.closeTrigger){

            ((RelativeLayout)jmeSurfaceView.getParent()).findViewById(R.id.animationSettings).animate().setDuration(600).rotation(-45).start();
            menu.animate().translationY(displayMetrics.heightPixels + menu.getLayoutParams().height).setDuration(600).start();

        }else if(v.getId() == TriggerID.simpleTrackTrigger){

            //getStateManager().detach(getStateManager().getState("SimpleAnimation",SimpleScaleTrack.class)); -> not working ??!
            if(!getStateManager().getState(SimpleScaleTrack.class).isEnabled()){
                getStateManager().getState(SimpleScaleTrack.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(SimpleScaleTrack.class).setEnabled(false);
                deActivateButton(v);
            }

        }else if(v.getId() == TriggerID.stackLoopsTrigger){

            if(!getStateManager().getState(StackLoops.class).isEnabled()){
                getStateManager().getState(StackLoops.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(StackLoops.class).setEnabled(false);
                deActivateButton(v);
            }

        }else if(v.getId() == TriggerID.basicArmatureTrigger){

            if(!getStateManager().getState(BasicArmature.class).isEnabled()){
                getStateManager().getState(BasicArmature.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(BasicArmature.class).setEnabled(false);
                deActivateButton(v);
            }

        }else if(v.getId() == TriggerID.basicTween){

            if(!getStateManager().getState(BasicTween.class).isEnabled()){
                getStateManager().getState(BasicTween.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(BasicTween.class).setEnabled(false);
                deActivateButton(v);
            }

        }else if(v.getId() == TriggerID.emitterTweenTrigger){

            if(!getStateManager().getState(EmitterTween.class).isEnabled()){
                getStateManager().getState(EmitterTween.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(EmitterTween.class).setEnabled(false);
                deActivateButton(v);
            }

        }else if(v.getId() == TriggerID.blendableAnimTrigger){

            if(!getStateManager().getState(BlendableAnimation.class).isEnabled()){
                getStateManager().getState(BlendableAnimation.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(BlendableAnimation.class).setEnabled(false);
                deActivateButton(v);
            }

        }else if(v.getId() == TriggerID.blenderTweenTrigger){

            if(!getStateManager().getState(BlenderTween.class).isEnabled()){
                getStateManager().getState(BlenderTween.class).setEnabled(true);
                activateButton(v);
            }else{
                getStateManager().getState(BlenderTween.class).setEnabled(false);
                deActivateButton(v);
            }

        }else if(v.getId() == TriggerID.animLayersTrigger){

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
        Toast.makeText(jmeSurfaceView.getContext(),"Animation Started", LENGTH_LONG).show();
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
        Toast.makeText(jmeSurfaceView.getContext(),"Animation Stopped", LENGTH_LONG).show();
    }

    protected void invalidateUiPager(boolean gridOn){
        UiPager uiPager = (UiPager) ((ScrollView)uiStateManager.getChildUiStateById(SCROLLABLE_CONTENT)).getChildAt(0);
        TriggerModel[] model = new TriggerModel[]{
                new TriggerModel("Scale Track", TriggerID.simpleTrackTrigger, 0),
                new TriggerModel("Basic Armature", TriggerID.basicArmatureTrigger, 1),
                new TriggerModel("Stack Loops", TriggerID.stackLoopsTrigger, 2),
                new TriggerModel("Basic Tween", TriggerID.basicTween, 3),
                new TriggerModel("Emitter Tween", TriggerID.emitterTweenTrigger, 4),
                new TriggerModel("Bottle Fall", TriggerID.blendableAnimTrigger, 5),
                new TriggerModel("Blender Anim", TriggerID.blenderTweenTrigger, 6),
                new TriggerModel("Composite", TriggerID.animLayersTrigger, 7),
                new TriggerModel("Blank Field 0", 'B', 8),
                new TriggerModel("Blank Field 1", 'B', 9),
                new TriggerModel("Close Menu", TriggerID.closeTrigger, 10),
        };
        uiPager.removeAllViews();
        if(gridOn){
            uiPager.setColumnCount(3);
            uiPager.setRowCount(3);
        }else{
            uiPager.setColumnCount(1);
            uiPager.setRowCount(1);
        }
        for (TriggerModel triggerModel : model) {
            RelativeLayout relativeLayout = (RelativeLayout) uiStateManager.fromXML(R.layout.menu_trigger);
            relativeLayout.setPadding(20,20,20,20);
            Button button = relativeLayout.findViewById(R.id.trigger);
            //relativeLayout.getPaddingEnd()*(uiPager.getColumnCount()-1) is the total length of the padding between the buttons only (excluding the sides, that's why (-1)), this number is zero when ColumnCount is 1
            button.setLayoutParams(new RelativeLayout.LayoutParams(displayMetrics.widthPixels/uiPager.getColumnCount() - relativeLayout.getPaddingEnd()*(uiPager.getColumnCount()-1),
                    menu.getLayoutParams().height/4));
            button.setText(triggerModel.getBtnText());
            button.setId(triggerModel.getBtnID());
            button.setOnClickListener(AnimationFactory.this);
            //set the red color for the close menu button ONLY
            if(triggerModel.getBtnID() == TriggerID.closeTrigger){
                button.setBackgroundColor(Color.RED);
            }
            uiPager.attachUiState(relativeLayout, triggerModel.getBtnIndex());
        }
    }

    /**
     * <ul>
     * <li>Intended to disable the clipActions from their corresponding AnimLayers</li>
     * <li>Reset the current running animations to Spatial's default transformation</li>
     * </ul>
     */
    protected void disableAllStates(){
        jmeSurfaceView.getLegacyApplication().enqueue(()->{
            /*remove the current ClipActions from their corresponding layers (pause/stop the animation)*/
            getStateManager().getState(SimpleScaleTrack.class).setEnabled(false);
            getStateManager().getState(StackLoops.class).setEnabled(false);
            getStateManager().getState(BasicArmature.class).setEnabled(false);
            getStateManager().getState(BasicTween.class).setEnabled(false);
            getStateManager().getState(EmitterTween.class).setEnabled(false);
            getStateManager().getState(BlendableAnimation.class).setEnabled(false);
            getStateManager().getState(BlenderTween.class).setEnabled(false);
            getStateManager().getState(AnimLayers.class).setEnabled(false);
            /*Animation reset for any instance implementing HasLocalTransform*/
            ((Node)dataBaseStack).getChild("Cylinder.001").setLocalTransform(defaultOne);
            ((Node)dataBaseStack).getChild("Cylinder.002").setLocalTransform(defaultTwo);
            ((Node)dataBaseStack).getChild("Cylinder.003").setLocalTransform(defaultThree);
            dataBaseStack.setLocalTransform(defaultStack);
            /*get the joint of the armature & reset it*/
            getStateManager().getState(BasicArmature.class).getJoint0().setLocalTransform(defaultOne);
            getStateManager().getState(BasicArmature.class).getJoint1().setLocalTransform(defaultThree);
        });
    }
}
