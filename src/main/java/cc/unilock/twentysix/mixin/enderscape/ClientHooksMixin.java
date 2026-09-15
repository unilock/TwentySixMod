package cc.unilock.twentysix.mixin.enderscape;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.ClientHooks;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.renderer.level.EnderscapeSkybox;
import net.penumbra.enderscape.renderer.value.EndFlashParameters;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static net.penumbra.enderscape.renderer.level.EnderscapeSkybox.scaleWithoutOverflow;

@Mixin(ClientHooks.class)
public class ClientHooksMixin {
	@ModifyArgs(
			method = "getFogColor",
			at = @At(
					value = "INVOKE",
					target = "Lorg/joml/Vector4f;set(FFFF)Lorg/joml/Vector4f;"
			)
	)
	private static void Enderscape$getBrightnessDependentFogColor(Args args) {
		ClientLevel level = Minecraft.getInstance().level;
		Vector3f original = new Vector3f(args.get(0), args.get(1), args.get(2));

		if (level != null && level.dimension() == Level.END) {
			if (EnderscapeConfig.getInstance().skyboxUpdateEnabled) {
				float gamma = EnderscapeSkybox.gammaFactor();
				original = scaleWithoutOverflow(original, gamma);
			}

			float brightness = EndFlashParameters.skyboxBrightness();
			original = scaleWithoutOverflow(original, brightness);

			args.set(0, original.x());
			args.set(1, original.y());
			args.set(2, original.z());
		}
	}
}
