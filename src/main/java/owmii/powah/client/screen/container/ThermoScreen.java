package owmii.powah.client.screen.container;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.thermo.ThermoTile;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.ThermoContainer;
import owmii.powah.lib.client.screen.container.AbstractEnergyScreen;
import owmii.powah.lib.client.util.Text;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.util.Util;

public class ThermoScreen extends AbstractEnergyScreen<ThermoTile, ThermoContainer> {
    public ThermoScreen(ThermoContainer container, Inventory inv, Component title) {
        super(container, inv, title, Textures.THERMO);

        addTankArea(te::getTank, 157, 5, 14, 65, "info.lollipop.coolant", (content, lines) -> {
            lines.add(Component.translatable("info.lollipop.temperature").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                    .append(Component
                            .translatable("info.lollipop.temperature.c",
                                    "" + ChatFormatting.AQUA + PowahAPI.getCoolant(content.getFluid()))
                            .withStyle(ChatFormatting.DARK_GRAY)));
        });
    }

    @Override
    protected void drawBackground(GuiGraphics gui, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(gui, partialTicks, mouseX, mouseY);
        Textures.THERMO_GAUGE.drawScalableH(gui, this.te.getEnergy().subSized(), this.leftPos + 5, this.topPos + 5);

        long percent = this.te.getGeneration() > 0 ? (100 * this.te.generating) / this.te.getGeneration() : 0;
        gui.drawString(font, percent + "%" + " (" + this.te.generating + " FE/t)", this.leftPos + 34, this.topPos + 10, 5592405, false);
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
