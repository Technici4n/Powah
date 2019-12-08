package zeroneye.powah.client.gui.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import zeroneye.powah.Powah;
import zeroneye.powah.inventory.EnergyHopperContainer;

public class EnergyHopperScreen extends PowahScreen<EnergyHopperContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/blank.png");

    public EnergyHopperScreen(EnergyHopperContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    @Override
    protected ResourceLocation getSubBackGroundImage() {
        return GUI_TEXTURE;
    }
}
