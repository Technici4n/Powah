package owmii.powah.block.discharger;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import owmii.powah.EnvHandler;
import owmii.powah.config.v2.types.EnergyConfig;
import owmii.powah.lib.block.AbstractEnergyStorage;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.logistics.Transfer;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;

public class EnergyDischargerTile extends AbstractEnergyStorage<EnergyConfig, EnergyDischargerBlock> implements IInventoryHolder {
    public EnergyDischargerTile(BlockPos pos, BlockState state, Tier variant) {
        super(Tiles.ENERGY_DISCHARGER.get(), pos, state, variant);
        this.inv.add(7);
    }

    public EnergyDischargerTile(BlockPos pos, BlockState state) {
        this(pos, state, Tier.STARTER);
    }

    @Override
    protected int postTick(Level world) {
        long extracted = 0;
        if (!isRemote()) {
            if (checkRedstone()) {
                extracted = EnvHandler.INSTANCE.dischargeItemsInInventory(this.inv, getEnergyTransfer(), getEnergyCapacity() - energy.getStored());
                this.energy.produce(extracted);
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
        return EnvHandler.INSTANCE.canDischarge(stack);
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
