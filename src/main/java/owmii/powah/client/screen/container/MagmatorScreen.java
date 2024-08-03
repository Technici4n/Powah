package owmii.powah.client.screen.container;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.magmator.MagmatorTile;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.MagmatorContainer;
import owmii.powah.lib.client.screen.container.AbstractEnergyScreen;
import owmii.powah.lib.client.util.Text;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.util.Ticker;
import owmii.powah.util.Util;

public class MagmatorScreen extends AbstractEnergyScreen<MagmatorTile, MagmatorContainer> {
    private final Ticker heat = new Ticker(20);

    public MagmatorScreen(MagmatorContainer container, Inventory inv, Component title) {
        super(container, inv, title, Textures.MAGMATOR);
        if (this.te.isBurning()) {
            this.heat.setTicks(20);
        }
        addTankArea(
                te::getTank,
                157,
                5,
                14,
                65,
                "info.lollipop.fluid",
                (content, lines) -> {
                    lines.add(Component.translatable("info.lollipop.Gain").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                            .append(Component
                                    .translatable("info.lollipop.fe.per.mb", PowahAPI.getMagmaticFluidEnergyProduced(content.getFluid()), "100")
                                    .withStyle(ChatFormatting.DARK_GRAY)));
                });
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
    protected void drawBackground(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(guiGraphics, partialTicks, mouseX, mouseY);
        Textures.FURNATOR_GAUGE.drawScalableH(guiGraphics, this.te.getEnergy().subSized(), this.leftPos + 5, this.topPos + 5);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.heat.subSized());
        Textures.MAGMATOR_BUFFER.draw(guiGraphics, this.leftPos + 83, this.topPos + 29);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    protected void renderTooltip(GuiGraphics gui, int mouseX, int mouseY) {
        super.renderTooltip(gui, mouseX, mouseY);
        if (Textures.FURNATOR_GAUGE.isMouseOver(this.leftPos + 5, this.topPos + 5, mouseX, mouseY)) {
            List<Component> list = new ArrayList<>();
            Energy energy = this.te.getEnergy();
            list.add(Component.translatable("info.lollipop.stored").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                    .append(Component
                            .translatable("info.lollipop.fe.stored", Util.addCommas(energy.getStored()), Util.numFormat(energy.getCapacity()))
                            .withStyle(ChatFormatting.DARK_GRAY)));
            list.add(Component.translatable("info.lollipop.generates").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                    .append(Component.translatable("info.lollipop.fe.pet.tick", Util.numFormat(this.te.getGeneration()))
                            .withStyle(ChatFormatting.DARK_GRAY)));
            list.add(Component.translatable("info.lollipop.max.extract").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                    .append(Component.translatable("info.lollipop.fe.pet.tick", Util.numFormat(energy.getMaxExtract()))
                            .withStyle(ChatFormatting.DARK_GRAY)));
            gui.renderComponentTooltip(font, list, mouseX, mouseY);
        }
    }
}
