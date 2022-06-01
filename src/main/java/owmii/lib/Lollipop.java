package owmii.lib;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import owmii.lib.api.IClient;
import owmii.lib.api.IMod;
import owmii.lib.network.Network;
import owmii.lib.network.Packets;

import javax.annotation.Nullable;

public class Lollipop {
    public static final String MOD_ID = "lollipop";
    public static final Network NET = new Network(MOD_ID);
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void setup(FMLCommonSetupEvent event) {
        Packets.register();
    }
}
