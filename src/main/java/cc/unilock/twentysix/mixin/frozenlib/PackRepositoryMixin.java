package cc.unilock.twentysix.mixin.frozenlib;

import net.frozenblock.lib.resource_pack.impl.client.PackRepositoryInterface;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PackRepository.class)
public abstract class PackRepositoryMixin implements PackRepositoryInterface {
	@Shadow
	public abstract void addPackFinder(RepositorySource packFinder);

	@Unique
	public void frozenLib$addRepositorySource(RepositorySource source) {
		this.addPackFinder(source);
	}
}
