package owmii.powah.client.screen.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.hooks.fluid.FluidStackHooks;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import owmii.powah.lib.client.screen.container.AbstractEnergyScreen;
import owmii.powah.lib.client.screen.widget.IconButton;
import owmii.powah.lib.client.util.Draw;
import owmii.powah.lib.client.util.Text;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.util.Util;
import owmii.powah.Powah;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.reactor.ReactorTile;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.ReactorContainer;
import owmii.powah.network.Network;
import owmii.powah.network.packet.SwitchGenModePacket;

import java.util.ArrayList;
import java.util.List;

public class ReactorScreen extends AbstractEnergyScreen<ReactorTile, ReactorContainer> {
    private IconButton modeButton = IconButton.EMPTY;

    public ReactorScreen(ReactorContainer container, Inventory inv, Component title) {
        super(container, inv, title, Textures.REACTOR);
    }

    @Override
    protected void init() {
        super.init();
        this.modeButton = addRenderableWidget(new IconButton(this.leftPos - 11, this.topPos + 10, Textures.REACTOR_GEN_MODE.get(this.te.isGenModeOn()), b -> {
            Network.toServer(new SwitchGenModePacket(this.te.getBlockPos()));
            this.te.setGenModeOn(!this.te.isGenModeOn());
        }, this).setTooltip(tooltip -> {
            tooltip.add(new TranslatableComponent("info.powah.gen.mode").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                    .append(new TranslatableComponent("info.lollipop." + (this.te.isGenModeOn() ? "on" : "off")).withStyle(this.te.isGenModeOn() ? ChatFormatting.GREEN : ChatFormatting.RED)));
            tooltip.add(new TranslatableComponent("info.powah.gen.mode.desc").withStyle(ChatFormatting.DARK_GRAY));
        }));
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.modeButton.setTexture(Textures.REACTOR_GEN_MODE.get(this.te.isGenModeOn()));
    }

