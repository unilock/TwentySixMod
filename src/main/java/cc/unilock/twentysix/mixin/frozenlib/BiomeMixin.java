package cc.unilock.twentysix.mixin.frozenlib;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.frozenblock.lib.worldgen.biome.impl.BiomeInterface;
import net.frozenblock.lib.worldgen.biome.impl.FrozenGrassColorModifier;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Biome.class)
public class BiomeMixin implements BiomeInterface {
	@Unique
	private FrozenGrassColorModifier frozenLib$frozenGrassColorModifier;

	@ModifyReturnValue(
			method = "getGrassColor",
			at = @At(
					value = "RETURN"
			)
	)
	public int frozenLib$modifyGrassColor(int original, double x, double y) {
		if (this.frozenLib$frozenGrassColorModifier != null) return this.frozenLib$frozenGrassColorModifier.modifyGrassColor(x, y, original);
		return original;
	}

	@Override
	public void frozenLib$setFrozenGrassColorModifier(FrozenGrassColorModifier frozenGrassColorModifier) {
		this.frozenLib$frozenGrassColorModifier = frozenGrassColorModifier;
	}

	@Override
	public FrozenGrassColorModifier frozenLib$getFrozenGrassColorModifier() {
		return this.frozenLib$frozenGrassColorModifier;
	}
}
