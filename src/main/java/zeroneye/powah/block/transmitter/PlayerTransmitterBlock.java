package zeroneye.powah.block.transmitter;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import zeroneye.lib.block.TileBase;
import zeroneye.lib.inventory.ContainerBase;
import zeroneye.lib.util.Energy;
import zeroneye.powah.block.IBlocks;
import zeroneye.powah.block.PowahBlock;
import zeroneye.powah.block.cable.CableBlock;
import zeroneye.powah.inventory.IContainers;
import zeroneye.powah.inventory.PlayerTransmitterContainer;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

import static net.minecraft.util.math.shapes.VoxelShapes.combineAndSimplify;

public class PlayerTransmitterBlock extends PowahBlock implements IWaterLoggable {
    private static final Map<Direction, VoxelShape> SHAPES = new HashMap<>();
    private final int slots;
    private final boolean acrossDim;

    public PlayerTransmitterBlock(Properties properties, int capacity, int transfer, int slots, boolean acrossDim) {
        super(properties, capacity, transfer, transfer);
        this.slots = slots;
        this.acrossDim = acrossDim;
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP).with(WATERLOGGED, false));
    }

    static {
        SHAPES.put(Direction.UP, makeCuboidShape(10.0D, 0.0D, 10.0D, 6.0D, 14.0D, 6.0D));
        SHAPES.put(Direction.DOWN, makeCuboidShape(10.0D, 2.0D, 10.0D, 6.0D, 16.0D, 6.0D));
        SHAPES.put(Direction.NORTH, combineAndSimplify(makeCuboidShape(10.0D, 6.0D, 12.0D, 6.0D, 16.0D, 8.0D), makeCuboidShape(10.0D, 6.0D, 8.0D, 6.0D, 10.0D, 16.0D), IBooleanFunction.OR));
        SHAPES.put(Direction.SOUTH, combineAndSimplify(makeCuboidShape(10.0D, 6.0D, 8.0D, 6.0D, 16.0D, 4.0D), makeCuboidShape(10.0D, 6.0D, 0.0D, 6.0D, 10.0D, 8.0D), IBooleanFunction.OR));
        SHAPES.put(Direction.WEST, combineAndSimplify(makeCuboidShape(12.0D, 6.0D, 10.0D, 8.0D, 16.0D, 6.0D), makeCuboidShape(8.0D, 6.0D, 10.0D, 16.0D, 10.0D, 6.0D), IBooleanFunction.OR));
        SHAPES.put(Direction.EAST, combineAndSimplify(makeCuboidShape(8.0D, 6.0D, 10.0D, 4.0D, 16.0D, 6.0D), makeCuboidShape(0.0D, 6.0D, 10.0D, 6.0D, 10.0D, 6.0D), IBooleanFunction.OR));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES.get(state.get(FACING));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PlayerTransmitterTile(this.capacity, this.maxReceive, this.acrossDim);
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, TileBase.TickableInv inv) {
        if (this == IBlocks.PLAYER_TRANSMITTER) {
            return new PlayerTransmitterContainer(IContainers.PLAYER_TRANSMITTER, id, playerInventory, (PlayerTransmitterTile) inv, this.slots);
        } else if (this == IBlocks.PLAYER_TRANSMITTER_DIM) {
            return new PlayerTransmitterContainer(IContainers.PLAYER_TRANSMITTER_DIM, id, playerInventory, (PlayerTransmitterTile) inv, this.slots);
        }
        return super.getContainer(id, playerInventory, inv);
    }

    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        Direction direction = state.get(FACING);
        BlockPos blockpos = pos.offset(direction.getOpposite());
        BlockState state1 = worldIn.getBlockState(blockpos);
        TileEntity tile = worldIn.getTileEntity(blockpos);
        return state1.getBlock() instanceof CableBlock || tile != null && !(tile instanceof PlayerTransmitterTile) && Energy.hasEnergy(tile, direction);
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        for (Rotation rotation : Rotation.values()) {
            if (!rotation.equals(Rotation.NONE)) {
                if (isValidPosition(super.rotate(state, world, pos, rotation), world, pos)) {
                    return super.rotate(state, world, pos, rotation);
                }
            }
        }
        return state;
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    public int getSlots() {
        return slots;
    }

    public boolean isAcrossDim() {
        return acrossDim;
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
