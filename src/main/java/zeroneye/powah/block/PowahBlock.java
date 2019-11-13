package zeroneye.powah.block;

import zeroneye.lib.block.BlockBase;

public abstract class PowahBlock extends BlockBase {
    protected final int capacity;
    protected final int transfer;

    public PowahBlock(Properties properties, int capacity, int transfer) {
        super(properties);
        this.capacity = capacity;
        this.transfer = transfer;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getTransfer() {
        return transfer;
    }
}
