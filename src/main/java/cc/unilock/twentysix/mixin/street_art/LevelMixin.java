package cc.unilock.twentysix.mixin.street_art;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.streetart.AttachmentTypes;
import com.streetart.managers.GServerChunkManager;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public abstract class LevelMixin {
	@Shadow
	public abstract BlockState getBlockState(final BlockPos pos);
	@Shadow
	public abstract boolean isClientSide();

	@Inject(
			method = "markAndNotifyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/chunk/LevelChunk;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;II)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"
			)
	)
	private void streetart$storeOldVoxelShape(final CallbackInfo ci, @Local(argsOnly = true, name = "pos") BlockPos pos, @Share("oldVoxelShape") final LocalRef<VoxelShape> oldVoxelShape) {
		if (!this.isClientSide()) {
			oldVoxelShape.set(this.getBlockState(pos).getShape((Level) (Object) this, pos));
		}
	}

	@Inject(
			method = "markAndNotifyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/chunk/LevelChunk;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;II)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;setBlocksDirty(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;)V"
			)
	)
	private void streetart$compareVoxelShape(final CallbackInfo ci, @Local(argsOnly = true, name = "pos") final BlockPos pos, @Local(name = "newState") final BlockState newState, @Share("oldVoxelShape") final LocalRef<VoxelShape> oldVoxelShape) {
		if ((Object)this instanceof final ServerLevel serverLevel) {
			final VoxelShape newShape = newState.getShape(serverLevel, pos);
			if (newShape != oldVoxelShape.get()) {
				final ChunkAccess access = serverLevel.getChunk(pos);
				final GServerChunkManager manager = ((AttachmentTarget) access).getAttached(AttachmentTypes.CHUNK_MANAGER);

				if (manager != null) {
					manager.markForRemoval(pos);
					for (final Direction dir : Direction.values()) {
						if (Block.isShapeFullBlock(newShape.getFaceShape(dir))) {
							final GServerChunkManager manager2 = ((AttachmentTarget) serverLevel.getChunk(pos.relative(dir))).getAttached(AttachmentTypes.CHUNK_MANAGER);
							if (manager2 != null) {
								manager2.markSmothered(pos.relative(dir), dir.getOpposite());
							}
						}
					}
				}
			}
		}
	}
}
