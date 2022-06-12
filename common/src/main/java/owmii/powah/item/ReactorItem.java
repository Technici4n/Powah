package owmii.powah.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.AABB;
import owmii.powah.config.v2.types.GeneratorConfig;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.util.Player;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.block.reactor.ReactorBlock;
import owmii.powah.client.render.tile.ReactorItemRenderer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class ReactorItem extends EnergyBlockItem<GeneratorConfig, ReactorBlock> {
    public ReactorItem(ReactorBlock block, Properties properties, @Nullable CreativeModeTab group) {
        super(block, properties, group);
    }

    /* TODO ARCH
    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return new ReactorItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher());
            }
        });
    }
     */

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        if (!context.canPlace()) return InteractionResult.FAIL;
        net.minecraft.world.entity.player.Player player = context.getPlayer();
        if (player == null || Player.isFake(player)) return InteractionResult.FAIL;
        ItemStack stack = context.getItemInHand();
        if (stack.getCount() < 36 && !player.isCreative()) {
            player.displayClientMessage(new TranslatableComponent("chat.powah.not.enough.blocks", "" + ChatFormatting.YELLOW + (36 - stack.getCount()) + ChatFormatting.RED).withStyle(ChatFormatting.RED), true);
            return InteractionResult.FAIL;
        }
        BlockPos pos = context.getClickedPos();
        List<BlockPos> list = BlockPos.betweenClosedStream(pos.offset(-1, 0, -1), pos.offset(1, 3, 1))
                .map(BlockPos::immutable)
                .collect(Collectors.toList());

        for (BlockPos blockPos : list) {
            if (!context.getLevel().getBlockState(blockPos).getMaterial().isReplaceable()) return InteractionResult.FAIL;
        }
        List<LivingEntity> entities = context.getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(1.0D, 3.0D, 1.0D));
        if (!entities.isEmpty()) return InteractionResult.FAIL;

        stack.shrink(35);
        return super.place(context);
    }

    static final ResourceLocation OV_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/misc/reactor_ov.png");

    /* TODO ARCH
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void renderOv(RenderLevelLastEvent event) {
        Minecraft mc = Minecraft.getInstance();
        net.minecraft.world.entity.player.Player player = mc.player;
        if (player == null || mc.level == null) return;
        PoseStack matrix = event.getPoseStack();
        boolean flag = false;
        boolean flag1 = false;
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = player.getItemInHand(hand);
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
        HitResult result = mc.hitResult;
        if (result instanceof BlockHitResult br) {
            boolean isReplaceable = mc.level.getBlockState(br.getBlockPos()).getMaterial().isReplaceable() && !mc.level.isEmptyBlock(br.getBlockPos());
            if (mc.level.isEmptyBlock(br.getBlockPos()) || !isReplaceable && !br.getDirection().equals(Direction.UP)) return;
            BlockPos pos = isReplaceable ? br.getBlockPos() : br.getBlockPos().relative(br.getDirection());
            List<BlockPos> list = BlockPos.betweenClosedStream(pos.offset(-1, 0, -1), pos.offset(1, 3, 1)).map(BlockPos::immutable).collect(Collectors.toList());
            int color = 0x75e096;
            if (!flag1 && !player.isCreative()) color = 0xcf040e;
            if (color != 0xcf040e) {
                for (BlockPos blockPos : list) {
                    if (!mc.level.getBlockState(blockPos).getMaterial().isReplaceable()) {
                        color = 0xcf040e;
                        break;
                    }
                }
                List<LivingEntity> entities = mc.level.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(1.0D, 3.0D, 1.0D));
                if (!entities.isEmpty()) {
                    color = 0xcf040e;
                }
            }
            matrix.pushPose();
            Vec3 projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            matrix.translate(-projectedView.x, -projectedView.y, -projectedView.z);
            matrix.translate(-1.0D, 0.001D, -1.0D);
            float r = (color >> 16 & 0xFF) / 255.0F;
            float g = (color >> 8 & 0xFF) / 255.0F;
            float b = (color & 0xFF) / 255.0F;
            MultiBufferSource.BufferSource rtb = mc.renderBuffers().bufferSource();
            VertexConsumer buffer = rtb.getBuffer(RenderTypes.getTextBlended(OV_TEXTURE));
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            buffer.vertex(matrix.last().pose(), pos.getX(), pos.getY(), pos.getZ() + 3).color(r, g, b, 1.0F).uv(0.0F, 1.0F).uv2(Render.MAX_LIGHT).endVertex();
            buffer.vertex(matrix.last().pose(), pos.getX() + 3, pos.getY(), pos.getZ() + 3).color(r, g, b, 1.0F).uv(1.0F, 1.0F).uv2(Render.MAX_LIGHT).endVertex();
            buffer.vertex(matrix.last().pose(), pos.getX() + 3, pos.getY(), pos.getZ()).color(r, g, b, 1.0F).uv(1.0F, 0.0F).uv2(Render.MAX_LIGHT).endVertex();
            buffer.vertex(matrix.last().pose(), pos.getX(), pos.getY(), pos.getZ()).color(r, g, b, 1.0F).uv(0.0F, 0.0F).uv2(Render.MAX_LIGHT).endVertex();
            rtb.endBatch(RenderTypes.getTextBlended(OV_TEXTURE));
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            matrix.popPose();

        }
    }
     */
}
