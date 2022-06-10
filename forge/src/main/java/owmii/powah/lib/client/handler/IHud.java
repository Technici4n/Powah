package owmii.powah.lib.client.handler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;

public interface IHud {
    @OnlyIn(Dist.CLIENT)
    boolean renderHud(PoseStack matrix, BlockState state, Level world, BlockPos pos, Player player, BlockHitResult result, @Nullable BlockEntity te);
}
