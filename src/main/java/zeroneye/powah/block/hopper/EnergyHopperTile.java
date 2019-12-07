package zeroneye.powah.block.hopper;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import zeroneye.powah.block.PowahTile;

public class EnergyHopperTile extends PowahTile {
    public EnergyHopperTile(int capacity, int maxReceive) {
        super(null, capacity, maxReceive, 0, false);
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
        BlockPos invPos = this.pos.offset(side);
        TileEntity tile = this.world.getTileEntity(invPos);

        if (tile instanceof IInventory) {
            IInventory inventory = (IInventory) tile;
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                extracted[0] += chargeItem(inventory.getStackInSlot(i));
            }
        } else if (tile != null) {
            tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(iItemHandler -> {
                for (int i = 0; i < iItemHandler.getSlots(); i++) {
                    extracted[0] += chargeItem(iItemHandler.getStackInSlot(i));
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
