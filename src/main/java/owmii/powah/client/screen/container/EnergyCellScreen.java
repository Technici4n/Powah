package owmii.powah.client.screen.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import owmii.lib.client.screen.container.AbstractEnergyScreen;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.util.Util;
import owmii.powah.block.energycell.EnergyCellTile;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.EnergyCellContainer;

public class EnergyCellScreen extends AbstractEnergyScreen<EnergyCellTile, EnergyCellContainer> {
    public EnergyCellScreen(EnergyCellContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, Textures.ENERGY_CELL);
    }

    @Override
    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Textures.ENERGY_CELL_GAUGE.drawScalableW(matrix, this.te.getEnergy().subSized(), this.guiLeft + 31, this.guiTop + 6);
    }

    @Override
    protected void drawForeground(MatrixStack matrix, int mouseX, int mouseY) {
        super.drawForeground(matrix, mouseX, mouseY);
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        int a = (int) (255.0D * 1.0D * 0.4D) << 24;
        Energy e = this.te.getEnergy();
        String s = Util.addCommas(e.getStored()) + "/" + Util.numFormat(e.getCapacity()) + " FE";
        this.font.drawString(matrix, this.te.isCreative() ? I18n.format("info.powah.unlimited") : s, 38, 13.0F, a);
        this.font.drawString(matrix, Util.numFormat(e.getMaxExtract()) + " FE/t", 38, 27.0F, a);
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
    }
}
