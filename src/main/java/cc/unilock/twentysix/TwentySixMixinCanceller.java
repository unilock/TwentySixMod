package cc.unilock.twentysix;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.List;

public class TwentySixMixinCanceller implements MixinCanceller {
	private static final HashSet<String> SHOULD_CANCEL = Sets.newHashSet(
//			"com.example.mod.mixin.MixinMinecraft"
	);

	@Override
	public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
		return SHOULD_CANCEL.contains(mixinClassName);
	}
}
