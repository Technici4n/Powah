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
import zeroneye.powah.Powah;
import zeroneye.powah.api.PowahAPI;
import zeroneye.powah.block.generator.magmatic.MagmaticGenTile;
import zeroneye.powah.energy.PowahStorage;
import zeroneye.powah.inventory.MagmaticGenContainer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class MagmaticGenScreen extends PowahScreen<MagmaticGenContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/magmatic_generator.png");
    private static final ResourceLocation GUI_CONFIG_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/configuration_with_fluid.png");

    @Nullable
    private ResourceLocation fluidTexture;

    public MagmaticGenScreen(MagmaticGenContainer container, PlayerInventory playerInventory, ITextComponent name) {
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
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(partialTicks, mouseX, mouseY);
        MagmaticGenTile genTile = this.container.getInv();
        FluidTank tank = genTile.getTank();
        if (!tank.isEmpty()) {
            FluidStack fluidStack = tank.getFluid();
            Fluid fluid = fluidStack.getFluid();
            AtlasTexture textureMap = Minecraft.getInstance().getTextureMap();
            FluidAttributes fa = fluid.getAttributes();
            ResourceLocation still = fa.getStill(fluidStack);
            if (still != null) {
                int color = fa.getColor(fluidStack);
                float red = (color >> 16 & 0xFF) / 255.0F;
                float green = (color >> 8 & 0xFF) / 255.0F;
                float blue = (color & 0xFF) / 255.0F;
                GlStateManager.color3f(red, green, blue);
                TextureAtlasSprite sprite = textureMap.getSprite(still);
                bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
                Draw2D.gaugeV(sprite, x + 158, y + 4, 14, 64, tank.getCapacity(), tank.getFluidAmount());
                GlStateManager.color3f(1.0F, 1.0F, 1.0F);
            }
        }
        if (genTile.nextGen > 0 && !this.sideButtons[0].visible) {
            bindTexture(getSubBackGroundImage());
            FluidStack fluid = tank.getFluid();
            if (!fluid.isEmpty()) {
                if (genTile.nextGenCap >= genTile.nextGen) {
                    Draw2D.gaugeV(this.x + 85, this.y + 25, 11, 23, 0, 72, genTile.nextGenCap, genTile.nextGen);
                }
            }
        }
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        super.renderHoveredToolTip(mouseX, mouseY);
        MagmaticGenTile genTile = this.container.getInv();
        FluidTank tank = genTile.getTank();
        if (isMouseOver(mouseX - 85, mouseY - 25, 11, 23)) {
            List<String> list = new ArrayList<>();
            int perSeq = PowahAPI.getFluidHeat(tank.getFluid().getFluid());
            int percent = perSeq > 0 ? (100 * genTile.nextGen) / perSeq : 0;
            list.add(TextFormatting.GRAY + I18n.format("info.powah.buffer", "" + TextFormatting.DARK_GRAY + percent + "%"));
            renderTooltip(list, mouseX, mouseY);
        } else if (!tank.isEmpty() && isMouseOver(mouseX - 157, mouseY - 3, 16, 66)) {
            PowahStorage storage = this.tile.getInternal();
            List<String> list = new ArrayList<>();
            list.add(TextFormatting.GRAY + I18n.format("info.powah.fluid", "" + TextFormatting.GOLD + tank.getFluid().getDisplayName().getString()));
            list.add(TextFormatting.GRAY + I18n.format("info.powah.stored.fluid", "" + TextFormatting.DARK_GRAY + tank.getFluidAmount(), tank.getCapacity()));
            int perSeq = PowahAPI.getFluidHeat(tank.getFluid().getFluid());
            list.add(TextFormatting.GRAY + I18n.format("info.powah.per.mb", "" + TextFormatting.DARK_GRAY + perSeq, 100));
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
