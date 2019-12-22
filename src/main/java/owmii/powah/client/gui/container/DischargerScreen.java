package owmii.powah.client.gui.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import owmii.powah.Powah;
import owmii.powah.inventory.DischargerContainer;

public class DischargerScreen extends PowahScreen<DischargerContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/discharger.png");

    public DischargerScreen(DischargerContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    @Override
    protected ResourceLocation getSubBackGroundImage() {
        return GUI_TEXTURE;
    }
}
