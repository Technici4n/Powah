package owmii.powah.block.discharger;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import owmii.lib.block.AbstractEnergyStorage;
import owmii.lib.block.IInventoryHolder;
import owmii.lib.logistics.Transfer;
import owmii.lib.logistics.energy.Energy;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;
import owmii.powah.config.EnergyDischargerConfig;

public class EnergyDischargerTile extends AbstractEnergyStorage<Tier, EnergyDischargerConfig, EnergyDischargerBlock> implements IInventoryHolder {
    public EnergyDischargerTile(BlockPos pos, BlockState state, Tier variant) {
        super(Tiles.ENERGY_DISCHARGER.get(), pos, state, variant);
        this.inv.add(7);
    }

    public EnergyDischargerTile(BlockPos pos, BlockState state) {
        this(pos, state, Tier.STARTER);
    }

    @Override
    protected int postTick(Level world) {
        int extracted = 0;
        if (!isRemote()) {
            if (checkRedstone()) {
                for (int i = 0; i < this.inv.getSlots(); i++) {
                    final ItemStack stack = this.inv.getStackInSlot(i);
                    long amount = Math.min(getEnergyTransfer(), Energy.getStored(stack));
                    long received = Math.min(getEnergyCapacity() - getEnergy().getStored(), amount);
                    int j = Energy.extract(stack, received, false);
                    this.energy.produce(j);
                    extracted += j;
                }
            }
            extracted += extractFromSides(world);
        }
        return extracted > 0 ? 5 : -1;
    }

    @Override
    public boolean keepEnergy() {
        return true;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        final boolean[] b = {false};
        Energy.get(stack).ifPresent(storage -> {
            if (storage.canExtract()) {
                b[0] = storage.getEnergyStored() > 0;
            }
        });
        return b[0];
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public Transfer getTransferType() {
        return Transfer.EXTRACT;
    }
}
