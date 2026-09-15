package cc.unilock.twentysix.mixin.frozenlib;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryLoadTask;
import net.minecraft.resources.ResourceKey;
import org.quiltmc.qsl.frozenblock.core.registry.api.event.RegistryEvents;
import org.quiltmc.qsl.frozenblock.core.registry.impl.DynamicRegistryManagerSetupContextImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderMixin {
	@Inject(
			method = "lambda$load$0",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/resources/RegistryDataLoader;createContext(Ljava/util/List;Ljava/util/List;)Lnet/minecraft/resources/RegistryOps$RegistryInfoLookup;",
					ordinal = 0,
					shift = At.Shift.AFTER
			)
	)
	private static void onDynamicSetup(
			List<RegistryDataLoader.RegistryData<?>> registriesToLoad,
			RegistryDataLoader.LoaderFactory loaderFactory,
			List<HolderLookup.RegistryLookup<?>> contextRegistries,
			Executor executor,
			boolean fromResources,
			CallbackInfoReturnable<CompletableFuture<?>> cir,
			@Local(name = "loadTasks") List<RegistryLoadTask<?>> loadTasks
	) {
		RegistryEvents.DYNAMIC_REGISTRY_SETUP.invoker().onDynamicRegistrySetup(
				new DynamicRegistryManagerSetupContextImpl(loadTasks.stream().map(task -> task.registry))
		);
	}

	@Inject(
			method = "lambda$load$2",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;stream()Ljava/util/stream/Stream;",
					ordinal = 0
			)
	)
	private static void onDynamicLoaded(
			boolean fromResources,
			List<RegistryLoadTask<?>> loadTasks,
			Map<ResourceKey<?>, Exception> loadingErrors,
			Void ignored,
			CallbackInfoReturnable<RegistryAccess.Frozen> cir
	) {
		RegistryEvents.DYNAMIC_REGISTRY_LOADED.invoker().onDynamicRegistryLoaded(
				new DynamicRegistryManagerSetupContextImpl(loadTasks.stream().map(task -> task.registry))
		);
	}
}
