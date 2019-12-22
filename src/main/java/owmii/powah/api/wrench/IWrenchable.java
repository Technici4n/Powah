package owmii.powah.api.wrench;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface IWrenchable {
    boolean onWrench(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, Direction side, WrenchMode mode, Vec3d hit);
}
