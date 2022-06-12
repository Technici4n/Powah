package owmii.powah.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import owmii.powah.Powah;

@Mod(Powah.MOD_ID)
public class PowahForge {
	public PowahForge() {
		EventBuses.registerModEventBus(Powah.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

		Powah.init();

		if (FMLEnvironment.dist.isClient()) {
			try {
				Class.forName("owmii.powah.forge.client.PowahForgeClient").getMethod("init").invoke(null);
			} catch (Exception exception) {
				throw new RuntimeException("Failed to run powah forge client init", exception);
			}
		}
	}
}
