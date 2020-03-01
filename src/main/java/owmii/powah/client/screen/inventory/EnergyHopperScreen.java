package owmii.powah.client.screen.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.client.screen.EnergyScreen;
import owmii.powah.block.energyhopper.EnergyHopperTile;
import owmii.powah.inventory.EnergyHopperContainer;

@OnlyIn(Dist.CLIENT)
public class EnergyHopperScreen extends EnergyScreen<EnergyHopperTile, EnergyHopperContainer> {
    public EnergyHopperScreen(EnergyHopperContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }
}
