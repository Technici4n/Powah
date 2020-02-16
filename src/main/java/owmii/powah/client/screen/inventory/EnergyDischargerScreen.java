package owmii.powah.client.screen.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.client.screen.EnergyScreenBase;
import owmii.powah.block.energydischarger.EnergyDischargerTile;
import owmii.powah.inventory.EnergyDischargerContainer;

@OnlyIn(Dist.CLIENT)
public class EnergyDischargerScreen extends EnergyScreenBase<EnergyDischargerTile, EnergyDischargerContainer> {
    public EnergyDischargerScreen(EnergyDischargerContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }
}
