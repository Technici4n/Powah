package owmii.powah.block.energycell;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import owmii.lib.block.AbstractEnergyStorage;
import owmii.lib.block.IInventoryHolder;
import owmii.lib.logistics.TransferType;
import owmii.lib.logistics.energy.Energy;
import owmii.powah.block.ITiles;
import owmii.powah.block.Tier;
import owmii.powah.config.EnergyCellConfig;

import javax.annotation.Nullable;

public class EnergyCellTile extends AbstractEnergyStorage<Tier, EnergyCellConfig, EnergyCellBlock> implements IInventoryHolder {
    public EnergyCellTile(Tier tier) {
        super(ITiles.ENERGY_CELL, tier);
        this.inv.add(2);
    }

    public EnergyCellTile() {
        this(Tier.STARTER);
    }

    @Override
    protected void onFirstTick(World world) {
        super.onFirstTick(world);
        if (isCreative()) {
            this.energy.setStored(getEnergyCapacity());
        }
    }

    @Override
    protected int postTick(World world) {
        return chargeItems(2) + extractFromSides(world) > 0 ? 10 : -1;
    }

    @Override
    public long extractEnergy(int maxExtract, boolean simulate, @Nullable Direction side) {
        return super.extractEnergy(maxExtract, isCreative(), side);
    }

    @Override
    protected long receiveEnergy(int maxReceive, boolean simulate, @Nullable Direction side) {
        return super.receiveEnergy(maxReceive, simulate, side);
    }

    @Override
    public boolean canExtractEnergy(@Nullable Direction side) {
        return checkRedstone() && super.canExtractEnergy(side);
    }

    @Override
    public boolean canReceiveEnergy(@Nullable Direction side) {
        return checkRedstone() && super.canReceiveEnergy(side);
    }

    @Override
    public boolean keepEnergy() {
        return !isCreative();
    }

    @Override
    public TransferType getTransferType() {
        return isCreative() ? TransferType.EXTRACT : super.getTransferType();
    }

    public boolean isCreative() {
        return this.getVariant().equals(Tier.CREATIVE);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return Energy.get(stack).isPresent();
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return true;
    }
}
