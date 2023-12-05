package owmii.powah.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public final class ClientUtils {
    private ClientUtils() {
    }

    @Nullable
    public static TextureAtlasSprite getStillTexture(FluidStack fluidStack) {
        var renderProps = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        var spriteId = renderProps.getStillTexture(fluidStack);
        if (spriteId == null) {
            return null;
        }
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                .apply(spriteId);
    }

    public static int getFluidColor(FluidStack fluidStack) {
        var renderProps = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        return renderProps.getTintColor(fluidStack);
    }
}
