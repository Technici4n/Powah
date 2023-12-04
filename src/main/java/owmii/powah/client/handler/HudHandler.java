package owmii.powah.client.handler;

import dev.architectury.event.events.client.ClientGuiEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import owmii.powah.lib.client.handler.IHud;
import owmii.powah.lib.client.handler.IHudItem;

public class HudHandler {
    public static void register() {
        ClientGuiEvent.RENDER_HUD.register((gui, tickDelta) -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen == null) {
                Player player = mc.player;
                Level world = mc.level;
                if (world != null && player != null) {
                    HitResult hit = mc.hitResult;
                    if (hit instanceof BlockHitResult result) {
                        BlockPos pos = result.getBlockPos();
                        BlockState state = world.getBlockState(pos);
                        if (state.getBlock() instanceof IHud) {
                            ((IHud) state.getBlock()).renderHud(gui, state, world, pos, player, result, world.getBlockEntity(pos));
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
        });
    }
}
