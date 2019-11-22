package zeroneye.powah.block;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import zeroneye.lib.block.BlockBase;
import zeroneye.lib.item.BlockItemBase;
import zeroneye.powah.item.PowahBlockItem;

import javax.annotation.Nullable;

public abstract class PowahBlock extends BlockBase {
    protected final int capacity;
    protected final int transfer;
    protected boolean isCreative;

    public PowahBlock(Properties properties, int capacity, int transfer) {
        super(properties);
        this.capacity = capacity;
        this.transfer = transfer;
    }

    @Override
    public BlockItemBase getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return new PowahBlockItem(this, properties, group);
    }

    public int getCapacity() {
        return capacity;
    }

    public int getTransfer() {
        return transfer;
    }

    public boolean isCreative() {
        return isCreative;
    }

    @SuppressWarnings("unchecked")
    public <T extends PowahBlock> T setCreative(boolean isCreative) {
        this.isCreative = isCreative;
        return (T) this;
    }
}
