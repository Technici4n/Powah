package owmii.powah;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import owmii.powah.network.Packets;

import static owmii.lib.Lollipop.addModListener;

@Mod(Powah.MOD_ID)
public class Powah {
    public static final String MOD_ID = "powah";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public Powah() {
        addModListener(this::commonSetup);
        addModListener(this::clientSetup);
        addModListener(this::loadComplete);
    }

    void commonSetup(FMLCommonSetupEvent event) {
        Packets.register();
    }

    void clientSetup(FMLClientSetupEvent event) {
    }

    void loadComplete(FMLLoadCompleteEvent event) {

    }
}