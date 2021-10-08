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
        private int firstActiveAction = 0;

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
            //update values
            setBlendAction(blendAction);
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
        }

        public void setMaxRadius(float maxRadius) {
            this.maxRadius = maxRadius;
            //re-calculate the maxWave
            maxCircumference = 2 * FastMath.PI * maxRadius;
        }

        public void setMinRadius(float minRadius) {
            this.minRadius = minRadius;
            //recalculate values
            minCircumference = 2 * FastMath.PI * minRadius;
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
    public static class CubicInscribedTriSpace implements BlendSpace{

        private CustomBlendAction action;
        private float baseI;
        private float heightI;
        private float baseII;
        private float heightII;
        private float stepOfBlending;
        private float areaI;
        private float areaII;
        private enum Base10Constraint{
            ;
            public static float limitTo10(final float value){
                return value % 11;
            }
        }
        public CubicInscribedTriSpace(final float baseI, final float heightI, final float baseII, final float heightII){
            this.baseI = Base10Constraint.limitTo10(baseI);
            this.heightI = Base10Constraint.limitTo10(heightI);
            this.baseII = Base10Constraint.limitTo10(baseII);
            this.heightII = Base10Constraint.limitTo10(heightII);
        }
        @Override
        public void setBlendAction(BlendAction action) {
            if(action instanceof CustomBlendAction){
                this.action = (CustomBlendAction) action;
                final CustomBlendAction customBlendAction = ((CustomBlendAction) action);
                final int numberOfSpacesBetweenActions = customBlendAction.getActions().length - 1;

                areaI = 0.5f * baseI * heightI;
                areaII = 0.5f * baseII * heightII;
                stepOfBlending = (areaII - areaI) / numberOfSpacesBetweenActions;
            }else{
                throw new IllegalStateException("BlendAction Should be of type " + CustomBlendAction.class.getName());
            }
        }

        @Override
        public float getWeight() {
            //update-values
            setBlendAction(action);
            int currentStep = 0;
            int indexI = 0;
            for(int i = 0; i < action.getActions().length - 1 && currentStep < areaII; i++, currentStep+=stepOfBlending){
                indexI = i;
                //log the products
                System.out.println(currentStep + " " + stepOfBlending);
            }
            action.setFirstActiveIndex(indexI);
            action.setSecondActiveIndex(indexI + 1);
            final float scaleFactor = FastMath.pow((areaI / areaII), 3);
            final float inscribedTri = areaII - areaI;
            // -- NB: this scale weight gets applied to the secondActiveIndex within the blendAction & if the weight is less than 1, then the firstActiveIndex would run.
            //scale the inscribed area into tiny inscribed triangular areas triple times smaller than its initial value.
            return inscribedTri * scaleFactor;
        }

        @Override
        public void setValue(float value) {

        }

        public void setBaseI(float baseI) {
            this.baseI = baseI;
        }

        public void setBaseII(float baseII) {
            this.baseII = baseII;
        }

        public void setHeightI(float heightI) {
            this.heightI = heightI;
        }

        public void setHeightII(float heightII) {
            this.heightII = heightII;
        }
    }
    public static class PieChartSpace implements BlendSpace{
        private CustomBlendAction action;
        private float radius;
        private float angle;
        private float area;
        private float part;
        private int lastActionIndex;
        private enum ValueConstraint {;
            public static float constrainAngleTo360(final float angle){
                return angle % 361f;
            }
            public static float constraintRadiusToUnitCircle(final float radius){
                return radius % 1.1f;
            }
        }
        public PieChartSpace(final float radius, final float angle){
            this.radius = ValueConstraint.constraintRadiusToUnitCircle(radius);
            this.angle = ValueConstraint.constrainAngleTo360(angle);
        }
        @Override
        public void setBlendAction(BlendAction action) {
            if(action instanceof CustomBlendAction){
                this.action = (CustomBlendAction) action;
                this.action.setFirstActiveIndex(this.action.getActions().length - 2);
                this.action.setSecondActiveIndex(this.action.getActions().length - 1);
                area = FastMath.PI * FastMath.pow(radius, 2f);
                part = (angle / 360f) * area;
            }else {
                throw new IllegalStateException("BlendAction Should be of type " + CustomBlendAction.class.getName());
            }
        }

        @Override
        public float getWeight() {
            //keep the values updated with the loop (coherent update).
            setBlendAction(action);
            final float areaOfUnitCircle = FastMath.PI;
            //the scaleFactor is the factor of ratio between the user's area & the unit circle area
            final float scaleFactor = area / areaOfUnitCircle;
            //get the currentStep that involves a chosen scaled pieChart chunk by a kind of scaleFactor.
            //the scaleFactor aims at scaling the pieChart chunk again with respect to the unitCircleArea.
            float currentStep = part * scaleFactor;
            if(lastActionIndex < action.getActions().length - 1) {
                action.setFirstActiveIndex(lastActionIndex++);
                action.setSecondActiveIndex(lastActionIndex);
            }else{
                lastActionIndex = 0;
            }
            //limit values to 1 aka 100%
            return Math.min(currentStep, 1f);
        }

        @Override
        public void setValue(float inscribedPart) {
            this.part = inscribedPart;
        }

        public void setAngle(float angle) {
            this.angle = ValueConstraint.constrainAngleTo360(angle);
        }

        public void setRadius(float radius) {
            this.radius = ValueConstraint.constraintRadiusToUnitCircle(radius);
        }

        public void setArea(float area) {
            this.area = area;
        }
    }
}
