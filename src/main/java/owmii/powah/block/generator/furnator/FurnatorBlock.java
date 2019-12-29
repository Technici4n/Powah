package owmii.powah.block.generator.furnator;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;
import owmii.lib.block.TileBase;
import owmii.lib.inventory.ContainerBase;
import owmii.powah.block.generator.GeneratorBlock;
import owmii.powah.inventory.FurnatorContainer;
import owmii.powah.inventory.IContainers;

import javax.annotation.Nullable;

public class FurnatorBlock extends GeneratorBlock implements IWaterLoggable {

    public FurnatorBlock(Properties properties, int capacity, int transfer, int perTick) {
        super(properties, capacity, transfer, perTick);
        setDefaultState(this.stateContainer.getBaseState().with(H_FACING, Direction.NORTH).with(LIT, false).with(WATERLOGGED, false));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FurnatorTile(this.capacity, this.maxExtract, this.perTick);
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, TileBase inv) {
        return new FurnatorContainer(IContainers.FURNATOR, id, playerInventory, (FurnatorTile) inv);

    }

    @Override
    protected FacingType getFacingType() {
        return FacingType.HORIZONTAL;
    }
}
