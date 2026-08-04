package cc.unilock.twentysix.mixin.enderscape;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
import net.penumbra.enderscape.manager.ClientsideDashJumpManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public class GuiMixin {
	@WrapOperation(
			method = "extractContextualInfoBarBackground",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/contextualbar/ContextualBarRenderer;extractBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V"
			)
	)
	public void Enderscape$stopRenderingBarBackground(ContextualBarRenderer instance, GuiGraphicsExtractor graphics, DeltaTracker tracker, Operation<Void> original) {
		if (ClientsideDashJumpManager.skipRenderingDashJumpBar()) {
			original.call(instance, graphics, tracker);
		} else {
			ClientsideDashJumpManager.extractDashJumpChargeBar(graphics);
		}
	}

	@WrapOperation(
			method = "extractContextualInfoBar",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/contextualbar/ContextualBarRenderer;extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V"
			)
	)
	public void Enderscape$stopRenderingBar(ContextualBarRenderer instance, GuiGraphicsExtractor graphics, DeltaTracker tracker, Operation<Void> original) {
		if (ClientsideDashJumpManager.skipRenderingDashJumpBar()) {
			original.call(instance, graphics, tracker);
		}
	}
}
