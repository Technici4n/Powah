package owmii.powah.client.screen.container;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Inventory;
import owmii.powah.block.energycell.EnergyCellBlockEntity;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.EnergyCellMenu;
import owmii.powah.lib.client.screen.container.PowahBaseEnergyScreen;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.util.Util;

public class EnergyCellScreen extends PowahBaseEnergyScreen<EnergyCellBlockEntity, EnergyCellMenu> {
    public EnergyCellScreen(EnergyCellMenu container, Inventory inv, Component title) {
        super(container, inv, title, Textures.ENERGY_CELL);
    }

    @Override
    protected void drawBackground(GuiGraphicsExtractor guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(guiGraphics, partialTicks, mouseX, mouseY);
        Textures.ENERGY_CELL_GAUGE.drawScalableW(guiGraphics, this.te.getEnergy().subSized(), this.leftPos + 31, this.topPos + 6);
    }

    @Override
    protected void drawForeground(GuiGraphicsExtractor gui, int mouseX, int mouseY) {
        super.drawForeground(gui, mouseX, mouseY);
        int a = ARGB.black(0.4f);
        Energy e = this.te.getEnergy();
        String s = Util.addCommas(e.getStored()) + "/" + Util.numFormat(e.getCapacity()) + " FE";
        gui.text(font, this.te.isCreative() ? I18n.get("info.powah.unlimited") : s, 38, 13, a, false);
        gui.text(font, Util.numFormat(e.getMaxExtract()) + " FE/t", 38, 27, a, false);
    }
}
