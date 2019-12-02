package zeroneye.powah.block;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import zeroneye.lib.block.BlockBase;
import zeroneye.lib.item.BlockItemBase;
import zeroneye.powah.item.PowahBlockItem;

import javax.annotation.Nullable;

public abstract class PowahBlock extends BlockBase {
    protected int capacity;
    protected int maxExtract;
    protected int maxReceive;
    protected boolean isCreative;

    public PowahBlock(Properties properties, int capacity, int maxExtract, int maxReceive) {
        super(properties);

        this.capacity = capacity;
        this.maxExtract = maxExtract;
        this.maxReceive = maxReceive;
    }

    @Override
    public BlockItemBase getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return new PowahBlockItem(this, properties, group);
    }

    public int getCapacity() {
        return capacity;
    }

    public int getMaxExtract() {
        return maxExtract;
    }

    public int getMaxReceive() {
        return maxReceive;
    }

    public boolean isCreative() {
        return isCreative;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;
    }

    public void setMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;
    }

    public void setTransfer(int transfer) {
        this.maxReceive = transfer;
        this.maxExtract = transfer;
    }

    @SuppressWarnings("unchecked")
    public <T extends PowahBlock> T setCreative(boolean isCreative) {
        this.isCreative = isCreative;
        return (T) this;
    }
}
