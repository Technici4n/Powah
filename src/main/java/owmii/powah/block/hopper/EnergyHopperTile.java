package owmii.powah.block.hopper;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import owmii.lib.block.AbstractEnergyStorage;
import owmii.lib.block.IInventoryHolder;
import owmii.powah.block.ITiles;
import owmii.powah.block.Tier;
import owmii.powah.config.EnergyHopperConfig;

public class EnergyHopperTile extends AbstractEnergyStorage<Tier, EnergyHopperConfig, EnergyHopperBlock> implements IInventoryHolder {
    public EnergyHopperTile(Tier variant) {
        super(ITiles.ENERGY_HOPPER, variant);
    }

    public EnergyHopperTile() {
        this(Tier.STARTER);
    }

    @Override
    protected int postTick(World world) {
        final int[] extracted = {0};
        if (!isRemote() && checkRedstone()) {
            Direction side = getBlockState().get(BlockStateProperties.FACING);
            TileEntity tile = world.getTileEntity(this.pos.offset(side));
            long charging = getConfig().getChargingSpeed(this.variant);
            if (tile instanceof IInventory) {
                IInventory inventory = (IInventory) tile;
                for (int i = 0; i < inventory.getSizeInventory(); i++) {
                    ItemStack stack = inventory.getStackInSlot(i);
                    if (stack.getCount() == 1) {
                        extracted[0] += chargeItem(stack, charging);
                    }
                }
            } else if (tile != null) {
                tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(iItemHandler -> {
                    for (int i = 0; i < iItemHandler.getSlots(); i++) {
                        ItemStack stack = iItemHandler.getStackInSlot(i);
                        if (stack.getCount() == 1) {
                            extracted[0] += chargeItem(stack, charging);
                        }
                    }
                });
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
