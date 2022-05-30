package owmii.lib.client.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IHudItem {
    @OnlyIn(Dist.CLIENT)
    boolean renderHud(World world, BlockPos pos, PlayerEntity player, Hand hand, Direction side, Vector3d hit);
}
