package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.customBlendAction;

import com.jme3.anim.tween.action.Action;
import com.jme3.anim.tween.action.BlendAction;
import com.jme3.anim.tween.action.BlendSpace;
import com.jme3.anim.tween.action.BlendableAction;
import com.jme3.math.FastMath;
import com.jme3.math.Vector4f;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A new runnable blendSpace test through a blendAction implementation utility, using the composition & concrete classes.
 * @author pavl_g.
 */
public final class CustomBlendAction extends BlendAction {

    public CustomBlendAction(BlendSpace blendSpace, BlendableAction... actions) {
        super(blendSpace, actions);
    }

    @Override
    public void setFirstActiveIndex(int index) {
        super.setFirstActiveIndex(index);
    }

    @Override
    public void setSecondActiveIndex(int index) {
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
        private float areaI;
        private float areaII;
        private float blendingStep = 0f;
        private float currentStep = 0f;
        private int indexI = 0;
        private float inscribedTri;
        private boolean incremental = false;
        /**
         * Use default impl.
         */
        public CubicInscribedTriSpace(){
            this(2f, 4f, 8f, 8f);
        }
        public CubicInscribedTriSpace(final float baseI, final float heightI, final float baseII, final float heightII){
            this.baseI = baseI;
            this.heightI = heightI;
            this.baseII = baseII;
            this.heightII = heightII;
        }
        @Override
        public void setBlendAction(BlendAction action) {
            if(action instanceof CustomBlendAction){
                this.action = (CustomBlendAction) action;
                areaI = 0.5f * baseI * heightI;
                areaII = 0.5f * baseII * heightII;
                //shuffle areas if areaI > areaII.
                if(areaI > areaII){
                    final float tempI = areaI;
                    final float tempII = areaII;
                    areaII = tempI;
                    areaI = tempII;
                }
                inscribedTri = areaII - areaI;
                final int numberOfBlendingSpaces = ((CustomBlendAction) action).getActions().length - 1;
                blendingStep = inscribedTri / numberOfBlendingSpaces;
            }else{
                throw new IllegalStateException("BlendAction Should be of type " + CustomBlendAction.class.getName());
            }
        }

        @Override
        public float getWeight() {
            //update-values
            setBlendAction(action);
            if(currentStep < inscribedTri && indexI < action.getActions().length - 1){
                if(isIncremental()) {
                    currentStep += blendingStep;
                }else{
                    currentStep = blendingStep;
                }
                action.setFirstActiveIndex(indexI++);
                action.setSecondActiveIndex(indexI);
            }else{
                indexI = 0;
            }
            final float scaleFactor = FastMath.pow((areaI / areaII), 3);
            // -- NB: this scale weight gets applied to the secondActiveIndex within the blendAction & if the weight is less than 1, then the firstActiveIndex would run.
            //scale the inscribed area into tiny inscribed triangular areas triple times smaller than its initial value.
            return currentStep * scaleFactor;
        }

        @Override
        public void setValue(float inscribedTri) {
            this.inscribedTri = inscribedTri;
        }

        public void setIncremental(boolean incremental) {
            this.incremental = incremental;
        }

        public boolean isIncremental() {
            return incremental;
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

    /**
     * A mathematical space used for blending between 2 successive {@link com.jme3.anim.tween.action.BlendableAction}s
     * based on a circle sector area {@link com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.customBlendAction.CustomBlendAction.PieChartSpace#sectorArea} calculated from {@link com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.customBlendAction.CustomBlendAction.PieChartSpace#radius} and {@link com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.customBlendAction.CustomBlendAction.PieChartSpace#angle},
     * the sector area is scaled by the scaleFactor (userCircleArea / unitCircleArea) at the last step before applying values,
     * and the step value represents a fraction from 0 to 1 (when the area of the circle sector reaches the areaOfUnitCircle), it means by how much the
     * sectorArea approximates the unit circle area.
     *
     * @author pavl_g.
     */
    public static class PieChartSpace implements BlendSpace {

        protected CustomBlendAction action;
        //pie-chart radius -- max is 1f
        protected float radius;
        //sector angle -- max is 360f in degrees
        protected float angle;
        //pie-chart area
        protected float area;
        //pie-chart sector area
        protected float sectorArea;
        protected boolean sectorAreaManualAdjustment;
        //exposing the action indices
        private int firstActionIndex = 0;
        private int secondActionIndex = 0;

        /**
         * Instantiates a default pie chart space implementation.
         * Default radius = 1f.
         * Default angle = 45 degrees.
         * Default area = 0.125 of unit circle area (0.125 * PI).
         */
        public PieChartSpace() {
            this(1f, 45f);
        }

        /**
         * Instantiates a pie chart space with a radius and a sector angle,
         * radius is clamped in the range [0, 1] and angle is clamped
         * in the range [0, 360].
         *
         * @param radius circle radius.
         * @param angle  sector angle in degrees.
         */
        public PieChartSpace(final float radius, final float angle) {
            //implicit suppression to extrapolation
            //clamp values in the range :  r = [0, 1] and angle = [0, 360].
            this.radius = radius % 1.1f;
            this.angle = angle % 360.1f;
        }

        @Override
        public void setBlendAction(BlendAction action) {
            if(action instanceof CustomBlendAction) {
                this.action = (CustomBlendAction) action;
                //use default actions indices
                this.action.setFirstActiveIndex(this.action.getActions().length - 2);
                this.action.setSecondActiveIndex(this.action.getActions().length - 1);
            }
        }

        @Override
        public float getWeight() {
            calculatePieChartTotalArea();
            if (!sectorAreaManualAdjustment) {
                calculateSectorArea();
            }
            //calculate the unit circle area.
            final float areaOfUnitCircle = FastMath.PI;
            //the scaleFactor is the factor of ratio between the user's area and the unit circle area.
            final float scaleFactor = area / areaOfUnitCircle;
            //scaling the pieChart sector area with respect to the unitCircleArea.
            final float scaledSector = sectorArea * scaleFactor;
            //converting the step value to percentage from 0% (no stepping) to 100% (unitCircleArea).
            final float step = scaledSector / areaOfUnitCircle;
            //use the step as a delta value of interpolation
            return step;
        }

        /**
         * Calculate the sector area from {@link com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.customBlendAction.CustomBlendAction.PieChartSpace#area} and {@link com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.customBlendAction.CustomBlendAction.PieChartSpace#angle}.
         */
        protected void calculateSectorArea() {
            //calculate the sector area
            sectorArea = (angle / 360f) * area;
        }

        /**
         * Calculate pie chart total area from {@link com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.customBlendAction.CustomBlendAction.PieChartSpace#radius}.
         */
        protected void calculatePieChartTotalArea() {
            //calculate the area of the pieChart
            area = FastMath.PI * FastMath.pow(radius, 2f);
        }

        /**
         * Manually alters the value of the sector area.
         * Notes :
         * - Altering the value of the sector area manually would
         * ignore both {@link com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.customBlendAction.CustomBlendAction.PieChartSpace#area} and {@link com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.customBlendAction.CustomBlendAction.PieChartSpace#angle}.
         * <p>
         * - Adjust {@link com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.customBlendAction.CustomBlendAction.PieChartSpace#setSectorAreaManualAdjustment(boolean)} to false
         * to neutralize the manual effect and return back to using both (the pie chart area and the angle).
         *
         * @param sectorArea a sector area to use.
         */
        @Override
        public void setValue(float sectorArea) {
            this.sectorArea = sectorArea;
            //activates sector area adjustment ignoring the angle and radius
            this.sectorAreaManualAdjustment = true;
        }

        /**
         * Tests whether the manual adjustment is activated.
         *
         * @return true if manual adjustment is enabled, false otherwise.
         */
        public boolean isSectorAreaManualAdjustment() {
            return sectorAreaManualAdjustment;
        }

        /**
         * Enables/Disables the manual area adjustment flag.
         *
         * @param sectorAreaManualAdjustment true to enable manual adjustment of the sector area ignoring both the pie chart area and the angle,
         *                                   false to use the pie chart area and the angle to calculate the sector area and ignore {@link com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.customBlendAction.CustomBlendAction.PieChartSpace#setValue(float)}.
         */
        public void setSectorAreaManualAdjustment(boolean sectorAreaManualAdjustment) {
            this.sectorAreaManualAdjustment = sectorAreaManualAdjustment;
        }

        /**
         * Gets the angle value of the pie-chart sector in degrees.
         *
         * @return the angle in degrees.
         */
        public float getAngle() {
            return angle;
        }

        /**
         * Alters the angle value of the pie-chart sector.
         * Values are internally clamped in the range of [0, 360].
         *
         * @param angle the angle in degrees.
         */
        public void setAngle(float angle) {
            this.angle = angle % 360.1f;
        }

        /**
         * Gets the radius of the pie-chart.
         *
         * @return the radius of the circle.
         */
        public float getRadius() {
            return radius;
        }

        /**
         * Alters the radius of the pie-chart.
         * Values are internally clamped in the range of [0, 1].
         *
         * @param radius the circle radius.
         */
        public void setRadius(float radius) {
            this.radius = radius % 1.1f;
        }

        /**
         * Gets the sector area, sector area depends on both {@link com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.customBlendAction.CustomBlendAction.PieChartSpace#angle}, {@link com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.customBlendAction.CustomBlendAction.PieChartSpace#radius}.
         * To change the sector area on runtime use : {@link com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.customBlendAction.CustomBlendAction.PieChartSpace#setAngle(float)} and {@link com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.customBlendAction.CustomBlendAction.PieChartSpace#setRadius(float)}
         * or use {@link com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper.customBlendAction.CustomBlendAction.PieChartSpace#setValue(float)} to directly alter the sector area.
         *
         * @return the sector area.
         */
        public float getSectorArea() {
            return sectorArea;
        }

        /**
         * Alters the index of the first action.
         * Usually values represented here depends on the number of {@link com.jme3.anim.tween.action.BlendableAction}s used within
         * {@link com.jme3.anim.tween.action.BlendAction} arguments.
         * Default index of the second action is : action.getActions().length - 2
         *
         * @param firstActionIndex the index of the first action.
         */
        public void setFirstAction(int firstActionIndex) {
            //sanity check the inputs
            if (action == null) {
                throw new IllegalStateException("A BlendAction instance hasn't been assigned to this blendSpace yet, use this setter after instantiating a BlendAction on this space !");
            }
            assert (firstActionIndex < action.getActions().length);
            action.setFirstActiveIndex(firstActionIndex);
            this.firstActionIndex = firstActionIndex;
        }

        /**
         * Alters the index of the second action.
         * Usually values represented here depends on the number of {@link com.jme3.anim.tween.action.BlendableAction}s used within
         * {@link com.jme3.anim.tween.action.BlendAction} arguments.
         * Default index of the second action is : action.getActions().length - 1
         *
         * @param secondActionIndex the index of the second action.
         */
        public void setSecondAction(int secondActionIndex) {
            //sanity check the inputs
            if (action == null) {
                throw new IllegalStateException("A BlendAction instance hasn't been assigned to this blendSpace yet, use this setter after instantiating a BlendAction on this space!");
            }
            assert (secondActionIndex < action.getActions().length);
            action.setSecondActiveIndex(secondActionIndex);
            this.secondActionIndex = secondActionIndex;
        }

        /**
         * Gets the actions involved in the space blend action.
         *
         * @return the actions of the space blend action.
         */
        public Action[] getActions() {
            return action.getActions();
        }

        /**
         * Gets the first action index.
         * Default index of the first action is : action.getActions().length - 2
         *
         * @return the first action index.
         */
        public int getFirstActionIndex() {
            return firstActionIndex;
        }

        /**
         * Gets the second action index.
         * Default index of the second action is : action.getActions().length - 1
         *
         * @return the second action index.
         */
        public int getSecondActionIndex() {
            return secondActionIndex;
        }
    }


    /**
     * A custom implementation of the {@link BlendSpace} interface, used for calculating the blendWeight value based on
     * the continuity/integration of some values {@link ContinuitySpace#values} along some functions {@link ContinuitySpace#function}, the net result is
     * a blendable animation with a propagation wave of values/directions represented by the slope fields of the utilized Function.
     * @see com.jme3.anim.tween.action.LinearBlendSpace
     * @see PieChartSpace
     * @author pavl_g.
     */
    public static class ContinuitySpace implements BlendSpace{

        private Function function = Function.QUADRATIC;
        private Vector4f[] values;
        private float scaleFactor = 1f;

        /**
         * Choose a function for the continuity, Default is {@link Function#QUADRATIC}.
         */
        public enum Function{
            SINE, COSINE, TANGENT, QUADRATIC, RECIPROCAL, CUBIC, RADICAL, ABSOLUTE,
        }

        /**
         * Instantiate a continuity blendSpace instance, passing in the values to be used in a function.
         * Values are broken down into 4 fields :
         * <li> {@link Vector4f#getX()} represents the x coordinate to be plugged into the equation.</li>
         * <li> {@link Vector4f#getY()} represents the coefficient of x or x^2 for the quadratic eqautions.</li>
         * <li> {@link Vector4f#getZ()} represents the coefficient value of x inside the quadratic equation (ax^2 + </li>
         * <li> {@link Vector4f#getW()} represents the value of c (ex : y = ax^2 + bx + c), which is the vertical distance between the subsequent functions.</li>
         *
         * @param values values in vector4f designation, representing the matrix of the system.
         */
        public ContinuitySpace(final Vector4f[] values){
            //limit the input to x element [0, 1], b element [0, 0.5], c element [0, 0.5].
            for(Vector4f value : values){
                value.setX(value.getX() % 1.1f);
                value.setZ(value.getZ() % 0.6f);
                value.setY(value.getY() % 0.6f);
            }
            this.values = values;
        }

        /**
         * set the values of the current system.
         * @see ContinuitySpace#ContinuitySpace(Vector4f[])
         * @param values the values.
         */
        public void setValues(final Vector4f[] values){
            this.values = values;
        }

        /**
         * Sets the scale factor.
         * @param scaleFactor the scale factor.
         */
        public void setScaleFactor(float scaleFactor) {
            this.scaleFactor = scaleFactor;
        }

        public float getScaleFactor() {
            return scaleFactor;
        }

        public void setFunction(Function function) {
            this.function = function;
        }

        public Function getFunction() {
            return function;
        }

        @Override
        public void setBlendAction(BlendAction action) {
            if(action instanceof CustomBlendAction){
                CustomBlendAction action1 = (CustomBlendAction) action;
                action1.setFirstActiveIndex(action1.getActions().length - 2);
                action1.setSecondActiveIndex(action1.getActions().length - 1);
            }else {
                throw new IllegalStateException("BlendAction Should be of type " + CustomBlendAction.class.getName());
            }
        }

        @Override
        public float getWeight() {
            //serial addition result
            final float sigmaValue = getAreaBySigma();
            return FastMath.abs(sigmaValue * scaleFactor);
        }

        @Override
        public void setValue(float value) {
            this.scaleFactor = value;
        }

        /**
         * find the sigma of the System functions.
         * @return a value represents the summation of results of the current chosen system.
         */
        private float getAreaBySigma(){
            final ArrayList<Float> getLimits = (ArrayList<Float>) getLimitsOutput();
            float integral = 0;
            for(float value : getLimits){
                integral += value;
            }
            return integral;
        }

//        private float getDifferentiationSigma(){
//            final float sigmaOfDifferentiation = 0;
//
//        }
        /**
         * Find all the values of the current chosen function {@link ContinuitySpace#getFunction()} based on {@link ContinuitySpace#values}
         * and collect them inside a collection instance.
         * @return a collection of the calculated values when x approaches a value.
         */
        private Collection<Float> getLimitsOutput() {
            final Collection<Float> limits = new ArrayList<>();
            for(Vector4f value : values) {
                // calculate the function result when x approaches a value (lim f(x) x -> value.getX()).
                limits.add(getY(value.getX(), value.getY(), value.getZ(), value.getW()));
            }
            return limits;
        }

        private float getY(final float x, final float a, final float b, final float c){
            switch (function){
                case SINE:
                    return FastMath.sin(x) + c;
                case COSINE:
                    return FastMath.cos(x) + c;
                case TANGENT:
                    return FastMath.tan(x) + c;
                case CUBIC:
                    return a * FastMath.pow(x, 3) + c;
                case RECIPROCAL:
                    return a * (1/x) + c;
                case RADICAL:
                    return a * FastMath.sqrt(x) + c;
                case ABSOLUTE:
                    return a * FastMath.abs(x) + c;
                default:
                    return a * FastMath.pow(x, 2) + b * x + c;
            }
        }
    }
}
