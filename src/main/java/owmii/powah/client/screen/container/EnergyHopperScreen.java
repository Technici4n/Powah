package owmii.powah.client.screen.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import owmii.lib.client.screen.container.AbstractEnergyScreen;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.util.Util;
import owmii.powah.block.hopper.EnergyHopperTile;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.EnergyHopperContainer;

public class EnergyHopperScreen extends AbstractEnergyScreen<EnergyHopperTile, EnergyHopperContainer> {
    public EnergyHopperScreen(EnergyHopperContainer container, Inventory inv, Component title) {
        super(container, inv, title, Textures.WIDE_ENERGY);
    }

    @Override
    protected void drawBackground(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Textures.WIDE_ENERGY_GAUGE.drawScalableW(matrix, this.te.getEnergy().subSized(), this.leftPos + 6, this.topPos + 6);
    }

    @Override
    protected void drawForeground(PoseStack matrix, int mouseX, int mouseY) {
        super.drawForeground(matrix, mouseX, mouseY);
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        int a = (int) (255.0D * 1.0D * 0.4D) << 24;
        Energy e = this.te.getEnergy();
        String s = Util.addCommas(e.getStored()) + "/" + Util.numFormat(e.getCapacity()) + " FE";
        this.font.draw(matrix, s, 12, 13.0F, a);
        this.font.draw(matrix, Util.numFormat(e.getMaxExtract()) + " FE/t", 12, 27.0F, a);
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
    }
}
