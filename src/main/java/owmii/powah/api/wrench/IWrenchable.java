package owmii.powah.api.wrench;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public interface IWrenchable {
    boolean onWrench(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, Direction side, WrenchMode mode, Vec3 hit);
}
