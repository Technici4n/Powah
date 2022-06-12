package owmii.powah.client.render.tile;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.FluidStackHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import owmii.powah.lib.client.renderer.tile.AbstractTileRenderer;
import owmii.powah.lib.client.util.Render;
import owmii.powah.block.magmator.MagmatorTile;

public class MagmatorRenderer extends AbstractTileRenderer<MagmatorTile> {
    protected MagmatorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(MagmatorTile te, float pt, PoseStack matrix, MultiBufferSource rtb, Minecraft mc, ClientLevel world, LocalPlayer player, int light, int ov) {
        var tank = te.getTank();
        if (!tank.isEmpty()) {
            FluidStack fluidStack = tank.getFluid();
            var sprite = FluidStackHooks.getStillTexture(fluidStack);
            if (sprite != null) {
                int color = FluidStackHooks.getColor(fluidStack);
                float red = (color >> 16 & 0xFF) / 255.0F;
                float green = (color >> 8 & 0xFF) / 255.0F;
                float blue = (color & 0xFF) / 255.0F;
                RenderSystem.setShaderColor(red, green, blue, 1);
                VertexConsumer buffer = rtb.getBuffer(RenderType.text(sprite.atlas().location()));
                matrix.pushPose();
                float fill = (tank.getFluidAmount() * (0.45F)) / tank.getCapacity();
                matrix.translate(0.1875f, 0.51D + fill, 0.1875f);
                matrix.scale(0.625f, 1.0F, 0.625f);
                Render.quad(matrix.last().pose(), buffer, sprite, 1.0F, 1.0F, red, green, blue);
                matrix.popPose();
                RenderSystem.setShaderColor(1, 1, 1, 1);
            }
        }
    }
}
