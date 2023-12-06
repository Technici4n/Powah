package owmii.powah.util;

import com.google.common.primitives.Ints;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.capabilities.Capabilities;

public final class EnergyUtil {
    private EnergyUtil() {
    }

    public static boolean hasEnergy(Level level, BlockPos pos, Direction side) {
        var be = level.getBlockEntity(pos);
        return be != null && be.getCapability(Capabilities.ENERGY, side).isPresent();
    }

    public static long pushEnergy(Level level, BlockPos pos, Direction side, long howMuch) {
        var be = level.getBlockEntity(pos);
        var handler = be != null ? be.getCapability(Capabilities.ENERGY, side).orElse(null) : null;
        return handler != null ? handler.receiveEnergy(Ints.saturatedCast(howMuch), false) : 0;
    }
}
