package owmii.powah.client.screen.container;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import owmii.powah.block.hopper.EnergyHopperTile;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.EnergyHopperContainer;
import owmii.powah.lib.client.screen.container.AbstractEnergyScreen;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.util.Util;

public class EnergyHopperScreen extends AbstractEnergyScreen<EnergyHopperTile, EnergyHopperContainer> {
    public EnergyHopperScreen(EnergyHopperContainer container, Inventory inv, Component title) {
        super(container, inv, title, Textures.WIDE_ENERGY);
    }

    @Override
    protected void drawBackground(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(guiGraphics, partialTicks, mouseX, mouseY);
        Textures.WIDE_ENERGY_GAUGE.drawScalableW(guiGraphics, this.te.getEnergy().subSized(), this.leftPos + 6, this.topPos + 6);
    }

    @Override
    protected void drawForeground(GuiGraphics gui, int mouseX, int mouseY) {
        super.drawForeground(gui, mouseX, mouseY);
        RenderSystem.enableBlend();
        int a = (int) (255.0D * 1.0D * 0.4D) << 24;
        Energy e = this.te.getEnergy();
        String s = Util.addCommas(e.getStored()) + "/" + Util.numFormat(e.getCapacity()) + " FE";
        gui.drawString(font, s, 12, 13, a, false);
        gui.drawString(font, Util.numFormat(e.getMaxExtract()) + " FE/t", 12, 27, a, false);
        RenderSystem.disableBlend();
    }
}
