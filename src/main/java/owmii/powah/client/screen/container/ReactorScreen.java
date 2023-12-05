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
import owmii.powah.block.reactor.ReactorTile;
import owmii.powah.client.ClientUtils;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.ReactorContainer;
import owmii.powah.lib.client.screen.container.AbstractEnergyScreen;
import owmii.powah.lib.client.screen.widget.IconButton;
import owmii.powah.lib.client.util.Draw;
import owmii.powah.lib.client.util.Text;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.util.Util;
import owmii.powah.network.Network;
import owmii.powah.network.packet.SwitchGenModePacket;

public class ReactorScreen extends AbstractEnergyScreen<ReactorTile, ReactorContainer> {
    private IconButton modeButton = IconButton.EMPTY;

    public ReactorScreen(ReactorContainer container, Inventory inv, Component title) {
        super(container, inv, title, Textures.REACTOR);
    }

    @Override
    protected void init() {
        super.init();
        this.modeButton = addRenderableWidget(
                new IconButton(this.leftPos - 11, this.topPos + 10, Textures.REACTOR_GEN_MODE.get(this.te.isGenModeOn()), b -> {
                    Network.toServer(new SwitchGenModePacket(this.te.getBlockPos()));
                    this.te.setGenModeOn(!this.te.isGenModeOn());
                }, this).setTooltipSupplier(() -> List.of(
                        Component.translatable("info.powah.gen.mode").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                                .append(Component.translatable("info.lollipop." + (this.te.isGenModeOn() ? "on" : "off"))
                                        .withStyle(this.te.isGenModeOn() ? ChatFormatting.GREEN : ChatFormatting.RED)),
                        Component.translatable("info.powah.gen.mode.desc").withStyle(ChatFormatting.DARK_GRAY))));
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.modeButton.setTexture(Textures.REACTOR_GEN_MODE.get(this.te.isGenModeOn()));
    }

    @Override
    protected void drawBackground(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(guiGraphics, partialTicks, mouseX, mouseY);
        Textures.REACTOR_GAUGE.drawScalableH(guiGraphics, this.te.getEnergy().subSized(), this.leftPos + 5, this.topPos + 5);
        Textures.REACTOR_GAUGE_URN.drawScalableH(guiGraphics, this.te.fuel.subSized(), this.leftPos + 103, this.topPos + 13);
        Textures.REACTOR_GAUGE_CARBON.drawScalableH(guiGraphics, this.te.carbon.subSized(), this.leftPos + 51, this.topPos + 6);
        Textures.REACTOR_GAUGE_REDSTONE.drawScalableH(guiGraphics, this.te.redstone.subSized(), this.leftPos + 51, this.topPos + 52);
        Textures.REACTOR_GAUGE_COOLANT.drawScalableH(guiGraphics, this.te.solidCoolant.subSized(), this.leftPos + 140, this.topPos + 52);
        Textures.REACTOR_GAUGE_TEMP.drawScalableH(guiGraphics, this.te.temp.subSized(), this.leftPos + 114, this.topPos + 28);

        Textures.REACTOR_GEN_MODE_BG.draw(guiGraphics, this.modeButton.getX() - 4, this.modeButton.getY() - 4);

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
    }

