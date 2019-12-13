package zeroneye.powah.block.energizing;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import zeroneye.powah.block.IBlocks;
import zeroneye.powah.block.PowahBlock;
import zeroneye.powah.config.Config;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.minecraft.util.math.shapes.VoxelShapes.combineAndSimplify;

public class EnergizingRodBlock extends PowahBlock implements IWaterLoggable {
    private static final Map<Direction, VoxelShape> VOXEL_SHAPES = new HashMap<>();
    private int energizingSpeed;

    public EnergizingRodBlock(Properties properties, int capacity, int maxReceive, int energizingSpeed) {
        super(properties, capacity, 0, maxReceive);
        this.energizingSpeed = energizingSpeed;
        setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.DOWN).with(WATERLOGGED, false));
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
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VOXEL_SHAPES.get(state.get(FACING));
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof EnergizingRodTile) {
            setOrbPos(worldIn, pos, (EnergizingRodTile) tileEntity);
        }
    }

    public void setOrbPos(World worldIn, BlockPos pos, EnergizingRodTile tile) {
        int range = Config.ENERGIZING_CONFIG.range.get();
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

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnergizingRodTile(this.capacity, this.maxReceive);
    }

    @Override
    protected void additionalTooltip(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("info.powah.energizing.speed", "" + TextFormatting.DARK_GRAY + this.energizingSpeed).applyTextStyle(TextFormatting.GRAY));
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return IBlocks.PLAYER_TRANSMITTER.isValidPosition(state, worldIn, pos);
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        return IBlocks.PLAYER_TRANSMITTER.rotate(state, world, pos, direction);
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    @Override
    protected FacingType getFacingType() {
        return FacingType.ALL;
    }

    public int getEnergizingSpeed() {
        return energizingSpeed;
    }

    public void setEnergizingSpeed(int energizingSpeed) {
        this.energizingSpeed = energizingSpeed;
    }
}
