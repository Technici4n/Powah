package owmii.powah.client.renderer.tile;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.util.math.V3d;
import owmii.powah.block.energizing.EnergizingOrbTile;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class EnergizingOrbRenderer extends TileEntityRenderer<EnergizingOrbTile> {
    @Override
    public void render(EnergizingOrbTile orb, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y, z);
        RenderHelper.disableStandardItemLighting();

        if (orb.hasWorld()) {
            Minecraft mc = Minecraft.getInstance();
            float ticks = (orb.ticks + partialTicks) / 200.0F;

            if (!orb.getInventory().isEmpty()) {
                ItemStack output = orb.getInventory().getStackInSlot(0);
                if (!output.isEmpty()) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translated(0.5D, 0.6D, 0.5D);
                    GlStateManager.scalef(0.4F, 0.4F, 0.4F);
                    GlStateManager.rotatef(-ticks * 360.0F, 0.0F, 1.0F, 0.0F);
                    mc.getItemRenderer().renderItem(output, ItemCameraTransforms.TransformType.FIXED);
                    GlStateManager.popMatrix();
                } else {
                    List<ItemStack> stacks = new ArrayList<>(orb.getInventory().getNonEmptyStacks());
                    List<V3d> circled = V3d.from(BlockPos.ZERO).circled(stacks.size(), 0.1D);
                    for (int i = 0; i < circled.size(); i++) {
                        V3d v3d1 = circled.get(i);
                        ItemStack stack = stacks.get(i);
                        if (!stack.isEmpty()) {
                            GlStateManager.pushMatrix();
                            if (stacks.size() == 1) {
                                GlStateManager.translated(0.5D, 0.6D, 0.5D);
                            } else {
                                GlStateManager.translated(v3d1.x + 0.5D, v3d1.y + 0.6D, v3d1.z + 0.5D);
                            }
                            GlStateManager.scalef(0.4F, 0.4F, 0.4F);
                            GlStateManager.rotatef(-ticks * 360.0F, 0, 1, 0);
                            mc.getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
                            GlStateManager.popMatrix();
                        }
                    }
                }
            }
        }
        RenderHelper.disableStandardItemLighting();

        GlStateManager.pushMatrix();
        GlStateManager.color3f(1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GlStateManager.translated(0.5D, 0.6D, 0.5D);
        GlStateManager.scalef(1.8F, 1.8F, 1.8F);
        bindTexture(EnergizingRodRenderer.CHARGE_TEXTURE);
        GlStateManager.depthMask(false);
        EnergizingRodRenderer.CHARGE_MODEL.render(0.0625F);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
