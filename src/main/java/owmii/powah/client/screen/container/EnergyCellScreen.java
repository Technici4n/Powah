package owmii.powah.client.screen.container;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import owmii.powah.block.energycell.EnergyCellTile;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.EnergyCellContainer;
import owmii.powah.lib.client.screen.container.AbstractEnergyScreen;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.util.Util;

public class EnergyCellScreen extends AbstractEnergyScreen<EnergyCellTile, EnergyCellContainer> {
    public EnergyCellScreen(EnergyCellContainer container, Inventory inv, Component title) {
        super(container, inv, title, Textures.ENERGY_CELL);
    }

    @Override
    protected void drawBackground(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(guiGraphics, partialTicks, mouseX, mouseY);
        Textures.ENERGY_CELL_GAUGE.drawScalableW(guiGraphics, this.te.getEnergy().subSized(), this.leftPos + 31, this.topPos + 6);
    }

    @Override
    protected void drawForeground(GuiGraphics gui, int mouseX, int mouseY) {
        super.drawForeground(gui, mouseX, mouseY);
        RenderSystem.enableBlend();
        int a = (int) (255.0D * 1.0D * 0.4D) << 24;
        Energy e = this.te.getEnergy();
        String s = Util.addCommas(e.getStored()) + "/" + Util.numFormat(e.getCapacity()) + " FE";
        gui.drawString(font, this.te.isCreative() ? I18n.get("info.powah.unlimited") : s, 38, 13, a, false);
        gui.drawString(font, Util.numFormat(e.getMaxExtract()) + " FE/t", 38, 27, a, false);
        RenderSystem.disableBlend();
    }
}
