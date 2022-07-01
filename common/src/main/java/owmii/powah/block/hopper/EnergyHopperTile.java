package owmii.powah.block.hopper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import owmii.powah.EnvHandler;
import owmii.powah.config.v2.types.ChargingConfig;
import owmii.powah.lib.block.AbstractEnergyStorage;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;

public class EnergyHopperTile extends AbstractEnergyStorage<ChargingConfig, EnergyHopperBlock> implements IInventoryHolder {
    public EnergyHopperTile(BlockPos pos, BlockState state, Tier variant) {
        super(Tiles.ENERGY_HOPPER.get(), pos, state, variant);
    }

    public EnergyHopperTile(BlockPos pos, BlockState state) {
        this(pos, state, Tier.STARTER);
    }

    @Override
    protected int postTick(Level world) {
        long extracted = 0;
        if (!isRemote() && checkRedstone()) {
            Direction side = getBlockState().getValue(BlockStateProperties.FACING);
            BlockEntity tile = world.getBlockEntity(this.worldPosition.relative(side));
            if (tile instanceof Container container) {
                extracted = EnvHandler.INSTANCE.chargeItemsInContainer(container, getConfig().charging_rates.get(this.variant), energy.getStored());
                energy.consume(extracted);
            }
        }
        return extracted > 0 ? 5 : super.postTick(world);
    }

    @Override
    public boolean keepEnergy() {
        return true;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return false;
    }
}
