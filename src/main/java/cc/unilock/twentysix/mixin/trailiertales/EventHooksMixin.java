package cc.unilock.twentysix.mixin.trailiertales;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.frozenblock.trailiertales.config.TTEntityConfig;
import net.frozenblock.trailiertales.entity.Apparition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.neoforge.event.EventHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EventHooks.class)
public class EventHooksMixin {
	@WrapOperation(
			method = "canEntityGrief",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/gamerules/GameRules;get(Lnet/minecraft/world/level/gamerules/GameRule;)Ljava/lang/Object;"
			)
	)
	private static Object trailierTales$ignoreMobGriefingForApparitionIfPossible(GameRules instance, GameRule<?> gameRule, Operation<?> original, @Local(argsOnly = true, name = "entity") Entity entity) {
		final Object returnValue = original.call(instance, gameRule);
		if (gameRule != GameRules.MOB_GRIEFING || !(returnValue instanceof Boolean bool)) return returnValue;
		if (bool) return true;
		if (entity instanceof Apparition) return TTEntityConfig.APPARITION_IGNORES_MOB_GRIEFING.get();
		return returnValue;
	}
}
