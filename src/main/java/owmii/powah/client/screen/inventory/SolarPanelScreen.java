package owmii.powah.client.screen.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.client.screen.EnergyScreenBase;
import owmii.powah.block.solarpanel.SolarPanelTile;
import owmii.powah.inventory.SolarPanelContainer;

@OnlyIn(Dist.CLIENT)
public class SolarPanelScreen extends EnergyScreenBase<SolarPanelTile, SolarPanelContainer> {
    public SolarPanelScreen(SolarPanelContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    @Override
    protected boolean showConfigButton() {
        return false;
    }
}
