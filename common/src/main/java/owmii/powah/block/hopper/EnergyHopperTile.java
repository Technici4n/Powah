package owmii.powah.block.hopper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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
        final int[] extracted = {0};
        if (!isRemote() && checkRedstone()) {
            Direction side = getBlockState().getValue(BlockStateProperties.FACING);
            BlockEntity tile = world.getBlockEntity(this.worldPosition.relative(side));
            long charging = getConfig().charging_rates.get(this.variant);
            if (tile instanceof Container) {
                Container inventory = (Container) tile;
                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    ItemStack stack = inventory.getItem(i);
                    if (stack.getCount() == 1) {
                        extracted[0] += chargeItem(stack, charging);
                    }
                }
            } else if (tile != null) {
        /* TODO ARCH
                tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(iItemHandler -> {
                    for (int i = 0; i < iItemHandler.getSlots(); i++) {
                        ItemStack stack = iItemHandler.getStackInSlot(i);
                        if (stack.getCount() == 1) {
                            extracted[0] += chargeItem(stack, charging);
                        }
                    }
                });
         */
            }
        }
        return extracted[0] > 0 ? 5 : super.postTick(world);
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
