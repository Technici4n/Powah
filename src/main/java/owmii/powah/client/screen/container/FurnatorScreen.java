package owmii.powah.client.screen.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import owmii.lib.client.screen.AbstractEnergyScreen;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.util.Ticker;
import owmii.lib.util.Util;
import owmii.powah.block.furnator.FurnatorTile;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.FurnatorContainer;

import java.util.ArrayList;
import java.util.List;

public class FurnatorScreen extends AbstractEnergyScreen<FurnatorTile, FurnatorContainer> {
    private final Ticker heat = new Ticker(20);

    public FurnatorScreen(FurnatorContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, Textures.FURNATOR);
        if (this.te.isBurning()) {
            this.heat.setTicks(20);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.te.isBurning()) {
            this.heat.onward();
        } else {
            this.heat.back();
        }
    }

    @Override
    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Textures.FURNATOR_GAUGE.drawScalableH(matrix, this.te.getEnergy().subSized(), this.guiLeft + 5, this.guiTop + 5);
        Textures.FURNATOR_CARBON_GAUGE.drawScalableH(matrix, this.te.getCarbon().subSized(), this.guiLeft + 110, this.guiTop + 18);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.heat.subSized());
        Textures.FURNATOR_BUFFER.draw(matrix, this.guiLeft + 94, this.guiTop + 43);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    protected void func_230459_a_(MatrixStack matrix, int mouseX, int mouseY) {
        super.func_230459_a_(matrix, mouseX, mouseY);
        if (Textures.FURNATOR_GAUGE.isMouseOver(this.guiLeft + 5, this.guiTop + 5, mouseX, mouseY)) {
            List<ITextComponent> list = new ArrayList<>();
            Energy energy = this.te.getEnergy();
            list.add(new TranslationTextComponent("info.lollipop.stored.energy.fe", TextFormatting.GRAY + Util.addCommas(energy.getStored()), TextFormatting.GRAY + Util.numFormat(energy.getCapacity())).mergeStyle(TextFormatting.DARK_GRAY));
            list.add(new TranslationTextComponent("info.lollipop.generates", TextFormatting.GRAY + Util.numFormat(this.te.getGeneration())).mergeStyle(TextFormatting.DARK_GRAY));
            list.add(new TranslationTextComponent("info.lollipop.max.transfer.fe", TextFormatting.GRAY + Util.numFormat(energy.getMaxExtract())).mergeStyle(TextFormatting.DARK_GRAY));
            func_243308_b(matrix, list, mouseX, mouseY);
        }
        if (Textures.FURNATOR_CARBON_GAUGE.isMouseOver(this.guiLeft + 110, this.guiTop + 18, mouseX, mouseY)) {
            List<ITextComponent> list = new ArrayList<>();
            list.add(new TranslationTextComponent("info.powah.carbon").mergeStyle(TextFormatting.DARK_GRAY));
            func_243308_b(matrix, list, mouseX, mouseY);
        }
    }
}
