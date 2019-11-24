package zeroneye.powah.block.generator.furnator;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import zeroneye.powah.block.ITiles;
import zeroneye.powah.block.generator.GeneratorTile;

public class FurnatorTile extends GeneratorTile {
    public FurnatorTile(int capacity, int transfer, int perTick) {
        super(ITiles.FURNATOR, capacity, transfer, perTick);
    }

    public FurnatorTile() {
        this(0, 0, 0);
    }

    @Override
    protected void generate() {
        final ItemStack fuelStack = getStackInSlot(1);
        if (this.nextGen <= 0 && !fuelStack.isEmpty()) {
            this.nextGenCap = ForgeHooks.getBurnTime(fuelStack) * 60;// TODO add config burne time
            this.nextGen = this.nextGenCap;
            if (fuelStack.hasContainerItem())
                setInventorySlotContents(1, fuelStack.getContainerItem());
            else {
                fuelStack.shrink(1);
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return 1 + super.getSizeInventory();
    }

    @Override
    public int getChargingSlots() {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        return (ForgeHooks.getBurnTime(itemStack) > 0 && index == 1) && super.isItemValidForSlot(index, itemStack);
    }
}
