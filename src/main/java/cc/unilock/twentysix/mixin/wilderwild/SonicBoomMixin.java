package cc.unilock.twentysix.mixin.wilderwild;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.frozenblock.wilderwild.block.EchoGlassBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

@Mixin(SonicBoom.class)
public class SonicBoomMixin {
	@Unique
	private static final MethodHandle IS_OCCLUDED;
	static {
		try {
			IS_OCCLUDED = MethodHandles.privateLookupIn(SonicBoom.class, MethodHandles.lookup()).findStatic(SonicBoom.class, "wilderWild$isOccluded", MethodType.methodType(BlockPos.class, Level.class, Vec3.class, Vec3.class));
		} catch (Throwable e) {
			throw new RuntimeException("Failed to find MethodHandle for SonicBoom#wilderWild$isOccluded");
		}
	}
	
	@Inject(
			method = "lambda$tick$2",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/util/Mth;floor(D)I"
			)
	)
	private static void wilderWild$tick(
			Warden body, ServerLevel level, LivingEntity target, CallbackInfo ci,
			@Local(name = "source") Vec3 source,
			@Local(name = "delta") Vec3 delta,
			@Local(name = "normalize") Vec3 normalize,
			@Share("source") LocalRef<Vec3> sourceRef,
			@Share("delta") LocalRef<Vec3> deltaRef,
			@Share("normalize") LocalRef<Vec3> normalizeRef
	) {
		sourceRef.set(source);
		deltaRef.set(delta);
		normalizeRef.set(normalize);
	}
	
	@Inject(
			method = "lambda$tick$2",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"
			),
			cancellable = true
	)
	private static void wilderWild$tick(
			Warden warden, ServerLevel level, LivingEntity entity, CallbackInfo info,
			@Share("source") LocalRef<Vec3> sourceRef,
			@Share("delta") LocalRef<Vec3> deltaRef,
			@Share("normalize") LocalRef<Vec3> normalizeRef
	) {
		Vec3 source = sourceRef.get();
		Vec3 delta = deltaRef.get();
		Vec3 normalize = normalizeRef.get();
		for (int i = 1; i < Mth.floor(delta.length()) + 7; ++i) {
			final Vec3 vec34 = source.add(normalize.scale(i));
			BlockPos hitPos; try { hitPos = (BlockPos) IS_OCCLUDED.invoke(level, source, vec34); } catch (Throwable e) { throw new RuntimeException("Failed to invoke MethodHandle for SonicBoom#wilderWild$isOccluded"); }
			if (hitPos == null) continue;

			i = Mth.floor(delta.length()) + 10;
			info.cancel();
			final BlockState hitState = level.getBlockState(hitPos);
			if (hitState.getBlock() instanceof EchoGlassBlock) EchoGlassBlock.damage(level, hitPos, hitState, false);
		}
	}
}
