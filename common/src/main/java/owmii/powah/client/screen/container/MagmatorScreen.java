package owmii.powah.client.screen.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.hooks.fluid.FluidStackHooks;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.magmator.MagmatorTile;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.MagmatorContainer;
import owmii.powah.lib.client.screen.container.AbstractEnergyScreen;
import owmii.powah.lib.client.util.Draw;
import owmii.powah.lib.client.util.Text;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.util.Ticker;
import owmii.powah.lib.util.Util;

public class MagmatorScreen extends AbstractEnergyScreen<MagmatorTile, MagmatorContainer> {
    private final Ticker heat = new Ticker(20);

    public MagmatorScreen(MagmatorContainer container, Inventory inv, Component title) {
        super(container, inv, title, Textures.MAGMATOR);
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
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.heat.subSized());
        Textures.MAGMATOR_BUFFER.draw(matrix, this.leftPos + 83, this.topPos + 29);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        var tank = this.te.getTank();
        if (!tank.isEmpty()) {
            var fluidStack = tank.getFluid();
            var sprite = FluidStackHooks.getStillTexture(fluidStack);
            if (sprite != null) {
                int color = FluidStackHooks.getColor(fluidStack);
                float red = (color >> 16 & 0xFF) / 255.0F;
                float green = (color >> 8 & 0xFF) / 255.0F;
                float blue = (color & 0xFF) / 255.0F;
                RenderSystem.setShaderColor(red, green, blue, 1.0F);
                bindTexture(InventoryMenu.BLOCK_ATLAS);
                Draw.gaugeV(sprite, this.leftPos + 157, this.topPos + 5, 14, 65, (int) tank.getCapacity(), (int) tank.getFluidAmount());
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    @Override
    protected void renderTooltip(PoseStack matrix, int mouseX, int mouseY) {
        super.renderTooltip(matrix, mouseX, mouseY);
        if (Textures.FURNATOR_GAUGE.isMouseOver(this.leftPos + 5, this.topPos + 5, mouseX, mouseY)) {
            List<Component> list = new ArrayList<>();
            Energy energy = this.te.getEnergy();
            list.add(Component.translatable("info.lollipop.stored").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                    .append(Component
                            .translatable("info.lollipop.fe.stored", Util.addCommas(energy.getStored()), Util.numFormat(energy.getCapacity()))
                            .withStyle(ChatFormatting.DARK_GRAY)));
            list.add(Component.translatable("info.lollipop.generates").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                    .append(Component.literal(Util.numFormat(this.te.getGeneration())).append(Component.translatable("info.lollipop.fe.pet.tick"))
                            .withStyle(ChatFormatting.DARK_GRAY)));
            list.add(Component.translatable("info.lollipop.max.extract").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                    .append(Component.literal(Util.numFormat(energy.getMaxExtract())).append(Component.translatable("info.lollipop.fe.pet.tick"))
                            .withStyle(ChatFormatting.DARK_GRAY)));
            renderComponentTooltip(matrix, list, mouseX, mouseY);
        }

        var tank = this.te.getTank();
        if (isMouseOver(mouseX - 157, mouseY - 5, 14, 65)) {
            List<Component> list = new ArrayList<>();
            if (!tank.isEmpty()) {
                list.add(Component.translatable("info.lollipop.fluid").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                        .append(FluidStackHooks.getName(tank.getFluid()).plainCopy().withStyle(ChatFormatting.GOLD)));
                list.add(Component.translatable("info.lollipop.stored").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                        .append(Util.formatTankContent(tank)));
                list.add(Component.translatable("info.lollipop.Gain").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                        .append(Component.translatable("info.lollipop.fe.per.mb", PowahAPI.getMagmaticFluidHeat(tank.getFluid().getFluid()), "100")
                                .withStyle(ChatFormatting.DARK_GRAY)));
            } else {
                list.add(Component.translatable("info.lollipop.fluid").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                        .append(Component.literal("---").withStyle(ChatFormatting.DARK_GRAY)));
            }
            renderComponentTooltip(matrix, list, mouseX, mouseY);
        }
    }
}
