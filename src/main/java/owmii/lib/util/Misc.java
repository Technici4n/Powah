package owmii.lib.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Misc {
    /**
     * Checks if the given block can see the sky.
     * If the block itself blocks the sky, e.g. doesn't propagate skylight down, then check the block above it instead.
     */
    public static boolean canBlockSeeSky(World world, BlockPos pos) {
        // world#canBlockSeeSky does not act like how it is named and is really used for guardian spawning check
        return world.canSeeSky(pos);
    }
}
