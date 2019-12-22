package owmii.powah.block.discharger;

import net.minecraft.item.ItemStack;
import owmii.lib.util.Energy;
import owmii.powah.block.ITiles;
import owmii.powah.block.PowahTile;

public class DischargerTile extends PowahTile {
    public DischargerTile(int capacity, int maxExtract) {
        super(ITiles.DISCHARGER, capacity, 0, maxExtract, false);
        this.inv.add(4);
    }

    public DischargerTile() {
        this(0, 0);
    }

    @Override
    protected boolean postTicks() {
        super.postTicks();
        final int[] extracted = {0};

        for (int i = builtInSlots(); i < 4 + builtInSlots(); i++) {
            final ItemStack stack = this.inv.getStackInSlot(i);
            int amount = Math.min(this.internal.getMaxExtract(), Energy.getStored(stack));
            int received = Math.min(this.internal.getMaxEnergyStored() - this.internal.getEnergyStored(), amount);
            int j = Energy.extract(stack, received, false);
            this.internal.setEnergy(this.internal.getEnergyStored() + j);
            extracted[0] += j;
        }

        return extracted[0] > 0;
    }

    @Override
    public int getSlotLimit(int index) {
        return super.getSlotLimit(index);
    }

    @Override
    protected ExtractionType getExtractionType() {
        return ExtractionType.TILE;
    }

    @Override
    public boolean canInsert(int index, ItemStack stack) {
        final boolean[] b = {false};
        Energy.getForgeEnergy(stack).ifPresent(storage -> {
            if (storage.canExtract()) {
                b[0] = storage.getEnergyStored() > 0;
            }
        });
        return b[0];
    }
}
