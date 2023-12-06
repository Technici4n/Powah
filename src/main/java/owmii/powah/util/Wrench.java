package owmii.powah.util;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import owmii.powah.block.energizing.EnergizingOrbBlock;
import owmii.powah.data.ITags;
import owmii.powah.lib.block.AbstractEnergyBlock;

public final class Wrench {
    private Wrench() {
    }

    public static boolean removeWithWrench(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isSpectator() || !player.isShiftKeyDown() || !world.mayInteract(player, hitResult.getBlockPos())
                || !stack.is(ITags.Items.WRENCHES)) {
            return false;
        }
        var pos = hitResult.getBlockPos();
        var state = world.getBlockState(pos);
        if (state.getBlock() instanceof AbstractEnergyBlock<?, ?> || state.getBlock() instanceof EnergizingOrbBlock) {
            var entity = world.getBlockEntity(pos);
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            if (!player.isCreative()) {
                Block.dropResources(state, world, pos, entity);
            }
            // Play a cool sound
            var group = state.getSoundType();
            world.playSound(player, pos, group.getBreakSound(), SoundSource.BLOCKS, (group.getVolume() + 1.0F) / 2.0F,
                    group.getPitch() * 0.8F);
            return true;
        }
        return false;
    }
}
