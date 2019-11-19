package zeroneye.powah.block.generator;

import net.minecraft.tileentity.TileEntityType;
import zeroneye.powah.block.PowahTile;

public abstract class GeneratorTile extends PowahTile {
    public GeneratorTile(TileEntityType<?> type, int capacity, int transfer) {
        super(type, capacity, 0, transfer, false);
    }

    @Override
    protected void postTicks() {
        super.postTicks();
        if (doGenerate()) {
            generate();
        }
    }

    protected abstract boolean doGenerate();

    protected void generate() {
        int stored = getInternal().getEnergyStored();
        int capacity = getInternal().getMaxEnergyStored();
        getInternal().setEnergy(stored + perTick());
    }

    public int perTick() {
        return getInternal().getMaxExtract();
    }
}
