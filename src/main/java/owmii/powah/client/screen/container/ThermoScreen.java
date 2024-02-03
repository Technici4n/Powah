package owmii.powah.client.screen.container;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.thermo.ThermoTile;
import owmii.powah.client.ClientUtils;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.ThermoContainer;
import owmii.powah.lib.client.screen.container.AbstractEnergyScreen;
import owmii.powah.lib.client.util.Draw;
import owmii.powah.lib.client.util.Text;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.util.Util;

public class ThermoScreen extends AbstractEnergyScreen<ThermoTile, ThermoContainer> {
    public ThermoScreen(ThermoContainer container, Inventory inv, Component title) {
        super(container, inv, title, Textures.THERMO);
    }

    @Override
    protected void drawBackground(GuiGraphics gui, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(gui, partialTicks, mouseX, mouseY);
        Textures.THERMO_GAUGE.drawScalableH(gui, this.te.getEnergy().subSized(), this.leftPos + 5, this.topPos + 5);

        var tank = this.te.getTank();
        if (!tank.isEmpty()) {
            var fluidStack = tank.getFluid();
            var sprite = ClientUtils.getStillTexture(fluidStack);
            if (sprite != null) {
                int color = ClientUtils.getFluidColor(fluidStack);
                float red = (color >> 16 & 0xFF) / 255.0F;
                float green = (color >> 8 & 0xFF) / 255.0F;
                float blue = (color & 0xFF) / 255.0F;
                RenderSystem.setShaderColor(red, green, blue, 1.0F);
                bindTexture(InventoryMenu.BLOCK_ATLAS);
                Draw.gaugeV(sprite, this.leftPos + 157, this.topPos + 5, 14, 65, (int) tank.getCapacity(), (int) tank.getFluidAmount());
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }

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

        var tank = this.te.getTank();
        if (isMouseOver(mouseX - 157, mouseY - 5, 14, 65)) {
            List<Component> list = new ArrayList<>();
            if (!tank.isEmpty()) {
                list.add(Component.translatable("info.lollipop.coolant").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                        .append(tank.getFluid().getDisplayName().plainCopy().withStyle(ChatFormatting.AQUA)));
                list.add(Component.translatable("info.lollipop.stored").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                        .append(Util.formatTankContent(tank)));
                list.add(Component.translatable("info.lollipop.temperature").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                        .append(Component
                                .translatable("info.lollipop.temperature.c",
                                        "" + ChatFormatting.AQUA + PowahAPI.getCoolant(tank.getFluid().getFluid()))
                                .withStyle(ChatFormatting.DARK_GRAY)));
            } else {
                list.add(Component.translatable("info.lollipop.fluid").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                        .append(Component.literal("---").withStyle(ChatFormatting.DARK_GRAY)));
            }
            gui.renderComponentTooltip(font, list, mouseX, mouseY);
        }
    }
}
