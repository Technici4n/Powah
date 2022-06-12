package owmii.powah.fabric;

import net.fabricmc.api.ModInitializer;
import owmii.powah.Powah;

public class PowahFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		Powah.init();
	}
}
