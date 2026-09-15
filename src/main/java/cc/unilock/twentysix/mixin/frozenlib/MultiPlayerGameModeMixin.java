package cc.unilock.twentysix.mixin.frozenlib;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.frozenblock.wilderwild.block.EchoGlassBlock;
import net.frozenblock.wilderwild.block.MesogleaBlock;
import net.frozenblock.wilderwild.block.impl.SnowloggingUtils;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
	@Shadow
	public abstract GameType getPlayerMode();

	@WrapOperation(
			method = "destroyBlock",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;onDestroyedByPlayer(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;ZLnet/minecraft/world/level/material/FluidState;)Z"
			)
	)
	public boolean wilderWild$destroyBlockB(
			BlockState destroyedState, Level level, BlockPos pos, Player player, ItemStack toolStack, boolean willHarvest, FluidState fluid, Operation<Boolean> original
	) {
		if (SnowloggingUtils.isSnowlogged(destroyedState)) {
			original.call(destroyedState.setValue(SnowloggingUtils.SNOW_LAYERS, 0), level, pos, player, toolStack, willHarvest, fluid);
			return true;
		}
		if (destroyedState.getBlock() instanceof MesogleaBlock) {
			original.call(Blocks.AIR.defaultBlockState(), level, pos, player, toolStack, willHarvest, fluid);
			return true;
		}
		if (destroyedState.getBlock() instanceof EchoGlassBlock && EchoGlassBlock.canDamage(destroyedState) && !this.getPlayerMode().isCreative()) {
			final var silkTouch = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH);
			if (EnchantmentHelper.getItemEnchantmentLevel(silkTouch, toolStack) < 1) EchoGlassBlock.setDamagedState(level, pos, destroyedState);
			return true;
		}
		return original.call(destroyedState, level, pos, player, toolStack, willHarvest, fluid);
	}
}
