package owmii.lib.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public interface IHud {
    @OnlyIn(Dist.CLIENT)
    boolean renderHud(MatrixStack matrix, BlockState state, World world, BlockPos pos, PlayerEntity player, BlockRayTraceResult result, @Nullable TileEntity te);
}
