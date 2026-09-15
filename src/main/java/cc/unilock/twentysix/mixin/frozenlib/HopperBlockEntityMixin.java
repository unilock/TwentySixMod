package cc.unilock.twentysix.mixin.frozenlib;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.frozenblock.lib.block.storage.api.hopper.HopperApi;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {
	@ModifyExpressionValue(
			method = "ejectItems",
			at = @At(
					value = "INVOKE",
					target = "Lnet/neoforged/neoforge/transfer/item/ContainerOrHandler;container()Lnet/minecraft/world/Container;",
					ordinal = 0
			)
	)
	private static Container frozenLib$preventEjectionA(
			Container original,
			@Share("frozenLib$container") LocalRef<Container> containerRef
	) {
		containerRef.set(original);
		return original;
	}

	@Inject(
			method = "ejectItems",
			at = @At(
					value = "INVOKE",
					target = "Lnet/neoforged/neoforge/transfer/item/ContainerOrHandler;container()Lnet/minecraft/world/Container;",
					ordinal = 0,
					shift = At.Shift.AFTER
			),
			cancellable = true
	)
	private static void frozenLib$preventEjectionB(
			Level level, BlockPos pos, HopperBlockEntity hopper, CallbackInfoReturnable<Boolean> info,
			@Share("frozenLib$container") LocalRef<Container> containerRef
	) {
		if (HopperApi.isContainerBlacklisted(containerRef.get())) info.setReturnValue(false);
	}

	@ModifyExpressionValue(
			method = "suckInItems",
			at = @At(
					value = "INVOKE",
					target = "Lnet/neoforged/neoforge/transfer/item/ContainerOrHandler;container()Lnet/minecraft/world/Container;",
					ordinal = 0
			)
	)
	private static Container frozenLib$preventInsertionA(
			Container original,
			@Share("frozenLib$container") LocalRef<Container> containerRef
	) {
		containerRef.set(original);
		return original;
	}

	@Inject(
			method = "suckInItems",
			at = @At(
					value = "INVOKE",
					target = "Lnet/neoforged/neoforge/transfer/item/ContainerOrHandler;container()Lnet/minecraft/world/Container;",
					ordinal = 0,
					shift = At.Shift.AFTER
			),
			cancellable = true
	)
	private static void frozenLib$preventInsertionB(
			Level level, Hopper hopper, CallbackInfoReturnable<Boolean> info,
			@Share("frozenLib$container") LocalRef<Container> containerRef
	) {
		if (HopperApi.isContainerBlacklisted(containerRef.get())) info.setReturnValue(false);
	}
}