    @Override
    protected void drawBackground(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Textures.REACTOR_GAUGE.drawScalableH(matrix, this.te.getEnergy().subSized(), this.leftPos + 5, this.topPos + 5);
        Textures.REACTOR_GAUGE_URN.drawScalableH(matrix, this.te.fuel.subSized(), this.leftPos + 103, this.topPos + 13);
        Textures.REACTOR_GAUGE_CARBON.drawScalableH(matrix, this.te.carbon.subSized(), this.leftPos + 51, this.topPos + 6);
        Textures.REACTOR_GAUGE_REDSTONE.drawScalableH(matrix, this.te.redstone.subSized(), this.leftPos + 51, this.topPos + 52);
        Textures.REACTOR_GAUGE_COOLANT.drawScalableH(matrix, this.te.solidCoolant.subSized(), this.leftPos + 140, this.topPos + 52);
        Textures.REACTOR_GAUGE_TEMP.drawScalableH(matrix, this.te.temp.subSized(), this.leftPos + 114, this.topPos + 28);

        Textures.REACTOR_GEN_MODE_BG.draw(matrix, this.modeButton.x - 4, this.modeButton.y - 4);

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
        if (Textures.REACTOR_GAUGE.isMouseOver(this.leftPos + 5, this.topPos + 5, mouseX, mouseY)) {
            List<Component> list = new ArrayList<>();
            Energy energy = this.te.getEnergy();
            list.add(new TranslatableComponent("info.powah.gen.mode").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(new TranslatableComponent("info.lollipop." + (this.te.isGenModeOn() ? "on" : "off")).withStyle(this.te.isGenModeOn() ? ChatFormatting.GREEN : ChatFormatting.RED)));
            list.add(new TranslatableComponent("info.lollipop.stored").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(new TranslatableComponent("info.lollipop.fe.stored", Util.addCommas(energy.getStored()), Util.numFormat(energy.getCapacity())).withStyle(ChatFormatting.DARK_GRAY)));
            list.add(new TranslatableComponent("info.powah.generation.factor").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(new TextComponent(Util.numFormat(this.te.getGeneration())).append(new TranslatableComponent("info.lollipop.fe.pet.tick")).withStyle(ChatFormatting.DARK_GRAY)));
            list.add(new TranslatableComponent("info.lollipop.generating").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(new TextComponent(Util.numFormat((long) this.te.calcProduction())).append(new TranslatableComponent("info.lollipop.fe.pet.tick")).withStyle(ChatFormatting.DARK_GRAY)));
            list.add(new TranslatableComponent("info.lollipop.max.extract").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(new TextComponent(Util.numFormat(energy.getMaxExtract())).append(new TranslatableComponent("info.lollipop.fe.pet.tick")).withStyle(ChatFormatting.DARK_GRAY)));

            renderComponentTooltip(matrix, list, mouseX, mouseY);
        }

        if (Textures.REACTOR_GAUGE_TEMP.isMouseOver(this.leftPos + 114, this.topPos + 28, mouseX, mouseY)) {
            List<Component> list = new ArrayList<>();
            list.add(new TextComponent(ChatFormatting.GRAY + String.format("%.1f", this.te.temp.getTicks()) + " C"));
            renderComponentTooltip(matrix, list, mouseX, mouseY);
        }

        if (Textures.REACTOR_GAUGE_URN.isMouseOver(this.leftPos + 103, this.topPos + 13, mouseX, mouseY)) {
            List<Component> list = new ArrayList<>();
            list.add(new TranslatableComponent("item.powah.uraninite").withStyle(ChatFormatting.GREEN));
            list.add(new TranslatableComponent("info.lollipop.stored").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(new TranslatableComponent("info.lollipop.mb.stored", String.format("%.0f", this.te.fuel.getTicks()), String.format("%.0f", this.te.fuel.getMax())).withStyle(ChatFormatting.DARK_GRAY)));
            list.add(new TranslatableComponent("info.lollipop.using").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(new TextComponent(ChatFormatting.GREEN + String.format("%.4f", this.te.calcConsumption())).append(new TranslatableComponent("info.lollipop.mb.pet.tick")).withStyle(ChatFormatting.DARK_GRAY)));
            renderComponentTooltip(matrix, list, mouseX, mouseY);
        }

        if (Textures.REACTOR_GAUGE_CARBON.isMouseOver(this.leftPos + 51, this.topPos + 6, mouseX, mouseY)) {
            List<Component> list = new ArrayList<>();
            boolean b = this.te.carbon.isEmpty();
            list.add((new TranslatableComponent("info.powah.carbon")).withStyle(ChatFormatting.GRAY));
            list.add(new TranslatableComponent("info.lollipop.stored").withStyle(ChatFormatting.DARK_GRAY).append(Text.COLON).append(new TranslatableComponent("info.lollipop.mb.stored", String.format("%.1f", this.te.carbon.getTicks()), String.format("%.1f", this.te.carbon.getMax())).withStyle(ChatFormatting.DARK_GRAY)));
            list.add(new TextComponent(""));
            list.add(new TranslatableComponent("enchantment.minecraft.efficiency").withStyle(ChatFormatting.DARK_AQUA));
            list.add(new TextComponent(ChatFormatting.DARK_RED + (b ? "+0 C" : "+180 C")));
            renderComponentTooltip(matrix, list, mouseX, mouseY);
        }

        if (Textures.REACTOR_GAUGE_REDSTONE.isMouseOver(this.leftPos + 51, this.topPos + 52, mouseX, mouseY)) {
            List<Component> list = new ArrayList<>();
            boolean b = this.te.redstone.isEmpty();
            list.add(new TranslatableComponent("info.powah.redstone").withStyle(ChatFormatting.GRAY));
            list.add(new TranslatableComponent("info.lollipop.stored").withStyle(ChatFormatting.DARK_GRAY).append(Text.COLON).append(new TranslatableComponent("info.lollipop.mb.stored", String.format("%.1f", this.te.redstone.getTicks()), String.format("%.1f", this.te.redstone.getMax())).withStyle(ChatFormatting.DARK_GRAY)));
            list.add(new TextComponent(""));
            list.add(new TranslatableComponent("info.powah.production").withStyle(ChatFormatting.DARK_AQUA));
            list.add(new TranslatableComponent("info.powah.fuel.consumption").withStyle(ChatFormatting.DARK_RED));
            list.add(new TextComponent(ChatFormatting.DARK_RED + (b ? "+0 C" : "+120 C")));
            renderComponentTooltip(matrix, list, mouseX, mouseY);
        }

        if (Textures.REACTOR_GAUGE_COOLANT.isMouseOver(this.leftPos + 140, this.topPos + 52, mouseX, mouseY)) {
            List<Component> list = new ArrayList<>();
            list.add(new TranslatableComponent("info.powah.solid.coolant").withStyle(ChatFormatting.GRAY));
            list.add(new TranslatableComponent("info.lollipop.stored").withStyle(ChatFormatting.DARK_GRAY).append(Text.COLON).append(new TranslatableComponent("info.lollipop.mb.stored", String.format("%.1f", this.te.solidCoolant.getTicks()), String.format("%.1f", this.te.solidCoolant.getMax())).withStyle(ChatFormatting.DARK_GRAY)));
            list.add(new TextComponent("" + ChatFormatting.AQUA + this.te.solidCoolantTemp + " C"));
            renderComponentTooltip(matrix, list, mouseX, mouseY);
        }

        var tank = this.te.getTank();
        if (isMouseOver(mouseX - 157, mouseY - 5, 14, 65)) {
            List<Component> list = new ArrayList<>();
            if (!tank.isEmpty()) {
                list.add(new TranslatableComponent("info.lollipop.coolant").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(FluidStackHooks.getName(tank.getFluid()).plainCopy().withStyle(ChatFormatting.AQUA)));
                list.add(new TranslatableComponent("info.lollipop.stored").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(new TranslatableComponent("info.lollipop.mb.stored", Util.addCommas(tank.getFluidAmount()), Util.numFormat(tank.getCapacity())).withStyle(ChatFormatting.DARK_GRAY)));
                list.add(new TranslatableComponent("info.lollipop.temperature").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(new TranslatableComponent("info.lollipop.temperature.c", "" + ChatFormatting.AQUA + PowahAPI.getCoolant(tank.getFluid().getFluid())).withStyle(ChatFormatting.DARK_GRAY)));
            } else {
                list.add(new TranslatableComponent("info.lollipop.fluid").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(new TextComponent("---").withStyle(ChatFormatting.DARK_GRAY)));
            }
            renderComponentTooltip(matrix, list, mouseX, mouseY);
        }
    }
}
