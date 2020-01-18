package owmii.powah.block.endergate;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import owmii.lib.block.TileBase;
import owmii.lib.inventory.ContainerBase;
import owmii.lib.util.Energy;
import owmii.powah.api.cable.ICable;
import owmii.powah.block.endercell.EnderCellBlock;
import owmii.powah.inventory.EnderCellContainer;
import owmii.powah.inventory.IContainers;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class EnderGateBlock extends EnderCellBlock implements IWaterLoggable {
    private static final Map<Direction, VoxelShape> VOXEL_SHAPES = new HashMap<>();

    public EnderGateBlock(Properties properties, int capacity, int maxExtract, int maxReceive) {
        super(properties, capacity, maxExtract, maxReceive);
        setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, false).with(FACING, Direction.NORTH));
    }

    static {
        VOXEL_SHAPES.put(Direction.UP, makeCuboidShape(6.0D, 15.5D, 6.0D, 10.0D, 16.0D, 10.0D));
        VOXEL_SHAPES.put(Direction.DOWN, makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 0.5D, 10.0D));
        VOXEL_SHAPES.put(Direction.NORTH, makeCuboidShape(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 0.5D));
        VOXEL_SHAPES.put(Direction.SOUTH, makeCuboidShape(6.0D, 6.0D, 15.5D, 10.0D, 10.0D, 16.0D));
        VOXEL_SHAPES.put(Direction.EAST, makeCuboidShape(15.5D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D));
        VOXEL_SHAPES.put(Direction.WEST, makeCuboidShape(0.0D, 6.0D, 6.0D, 0.5D, 10.0D, 10.0D));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnderGateTile(this.maxReceive, this.maxExtract, this.channels);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VOXEL_SHAPES.get(state.get(FACING));
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        Direction direction = state.get(FACING);
        BlockPos blockpos = pos.offset(direction);
        BlockState state1 = worldIn.getBlockState(blockpos);
        TileEntity tile = worldIn.getTileEntity(blockpos);
        return state1.getBlock() instanceof ICable || tile != null && Energy.getForgeEnergy(tile, direction).isPresent();
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, TileBase inv) {
        if (inv instanceof EnderGateTile) {
            return new EnderCellContainer(IContainers.ENDER_CELL, id, playerInventory, (EnderGateTile) inv);
        }
        return null;
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    @Override
    protected FacingType getFacingType() {
        return FacingType.ALL;
    }
}
