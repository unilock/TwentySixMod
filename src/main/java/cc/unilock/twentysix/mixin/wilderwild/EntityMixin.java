package cc.unilock.twentysix.mixin.wilderwild;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.frozenblock.wilderwild.config.WWEntityConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityFluidInteraction;
import net.minecraft.world.entity.monster.warden.Warden;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
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
