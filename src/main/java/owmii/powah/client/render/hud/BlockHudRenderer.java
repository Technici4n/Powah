package owmii.powah.client.render.hud;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public interface BlockHudRenderer {
    boolean renderHud(GuiGraphicsExtractor gui, BlockState state, Level world, BlockPos pos, Player player, BlockHitResult result,
            @Nullable BlockEntity te);
}
