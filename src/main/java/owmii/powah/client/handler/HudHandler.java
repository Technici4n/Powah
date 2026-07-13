package owmii.powah.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.common.NeoForge;
import owmii.powah.client.PowahClient;

public class HudHandler {
    public static void register(PowahClient powahClient) {
        NeoForge.EVENT_BUS.addListener((RenderGuiEvent.Post event) -> {
            var gui = event.getGuiGraphics();
            Minecraft mc = Minecraft.getInstance();
            if (mc.gui.screen() == null) {
                Player player = mc.player;
                Level world = mc.level;
                if (world != null && player != null) {
                    HitResult hit = mc.hitResult;
                    if (hit instanceof BlockHitResult result) {
                        BlockPos pos = result.getBlockPos();
                        BlockState state = world.getBlockState(pos);
                        var blockHudRenderer = powahClient.getBlockHudRenderer(state);
                        if (blockHudRenderer != null) {
                            blockHudRenderer.renderHud(gui, state, world, pos, player, result, world.getBlockEntity(pos));
                        }
                        for (InteractionHand hand : InteractionHand.values()) {
                            ItemStack stack = player.getItemInHand(hand);
                            if (!stack.isEmpty()) {
                                var itemHudRenderer = powahClient.getItemHudRenderer(stack);
                                if (itemHudRenderer != null
                                        && itemHudRenderer.renderHud(world, pos, player, hand, result.getDirection(), result.getLocation())) {
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
