package cc.unilock.twentysix.mixin.wilderwild;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.frozenblock.wilderwild.block.impl.SnowloggingUtils;
import net.frozenblock.wilderwild.config.WWEntityConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityFluidInteraction;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
	@WrapOperation(
			method = "spawnSprintParticle",
			at = @At(
					value = "NEW",
					target = "(Lnet/minecraft/core/particles/ParticleType;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/particles/BlockParticleOption;"
			)
	)
	public BlockParticleOption wilderWild$spawnSprintParticle(ParticleType<?> type, BlockState state, BlockPos pos, Operation<BlockParticleOption> original) {
		if (SnowloggingUtils.isSnowlogged(state)) state = SnowloggingUtils.getSnowEquivalent(state);
		return original.call(type, state, pos);
	}

	@WrapWithCondition(
			method = "updateFluidInteraction",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/EntityFluidInteraction;applyCurrentTo(Lnet/minecraft/world/entity/Entity;)V"
			)
	)
	public boolean wilderWild$stopWaterFromPushingWardens(EntityFluidInteraction instance, Entity entity, @Local(name = "inWater") boolean inWater) {
		if (inWater && entity instanceof Warden) {
			return WWEntityConfig.WARDEN_SWIMS.get();
		}
		return true;
	}
}
