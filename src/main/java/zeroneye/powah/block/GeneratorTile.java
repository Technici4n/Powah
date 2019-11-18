package zeroneye.powah.block;

import net.minecraft.tileentity.TileEntityType;

public abstract class GeneratorTile extends PowahTile {

    public GeneratorTile(TileEntityType<?> type, int capacity, int transfer, boolean canReceive, boolean canExtract, boolean isCreative) {
        super(type, capacity, 0, transfer, canReceive, canExtract, isCreative);
    }

    public int perTick() {
        return getBlock() instanceof GeneratorBlock ? ((GeneratorBlock) getBlock()).perTick() : 0;
    }
}
