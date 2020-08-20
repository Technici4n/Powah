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
import owmii.lib.client.screen.AbstractEnergyScreen;
import owmii.lib.client.screen.widget.IconButton;
import owmii.lib.client.util.Draw;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.util.Util;
import owmii.powah.Powah;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.reactor.ReactorTile;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.ReactorContainer;
import owmii.powah.network.packet.SwitchGenModePacket;

import java.util.ArrayList;
import java.util.List;

public class ReactorScreen extends AbstractEnergyScreen<ReactorTile, ReactorContainer> {
    private IconButton modeButton = IconButton.EMPTY;

    public ReactorScreen(ReactorContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, Textures.REACTOR);
    }

    @Override
    protected void init() {
        super.init();
        this.modeButton = addButton(new IconButton(this.guiLeft - 11, this.guiTop + 10, Textures.REACTOR_GEN_MODE.get(this.te.isGenModeOn()), b -> {
            Powah.NET.toServer(new SwitchGenModePacket(this.te.getPos()));
            this.te.setGenModeOn(!this.te.isGenModeOn());
        }, this).setTooltip(tooltip -> {
            tooltip.add(new TranslationTextComponent("info.powah.gen.mode." + this.te.isGenModeOn(), this.te.isGenModeOn() ? TextFormatting.DARK_GRAY : TextFormatting.DARK_RED).mergeStyle(TextFormatting.GRAY));
            tooltip.add(new TranslationTextComponent("info.powah.gen.mode").mergeStyle(TextFormatting.DARK_GRAY));
        }));
    }

    @Override
    public void tick() {
        super.tick();
        this.modeButton.setTexture(Textures.REACTOR_GEN_MODE.get(this.te.isGenModeOn()));
    }

    @Override
    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Textures.REACTOR_GAUGE.drawScalableH(matrix, this.te.getEnergy().subSized(), this.guiLeft + 5, this.guiTop + 5);
        Textures.REACTOR_GAUGE_URN.drawScalableH(matrix, this.te.fuel.subSized(), this.guiLeft + 103, this.guiTop + 13);
        Textures.REACTOR_GAUGE_CARBON.drawScalableH(matrix, this.te.carbon.subSized(), this.guiLeft + 51, this.guiTop + 6);
        Textures.REACTOR_GAUGE_REDSTONE.drawScalableH(matrix, this.te.redstone.subSized(), this.guiLeft + 51, this.guiTop + 52);
        Textures.REACTOR_GAUGE_COOLANT.drawScalableH(matrix, this.te.solidCoolant.subSized(), this.guiLeft + 140, this.guiTop + 52);
        Textures.REACTOR_GAUGE_TEMP.drawScalableH(matrix, this.te.temp.subSized(), this.guiLeft + 114, this.guiTop + 28);

        Textures.REACTOR_GEN_MODE_BG.draw(matrix, this.modeButton.x - 4, this.modeButton.y - 4);

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
    }

    @Override
    protected void func_230459_a_(MatrixStack matrix, int mouseX, int mouseY) {
        super.func_230459_a_(matrix, mouseX, mouseY);
        if (Textures.REACTOR_GAUGE.isMouseOver(this.guiLeft + 5, this.guiTop + 5, mouseX, mouseY)) {
            List<ITextComponent> list = new ArrayList<>();
            Energy energy = this.te.getEnergy();
            list.add(new TranslationTextComponent("info.lollipop.stored.energy.fe", TextFormatting.GRAY + Util.addCommas(energy.getStored()), TextFormatting.GRAY + Util.numFormat(energy.getCapacity())).mergeStyle(TextFormatting.DARK_GRAY));
            list.add(new TranslationTextComponent("info.lollipop.generates", TextFormatting.GRAY + Util.numFormat((long) this.te.calcProduction())).mergeStyle(TextFormatting.DARK_GRAY));
            list.add(new TranslationTextComponent("info.lollipop.max.transfer.fe", TextFormatting.GRAY + Util.numFormat(energy.getMaxExtract())).mergeStyle(TextFormatting.DARK_GRAY));
            renderTooltip(matrix, list, mouseX, mouseY);
        }

        if (Textures.REACTOR_GAUGE_TEMP.isMouseOver(this.guiLeft + 114, this.guiTop + 28, mouseX, mouseY)) {
            List<ITextComponent> list = new ArrayList<>();
            list.add(new StringTextComponent(TextFormatting.GRAY + String.format("%.1f", this.te.temp.getTicks()) + " C"));
            renderTooltip(matrix, list, mouseX, mouseY);
        }

        if (Textures.REACTOR_GAUGE_URN.isMouseOver(this.guiLeft + 103, this.guiTop + 13, mouseX, mouseY)) {
            List<ITextComponent> list = new ArrayList<>();
            list.add(new TranslationTextComponent("item.powah.uraninite").mergeStyle(TextFormatting.GREEN));
            list.add(new TranslationTextComponent("info.lollipop.fluid.stored", TextFormatting.DARK_GRAY + String.format("%.0f", this.te.fuel.getTicks()), String.format("%.0f", this.te.fuel.getMax())).mergeStyle(TextFormatting.DARK_GRAY));
            list.add(new TranslationTextComponent("info.lollipop.fluid.using.per.tick", TextFormatting.GREEN + String.format("%.4f", this.te.calcConsumption())).mergeStyle(TextFormatting.DARK_GRAY));
            renderTooltip(matrix, list, mouseX, mouseY);
        }

        if (Textures.REACTOR_GAUGE_CARBON.isMouseOver(this.guiLeft + 51, this.guiTop + 6, mouseX, mouseY)) {
            List<ITextComponent> list = new ArrayList<>();
            boolean b = this.te.carbon.isEmpty();
            list.add((new TranslationTextComponent("info.powah.carbon")).mergeStyle(TextFormatting.GRAY));
            list.add(new TranslationTextComponent("info.lollipop.fluid.stored", TextFormatting.DARK_GRAY + String.format("%.1f", this.te.carbon.getTicks()), String.format("%.1f", this.te.carbon.getMax())).mergeStyle(TextFormatting.DARK_GRAY));
            list.add(new StringTextComponent(""));
            list.add(new TranslationTextComponent("enchantment.minecraft.efficiency").mergeStyle(TextFormatting.DARK_AQUA));
            list.add(new StringTextComponent(TextFormatting.DARK_RED + (b ? "+0 C" : "+180 C")));
            renderTooltip(matrix, list, mouseX, mouseY);
        }

        if (Textures.REACTOR_GAUGE_REDSTONE.isMouseOver(this.guiLeft + 51, this.guiTop + 52, mouseX, mouseY)) {
            List<ITextComponent> list = new ArrayList<>();
            boolean b = this.te.redstone.isEmpty();
            list.add(new TranslationTextComponent("info.powah.redstone").mergeStyle(TextFormatting.GRAY));
            list.add(new TranslationTextComponent("info.lollipop.fluid.stored", TextFormatting.DARK_GRAY + String.format("%.1f", this.te.redstone.getTicks()), String.format("%.1f", this.te.redstone.getMax())).mergeStyle(TextFormatting.DARK_GRAY));
            list.add(new StringTextComponent(""));
            list.add(new TranslationTextComponent("info.powah.production").mergeStyle(TextFormatting.DARK_AQUA));
            list.add(new TranslationTextComponent("info.powah.fuel.consumption").mergeStyle(TextFormatting.DARK_RED));
            list.add(new StringTextComponent(TextFormatting.DARK_RED + (b ? "+0 C" : "+120 C")));
            renderTooltip(matrix, list, mouseX, mouseY);
        }

        if (Textures.REACTOR_GAUGE_COOLANT.isMouseOver(this.guiLeft + 140, this.guiTop + 52, mouseX, mouseY)) {
            List<ITextComponent> list = new ArrayList<>();
            list.add(new TranslationTextComponent("info.powah.solid.coolant").mergeStyle(TextFormatting.GRAY));
            list.add(new TranslationTextComponent("info.lollipop.fluid.stored", TextFormatting.DARK_GRAY + String.format("%.1f", this.te.solidCoolant.getTicks()), String.format("%.1f", this.te.solidCoolant.getMax())).mergeStyle(TextFormatting.DARK_GRAY));
            list.add(new StringTextComponent("" + TextFormatting.AQUA + this.te.solidCoolantTemp + " C"));
            renderTooltip(matrix, list, mouseX, mouseY);
        }

        FluidTank tank = this.te.getTank();
        if (isMouseOver(mouseX - 157, mouseY - 5, 14, 65)) {
            List<ITextComponent> list = new ArrayList<>();
            if (!tank.isEmpty()) {
                list.add(new TranslationTextComponent("info.lollipop.fluid", TextFormatting.AQUA + tank.getFluid().getDisplayName().getString()).mergeStyle(TextFormatting.GRAY));
                list.add(new TranslationTextComponent("info.lollipop.fluid.stored", "" + tank.getFluidAmount(), tank.getCapacity()).mergeStyle(TextFormatting.DARK_GRAY));
                list.add(new TranslationTextComponent("info.lollipop.temperature", "" + TextFormatting.GRAY + PowahAPI.getCoolant(tank.getFluid().getFluid()), "100").mergeStyle(TextFormatting.DARK_GRAY));
            } else {
                list.add(new TranslationTextComponent("info.lollipop.fluid", TextFormatting.DARK_GRAY + "----").mergeStyle(TextFormatting.DARK_GRAY));
            }
            renderTooltip(matrix, list, mouseX, mouseY);
        }
    }
}
