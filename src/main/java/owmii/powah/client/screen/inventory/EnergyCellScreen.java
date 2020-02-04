package owmii.powah.client.screen.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import owmii.lib.client.screen.EnergyScreenBase;
import owmii.powah.Powah;
import owmii.powah.block.energycell.EnergyCellTile;
import owmii.powah.inventory.EnergyCellContainer;

public class EnergyCellScreen extends EnergyScreenBase<EnergyCellTile, EnergyCellContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/blank.png");

    public EnergyCellScreen(final EnergyCellContainer container, final PlayerInventory playerInventory, final ITextComponent name) {
        super(container, playerInventory, name);
    }

}
