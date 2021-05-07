package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer.animationWrapper;

interface LayerBuilder {
    String LAYER_ARMATURE_LAYERS = AnimLayers.class.getSimpleName();
    String LAYER_STACKS = AnimLayers.class.getSimpleName();
    String LAYER_BASIC_ARMATURE = BasicArmature.class.getSimpleName();
    String LAYER_BASIC_TWEEN = BasicTween.class.getSimpleName();
    String LAYER_BLENDABLE_ANIM = BlendableAnimation.class.getSimpleName();
    String LAYER_BLENDER_TWEEN = BlenderTween.class.getSimpleName();
    String LAYER_EMITTER_TWEEN = EmitterTween.class.getSimpleName();
    String LAYER_SIMPLE_TRACK = SimpleScaleTrack.class.getSimpleName();
}
