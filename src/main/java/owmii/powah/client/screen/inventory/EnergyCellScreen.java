package owmii.powah.client.screen.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.client.screen.EnergyScreen;
import owmii.powah.block.energycell.EnergyCellTile;
import owmii.powah.inventory.EnergyCellContainer;

@OnlyIn(Dist.CLIENT)
public class EnergyCellScreen extends EnergyScreen<EnergyCellTile, EnergyCellContainer> {
    public EnergyCellScreen(EnergyCellContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }
}
