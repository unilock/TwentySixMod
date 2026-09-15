package cc.unilock.twentysix.mixin.frozenlib;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.frozenblock.lib.item.api.shovel.ShovelApi;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShovelItem.class)
public class ShovelItemMixin {
	@ModifyExpressionValue(
			method = "useOn",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/context/UseOnContext;getClickedFace()Lnet/minecraft/core/Direction;",
					ordinal = 0
			)
	)
	public Direction frozenlib$startShovelBehavior(
			Direction original,
			@Local Level level, @Local BlockPos pos, @Local BlockState state,
			@Share("frozenLib$isCustomBehavior") LocalBooleanRef isCustomBehavior,
			@Share("frozenLib$direction") LocalRef<Direction> direction,
			@Share("frozenLib$shovelBehavior") LocalRef<ShovelApi.ShovelBehavior> shovelBehavior
	) {
		direction.set(original);
		isCustomBehavior.set(false);

		final ShovelApi.ShovelBehavior possibleBehavior = ShovelApi.get(state.getBlock());
		if (possibleBehavior == null || !possibleBehavior.meetsRequirements(level, pos, original, state)) return original;

		isCustomBehavior.set(true);
		shovelBehavior.set(possibleBehavior);
		return Direction.UP;
	}

	@ModifyExpressionValue(
			method = "useOn",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;getToolModifiedState(Lnet/minecraft/world/item/context/UseOnContext;Lnet/neoforged/neoforge/common/ItemAbility;Z)Lnet/minecraft/world/level/block/state/BlockState;",
					ordinal = 0
			)
	)
	public BlockState frozenlib$removeOtherBehaviorsA(
			BlockState original, @Share("frozenLib$isCustomBehavior") LocalBooleanRef isCustomBehavior
	) {
		if (isCustomBehavior.get()) return null;
		return original;
	}

	@ModifyExpressionValue(
			method = "useOn",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;getToolModifiedState(Lnet/minecraft/world/item/context/UseOnContext;Lnet/neoforged/neoforge/common/ItemAbility;Z)Lnet/minecraft/world/level/block/state/BlockState;",
					ordinal = 1
			)
	)
	public BlockState frozenlib$removeOtherBehaviorsB(
			BlockState original, @Share("frozenLib$isCustomBehavior") LocalBooleanRef isCustomBehavior
	) {
		if (isCustomBehavior.get()) return null;
		return original;
	}

	@Inject(
			method = "useOn",
			at = @At(
					value = "JUMP",
					opcode = Opcodes.IFNULL,
					ordinal = 0
			)
	)
	public void frozenlib$runShovelBehavior(
			UseOnContext context, CallbackInfoReturnable<InteractionResult> info,
			@Local Level level,
			@Local BlockPos pos,
			@Local(ordinal = 0) BlockState state,
			@Local(ordinal = 2) LocalRef<BlockState> state3,
			@Share("frozenLib$direction") LocalRef<Direction> direction,
			@Share("frozenLib$shovelBehavior") LocalRef<ShovelApi.ShovelBehavior> shovelBehavior
	) {
		final ShovelApi.ShovelBehavior runBehavior = shovelBehavior.get();
		if (runBehavior == null) return;

		final BlockState outputState = runBehavior.getOutputBlockState(state);
		runBehavior.onSuccess(level, pos, direction.get(), outputState, state);
		state3.set(outputState);
	}
}
