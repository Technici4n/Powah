package owmii.powah.client.screen.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.client.screen.EnergyScreen;
import owmii.powah.block.playertransmitter.PlayerTransmitterTile;
import owmii.powah.inventory.PlayerTransmitterContainer;

@OnlyIn(Dist.CLIENT)
public class PlayerTransmitterScreen extends EnergyScreen<PlayerTransmitterTile, PlayerTransmitterContainer> {
    public PlayerTransmitterScreen(PlayerTransmitterContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    @Override
    protected void drawForeground(int mouseX, int mouseY) {
        super.drawForeground(mouseX, mouseY);
    }

    @Override
    protected boolean showConfigButton() {
        return false;
    }
}
