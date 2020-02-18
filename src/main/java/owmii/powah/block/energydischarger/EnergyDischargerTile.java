package owmii.powah.block.energydischarger;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import owmii.lib.block.TileBase;
import owmii.lib.energy.Energy;
import owmii.powah.block.ITiles;
import owmii.powah.block.Tier;

public class EnergyDischargerTile extends TileBase.EnergyStorage<Tier, EnergyDischargerBlock> {
    public EnergyDischargerTile(Tier variant) {
        super(ITiles.ENERGY_DISCHARGER, variant);
        this.inv.add(variant.ordinal() + 1);
    }

    public EnergyDischargerTile() {
        this(Tier.STARTER);
    }

    @Override
    protected boolean postTicks(World world) {
        final int[] extracted = {0};
        for (int i = 0; i < this.inv.getSlots(); i++) {
            final ItemStack stack = this.inv.getStackInSlot(i);
            long amount = Math.min(getMaxEnergyExtract(), Energy.getStored(stack));
            long received = Math.min(getEnergyCapacity() - getEnergyStored(), amount);
            int j = Energy.extract(stack, received, false);
            produceEnergy(j);
            extracted[0] += j;
        }
        return extracted[0] > 0;
    }

    @Override
    public boolean canInsert(int index, ItemStack stack) {
        final boolean[] b = {false};
        Energy.get(stack).ifPresent(storage -> {
            if (storage.canExtract()) {
                b[0] = storage.getEnergyStored() > 0;
            }
        });
        return b[0];
    }

    @Override
    public boolean keepEnergy() {
        return true;
    }
}
