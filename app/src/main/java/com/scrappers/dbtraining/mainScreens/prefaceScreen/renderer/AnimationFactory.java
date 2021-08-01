package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.TransformTrack;
import com.jme3.anim.tween.action.Action;
import com.jme3.app.Application;
import com.jme3.app.jmeSurfaceView.JmeSurfaceView;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
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
import com.scrappers.dbtraining.notifications.NotificationUtils;
import com.scrappers.superiorExtendedEngine.menuStates.UiStateManager;
import com.scrappers.superiorExtendedEngine.menuStates.UiStatesLooper;
import com.scrappers.superiorExtendedEngine.menuStates.uiPager.UiPager;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Executors;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_LONG;
import static com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.Scene.defaultOne;
import static com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.Scene.defaultStack;
import static com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.Scene.defaultThree;
import static com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.Scene.defaultTwo;

/**
 * Animation Factory class, contains different controls & settings/UI adjustment.
 * @author pavl_g
 */
public class AnimationFactory extends BaseAppState implements View.OnClickListener, SearchView.OnQueryTextListener {
    private final Spatial dataBaseStack;
    private final JmeSurfaceView jmeSurfaceView;
    private final DisplayMetrics displayMetrics = new DisplayMetrics();
    private LinearLayout menu;
    private UiStateManager uiStateManager;
    private static final char SCROLLABLE_CONTENT = 'S' + 'C' + 'R' + 'O' + 'L' + 'L';
    private static boolean gridOn = false;
    private static int sortAlgorithm = UiPager.A_Z;
    private EmitterTween emitterTween;
    private final String[] triggersString = new String[]{
            "Scale Track",
            "Basic Armature",
            "Stack Loops",
            "Basic Tween",
            "Emitter Tween",
            "Bottle Fall",
            "Blender Anim",
            "Composite",
            "Blank Field 0",
            "Blank Field 1",
            "Close Menu",
    };
    private final char[] triggersID = new char[]{
            TriggerID.simpleTrackTrigger,
            TriggerID.basicArmatureTrigger,
            TriggerID.stackLoopsTrigger,
            TriggerID.basicTween,
            TriggerID.emitterTweenTrigger,
            TriggerID.blendableAnimTrigger,
            TriggerID.blenderTweenTrigger,
            TriggerID.animLayersTrigger,
            'B',
            'B',
            TriggerID.closeTrigger
    };
    public AnimationFactory(Spatial dataBaseStack, JmeSurfaceView jmeSurfaceView) {
        this.dataBaseStack=dataBaseStack;
        this.jmeSurfaceView=jmeSurfaceView;
    }
    @Override
    protected void initialize(Application app) {
        ((AppCompatActivity)jmeSurfaceView.getContext()).runOnUiThread(()->{
            //set the display metrics of the current device, fetched from the mainActivity Holder context
            ((AppCompatActivity)jmeSurfaceView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            //set the click listeners
            ((RelativeLayout)jmeSurfaceView.getParent()).findViewById(R.id.animationSettings).setOnClickListener(this);
            ((RelativeLayout)jmeSurfaceView.getParent()).findViewById(R.id.gridSwitcher).setOnClickListener(this);
            ((RelativeLayout)jmeSurfaceView.getParent()).findViewById(R.id.scheduleReset).setOnClickListener(this);
            ((RelativeLayout)jmeSurfaceView.getParent()).findViewById(R.id.reset).setOnClickListener(this);
            //initialize the UiStateManager instance
            uiStateManager = new UiStateManager(((RelativeLayout)jmeSurfaceView.getParent()));
            //get the menu
            menu = (LinearLayout) uiStateManager.attachUiState(uiStateManager.fromXML(R.layout.animation_selection_menu));
            menu.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1200));
            menu.animate().translationY(displayMetrics.heightPixels + menu.getLayoutParams().height).setDuration(600).start();
            menu.setVisibility(INVISIBLE);
            menu.setHorizontalScrollBarEnabled(true);
            //uiPager starts here......
            UiPager uiPager = new UiPager(menu.getContext());
            LayoutTransition layoutTransition = new LayoutTransition();
            Animator animator = ObjectAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofFloat("translateX", uiPager.getLayoutParams().width, 0),
                    PropertyValuesHolder.ofFloat("translateY", uiPager.getLayoutParams().height, 0));
            animator.setDuration(300);
            animator.setStartDelay(100);
            animator.setInterpolator(new AccelerateInterpolator());
            layoutTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, animator);
            uiPager.setLayoutTransition(layoutTransition);
            //add the uiPager to a scrollView
            ScrollView scrollableContainer = uiPager.initializeScrollableContainer();
            scrollableContainer.setHorizontalScrollBarEnabled(true);
            scrollableContainer.setId(SCROLLABLE_CONTENT);
            //add the ScrollView (containing UiPager) to the menu.
            ((RelativeLayout)menu.findViewById(R.id.triggersMenu)).addView(scrollableContainer);
            //add-remove-update-integrate-disintegrate views
            try {
                updateUiPager(null, gridOn, sortAlgorithm);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //do the search work
            LinearLayout linearLayout = menu.findViewById(R.id.moreOptions);
            SearchView searchView = linearLayout.findViewById(R.id.search);
            searchView.setOnQueryTextListener(this);
            linearLayout.findViewById(R.id.sort).setOnClickListener(this);

        });
        SimpleScaleTrack simpleScaleTrack = new SimpleScaleTrack("SimpleScaleTrack", dataBaseStack);
        getStateManager().attach(simpleScaleTrack);
        getStateManager().attach(new BasicArmature("BasicArmatureTrack", dataBaseStack));
        getStateManager().attach(new StackLoops("StackLoopsTrack", dataBaseStack));
        getStateManager().attach(new BasicTween("BasicTweenTrack", dataBaseStack));
        emitterTween = new EmitterTween("EmitterTween", dataBaseStack);
        emitterTween.setAnimationEvents(new AnimEventEntity.AnimationEvents() {
            @Override
            public void onAnimationStart(AnimComposer animComposer, Action transformationStart) {

            }

            @Override
            public void onAnimationEnd(AnimComposer animComposer, Action transformEnd) {
                jmeSurfaceView.getLegacyApplication().enqueue(()->{
                    emitterTween.electricWaves1.getMaterial().setColor("GlowColor", ColorRGBA.randomColor().mult(ColorRGBA.LightGray));
                    emitterTween.electricWaves2.getMaterial().setColor("GlowColor",ColorRGBA.randomColor().mult(ColorRGBA.LightGray));
                    emitterTween.electricWaves3.getMaterial().setColor("GlowColor",ColorRGBA.randomColor().mult(ColorRGBA.LightGray));
                    emitterTween.electricWaves4.getMaterial().setColor("GlowColor",ColorRGBA.randomColor().mult(ColorRGBA.LightGray));
                    emitterTween.electricWaves5.getMaterial().setColor("GlowColor",ColorRGBA.randomColor().mult(ColorRGBA.LightGray));
                    emitterTween.electricWaves6.getMaterial().setColor("GlowColor",ColorRGBA.randomColor().mult(ColorRGBA.LightGray));
                    emitterTween.electricWaves7.getMaterial().setColor("GlowColor",ColorRGBA.randomColor().mult(ColorRGBA.LightGray));
                    emitterTween.electricWaves8.getMaterial().setColor("GlowColor",ColorRGBA.randomColor().mult(ColorRGBA.LightGray));
                    emitterTween.electricWaves9.getMaterial().setColor("GlowColor",ColorRGBA.randomColor().mult(ColorRGBA.LightGray));
                    emitterTween.electricWaves10.getMaterial().setColor("GlowColor",ColorRGBA.randomColor().mult(ColorRGBA.LightGray));
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

    /**
     * Buttons Click Listeners
     * @param v the current button/view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.animationSettings){
            ((RelativeLayout)jmeSurfaceView.getParent()).findViewById(R.id.animationSettings).animate().setDuration(100).rotation(45).start();
            menu.setVisibility(VISIBLE);
            menu.animate().translationY(displayMetrics.heightPixels - menu.getLayoutParams().height).setDuration(600).start();
        }
        if(v.getId() == R.id.sort){
            if(sortAlgorithm == UiPager.A_Z){
                sortAlgorithm = UiPager.Z_A;
            }else{
                sortAlgorithm = UiPager.A_Z;
            }
            try {
                updateUiPager(null, gridOn, sortAlgorithm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(v.getId() == R.id.reset){
            /*reset button*/
            resetState();
        }else if(v.getId() == R.id.scheduleReset){

            ResetScheduler.Builder builder = new ResetScheduler.Builder(jmeSurfaceView.getContext());
            LinearLayout scheduleResetMenu = (LinearLayout) uiStateManager.attachUiState(uiStateManager.fromXML(R.layout.animation_selection_menu));
            scheduleResetMenu.setLayoutParams(new LinearLayout.LayoutParams(displayMetrics.widthPixels,1200));

            builder.setActionListener(OrderReceiver.class, PendingIntent.FLAG_ONE_SHOT)
                   .setAlarmType(AlarmManager.RTC_WAKEUP);
            ResetScheduler resetScheduler = new ResetScheduler(jmeSurfaceView.getContext());
            resetScheduler.schedule(builder);

        }else if(v.getId() == R.id.gridSwitcher){
            if(gridOn){
                gridOn = false;
                ((ImageView)v).setImageDrawable(ContextCompat.getDrawable(jmeSurfaceView.getContext(), R.drawable.ic_baseline_grid_off_24));
                ((ImageView)v).setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(uiStateManager.getContext(), R.color.opacifiedWhite)));
            }else{
                gridOn = true;
                ((ImageView)v).setImageDrawable(ContextCompat.getDrawable(jmeSurfaceView.getContext(), R.drawable.ic_baseline_grid_on_24));
                ((ImageView)v).setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(uiStateManager.getContext(), R.color.gold)));
            }
            ((GradientDrawable) Objects.requireNonNull(
            ContextCompat.getDrawable(v.getContext(), R.drawable.toolbar_background))).setColors(new int[]{
            ContextCompat.getColor(v.getContext(),R.color.lightBlack),ContextCompat.getColor(v.getContext(),
            R.color.lightBlack),ContextCompat.getColor(v.getContext(),R.color.lightBlack)});
            try {
                updateUiPager(null, gridOn, sortAlgorithm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(v.getId() == R.id.dismissForever){

            ((ViewGroup)((ViewGroup)v.getParent()).getParent()).setVisibility(GONE);
            Toast.makeText(jmeSurfaceView.getContext(), "Powered By Jme Android", Toast.LENGTH_LONG).show();
            NotificationUtils.initializeInBackground(jmeSurfaceView.getContext());

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

    /**
     * Commands & updates the UiPager object & its inclusions based on a queryText & grid switch.
     * @param queryText the queryText that commands & update the UiStates based on the filteredDataModel from this queryText.
     * @param gridOn switches the grid on & off.
     * @throws Exception if interruption occurs.
     */
    protected void updateUiPager(@Nullable String[] queryText, boolean gridOn, int searchAlgorithm) throws Exception {
            UiPager uiPager = (UiPager) ((ScrollView)uiStateManager.getChildUiStateById(SCROLLABLE_CONTENT)).getChildAt(0);
            //uiPager pre-properties
            if(uiPager.hasUiStates()){
                uiPager.detachAllUiStates();
            }
            if(gridOn){
                uiPager.setColumnCount(3);
                uiPager.setRowCount(3);
            }else{
                uiPager.setColumnCount(1);
                uiPager.setRowCount(1);
            }
            //data model settings, parallel filling
            TriggerModel[] model = new TriggerModel[triggersString.length];
            for(int position = 0; position < model.length; position++){
                model[position] = new TriggerModel(triggersString[position], triggersID[position]);
            }
            //apply optional filters
            TriggerModel[] filteredModel;
            //do the filters only if the user inserts text.
            if(queryText != null){
                filteredModel = new TriggerModel[queryText.length];
                for (TriggerModel triggerModel : model) {
                    for (int j = 0; j < queryText.length; j++) {
                        if ( triggerModel.getBtnText().toLowerCase().contains(queryText[j].toLowerCase()) ){
                            filteredModel[j] = triggerModel;
                        }
                    }
                }
            }else{
                //apply sorting
                filteredModel = new TriggerModel[model.length];
                String[] sortedList = new String[0];
                sortedList = uiPager.sort(triggersString, searchAlgorithm);
                for (TriggerModel triggerModel : model) {
                    for(int pos = 0; pos < sortedList.length; pos++){
                        if ( triggerModel.getBtnText().equalsIgnoreCase(sortedList[pos]) ){
                            filteredModel[pos] = triggerModel;
                        }
                    }
                }
                //jme poster
                RelativeLayout topUpMenu = (RelativeLayout) uiStateManager.fromXML(R.layout.top_up_menu);
                topUpMenu.setLayoutParams(new GridLayout.LayoutParams(GridLayout.spec(0, uiPager.getColumnCount()),
                        GridLayout.spec(0, uiPager.getRowCount())));
                topUpMenu.findViewById(R.id.container).findViewById(R.id.dismissForever).setOnClickListener(this);
                uiPager.attachUiState(topUpMenu ,UiPager.SEQUENTIAL_ADD);
            }

            //apply data on UI
            for (TriggerModel triggerModel : filteredModel) {
                RelativeLayout relativeLayout = (RelativeLayout) uiStateManager.fromXML(R.layout.menu_trigger);
                relativeLayout.setPadding(5, 5, 5, 5);
                Button button = relativeLayout.findViewById(R.id.trigger);
                //relativeLayout.getPaddingEnd()*(uiPager.getColumnCount()-1) is the total length of the padding between the buttons only (excluding the sides, that's why (-1)), this number is zero when ColumnCount is 1
                button.setLayoutParams(new RelativeLayout.LayoutParams(displayMetrics.widthPixels / uiPager.getColumnCount() - relativeLayout.getPaddingEnd() * (uiPager.getColumnCount() - 1),
                        menu.getLayoutParams().height / 4));
                button.setText(triggerModel.getBtnText());
                button.setId(triggerModel.getBtnID());
                button.setOnClickListener(AnimationFactory.this);
                //set the red color for the close menu button ONLY
                if ( triggerModel.getBtnID() == TriggerID.closeTrigger ){
                    button.setBackgroundColor(Color.RED);
                }
                uiPager.attachUiState(relativeLayout, UiPager.SEQUENTIAL_ADD);
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

    /**
     * Reset the Animations & the Scene
     */
    private void resetState(){
        jmeSurfaceView.getLegacyApplication().enqueue(this::disableAllStates);
        UiPager uiPager = (UiPager) ((ScrollView)uiStateManager.getChildUiStateById(SCROLLABLE_CONTENT)).getChildAt(0);
        try {
            updateUiPager(null ,gridOn, sortAlgorithm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        uiPager.forEachUiState((UiStatesLooper.Modifiable.Looper) (currentView, position) -> {
            if(uiPager.hasUiStateByIndex(position)){
                if (((ViewGroup) currentView).getChildAt(0).getId() != TriggerID.closeTrigger &&
                        ((ViewGroup) currentView).getChildAt(0).getId() != R.id.container ){
                    deActivateButton(((ViewGroup) currentView).getChildAt(0));
                }
            }
        });
        Toast.makeText(jmeSurfaceView.getContext(),"Resettled the Scene !", LENGTH_LONG).show();
        emitterTween.electricWaves1.getMaterial().setColor("GlowColor", ColorRGBA.Cyan.mult(50f));
        emitterTween.electricWaves2.getMaterial().setColor("GlowColor",ColorRGBA.Cyan.mult(50f));
        emitterTween.electricWaves3.getMaterial().setColor("GlowColor",ColorRGBA.Cyan.mult(50f));
        emitterTween.electricWaves4.getMaterial().setColor("GlowColor",ColorRGBA.Cyan.mult(50f));
        emitterTween.electricWaves5.getMaterial().setColor("GlowColor",ColorRGBA.Cyan.mult(50f));
        emitterTween.electricWaves6.getMaterial().setColor("GlowColor",ColorRGBA.Cyan.mult(50f));
        emitterTween.electricWaves7.getMaterial().setColor("GlowColor",ColorRGBA.Cyan.mult(50f));
        emitterTween.electricWaves8.getMaterial().setColor("GlowColor",ColorRGBA.Cyan.mult(50f));
        emitterTween.electricWaves9.getMaterial().setColor("GlowColor",ColorRGBA.Cyan.mult(50f));
        emitterTween.electricWaves10.getMaterial().setColor("GlowColor",ColorRGBA.Cyan.mult(50f));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        UiPager uiPager = (UiPager) ((ScrollView)uiStateManager.getChildUiStateById(SCROLLABLE_CONTENT)).getChildAt(0);
        //search query
        try {
            //search if user only types something, if user types empty string then reset.
            if(!query.isEmpty()){
                updateUiPager(uiPager.search(triggersString, new String[]{query}, null), gridOn, sortAlgorithm);
            }else{
                updateUiPager(null, gridOn, sortAlgorithm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        UiPager uiPager = (UiPager) ((ScrollView)uiStateManager.getChildUiStateById(SCROLLABLE_CONTENT)).getChildAt(0);
        //search query
        try {
            //search if user only types something, if user types empty string then reset.
            if(!newText.isEmpty()){
                updateUiPager(uiPager.search(triggersString, new String[]{newText}, null), gridOn, sortAlgorithm);
            }else{
                updateUiPager(null, gridOn, sortAlgorithm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * A subclass helper, to schedule a reset for the scene using #{@link AlarmManager}.
     * @author pavl_g
     */
    public static class ResetScheduler {
        private final Context context;
        public ResetScheduler(final Context context){
            this.context = context;
        }
        public void schedule(Builder alarmBuilder){
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(alarmBuilder.getAlarmType(),
                    System.currentTimeMillis()+alarmBuilder.getScheduleTime(),
                     alarmBuilder.getActionListener());
        }

        /**
         * A Builder class to build the Alarm to be scheduled by the AlarmManager.
         */
        public static class Builder{
            public static final String RESET_ACTION = Builder.class.getName()+".StartResettingData";
            private final Context context;
            public static final int ALARM_BUILDER = 2222;
            private int alarmType = 0;
            private long scheduleTime = 0;
            private PendingIntent actionListener = null;

            public Builder(final Context context){
                this.context = context;
            }
            public Builder setAlarmType(int alarmType){
                this.alarmType = alarmType;
                return this;
            }
            public Builder setScheduleTime(long millis){
                this.scheduleTime = millis;
                return this;
            }
            public Builder setActionListener(Class<? extends BroadcastReceiver> clazz, int flags){
                Intent serviceIntent = new Intent(context, clazz);
                serviceIntent.setAction(RESET_ACTION);
                this.actionListener = PendingIntent.getBroadcast(context, ALARM_BUILDER, serviceIntent, flags);
                return this;
            }

            public int getAlarmType() {
                return alarmType;
            }

            public long getScheduleTime() {
                return scheduleTime;
            }

            public PendingIntent getActionListener() {
                return actionListener;
            }
        }
    }
}
