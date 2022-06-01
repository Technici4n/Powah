package owmii.powah.client.screen.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import owmii.lib.client.screen.container.AbstractEnergyScreen;
import owmii.lib.client.util.Text;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.util.Ticker;
import owmii.lib.util.Util;
import owmii.powah.block.furnator.FurnatorTile;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.FurnatorContainer;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;

public class FurnatorScreen extends AbstractEnergyScreen<FurnatorTile, FurnatorContainer> {
    private final Ticker heat = new Ticker(20);

    public FurnatorScreen(FurnatorContainer container, Inventory inv, Component title) {
        super(container, inv, title, Textures.FURNATOR);
        if (this.te.isBurning()) {
            this.heat.setTicks(20);
        }
    }

    @Override
    public void containerTick() {
        super.containerTick();
        if (this.te.isBurning()) {
            this.heat.onward();
        } else {
            this.heat.back();
        }
    }

    @Override
    protected void drawBackground(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Textures.FURNATOR_GAUGE.drawScalableH(matrix, this.te.getEnergy().subSized(), this.leftPos + 5, this.topPos + 5);
        Textures.FURNATOR_CARBON_GAUGE.drawScalableH(matrix, this.te.getCarbon().subSized(), this.leftPos + 110, this.topPos + 18);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.heat.subSized());
        Textures.FURNATOR_BUFFER.draw(matrix, this.leftPos + 94, this.topPos + 43);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    protected void renderTooltip(PoseStack matrix, int mouseX, int mouseY) {
        super.renderTooltip(matrix, mouseX, mouseY);
        if (Textures.FURNATOR_GAUGE.isMouseOver(this.leftPos + 5, this.topPos + 5, mouseX, mouseY)) {
            List<Component> list = new ArrayList<>();
            Energy energy = this.te.getEnergy();
            list.add(new TranslatableComponent("info.lollipop.stored").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(new TranslatableComponent("info.lollipop.fe.stored", Util.addCommas(energy.getStored()), Util.numFormat(energy.getCapacity())).withStyle(ChatFormatting.DARK_GRAY)));
            list.add(new TranslatableComponent("info.lollipop.generates").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(new TextComponent(Util.numFormat(this.te.getGeneration())).append(new TranslatableComponent("info.lollipop.fe.pet.tick")).withStyle(ChatFormatting.DARK_GRAY)));
            list.add(new TranslatableComponent("info.lollipop.max.extract").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(new TextComponent(Util.numFormat(energy.getMaxExtract())).append(new TranslatableComponent("info.lollipop.fe.pet.tick")).withStyle(ChatFormatting.DARK_GRAY)));
            renderComponentTooltip(matrix, list, mouseX, mouseY);
        }
        if (Textures.FURNATOR_CARBON_GAUGE.isMouseOver(this.leftPos + 110, this.topPos + 18, mouseX, mouseY)) {
            List<Component> list = new ArrayList<>();
            list.add(new TranslatableComponent("info.powah.carbon").withStyle(ChatFormatting.DARK_GRAY));
            renderComponentTooltip(matrix, list, mouseX, mouseY);
        }
    }
}
