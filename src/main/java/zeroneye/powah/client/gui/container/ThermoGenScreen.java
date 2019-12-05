package zeroneye.powah.client.gui.container;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import zeroneye.lib.client.util.Draw2D;
import zeroneye.lib.client.util.GL;
import zeroneye.powah.Powah;
import zeroneye.powah.api.PowahAPI;
import zeroneye.powah.block.generator.thermoelectric.ThermoGeneratorTile;
import zeroneye.powah.energy.PowahStorage;
import zeroneye.powah.inventory.ThermoGenContainer;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ThermoGenScreen extends PowahScreen<ThermoGenContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/thermo_generator.png");
    private static final ResourceLocation GUI_CONFIG_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/configuration_with_fluid.png");

    public ThermoGenScreen(ThermoGenContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    @Override
    protected void addMainButtons(int x, int y, int space) {
        super.addMainButtons(x - 23, y, space);
    }

    @Override
    protected void addSideConfig(int x, int y, int space) {
        super.addSideConfig(x - 23, y, space);
    }

    @Override
    protected void drawForeground(int mouseX, int mouseY) {
        super.drawForeground(mouseX, mouseY);
        ThermoGeneratorTile genTile = this.container.getInv();
        int percent = genTile.perTick() > 0 ? (100 * genTile.nextGen) / genTile.perTick() : 0;
        this.font.drawString(percent + "%" + " (" + genTile.nextGen + " EF/t)", 29, 10, 5592405);
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(partialTicks, mouseX, mouseY);
        ThermoGeneratorTile genTile = this.container.getInv();
        FluidTank tank = genTile.getTank();
        if (!tank.isEmpty()) {
            FluidStack fluidStack = tank.getFluid();
            Fluid fluid = fluidStack.getFluid();
            AtlasTexture textureMap = Minecraft.getInstance().getTextureMap();
            FluidAttributes fa = fluid.getAttributes();
            ResourceLocation still = fa.getStill(fluidStack);
            if (still != null) {
                int color = fa.getColor(fluidStack);
                GL.color(color);
                TextureAtlasSprite sprite = textureMap.getSprite(still);
                bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
                Draw2D.gaugeV(sprite, this.x + 158, this.y + 4, 14, 64, tank.getCapacity(), tank.getFluidAmount());
                GlStateManager.color3f(1.0F, 1.0F, 1.0F);
            }
        }


    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        super.renderHoveredToolTip(mouseX, mouseY);
        ThermoGeneratorTile genTile = this.container.getInv();
        FluidTank tank = genTile.getTank();
        if (!tank.isEmpty() && isMouseOver(mouseX - 157, mouseY - 3, 16, 66)) {
            PowahStorage storage = this.tile.getInternal();
            List<String> list = new ArrayList<>();
            list.add(TextFormatting.GRAY + I18n.format("info.powah.coolant", "" + TextFormatting.AQUA + tank.getFluid().getDisplayName().getString()));
            list.add(TextFormatting.GRAY + I18n.format("info.powah.stored.fluid", "" + TextFormatting.DARK_GRAY + tank.getFluidAmount(), tank.getCapacity()));
            int perSeq = PowahAPI.getCoolantFluid(tank.getFluid().getFluid());
            list.add(TextFormatting.GRAY + I18n.format("info.powah.cooling.mb", "" + TextFormatting.DARK_GRAY + perSeq));
            renderTooltip(list, mouseX, mouseY);
        }
    }

    @Override
    protected ResourceLocation getSubBackGroundImage() {
        return GUI_TEXTURE;
    }

    @Override
    protected ResourceLocation getConfigBackGroundImage() {
        return GUI_CONFIG_TEXTURE;
    }
}
