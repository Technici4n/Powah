package owmii.powah.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import owmii.powah.block.energizing.EnergizingOrbTile;
import owmii.powah.client.model.OrbModel;
import owmii.powah.client.model.PowahLayerDefinitions;
import owmii.powah.lib.client.renderer.tile.AbstractTileRenderer;
import owmii.powah.lib.logistics.inventory.Inventory;
import owmii.powah.util.math.V3d;

public class EnergizingOrbRenderer extends AbstractTileRenderer<EnergizingOrbTile> {
    private final OrbModel model;

    protected EnergizingOrbRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        model = new OrbModel(context.bakeLayer(PowahLayerDefinitions.ORB));
    }

    @Override
    public void render(EnergizingOrbTile te, float pt, PoseStack matrix, MultiBufferSource rtb, Minecraft mc, ClientLevel world, LocalPlayer player,
            int light, int ov) {
        Inventory inv = te.getInventory();
        Direction up = te.getOrbUp();
        double x = 0.5 + up.getStepX() * 0.1;
        double y = 0.5 + up.getStepY() * 0.1;
        double z = 0.5 + up.getStepZ() * 0.1;

        if (!inv.isEmpty()) {
            float ticks = (te.ticks + pt) / 200.0F;
            ItemStack output = inv.getStackInSlot(0);
            if (!output.isEmpty()) {
                matrix.pushPose();
                matrix.translate(x, y, z);
                matrix.mulPose(Axis.YP.rotationDegrees(-ticks * 360.0F));
                matrix.scale(0.35F, 0.35F, 0.35F);
                mc.getItemRenderer().renderStatic(output, ItemDisplayContext.FIXED, light, ov, matrix, rtb, world, 0);
                matrix.popPose();
            } else {
                List<ItemStack> stacks = new ArrayList<>(inv.getNonEmptyStacks());
                List<V3d> circled = V3d.from(Vec3.ZERO).circled(stacks.size(), 0.1D);
                for (int i = 0; i < circled.size(); i++) {
                    V3d v3d1 = circled.get(i);
                    ItemStack stack = stacks.get(i);
                    if (!stack.isEmpty()) {
                        matrix.pushPose();
                        if (stacks.size() == 1) {
                            matrix.translate(x, y, z);
                        } else {
                            matrix.translate(v3d1.x + x, v3d1.y + y, v3d1.z + z);
                        }
                        matrix.scale(0.35F, 0.35F, 0.35F);
                        matrix.mulPose(Axis.YP.rotationDegrees(-ticks * 360.0F));
                        mc.getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, light, ov, matrix, rtb, world, 0);
                        matrix.popPose();
                    }
                }
            }
        }

        matrix.pushPose();
        matrix.translate(0.5D, 0.5D, 0.5D);
        matrix.mulPose(up.getRotation());
        matrix.translate(0.0D, 0.1D, 0.0D);
        matrix.scale(1.8F, 1.8F, 1.8F);
        model.render(te, this, matrix, rtb, light, ov);
        matrix.popPose();
    }
}