    @Override
    protected void renderTooltip(GuiGraphics gui, int mouseX, int mouseY) {
        super.renderTooltip(gui, mouseX, mouseY);
        if (Textures.REACTOR_GAUGE.isMouseOver(this.leftPos + 5, this.topPos + 5, mouseX, mouseY)) {
            List<Component> list = new ArrayList<>();
            Energy energy = this.te.getEnergy();
            list.add(Component.translatable("info.powah.gen.mode").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                    .append(Component.translatable("info.lollipop." + (this.te.isGenModeOn() ? "on" : "off"))
                            .withStyle(this.te.isGenModeOn() ? ChatFormatting.GREEN : ChatFormatting.RED)));
            list.add(Component.translatable("info.lollipop.stored").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                    .append(Component
                            .translatable("info.lollipop.fe.stored", Util.addCommas(energy.getStored()), Util.numFormat(energy.getCapacity()))
                            .withStyle(ChatFormatting.DARK_GRAY)));
            list.add(Component.translatable("info.powah.generation.factor").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                    .append(Component.literal(Util.numFormat(this.te.getGeneration())).append(Component.translatable("info.lollipop.fe.pet.tick"))
                            .withStyle(ChatFormatting.DARK_GRAY)));
            list.add(Component.translatable("info.lollipop.generating").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                    .append(Component.literal(Util.numFormat((long) this.te.calcProduction()))
                            .append(Component.translatable("info.lollipop.fe.pet.tick")).withStyle(ChatFormatting.DARK_GRAY)));
            list.add(Component.translatable("info.lollipop.max.extract").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                    .append(Component.literal(Util.numFormat(energy.getMaxExtract())).append(Component.translatable("info.lollipop.fe.pet.tick"))
                            .withStyle(ChatFormatting.DARK_GRAY)));

            gui.renderComponentTooltip(font, list, mouseX, mouseY);
        }

        if (Textures.REACTOR_GAUGE_TEMP.isMouseOver(this.leftPos + 114, this.topPos + 28, mouseX, mouseY)) {
            List<Component> list = new ArrayList<>();
            list.add(Component.literal(ChatFormatting.GRAY + String.format("%.1f", this.te.temp.getTicks()) + " C"));
            gui.renderComponentTooltip(font, list, mouseX, mouseY);
        }

        if (Textures.REACTOR_GAUGE_URN.isMouseOver(this.leftPos + 103, this.topPos + 13, mouseX, mouseY)) {
            List<Component> list = new ArrayList<>();
            list.add(Component.translatable("item.powah.uraninite").withStyle(ChatFormatting.GREEN));
            list.add(Component.translatable("info.lollipop.stored").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                    .append(Component.translatable("info.lollipop.mb.stored", String.format("%.0f", this.te.fuel.getTicks()),
                            String.format("%.0f", this.te.fuel.getMax())).withStyle(ChatFormatting.DARK_GRAY)));
            list.add(Component.translatable("info.lollipop.using").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                    .append(Component.literal(ChatFormatting.GREEN + String.format("%.4f", this.te.calcConsumption()))
                            .append(Component.translatable("info.lollipop.mb.pet.tick")).withStyle(ChatFormatting.DARK_GRAY)));
            gui.renderComponentTooltip(font, list, mouseX, mouseY);
        }

        if (Textures.REACTOR_GAUGE_CARBON.isMouseOver(this.leftPos + 51, this.topPos + 6, mouseX, mouseY)) {
            List<Component> list = new ArrayList<>();
            boolean b = this.te.carbon.isEmpty();
            list.add((Component.translatable("info.powah.carbon")).withStyle(ChatFormatting.GRAY));
            list.add(Component.translatable("info.lollipop.stored").withStyle(ChatFormatting.DARK_GRAY).append(Text.COLON)
                    .append(Component.translatable("info.lollipop.mb.stored", String.format("%.1f", this.te.carbon.getTicks()),
                            String.format("%.1f", this.te.carbon.getMax())).withStyle(ChatFormatting.DARK_GRAY)));
            list.add(Component.empty());
            list.add(Component.translatable("enchantment.minecraft.efficiency").withStyle(ChatFormatting.DARK_AQUA));
            list.add(Component.literal(ChatFormatting.DARK_RED + (b ? "+0 C" : "+180 C")));
            gui.renderComponentTooltip(font, list, mouseX, mouseY);
        }

        if (Textures.REACTOR_GAUGE_REDSTONE.isMouseOver(this.leftPos + 51, this.topPos + 52, mouseX, mouseY)) {
            List<Component> list = new ArrayList<>();
            boolean b = this.te.redstone.isEmpty();
            list.add(Component.translatable("info.powah.redstone").withStyle(ChatFormatting.GRAY));
            list.add(Component.translatable("info.lollipop.stored").withStyle(ChatFormatting.DARK_GRAY).append(Text.COLON)
                    .append(Component.translatable("info.lollipop.mb.stored", String.format("%.1f", this.te.redstone.getTicks()),
                            String.format("%.1f", this.te.redstone.getMax())).withStyle(ChatFormatting.DARK_GRAY)));
            list.add(Component.empty());
            list.add(Component.translatable("info.powah.production").withStyle(ChatFormatting.DARK_AQUA));
            list.add(Component.translatable("info.powah.fuel.consumption").withStyle(ChatFormatting.DARK_RED));
            list.add(Component.literal(ChatFormatting.DARK_RED + (b ? "+0 C" : "+120 C")));
            gui.renderComponentTooltip(font, list, mouseX, mouseY);
        }

        if (Textures.REACTOR_GAUGE_COOLANT.isMouseOver(this.leftPos + 140, this.topPos + 52, mouseX, mouseY)) {
            List<Component> list = new ArrayList<>();
            list.add(Component.translatable("info.powah.solid.coolant").withStyle(ChatFormatting.GRAY));
            list.add(Component.translatable("info.lollipop.stored").withStyle(ChatFormatting.DARK_GRAY).append(Text.COLON)
                    .append(Component.translatable("info.lollipop.mb.stored", String.format("%.1f", this.te.solidCoolant.getTicks()),
                            String.format("%.1f", this.te.solidCoolant.getMax())).withStyle(ChatFormatting.DARK_GRAY)));
            list.add(Component.literal("" + ChatFormatting.AQUA + this.te.solidCoolantTemp + " C"));
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
