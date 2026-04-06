package owmii.powah.client.screen.container;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Inventory;
import owmii.powah.block.furnator.FurnatorBlockEntity;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.FurnatorMenu;
import owmii.powah.lib.client.screen.container.PowahBaseEnergyScreen;
import owmii.powah.lib.client.util.Text;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.util.Ticker;
import owmii.powah.util.Util;

public class FurnatorScreen extends PowahBaseEnergyScreen<FurnatorBlockEntity, FurnatorMenu> {
    private final Ticker heat = new Ticker(20);

    public FurnatorScreen(FurnatorMenu container, Inventory inv, Component title) {
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
    protected void drawBackground(GuiGraphicsExtractor guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(guiGraphics, partialTicks, mouseX, mouseY);
        Textures.FURNATOR_GAUGE.drawScalableH(guiGraphics, this.te.getEnergy().subSized(), this.leftPos + 5, this.topPos + 5);
        Textures.FURNATOR_CARBON_GAUGE.drawScalableH(guiGraphics, this.te.getCarbon().subSized(), this.leftPos + 110, this.topPos + 18);
        var alpha = this.heat.subSized();
        Textures.FURNATOR_BUFFER.draw(guiGraphics, this.leftPos + 94, this.topPos + 43, ARGB.white(alpha));
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor gui, int mouseX, int mouseY) {
        super.extractTooltip(gui, mouseX, mouseY);
        if (Textures.FURNATOR_GAUGE.isMouseOver(this.leftPos + 5, this.topPos + 5, mouseX, mouseY)) {
            List<Component> list = new ArrayList<>();
            Energy energy = this.te.getEnergy();
            list.add(Component.translatable("info.lollipop.stored").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                    .append(Component
                            .translatable("info.lollipop.fe.stored", Util.addCommas(energy.getStored()), Util.numFormat(energy.getCapacity()))
                            .withStyle(ChatFormatting.DARK_GRAY)));
            list.add(Component.translatable("info.lollipop.generates").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                    .append(Component.translatable("info.lollipop.fe.pet.tick", Util.numFormat(this.te.getEnergyGeneration()))
                            .withStyle(ChatFormatting.DARK_GRAY)));
            list.add(Component.translatable("info.lollipop.max.extract").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                    .append(Component.translatable("info.lollipop.fe.pet.tick", Util.numFormat(energy.getMaxExtract()))
                            .withStyle(ChatFormatting.DARK_GRAY)));
            gui.setComponentTooltipForNextFrame(font, list, mouseX, mouseY);
        }
        if (Textures.FURNATOR_CARBON_GAUGE.isMouseOver(this.leftPos + 110, this.topPos + 18, mouseX, mouseY)) {
            List<Component> list = new ArrayList<>();
            list.add(Component.translatable("info.powah.carbon").withStyle(ChatFormatting.DARK_GRAY));
            gui.setComponentTooltipForNextFrame(font, list, mouseX, mouseY);
        }
    }
}
