package zeroneye.powah.client.renderer.tile;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import zeroneye.lib.util.Energy;
import zeroneye.powah.Powah;
import zeroneye.powah.block.cable.CableBlock;
import zeroneye.powah.block.cable.CableTile;
import zeroneye.powah.client.model.CableFaceModel;
import zeroneye.powah.item.WrenchItem;

@OnlyIn(Dist.CLIENT)
public class CableRenderer extends TileEntityRenderer<CableTile> {
    public static final ResourceLocation FC = new ResourceLocation(Powah.MOD_ID, "textures/ter/cable_face_all.png");
    private static final CableFaceModel MODEL = new CableFaceModel();

    @Override
    public void render(CableTile cable, double x, double y, double z, float partialTicks, int destroyStage) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (cable.getWorld() != null) {
            World world = cable.getWorld();
            GlStateManager.pushMatrix();
            GlStateManager.translated(x, y, z);
            GlStateManager.translatef(0.5F, -0.5F, 0.5F);
            GlStateManager.enableRescaleNormal();
            for (Direction side : Direction.values()) {
                final BlockPos pos = cable.getPos().offset(side);
                final BlockState state = world.getBlockState(pos);
                if (!(state.getBlock() instanceof CableBlock) && Energy.isPresent(getWorld().getTileEntity(pos), side)) {
                    ItemStack stack = player.getHeldItemMainhand();
                    if (stack.getItem() instanceof WrenchItem) {
                        bindTexture(new ResourceLocation(Powah.MOD_ID, "textures/ter/cable_face_" + cable.getSideConfig().getPowerMode(side).name().toLowerCase() + ".png"));
                    } else {
                        bindTexture(FC);
                    }
                    MODEL.render(side, 0.0625F, 0);
                }
            }
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
        }
    }
}
