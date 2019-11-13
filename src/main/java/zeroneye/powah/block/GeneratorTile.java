package zeroneye.powah.block;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;

public abstract class GeneratorTile extends PowahTile {
    public GeneratorTile(TileEntityType<?> type) {
        super(type);
    }

    public int perTick() {
        Block block = getBlockState().getBlock();
        return block instanceof GeneratorBlock ? ((GeneratorBlock) block).perTick() : 0;
    }
}
