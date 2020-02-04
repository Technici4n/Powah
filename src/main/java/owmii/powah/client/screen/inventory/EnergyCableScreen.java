package owmii.powah.client.screen.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import owmii.lib.client.screen.EnergyScreenBase;
import owmii.powah.Powah;
import owmii.powah.block.cable.EnergyCableTile;
import owmii.powah.inventory.EnergyCableContainer;

public class EnergyCableScreen extends EnergyScreenBase<EnergyCableTile, EnergyCableContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/blank.png");

    public EnergyCableScreen(EnergyCableContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }
}
