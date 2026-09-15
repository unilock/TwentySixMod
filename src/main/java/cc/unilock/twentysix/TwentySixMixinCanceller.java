package cc.unilock.twentysix;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.List;

public class TwentySixMixinCanceller implements MixinCanceller {
	private static final HashSet<String> SHOULD_CANCEL = Sets.newHashSet(
			"com.streetart.mixin.LevelMixin",
			"net.frozenblock.lib.block.mixin.friction.LivingEntityMixin",
			"net.frozenblock.lib.block.mixin.storage.hopper.HopperBlockEntityMixin",
			"net.frozenblock.lib.item.mixin.shovel.ShovelItemMixin",
			"net.frozenblock.trailiertales.mixin.common.coffin.ItemStackMixin",
			"net.frozenblock.wilderwild.mixin.warden.EntityMixin",
			"org.quiltmc.qsl.frozenblock.core.registry.mixin.MappedRegistryMixin",
			"org.quiltmc.qsl.frozenblock.core.registry.mixin.RegistryDataLoaderMixin",

			// TODO
			"net.frozenblock.wilderwild.mixin.entity.penguin.BlocksMixin"
	);

	@Override
	public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
		return SHOULD_CANCEL.contains(mixinClassName);
	}
}
