package owmii.powah.client.handler;

import dev.architectury.event.events.client.ClientTextureStitchEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import owmii.powah.Powah;

public class TextureHandler {
    public static final ResourceLocation FURNATOR_LIT = new ResourceLocation(Powah.MOD_ID, "block/furnator_lit");

    public static void register() {
        ClientTextureStitchEvent.PRE.register((atlas, adder) -> {
            if (atlas.location().equals(InventoryMenu.BLOCK_ATLAS)) {
                adder.accept(FURNATOR_LIT);
            }
        });
    }
}
