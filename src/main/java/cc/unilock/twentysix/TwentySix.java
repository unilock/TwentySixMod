package cc.unilock.twentysix;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(TwentySix.MOD_ID)
public class TwentySix {
    public static final String MOD_ID = "twentysix";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TwentySix() {
        LOGGER.info("unilock was here");
    }
}
