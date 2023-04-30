package owmii.powah.client.screen.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import owmii.powah.block.transmitter.PlayerTransmitterTile;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.PlayerTransmitterContainer;
import owmii.powah.lib.client.screen.container.AbstractEnergyScreen;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.util.Util;

public class PlayerTransmitterScreen extends AbstractEnergyScreen<PlayerTransmitterTile, PlayerTransmitterContainer> {
    public PlayerTransmitterScreen(PlayerTransmitterContainer container, Inventory inv, Component title) {
        super(container, inv, title, Textures.PLAYER_TRANSMITTER);
    }

    @Override
    protected void drawBackground(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Textures.PLAYER_TRANSMITTER_GAUGE.drawScalableW(matrix, this.te.getEnergy().subSized(), this.leftPos + 31, this.topPos + 6);
        if (!this.te.getInventory().getStackInSlot(0).isEmpty())
            Textures.PLAYER_TRANSMITTER_ON.draw(matrix, this.leftPos + 9, this.topPos + 5);
    }

    @Override
    protected void drawForeground(PoseStack matrix, int mouseX, int mouseY) {
        super.drawForeground(matrix, mouseX, mouseY);
        RenderSystem.enableBlend();
        int a = (int) (255.0D * 1.0D * 0.4D) << 24;
        Energy e = this.te.getEnergy();
        String s = Util.addCommas(e.getStored()) + "/" + Util.numFormat(e.getCapacity()) + " FE";
        this.font.draw(matrix, s, 38, 13.0F, a);
        this.font.draw(matrix, Util.numFormat(e.getMaxExtract()) + " FE/t", 38, 27.0F, a);
        RenderSystem.disableBlend();
    }
}
