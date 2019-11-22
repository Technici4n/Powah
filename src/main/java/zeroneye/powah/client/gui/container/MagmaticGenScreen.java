package zeroneye.powah.client.gui.container;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import zeroneye.lib.client.util.Draw2D;
import zeroneye.powah.Powah;
import zeroneye.powah.block.generator.magmatic.MagmaticGenTile;
import zeroneye.powah.inventory.MagmaticGenContainer;

import javax.annotation.Nullable;

public class MagmaticGenScreen extends PowahScreen<MagmaticGenContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/magmatic_generator.png");
    private static final ResourceLocation GUI_CONFIG_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/configuration_with_fluid.png");

    @Nullable
    private ResourceLocation fluidTexture;

    public MagmaticGenScreen(MagmaticGenContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    @Override
    protected void init() {
        super.init();
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
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        MagmaticGenTile genTile = container.getInv();
        FluidTank tank = genTile.getTank();
        if (!tank.isEmpty()) {
            FluidStack fluidStack = tank.getFluid();
            Fluid fluid = fluidStack.getFluid();
            AtlasTexture textureMap = Minecraft.getInstance().getTextureMap();
            ResourceLocation still = fluid.getAttributes().getStill(fluidStack);
            if (still != null) {
                TextureAtlasSprite sprite = textureMap.getSprite(still);
                bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
                Draw2D.gaugeV(sprite, x + 158, y + 4, 14, 64, tank.getCapacity(), tank.getFluidAmount());
            }
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
