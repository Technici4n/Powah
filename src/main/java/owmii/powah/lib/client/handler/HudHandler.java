package owmii.powah.lib.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class HudHandler {
    @SubscribeEvent
    public static void renderHud(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL && mc.screen == null) {
            Player player = mc.player;
            Level world = mc.level;
            if (world != null && player != null) {
                HitResult hit = mc.hitResult;
                if (hit instanceof BlockHitResult) {
                    BlockHitResult result = (BlockHitResult) hit;
                    BlockPos pos = result.getBlockPos();
                    Vec3 sHit = result.getLocation();
                    BlockState state = world.getBlockState(pos);
                    if (state.getBlock() instanceof IHud) {
                        ((IHud) state.getBlock()).renderHud(event.getMatrixStack(), state, world, pos, player, result, world.getBlockEntity(pos));
                    }
                    for (InteractionHand hand : InteractionHand.values()) {
                        ItemStack stack = player.getItemInHand(hand);
                        if (stack.getItem() instanceof IHudItem) {
                            if (((IHudItem) stack.getItem()).renderHud(world, pos, player, hand, result.getDirection(), result.getLocation())) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
