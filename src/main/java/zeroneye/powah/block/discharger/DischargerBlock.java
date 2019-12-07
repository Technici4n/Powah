package zeroneye.powah.block.discharger;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import zeroneye.lib.block.TileBase;
import zeroneye.lib.inventory.ContainerBase;
import zeroneye.powah.block.PowahBlock;
import zeroneye.powah.inventory.DischargerContainer;
import zeroneye.powah.inventory.IContainers;

import javax.annotation.Nullable;

public class DischargerBlock extends PowahBlock implements IWaterLoggable {
    public DischargerBlock(Properties properties, int capacity, int maxExtract) {
        super(properties, capacity, maxExtract, 0);
        setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, false));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DischargerTile(this.capacity, this.maxExtract);
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, TileBase.TickableInv inv) {
        return new DischargerContainer(IContainers.DISCHARGER, id, playerInventory, (DischargerTile) inv);
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    @Override
    protected boolean waterLogged() {
        return true;
    }

    @Override
    protected FacingType getFacingType() {
        return FacingType.HORIZONTAL;
    }
}
