package cc.unilock.twentysix.mixin.wilderwild;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.frozenblock.wilderwild.block.MesogleaBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CommonHooks.class)
public class CommonHooksMixin {
	@WrapOperation(
			method = "onLivingBreathe",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z",
					ordinal = 0
			)
	)
	private static boolean wilderWild$isBubbleColumnOrMesogleaColumn(BlockState state, Object block, Operation<Boolean> operation) {
		return operation.call(state, block) || MesogleaBlock.hasBubbleColumn(state);
	}
}
