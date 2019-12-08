package zeroneye.powah.block.hopper;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.items.CapabilityItemHandler;
import zeroneye.lib.block.TileBase;
import zeroneye.lib.inventory.ContainerBase;
import zeroneye.lib.util.Energy;
import zeroneye.powah.block.PowahBlock;
import zeroneye.powah.inventory.EnergyHopperContainer;
import zeroneye.powah.inventory.IContainers;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class EnergyHopperBlock extends PowahBlock implements IWaterLoggable {
    public static final Map<Direction, VoxelShape> VOXEL_SHAPES = new HashMap<>();

    public EnergyHopperBlock(Properties properties, int capacity, int transfer) {
        super(properties, capacity, transfer, transfer);
        setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, false).with(FACING, Direction.NORTH));
    }

    static {
        VOXEL_SHAPES.put(Direction.UP, makeCuboidShape(0, 0, 0, 16, 12, 16));
        VOXEL_SHAPES.put(Direction.DOWN, makeCuboidShape(0, 4, 0, 16, 16, 16));
        VOXEL_SHAPES.put(Direction.NORTH, makeCuboidShape(0, 0, 4, 16, 16, 16));
        VOXEL_SHAPES.put(Direction.SOUTH, makeCuboidShape(0, 0, 0, 16, 16, 12));
        VOXEL_SHAPES.put(Direction.EAST, makeCuboidShape(0, 0, 0, 12, 16, 16));
        VOXEL_SHAPES.put(Direction.WEST, makeCuboidShape(4, 0, 0, 16, 16, 16));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VOXEL_SHAPES.get(state.get(FACING));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnergyHopperTile(this.capacity, this.maxReceive);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        Direction direction = state.get(FACING);
        BlockPos blockpos = pos.offset(direction);
        BlockState state1 = worldIn.getBlockState(blockpos);
        TileEntity tile = worldIn.getTileEntity(blockpos);
        return (tile instanceof IInventory || tile != null && tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction).isPresent()) && !Energy.hasEnergy(tile, direction);
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, TileBase.TickableInv inv) {
        return new EnergyHopperContainer(IContainers.ENERGY_HOPPER, id, playerInventory, (EnergyHopperTile) inv);
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
        return FacingType.ALL;
    }
}
