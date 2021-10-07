package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.customBlendAction;

import com.jme3.anim.tween.action.Action;
import com.jme3.anim.tween.action.BlendAction;
import com.jme3.anim.tween.action.BlendSpace;
import com.jme3.anim.tween.action.BlendableAction;
import com.jme3.math.FastMath;

/**
 * A new runnable blendSpace test through a blendAction implementation utility, using the composition & concrete classes.
 * @author pavl_g.
 */
public final class CustomBlendAction extends BlendAction {
    public CustomBlendAction(BlendSpace blendSpace, BlendableAction... actions) {
        super(blendSpace, actions);
    }

    @Override
    protected void setFirstActiveIndex(int index) {
        super.setFirstActiveIndex(index);
    }

    @Override
    protected void setSecondActiveIndex(int index) {
        super.setSecondActiveIndex(index);
    }

    @Override
    protected Action[] getActions() {
        return super.getActions();
    }

    /**
     * Radial BlendSpace testcase.
     * @author pavl_g.
     */
    public static class RadialBlendSpace implements BlendSpace {

        //Concrete Composition level-2
        private CustomBlendAction blendAction;
        private float minRadius;
        private float maxRadius;
        private float minCircumference;
        private float maxCircumference;
        private float step;
        private boolean shuffleFlag = false;
        int firstActiveAction = 0;

        public RadialBlendSpace(final float minRadius, final float maxRadius){
            this.minRadius = minRadius;
            this.maxRadius = maxRadius;
        }
        @Override
        public void setBlendAction(BlendAction action) {
            if(action instanceof CustomBlendAction){
                this.blendAction = (CustomBlendAction) action;
                //other useful assigns.
                //1) getting the minCircum & the maxCircum.
                minCircumference = 2 * FastMath.PI * minRadius;
                maxCircumference = 2 * FastMath.PI * maxRadius;
                //2)finding the step length to be used for shuffling between animations, based on a circle arcs(subscribed circles areas).
                final float numberOfSpacesBetweenActions = ((CustomBlendAction) action).getActions().length - 1;
                final float lengthOfBlendSpace = (maxCircumference - minCircumference);
                //3)the step used for blending between the blendActions.
                step = lengthOfBlendSpace / numberOfSpacesBetweenActions;
            }else{
                throw new IllegalStateException("BlendAction Should be of type " + CustomBlendAction.class.getName());
            }
        }

        /**
         * Get the weight for the current blendSpace.
         * @apiNote the blendSpace weight : is the scale of blendSpace.
         * @return a number from zero to one representing the scale of the blendingAction.
         */
        @Override
        public float getWeight() {
            final int numberOfActiveActions = blendAction.getActions().length;
            float lowStep = minCircumference, highStep = minCircumference;

            for(int activeIndex = 0; activeIndex < numberOfActiveActions && highStep < maxCircumference;
                                     activeIndex++, highStep += step){
                //get the firstActiveAction
                firstActiveAction = activeIndex;
                //assign the lowStep to get the value of old highStep
                lowStep = highStep;
                //log the values
                System.out.println(firstActiveAction + " "+lowStep+" "+highStep);
            }
            //set the active indices for the runnable actions, indices get collected based on the circum steps.
            if(!isShuffleFlag()) {
                blendAction.setFirstActiveIndex(firstActiveAction);
                blendAction.setSecondActiveIndex(firstActiveAction + 1);
            }
            //handle the redundant steps
            if(lowStep == highStep){
                return 0;
            }
            //return the ratio between the 2 circle waves (i.e : maxCircumference & minCircumference), NB : the 2 values are adjustable via their radii
            // (a circle perimeter represent the direction of propagation of the animation waves)
            return minCircumference / maxCircumference;
        }

        /**
         * Set the max value for the blending animation.
         * @param maxRadius the max value.
         */
        @Override
        public void setValue(float maxRadius) {
            this.maxRadius = maxRadius;
            //re-calculate the maxWave
            maxCircumference = 2 * FastMath.PI * maxRadius;
            //recalculate values
            setBlendAction(blendAction);
        }

        public void setMaxRadius(float maxRadius) {
            this.maxRadius = maxRadius;
            //re-calculate the maxWave
            maxCircumference = 2 * FastMath.PI * maxRadius;
            //recalculate values
            setBlendAction(blendAction);
        }

        public void setMinRadius(float minRadius) {
            this.minRadius = minRadius;
            //recalculate values
            minCircumference = 2 * FastMath.PI * minRadius;
            //recalculate values
            setBlendAction(blendAction);
        }

        public void shuffleActionIndices(){
            if(!isShuffleFlag()) {
                blendAction.setFirstActiveIndex(firstActiveAction + 1);
                blendAction.setSecondActiveIndex(firstActiveAction);
                setShuffleFlag(true);
            }else{
                blendAction.setFirstActiveIndex(firstActiveAction);
                blendAction.setSecondActiveIndex(firstActiveAction + 1);
                setShuffleFlag(false);
            }
        }

        public void setShuffleFlag(boolean shuffleFlag) {
            this.shuffleFlag = shuffleFlag;
        }

        public boolean isShuffleFlag() {
            return shuffleFlag;
        }
    }
    //...........Other useful blendSpaces...........

}
