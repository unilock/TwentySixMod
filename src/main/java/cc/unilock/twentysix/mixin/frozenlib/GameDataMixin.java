package cc.unilock.twentysix.mixin.frozenlib;

import net.frozenblock.lib.event.api.RegistryFreezeEvents;
import net.neoforged.neoforge.registries.GameData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameData.class)
public class GameDataMixin {
	@Inject(method = "freezeData", at = @At("HEAD"))
	private static void frozenLib$freezeBuiltinsStart(CallbackInfo ci) {
		RegistryFreezeEvents.START_REGISTRY_FREEZE.invoker().onStartRegistryFreeze(null, true);
	}

	@Inject(method = "freezeData", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/registries/RegistryManager;takeFrozenSnapshot()V"))
	private static void frozenLib$freezeBuiltinsEnd(CallbackInfo ci) {
		RegistryFreezeEvents.END_REGISTRY_FREEZE.invoker().onEndRegistryFreeze(null, true);
	}
}
