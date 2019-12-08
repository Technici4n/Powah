package zeroneye.powah.block.hopper;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.items.CapabilityItemHandler;
import zeroneye.powah.block.ITiles;
import zeroneye.powah.block.PowahTile;

public class EnergyHopperTile extends PowahTile {
    public EnergyHopperTile(int capacity, int maxReceive) {
        super(ITiles.ENERGY_HOPPER, capacity, maxReceive, maxReceive, false);
    }

    public EnergyHopperTile() {
        this(0, 0);
    }

    @Override
    protected boolean postTicks() {
        if (this.world == null) return false;
        if (this.world.isRemote) return false;

        final int[] extracted = {0};

        Direction side = getBlockState().get(EnergyHopperBlock.FACING);
        TileEntity tile = this.world.getTileEntity(this.pos.offset(side));

        if (tile instanceof IInventory) {
            IInventory inventory = (IInventory) tile;
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (stack.getCount() == 1) {
                    extracted[0] += chargeItem(stack, internal.getMaxReceive());
                }
            }
        } else if (tile != null) {
            tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(iItemHandler -> {
                for (int i = 0; i < iItemHandler.getSlots(); i++) {
                    ItemStack stack = iItemHandler.getStackInSlot(i);
                    if (stack.getCount() == 1) {
                        extracted[0] += chargeItem(stack, internal.getMaxReceive());
                    }
                }
            });
        }

        return extracted[0] > 0 || super.postTicks();
    }

    @Override
    protected ExtractionType getExtractionType() {
        return ExtractionType.OFF;
    }
}
