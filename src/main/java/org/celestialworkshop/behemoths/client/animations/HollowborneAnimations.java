package org.celestialworkshop.behemoths.client.animations;// Save this class in your mod and generate all required imports

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

/**
 * Made with Blockbench 5.0.7
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 * @author Author
 */
public class HollowborneAnimations {
	public static final AnimationDefinition DEATH = AnimationDefinition.Builder.withLength(2.0F).looping()
			.addAnimation("armml1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -100.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -100.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -42.5F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -42.5F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -27.5F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("armmr1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 52.5F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 52.5F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 47.5F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 47.5F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 37.5F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("armmrb1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.25F, KeyframeAnimations.degreeVec(-62.3432F, -6.3686F, -4.6276F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5F, KeyframeAnimations.degreeVec(-62.3432F, -6.3686F, -4.6276F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.0F, KeyframeAnimations.degreeVec(-62.3432F, -6.3686F, -4.6276F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.3333F, KeyframeAnimations.degreeVec(-37.8035F, -0.2611F, 3.3076F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("armmrb3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 35.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("armmlb1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-21.5561F, 6.608F, 16.2407F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-21.5561F, 6.608F, 16.2407F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.0F, KeyframeAnimations.degreeVec(-21.5561F, 6.608F, 16.2407F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.25F, KeyframeAnimations.degreeVec(-22.8271F, 1.9521F, 4.572F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("armmlb3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -52.5F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("subroot", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.25F, KeyframeAnimations.degreeVec(34.0724F, 8.5373F, -12.3796F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5833F, KeyframeAnimations.degreeVec(34.0724F, 8.5373F, -12.3796F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.8333F, KeyframeAnimations.degreeVec(34.8974F, 2.8654F, -4.0992F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.0F, KeyframeAnimations.degreeVec(34.8974F, 2.8654F, -4.0992F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.2083F, KeyframeAnimations.degreeVec(4.8974F, 2.8654F, -4.0992F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("subroot", new AnimationChannel(AnimationChannel.Targets.POSITION,
					new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -16.0F, 16.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, -16.0F, 16.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -16.0F, 16.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, -36.0F, 13.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.build();
}