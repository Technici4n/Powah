package owmii.powah.item;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;
import owmii.lib.util.Player;
import owmii.lib.util.Text;
import owmii.lib.util.math.V3d;
import owmii.powah.Powah;
import owmii.powah.block.PowahBlock;
import owmii.powah.block.generator.GeneratorBlock;
import owmii.powah.client.renderer.tile.ReactorRenderer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ReactorItem extends PowahBlockItem {
    public ReactorItem(PowahBlock block, Properties properties, @Nullable ItemGroup group) {
        super(block, properties, group);
    }

    @Override
    public ActionResultType tryPlace(BlockItemUseContext context) {
        if (!context.canPlace()) return ActionResultType.FAIL;
        PlayerEntity player = context.getPlayer();
        if (player == null || Player.isFake(player)) return ActionResultType.FAIL;
        ItemStack stack = context.getItem();
        if (stack.getCount() < 36 && !player.isCreative()) {
            player.sendStatusMessage(new TranslationTextComponent("chat.powah.not.enough.blocks", "" + TextFormatting.YELLOW + (36 - stack.getCount()) + TextFormatting.RED).applyTextStyle(TextFormatting.RED), true);
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
            ActiveRenderInfo renderInfo = mc.gameRenderer.getActiveRenderInfo();
            double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
            double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
            double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
            BlockPos pos = isReplaceable ? br.getPos() : br.getPos().offset(br.getFace());
            V3d v3d = V3d.from(pos).centerD().down().down(0.6D);
            List<BlockPos> list = BlockPos.getAllInBox(pos.add(-1, 0, -1), pos.add(1, 3, 1))
                    .map(BlockPos::toImmutable)
                    .collect(Collectors.toList());
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

            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            double offset = mc.world.getBlockState(pos).getMaterial().isReplaceable() ? 0.0D : 0.0D;
            GlStateManager.translated(-x, -y, -z);
            GlStateManager.translated(v3d.x, v3d.y + offset, v3d.z);
            float r = (color >> 16 & 0xFF) / 255.0F;
            float g = (color >> 8 & 0xFF) / 255.0F;
            float b = (color & 0xFF) / 255.0F;
            GlStateManager.color4f(r, g, b, 0.6F);
            int j3 = (int) (15728880 / 1.5F);
            int k3 = j3 >> 16 & '\uffff';
            int l3 = j3 & '\uffff';
            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, k3, l3);
            mc.getTextureManager().bindTexture(OV_TEXTURE);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            double dim = 1.5D;
            buffer.pos(dim, 0.0D, dim).tex(1, 1).endVertex();
            buffer.pos(dim, 0.0D, -dim).tex(1, 0).endVertex();
            buffer.pos(-dim, 0.0D, -dim).tex(0, 0).endVertex();
            buffer.pos(-dim, 0.0D, dim).tex(0, 1).endVertex();
            tessellator.draw();
            GlStateManager.disableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.popMatrix();
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Map<String, Object[]> getBookInfo(ItemStack stack, Map<String, Object[]> lines) {
        if (getBlock().getCapacity() > 0)
            lines.put("info.powah.capacity", new Object[]{Text.addCommas(getBlock().getCapacity())});
        if (getBlock() instanceof GeneratorBlock)
            lines.put("info.powah.generation.factor", new Object[]{Text.addCommas(((GeneratorBlock) getBlock()).perTick())});
        int maxIn = getBlock().getMaxReceive();
        int maxOut = getBlock().getMaxExtract();
        lines.put("info.powah.max.io", new Object[]{(maxIn == maxOut ? Text.addCommas(maxOut) : maxIn == 0 || maxOut == 0 ? Text.addCommas(Math.max(maxIn, maxOut)) : (Text.addCommas(maxIn) + "/" + Text.addCommas(maxOut)))});
        return lines;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderByItem(ItemStack stack) {
        GlStateManager.pushMatrix();
        GlStateManager.translated(0.5D, 0.5D, 0.5D);
        GlStateManager.scalef(1.0F, -1.0F, -1.0F);
        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(Powah.MOD_ID, "textures/ter/reactor_block"
                + Objects.requireNonNull(getRegistryName()).getPath().substring(7) + ".png"));
        ReactorRenderer.CUBE_MODEL.render();
        GlStateManager.popMatrix();
    }
}
