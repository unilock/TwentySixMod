package cc.unilock.twentysix.mixin.minecraft;

import cc.unilock.twentysix.TwentySix;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Map;

@Mixin(MappedRegistry.class)
public class MappedRegistryMixin<T> {
	@Shadow @Nullable private Map<T, Holder.Reference<T>> unregisteredIntrusiveHolders;

	@Inject(method = "freeze", at = @At(value = "NEW", target = "(Ljava/lang/String;)Ljava/lang/IllegalStateException;", ordinal = 1))
	private void twentysix$logUnregisteredIntrusiveHolderFQCNs(CallbackInfoReturnable<Registry<T>> cir) {
		assert this.unregisteredIntrusiveHolders != null;
		for (Holder.Reference<T> reference : this.unregisteredIntrusiveHolders.values()) {
			TwentySix.LOGGER.info("UNREGISTERED INTRUSIVE HOLDER FQCN: {}", reference.value().getClass());
		}
	}
}
