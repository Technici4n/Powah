package zeroneye.powah.block.storage;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import zeroneye.lib.block.IInvBase;
import zeroneye.lib.inventory.ContainerBase;
import zeroneye.powah.block.PowahBlock;
import zeroneye.powah.inventory.EnergyCellContainer;
import zeroneye.powah.inventory.IContainers;

import javax.annotation.Nullable;

public class EnergyCellBlock extends PowahBlock {
    public EnergyCellBlock(Properties properties, int capacity, int transfer) {
        super(properties, capacity, transfer);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnergyCellTile(getCapacity(), getTransfer(), true, true, isCreative());
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, IInvBase inv) {
        if (this == EnergyCells.BASIC.get()) {
            return new EnergyCellContainer(IContainers.ENERGY_CELL_BASIC, id, playerInventory, (EnergyCellTile) inv);
        } else if (this == EnergyCells.HARDENED.get()) {
            return new EnergyCellContainer(IContainers.ENERGY_CELL_HARDENED, id, playerInventory, (EnergyCellTile) inv);
        } else if (this == EnergyCells.BLAZING.get()) {
            return new EnergyCellContainer(IContainers.ENERGY_CELL_BLAZING, id, playerInventory, (EnergyCellTile) inv);
        } else if (this == EnergyCells.NIOTIC.get()) {
            return new EnergyCellContainer(IContainers.ENERGY_CELL_NIOTIC, id, playerInventory, (EnergyCellTile) inv);
        } else if (this == EnergyCells.SPIRITED.get()) {
            return new EnergyCellContainer(IContainers.ENERGY_CELL_SPIRITED, id, playerInventory, (EnergyCellTile) inv);
        } else if (this == EnergyCells.CREATIVE.get()) {
            return new EnergyCellContainer(IContainers.ENERGY_CELL_CREATIVE, id, playerInventory, (EnergyCellTile) inv);
        }
        return super.getContainer(id, playerInventory, inv);
    }
}
