package owmii.powah.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.SharedConstants;
import net.minecraft.world.InteractionResult;
import owmii.powah.Powah;
import owmii.powah.lib.item.ItemBase;
import owmii.powah.lib.util.Wrench;

public class PowahFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This prevents the annoying "No data fixer registered for charged_snowball"
        var checkDataFixer = SharedConstants.CHECK_DATA_FIXER_SCHEMA;
        SharedConstants.CHECK_DATA_FIXER_SCHEMA = false;

        Powah.init();

        SharedConstants.CHECK_DATA_FIXER_SCHEMA = checkDataFixer;

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (Wrench.removeWithWrench(player, world, hand, hitResult)) {
                return InteractionResult.sidedSuccess(world.isClientSide);
            }

            var stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof ItemBase itemBase) {
                return itemBase.onItemUseFirst(stack, player.level(), hitResult.getBlockPos(), player, hand, hitResult.getDirection(),
                        hitResult.getLocation());
            }
            return InteractionResult.PASS;
        });
    }
}
