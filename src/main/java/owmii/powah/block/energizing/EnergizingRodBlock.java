package owmii.powah.block.energizing;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import owmii.lib.block.AbstractEnergyBlock;
import owmii.lib.config.IEnergyConfig;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.minecraft.util.math.shapes.VoxelShapes.combineAndSimplify;

public class EnergizingRodBlock extends AbstractEnergyBlock<Tier> implements IWaterLoggable {
    private static final Map<Direction, VoxelShape> VOXEL_SHAPES = new HashMap<>();

    public EnergizingRodBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setStateProps(state -> state.with(FACING, Direction.DOWN));
    }

    static {
        VOXEL_SHAPES.put(Direction.UP, combineAndSimplify(makeCuboidShape(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D), combineAndSimplify(makeCuboidShape(7.0D, 13.0D, 7.0D, 9.0D, 16.0D, 9.0D), makeCuboidShape(7.25D, 9.0D, 7.25D, 8.75D, 13.0D, 8.75D), IBooleanFunction.OR), IBooleanFunction.OR));
        VOXEL_SHAPES.put(Direction.DOWN, combineAndSimplify(makeCuboidShape(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D), combineAndSimplify(makeCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 3.0D, 9.0D), makeCuboidShape(7.25D, 3.0D, 7.25D, 8.75D, 7.0D, 8.75D), IBooleanFunction.OR), IBooleanFunction.OR));
        VOXEL_SHAPES.put(Direction.NORTH, combineAndSimplify(makeCuboidShape(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D), combineAndSimplify(makeCuboidShape(7.0D, 7.0D, 0.0D, 9.0D, 9.0D, 3.0D), makeCuboidShape(7.25D, 7.25D, 3.0D, 8.75D, 8.75D, 7.0D), IBooleanFunction.OR), IBooleanFunction.OR));
        VOXEL_SHAPES.put(Direction.SOUTH, combineAndSimplify(makeCuboidShape(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D), combineAndSimplify(makeCuboidShape(7.0D, 7.0D, 13.0D, 9.0D, 9.0D, 16.0D), makeCuboidShape(7.25D, 7.25D, 13.0D, 8.75D, 8.75D, 9.0D), IBooleanFunction.OR), IBooleanFunction.OR));
        VOXEL_SHAPES.put(Direction.WEST, combineAndSimplify(makeCuboidShape(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D), combineAndSimplify(makeCuboidShape(0.0D, 7.0D, 7.0D, 3.0D, 9.0D, 9.0D), makeCuboidShape(3.0D, 7.25D, 7.25D, 7.0D, 8.75D, 8.75D), IBooleanFunction.OR), IBooleanFunction.OR));
        VOXEL_SHAPES.put(Direction.EAST, combineAndSimplify(makeCuboidShape(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D), combineAndSimplify(makeCuboidShape(13.0D, 7.0D, 7.0D, 16.0D, 9.0D, 9.0D), makeCuboidShape(13.0D, 7.25D, 7.25D, 9.0D, 8.75D, 8.75D), IBooleanFunction.OR), IBooleanFunction.OR));
    }

    @Override
    public int stackSize() {
        return 1;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VOXEL_SHAPES.get(state.get(FACING));
    }

    @Override
    public IEnergyConfig<Tier> getEnergyConfig() {
        return Configs.ENERGIZING;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnergizingRodTile(this.variant);
    }

    @Override
    protected boolean checkValidEnergySide() {
        return true;
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof EnergizingRodTile) {
            setOrbPos(worldIn, pos, (EnergizingRodTile) tileEntity);
        }
    }

    public void setOrbPos(World worldIn, BlockPos pos, EnergizingRodTile tile) {
        int range = Configs.ENERGIZING.range.get();
        List<BlockPos> list = BlockPos.getAllInBox(pos.add(-range, -range, -range), pos.add(range, range, range)).map(BlockPos::toImmutable).collect(Collectors.toList());
        for (BlockPos pos1 : list) {
            if (pos1.equals(BlockPos.ZERO)) continue;
            TileEntity tileEntity1 = worldIn.getTileEntity(pos1);
            if (tileEntity1 instanceof EnergizingOrbTile) {
                tile.setOrbPos(pos1);
                break;
            }
        }
    }

    @Override
    protected Facing getFacing() {
        return Facing.ALL;
    }
}
