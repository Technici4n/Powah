package owmii.powah.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.lib.client.util.Render;
import owmii.lib.client.util.RenderTypes;
import owmii.lib.item.EnergyBlockItem;
import owmii.lib.util.Player;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.block.reactor.ReactorBlock;
import owmii.powah.client.render.tile.ReactorRenderer;
import owmii.powah.config.generator.ReactorConfig;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ReactorItem extends EnergyBlockItem<Tier, ReactorConfig, ReactorBlock> {
    public ReactorItem(ReactorBlock block, Properties properties, @Nullable ItemGroup group) {
        super(block, properties, group);
    }

    @Override
    public ActionResultType tryPlace(BlockItemUseContext context) {
        if (!context.canPlace()) return ActionResultType.FAIL;
        PlayerEntity player = context.getPlayer();
        if (player == null || Player.isFake(player)) return ActionResultType.FAIL;
        ItemStack stack = context.getItem();
        if (stack.getCount() < 36 && !player.isCreative()) {
            player.sendStatusMessage(new TranslationTextComponent("chat.powah.not.enough.blocks", "" + TextFormatting.YELLOW + (36 - stack.getCount()) + TextFormatting.RED).func_240701_a_(TextFormatting.RED), true);
            return ActionResultType.FAIL;
        }
        BlockPos pos = context.getPos();
        List<BlockPos> list = BlockPos.getAllInBox(pos.add(-1, 0, -1), pos.add(1, 3, 1))
                .map(BlockPos::toImmutable)
                .collect(Collectors.toList());

        for (BlockPos blockPos : list) {
            if (!context.getWorld().getBlockState(blockPos).getMaterial().isReplaceable()) return ActionResultType.FAIL;
        }
        List<LivingEntity> entities = context.getWorld().getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(pos).grow(1.0D, 3.0D, 1.0D));
        if (!entities.isEmpty()) return ActionResultType.FAIL;

        stack.shrink(35);
        return super.tryPlace(context);
    }

    static final ResourceLocation OV_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/misc/reactor_ov.png");

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void renderOv(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if (player == null || mc.world == null) return;
        MatrixStack matrix = event.getMatrixStack();
        boolean flag = false;
        boolean flag1 = false;
        for (Hand hand : Hand.values()) {
            ItemStack stack = player.getHeldItem(hand);
            if (stack.getItem() instanceof ReactorItem) {
                flag = true;
                if (stack.getCount() >= 36) {
                    flag1 = true;
                    break;
                }
                break;
            }
        }

        if (!flag) return;
        RayTraceResult result = mc.objectMouseOver;
        if (result instanceof BlockRayTraceResult) {
            BlockRayTraceResult br = (BlockRayTraceResult) result;
            boolean isReplaceable = mc.world.getBlockState(br.getPos()).getMaterial().isReplaceable() && !mc.world.isAirBlock(br.getPos());
            if (mc.world.isAirBlock(br.getPos()) || !isReplaceable && !br.getFace().equals(Direction.UP)) return;
            BlockPos pos = isReplaceable ? br.getPos() : br.getPos().offset(br.getFace());
            List<BlockPos> list = BlockPos.getAllInBox(pos.add(-1, 0, -1), pos.add(1, 3, 1)).map(BlockPos::toImmutable).collect(Collectors.toList());
            int color = 0x75e096;
            if (!flag1 && !player.isCreative()) color = 0xcf040e;
            if (color != 0xcf040e) {
                for (BlockPos blockPos : list) {
                    if (!mc.world.getBlockState(blockPos).getMaterial().isReplaceable()) {
                        color = 0xcf040e;
                        break;
                    }
                }
                List<LivingEntity> entities = mc.world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(pos).grow(1.0D, 3.0D, 1.0D));
                if (!entities.isEmpty()) {
                    color = 0xcf040e;
                }
            }
            matrix.push();
            Vector3d projectedView = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
            matrix.translate(-projectedView.x, -projectedView.y, -projectedView.z);
            matrix.translate(-1.0D, 0.001D, -1.0D);
            float r = (color >> 16 & 0xFF) / 255.0F;
            float g = (color >> 8 & 0xFF) / 255.0F;
            float b = (color & 0xFF) / 255.0F;
            RenderSystem.pushMatrix();
            IRenderTypeBuffer.Impl rtb = mc.getRenderTypeBuffers().getBufferSource();
            IVertexBuilder buffer = rtb.getBuffer(RenderTypes.getTextBlended(OV_TEXTURE));
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            buffer.pos(matrix.getLast().getMatrix(), pos.getX(), pos.getY(), pos.getZ() + 3).color(r, g, b, 1.0F).tex(0.0F, 1.0F).lightmap(Render.MAX_LIGHT).endVertex();
            buffer.pos(matrix.getLast().getMatrix(), pos.getX() + 3, pos.getY(), pos.getZ() + 3).color(r, g, b, 1.0F).tex(1.0F, 1.0F).lightmap(Render.MAX_LIGHT).endVertex();
            buffer.pos(matrix.getLast().getMatrix(), pos.getX() + 3, pos.getY(), pos.getZ()).color(r, g, b, 1.0F).tex(1.0F, 0.0F).lightmap(Render.MAX_LIGHT).endVertex();
            buffer.pos(matrix.getLast().getMatrix(), pos.getX(), pos.getY(), pos.getZ()).color(r, g, b, 1.0F).tex(0.0F, 0.0F).lightmap(Render.MAX_LIGHT).endVertex();
            rtb.finish(RenderTypes.getTextBlended(OV_TEXTURE));
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.popMatrix();
            matrix.pop();

        }
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderByItem(ItemStack stack, MatrixStack matrix, IRenderTypeBuffer rtb, int light, int ov) {
        matrix.push();
        matrix.translate(0.5D, 0.5D, 0.5D);
        matrix.scale(1.0F, -1.0F, -1.0F);
        IVertexBuilder buffer = rtb.getBuffer(ReactorRenderer.CUBE_MODEL.getRenderType(new ResourceLocation(Powah.MOD_ID, "textures/model/tile/reactor_block_" + getVariant().getName() + ".png")));
        ReactorRenderer.CUBE_MODEL.render(matrix, buffer, light, ov, 1.0F, 1.0F, 1.0F, 1.0F);
        matrix.pop();
    }
}
