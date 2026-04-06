package owmii.powah.client.screen.container;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Inventory;
import owmii.powah.block.solar.SolarBlockEntity;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.SolarMenu;
import owmii.powah.lib.client.screen.container.PowahBaseEnergyScreen;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.util.Util;

public class SolarScreen extends PowahBaseEnergyScreen<SolarBlockEntity, SolarMenu> {
    public SolarScreen(SolarMenu container, Inventory inv, Component title) {
        super(container, inv, title, Textures.WIDE_ENERGY);
    }

    @Override
    protected void drawBackground(GuiGraphicsExtractor guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(guiGraphics, partialTicks, mouseX, mouseY);
        Textures.WIDE_ENERGY_GAUGE.drawScalableW(guiGraphics, this.te.getEnergy().subSized(), this.leftPos + 6, this.topPos + 6);
    }

    @Override
    protected void drawForeground(GuiGraphicsExtractor gui, int mouseX, int mouseY) {
        super.drawForeground(gui, mouseX, mouseY);
        int a = ARGB.black(0.4f);
        Energy e = this.te.getEnergy();
        String s = Util.addCommas(e.getStored()) + "/" + Util.numFormat(e.getCapacity()) + " FE";
        gui.text(font, s, 12, 13, a, false);
        gui.text(font, Util.numFormat(e.getMaxExtract()) + " FE/t", 12, 27, a, false);
    }
}
