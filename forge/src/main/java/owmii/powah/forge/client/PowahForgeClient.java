package owmii.powah.forge.client;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import owmii.powah.client.PowahClient;

public class PowahForgeClient {
	public static void init() {
		var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		modEventBus.addListener(PowahForgeClient::clientSetup);
	}

	public static void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(PowahClient::clientSetup);
	}
}
