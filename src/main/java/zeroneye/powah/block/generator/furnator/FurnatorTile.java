package zeroneye.powah.block.generator.furnator;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import zeroneye.powah.block.ITiles;
import zeroneye.powah.block.generator.GeneratorTile;
import zeroneye.powah.config.Config;

public class FurnatorTile extends GeneratorTile {
    public FurnatorTile(int capacity, int transfer, int perTick) {
        super(ITiles.FURNATOR, capacity, transfer, perTick);
        this.inv.add(1);
    }

    public FurnatorTile() {
        this(0, 0, 0);
    }

    @Override
    protected void generate() {
        final ItemStack fuelStack = this.inv.getStackInSlot(1);
        if (this.nextGen <= 0 && !fuelStack.isEmpty()) {
            this.nextGenCap = ForgeHooks.getBurnTime(fuelStack) * Config.FURNATOR_CONFIG.fuelEnergy.get();
            this.nextGen = this.nextGenCap;
            if (fuelStack.hasContainerItem())
                this.inv.setStack(1, fuelStack.getContainerItem());
            else {
                fuelStack.shrink(1);
            }
        }
    }

    @Override
    public int getChargingSlots() {
        return 1;
    }

    @Override
    public boolean canInsert(int index, ItemStack stack) {
        return (ForgeHooks.getBurnTime(stack) > 0 && index == 1) || super.canInsert(index, stack);
    }
}
