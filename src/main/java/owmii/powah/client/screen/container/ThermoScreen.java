package owmii.powah.client.screen.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import owmii.lib.client.screen.container.AbstractEnergyScreen;
import owmii.lib.client.util.Draw;
import owmii.lib.client.util.Text;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.util.Util;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.thermo.ThermoTile;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.ThermoContainer;

import java.util.ArrayList;
import java.util.List;

public class ThermoScreen extends AbstractEnergyScreen<ThermoTile, ThermoContainer> {
    public ThermoScreen(ThermoContainer container, Inventory inv, Component title) {
        super(container, inv, title, Textures.THERMO);
    }

    @Override
    protected void drawBackground(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Textures.THERMO_GAUGE.drawScalableH(matrix, this.te.getEnergy().subSized(), this.leftPos + 5, this.topPos + 5);

        FluidTank tank = this.te.getTank();
        if (!tank.isEmpty()) {
            FluidStack fluidStack = tank.getFluid();
            FluidAttributes fa = fluidStack.getFluid().getAttributes();
            ResourceLocation still = fa.getStillTexture(fluidStack);
            if (still != null) {
                int color = fa.getColor(fluidStack);
                float red = (color >> 16 & 0xFF) / 255.0F;
                float green = (color >> 8 & 0xFF) / 255.0F;
                float blue = (color & 0xFF) / 255.0F;
                RenderSystem.color3f(red, green, blue);
                TextureAtlasSprite sprite = this.mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(still);
                bindTexture(InventoryMenu.BLOCK_ATLAS);
                Draw.gaugeV(sprite, this.leftPos + 157, this.topPos + 5, 14, 65, tank.getCapacity(), tank.getFluidAmount());
                RenderSystem.color3f(1.0F, 1.0F, 1.0F);
            }
        }

        long percent = this.te.getGeneration() > 0 ? (100 * this.te.generating) / this.te.getGeneration() : 0;
        this.font.draw(matrix, percent + "%" + " (" + this.te.generating + " EF/t)", this.leftPos + 34, this.topPos + 10, 5592405);
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

        FluidTank tank = this.te.getTank();
        if (isMouseOver(mouseX - 157, mouseY - 5, 14, 65)) {
            List<Component> list = new ArrayList<>();
            if (!tank.isEmpty()) {
                list.add(new TranslatableComponent("info.lollipop.coolant").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(tank.getFluid().getDisplayName().plainCopy().withStyle(ChatFormatting.AQUA)));
                list.add(new TranslatableComponent("info.lollipop.stored").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(new TranslatableComponent("info.lollipop.mb.stored", Util.addCommas(tank.getFluidAmount()), Util.numFormat(tank.getCapacity())).withStyle(ChatFormatting.DARK_GRAY)));
                list.add(new TranslatableComponent("info.lollipop.temperature").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(new TranslatableComponent("info.lollipop.temperature.c", "" + ChatFormatting.AQUA + PowahAPI.getCoolant(tank.getFluid().getFluid())).withStyle(ChatFormatting.DARK_GRAY)));
            } else {
                list.add(new TranslatableComponent("info.lollipop.fluid").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(new TextComponent("---").withStyle(ChatFormatting.DARK_GRAY)));
            }
            renderComponentTooltip(matrix, list, mouseX, mouseY);
        }
    }
}
