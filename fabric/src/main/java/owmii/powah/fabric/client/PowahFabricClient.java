package owmii.powah.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.client.player.ClientPickBlockGatherCallback;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import owmii.powah.block.Blcks;
import owmii.powah.client.PowahClient;
import owmii.powah.client.handler.ReactorOverlayHandler;
import owmii.powah.client.render.tile.ReactorItemRenderer;
import owmii.powah.item.ReactorItem;
import owmii.powah.lib.block.AbstractBlock;

public class PowahFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PowahClient.init();
        PowahClient.clientSetup();

        var reactorRenderer = new ReactorItemRenderer();
        Blcks.REACTOR.getAll().forEach(block -> {
            var item = (ReactorItem) Registry.ITEM.get(Registry.BLOCK.getKey(block));
            BuiltinItemRendererRegistry.INSTANCE.register(item, reactorRenderer::renderByItem);
        });

        WorldRenderEvents.LAST.register(context -> {
            ReactorOverlayHandler.onRenderLast(context.matrixStack());
        });

        ClientPickBlockGatherCallback.EVENT.register((player, result) -> {
            if (result instanceof BlockHitResult bhr) {
                var level = player.level;
                if (level.getBlockState(bhr.getBlockPos()).getBlock() instanceof AbstractBlock<?, ?>abstractBlock) {
                    return abstractBlock.getCloneItemStack(level, bhr.getBlockPos());
                }
            }
            return ItemStack.EMPTY;
        });
    }
}
