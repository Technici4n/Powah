package owmii.powah.lib;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import owmii.powah.lib.network.Network;
import owmii.powah.lib.network.Packets;

public class Lollipop {
    public static final String MOD_ID = "lollipop";
    public static final Network NET = new Network(MOD_ID);
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void setup(FMLCommonSetupEvent event) {
        Packets.register();
    }
}
