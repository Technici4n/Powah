package owmii.powah.lib.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public interface IHud {
    @Environment(EnvType.CLIENT)
    boolean renderHud(PoseStack matrix, BlockState state, Level world, BlockPos pos, Player player, BlockHitResult result, @Nullable BlockEntity te);
}
