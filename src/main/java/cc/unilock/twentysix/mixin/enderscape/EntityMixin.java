package cc.unilock.twentysix.mixin.enderscape;

import net.minecraft.world.entity.Entity;
import net.penumbra.enderscape.registry.entity.EnderscapeAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Entity.class)
public class EntityMixin {
	@ModifyArg(
			method = "playCombinationStepSounds",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;playStepSound(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;FF)V"
			),
			index = 3
	)
	public float Enderscape$adjustCombinationStepSoundVolume(float original) {
		return Math.max(0.0F, original * (float) EnderscapeAttributes.getStealthMultiplier(Entity.class.cast(this)));
	}

	@ModifyArg(
			method = "playStepSound",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;playStepSound(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;FF)V"
			),
			index = 3
	)
	public float Enderscape$adjustStepSoundVolume(float original) {
		return Math.max(0.0F, original * (float) EnderscapeAttributes.getStealthMultiplier(Entity.class.cast(this)));
	}

	@ModifyArg(
			method = "playMuffledStepSound",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;playStepSound(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;FF)V"
			),
			index = 3
	)
	public float Enderscape$adjustMuffledStepSoundVolume(float original) {
		return Math.max(0.0F, original * (float) EnderscapeAttributes.getStealthMultiplier(Entity.class.cast(this)));
	}
}
