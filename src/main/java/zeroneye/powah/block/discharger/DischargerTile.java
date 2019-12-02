package zeroneye.powah.block.discharger;

import net.minecraft.item.ItemStack;
import zeroneye.lib.util.Energy;
import zeroneye.powah.block.ITiles;
import zeroneye.powah.block.PowahTile;

public class DischargerTile extends PowahTile {
    public DischargerTile(int capacity, int maxExtract) {
        super(ITiles.DISCHARGER, capacity, 0, maxExtract, false);
    }

    public DischargerTile() {
        this(0, 0);
    }

    @Override
    protected boolean postTicks() {
        super.postTicks();
        final int[] extracted = {0};

        for (int i = 0; i < 4; i++) {
            final ItemStack stack = getStackInSlot(i);
            int amount = Math.min(this.internal.getMaxExtract(), Energy.getStored(stack));
            int received = Math.min(this.internal.getMaxEnergyStored() - this.internal.getEnergyStored(), amount);
            int j = Energy.extract(stack, received, false);
            this.internal.setEnergy(this.internal.getEnergyStored() + j);
            extracted[0] += j;
        }

        return extracted[0] > 0;
    }

    @Override
    public int getSizeInventory() {
        return 4;
    }

    @Override
    protected ExtractionType getExtractionType() {
        return ExtractionType.TILE;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        final boolean[] b = {false};
        Energy.getForgeEnergy(itemStack).ifPresent(storage -> {
            if (storage.canExtract()) {
                b[0] = storage.getEnergyStored() > 0;
            }
        });
        return b[0];
    }
}
