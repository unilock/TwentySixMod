package cc.unilock.twentysix.mixin.frozenlib;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.frozenblock.lib.block.api.friction.BlockFrictionAPI;
import net.frozenblock.lib.block.api.friction.FrictionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@WrapOperation(
			method = "travelInAir",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;getFriction(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F"
			)
	)
	private float frozenLib$applyFrictionApi(
			BlockState instance, LevelReader levelReader, BlockPos blockPos, Entity entity, Operation<Float> original
	) {
		final FrictionContext frictionContext = new FrictionContext(
				(Level) levelReader,
				(LivingEntity) entity,
				instance,
				original.call(instance, levelReader, blockPos, entity)
		);
		BlockFrictionAPI.MODIFICATIONS.invoker().modifyFriction(frictionContext);

		return frictionContext.friction;
	}
}
