package owmii.powah.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.fluids.FluidStack;

public final class ClientUtils {
    private ClientUtils() {
    }

    public static TextureAtlasSprite getStillTexture(FluidStack fluidStack) {
        FluidState fluidState = fluidStack.getFluid().defaultFluidState();
        FluidModel fluidModel = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluidState);
        return fluidModel.stillMaterial().sprite();
    }

    public static int getFluidColor(FluidStack fluidStack) {
        FluidState fluidState = fluidStack.getFluid().defaultFluidState();
        FluidModel fluidModel = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluidState);
        FluidTintSource fluidTintSource = fluidModel.fluidTintSource();
        if (fluidTintSource != null)
            return fluidTintSource.color(fluidStack.getFluid().defaultFluidState());
        else
            return 0xFFFFFFFF;
    }
}
