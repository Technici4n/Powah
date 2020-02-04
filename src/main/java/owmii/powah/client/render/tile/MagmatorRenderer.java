package owmii.powah.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import owmii.lib.client.renderer.tile.AbstractTileRenderer;
import owmii.lib.client.util.Render;
import owmii.powah.block.magmator.MagmatorTile;

public class MagmatorRenderer extends AbstractTileRenderer<MagmatorTile> {
    public MagmatorRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(MagmatorTile te, float pt, MatrixStack matrix, IRenderTypeBuffer rtb, Minecraft mc, ClientWorld world, ClientPlayerEntity player, int light, int ov) {
        FluidTank tank = te.getTank();
        if (!tank.isEmpty()) {
            FluidStack fluidStack = new FluidStack(Fluids.LAVA, 1);
            FluidAttributes fa = fluidStack.getFluid().getAttributes();
            ResourceLocation still = fa.getStillTexture(fluidStack);
            if (still != null) {
                int color = fa.getColor(fluidStack);
                float red = (color >> 16 & 0xFF) / 255.0F;
                float green = (color >> 8 & 0xFF) / 255.0F;
                float blue = (color & 0xFF) / 255.0F;
                RenderSystem.color3f(red, green, blue);
                TextureAtlasSprite sprite = mc.getTextureGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(still);
                IVertexBuilder buffer = rtb.getBuffer(RenderType.text(sprite.getAtlasTexture().getBasePath()));
                matrix.push();
                float fill = (tank.getFluidAmount() * (0.45F)) / tank.getCapacity();
                matrix.translate(0.1875f, 0.51D + fill, 0.1875f);
                matrix.scale(0.625f, 1.0F, 0.625f);
                Render.quad(matrix.getLast().getPositionMatrix(), buffer, sprite, 1.0F, 1.0F, red, green, blue);
                matrix.pop();
                RenderSystem.color3f(1.0F, 1.0F, 1.0F);
            }
        }
    }
}
