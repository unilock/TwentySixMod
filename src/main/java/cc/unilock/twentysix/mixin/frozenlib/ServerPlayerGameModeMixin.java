package cc.unilock.twentysix.mixin.frozenlib;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.frozenblock.wilderwild.block.EchoGlassBlock;
import net.frozenblock.wilderwild.block.MesogleaBlock;
import net.frozenblock.wilderwild.block.impl.SnowloggingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin {
	@Shadow
	public abstract GameType getGameModeForPlayer();

	@Shadow
	@Final
	protected ServerPlayer player;

	@Shadow
	protected ServerLevel level;

	@WrapOperation(
			method = "destroyBlock",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/level/ServerPlayerGameMode;removeBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;ZLnet/minecraft/world/item/ItemStack;)Z",
					ordinal = 1
			)
	)
	public boolean wilderWild$destroyBlockB(
			ServerPlayerGameMode instance, BlockPos pos, BlockState adjustedState, boolean canHarvest, ItemStack toolStack, Operation<Boolean> original
	) {
		if (SnowloggingUtils.isSnowlogged(adjustedState)) {
			this.level.setBlockAndUpdate(pos, adjustedState.setValue(SnowloggingUtils.SNOW_LAYERS, 0));
			return true;
		}
		if (adjustedState.getBlock() instanceof MesogleaBlock) {
			this.level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			return true;
		}
		if (adjustedState.getBlock() instanceof EchoGlassBlock && EchoGlassBlock.canDamage(adjustedState) && !this.getGameModeForPlayer().isCreative()) {
			var silkTouch = this.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH);
			if (EnchantmentHelper.getItemEnchantmentLevel(silkTouch, this.player.getMainHandItem()) < 1) {
				EchoGlassBlock.setDamagedState(this.level, pos, adjustedState);
				return true;
			}
		}
		return original.call(instance, pos, adjustedState, canHarvest, toolStack);
	}
}
