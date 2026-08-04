package cc.unilock.twentysix;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.List;

public class TwentySixMixinCanceller implements MixinCanceller {
	private static final HashSet<String> SHOULD_CANCEL = Sets.newHashSet(
//			"com.terraformersmc.cinderscapes.mixin.MixinAlterGroundTreeDecorator"
	);

	@Override
	public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
		return SHOULD_CANCEL.contains(mixinClassName);
	}
}
