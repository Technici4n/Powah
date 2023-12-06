package owmii.powah.client.screen.container;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import owmii.powah.block.transmitter.PlayerTransmitterTile;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.PlayerTransmitterContainer;
import owmii.powah.lib.client.screen.container.AbstractEnergyScreen;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.util.Util;

public class PlayerTransmitterScreen extends AbstractEnergyScreen<PlayerTransmitterTile, PlayerTransmitterContainer> {
    public PlayerTransmitterScreen(PlayerTransmitterContainer container, Inventory inv, Component title) {
        super(container, inv, title, Textures.PLAYER_TRANSMITTER);
    }

    @Override
    protected void drawBackground(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(guiGraphics, partialTicks, mouseX, mouseY);
        Textures.PLAYER_TRANSMITTER_GAUGE.drawScalableW(guiGraphics, this.te.getEnergy().subSized(), this.leftPos + 31, this.topPos + 6);
        if (!this.te.getInventory().getStackInSlot(0).isEmpty())
            Textures.PLAYER_TRANSMITTER_ON.draw(guiGraphics, this.leftPos + 9, this.topPos + 5);
    }

    @Override
    protected void drawForeground(GuiGraphics gui, int mouseX, int mouseY) {
        super.drawForeground(gui, mouseX, mouseY);
        RenderSystem.enableBlend();
        int a = (int) (255.0D * 1.0D * 0.4D) << 24;
        Energy e = this.te.getEnergy();
        String s = Util.addCommas(e.getStored()) + "/" + Util.numFormat(e.getCapacity()) + " FE";
        gui.drawString(font, s, 38, 13, a, false);
        gui.drawString(font, Util.numFormat(e.getMaxExtract()) + " FE/t", 38, 27, a, false);
        RenderSystem.disableBlend();
    }
}
