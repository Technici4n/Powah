package owmii.powah.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import owmii.powah.client.PowahClient;

public class PowahFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		PowahClient.init();
		PowahClient.clientSetup();
	}
}
