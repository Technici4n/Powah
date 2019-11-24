package zeroneye.powah.client.renderer.tile;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import zeroneye.powah.block.generator.magmatic.MagmaticGenTile;

public class MagmaticGenRenderer extends TileEntityRenderer<MagmaticGenTile> {
    @Override
    public void render(MagmaticGenTile tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
        FluidTank tank = tileEntityIn.getTank();
        FluidStack fluidStack = tank.getFluid();
        if (!fluidStack.isEmpty()) {
            Fluid fluid = fluidStack.getFluid();
            ResourceLocation still = fluid.getAttributes().getStill(fluidStack);
            if (still != null) {
                GlStateManager.pushMatrix();
                GlStateManager.translated(x, y + 0.51F, z);
                RenderHelper.disableStandardItemLighting();
                float fill = (tank.getFluidAmount() * (0.45F)) / tank.getCapacity();
                GlStateManager.translated(0.5D, fill, 0.5D);
                AtlasTexture textureMap = Minecraft.getInstance().getTextureMap();
                TextureAtlasSprite sprite = textureMap.getSprite(still);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
                int j3 = (int) (15728880 / 1.5F);
                int k3 = j3 >> 16 & '\uffff';
                int l3 = j3 & '\uffff';
                GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, k3, l3);
                bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
                renderQuad(sprite, 0.65D);
                RenderHelper.enableStandardItemLighting();
                GlStateManager.popMatrix();
            }

        }
    }

    public static void renderQuad(TextureAtlasSprite texture, double dim) {
        float f0 = texture.getMinU();
        float f1 = texture.getMaxU();
        float f2 = texture.getMinV();
        float f3 = texture.getMaxV();
        dim /= 2.0D;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        buffer.pos(-dim, 0.0D, -dim).tex((double) f0, (double) f3).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(-dim, 0.0D, dim).tex((double) f1, (double) f3).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(dim, 0.0D, dim).tex((double) f1, (double) f2).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(dim, 0.0D, -dim).tex((double) f0, (double) f2).normal(0.0F, 1.0F, 0.0F).endVertex();
        tessellator.draw();
    }
}
