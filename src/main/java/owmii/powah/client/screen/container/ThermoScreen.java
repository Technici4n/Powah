package owmii.powah.client.screen.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
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
    public ThermoScreen(ThermoContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, Textures.THERMO);
    }

    @Override
    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Textures.THERMO_GAUGE.drawScalableH(matrix, this.te.getEnergy().subSized(), this.guiLeft + 5, this.guiTop + 5);

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
                TextureAtlasSprite sprite = this.mc.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(still);
                bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
                Draw.gaugeV(sprite, this.guiLeft + 157, this.guiTop + 5, 14, 65, tank.getCapacity(), tank.getFluidAmount());
                RenderSystem.color3f(1.0F, 1.0F, 1.0F);
            }
        }

        long percent = this.te.getGeneration() > 0 ? (100 * this.te.generating) / this.te.getGeneration() : 0;
        this.font.drawString(matrix, percent + "%" + " (" + this.te.generating + " EF/t)", this.guiLeft + 34, this.guiTop + 10, 5592405);
    }

    @Override
    protected void func_230459_a_(MatrixStack matrix, int mouseX, int mouseY) {
        super.func_230459_a_(matrix, mouseX, mouseY);
        if (Textures.FURNATOR_GAUGE.isMouseOver(this.guiLeft + 5, this.guiTop + 5, mouseX, mouseY)) {
            List<ITextComponent> list = new ArrayList<>();
            Energy energy = this.te.getEnergy();
            list.add(new TranslationTextComponent("info.lollipop.stored").mergeStyle(TextFormatting.GRAY).append(Text.COLON).append(new TranslationTextComponent("info.lollipop.fe.stored", Util.addCommas(energy.getStored()), Util.numFormat(energy.getCapacity())).mergeStyle(TextFormatting.DARK_GRAY)));
            list.add(new TranslationTextComponent("info.lollipop.generates").mergeStyle(TextFormatting.GRAY).append(Text.COLON).append(new StringTextComponent(Util.numFormat(this.te.getGeneration())).append(new TranslationTextComponent("info.lollipop.fe.pet.tick")).mergeStyle(TextFormatting.DARK_GRAY)));
            list.add(new TranslationTextComponent("info.lollipop.max.extract").mergeStyle(TextFormatting.GRAY).append(Text.COLON).append(new StringTextComponent(Util.numFormat(energy.getMaxExtract())).append(new TranslationTextComponent("info.lollipop.fe.pet.tick")).mergeStyle(TextFormatting.DARK_GRAY)));
            func_243308_b(matrix, list, mouseX, mouseY);
        }

        FluidTank tank = this.te.getTank();
        if (isMouseOver(mouseX - 157, mouseY - 5, 14, 65)) {
            List<ITextComponent> list = new ArrayList<>();
            if (!tank.isEmpty()) {
                list.add(new TranslationTextComponent("info.lollipop.coolant").mergeStyle(TextFormatting.GRAY).append(Text.COLON).append(tank.getFluid().getDisplayName().copyRaw().mergeStyle(TextFormatting.AQUA)));
                list.add(new TranslationTextComponent("info.lollipop.stored").mergeStyle(TextFormatting.GRAY).append(Text.COLON).append(new TranslationTextComponent("info.lollipop.mb.stored", Util.addCommas(tank.getFluidAmount()), Util.numFormat(tank.getCapacity())).mergeStyle(TextFormatting.DARK_GRAY)));
                list.add(new TranslationTextComponent("info.lollipop.temperature").mergeStyle(TextFormatting.GRAY).append(Text.COLON).append(new TranslationTextComponent("info.lollipop.temperature.c", "" + TextFormatting.AQUA + PowahAPI.getCoolant(tank.getFluid().getFluid())).mergeStyle(TextFormatting.DARK_GRAY)));
            } else {
                list.add(new TranslationTextComponent("info.lollipop.fluid").mergeStyle(TextFormatting.GRAY).append(Text.COLON).append(new StringTextComponent("---").mergeStyle(TextFormatting.DARK_GRAY)));
            }
            func_243308_b(matrix, list, mouseX, mouseY);
        }
    }
}
