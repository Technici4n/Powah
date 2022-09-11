package owmii.powah.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import owmii.powah.Powah;
import owmii.powah.forge.compat.curios.CuriosCompat;
import owmii.powah.forge.data.DataEvents;

@Mod(Powah.MOD_ID)
public class PowahForge {
	public PowahForge() {
		var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		EventBuses.registerModEventBus(Powah.MOD_ID, modEventBus);

		Powah.init();
		modEventBus.addListener(DataEvents::gatherData);

		if (FMLEnvironment.dist.isClient()) {
			try {
				Class.forName("owmii.powah.forge.client.PowahForgeClient").getMethod("init").invoke(null);
			} catch (Exception exception) {
				throw new RuntimeException("Failed to run powah forge client init", exception);
			}
		}

		if (ModList.get().isLoaded("curios")) {
			CuriosCompat.init();
		}
	}
}
