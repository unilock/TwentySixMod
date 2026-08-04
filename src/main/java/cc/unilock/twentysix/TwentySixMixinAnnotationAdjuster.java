package cc.unilock.twentysix;

import com.bawnorton.mixinsquared.adjuster.tools.AdjustableAnnotationNode;
import com.bawnorton.mixinsquared.api.MixinAnnotationAdjuster;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

public class TwentySixMixinAnnotationAdjuster implements MixinAnnotationAdjuster {
	@Override
	public AdjustableAnnotationNode adjust(List<String> targetClassNames, String mixinClassName, MethodNode handlerNode, AdjustableAnnotationNode annotationNode) {
		if ("net.penumbra.enderscape.mixin.entity.EntityMixin".equals(mixinClassName) && ("Enderscape$adjustCombinationStepSoundVolume".equals(handlerNode.name) || "Enderscape$adjustStepSoundVolume".equals(handlerNode.name) || "Enderscape$adjustMuffledStepSoundVolume".equals(handlerNode.name))) {
			return null;
		}

		if ("net.penumbra.enderscape.mixin.client.renderer.GuiMixin".equals(mixinClassName) && ("Enderscape$stopRenderingBarBackground".equals(handlerNode.name) || "Enderscape$stopRenderingBar".equals(handlerNode.name))) {
			return null;
		}

		return annotationNode;
	}
}
