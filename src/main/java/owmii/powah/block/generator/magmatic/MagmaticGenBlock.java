package owmii.powah.block.generator.magmatic;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import owmii.lib.block.TileBase;
import owmii.lib.inventory.ContainerBase;
import owmii.powah.block.generator.GeneratorBlock;
import owmii.powah.inventory.IContainers;
import owmii.powah.inventory.MagmaticGenContainer;

import javax.annotation.Nullable;

import static net.minecraft.util.math.shapes.VoxelShapes.combineAndSimplify;
import static net.minecraft.util.math.shapes.VoxelShapes.fullCube;

public class MagmaticGenBlock extends GeneratorBlock implements IWaterLoggable {
    public static final VoxelShape SHAPE = combineAndSimplify(makeCuboidShape(1.0D, 8.0D, 1.0D, 15.0D, 16.0D, 15.0D), makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), IBooleanFunction.OR);
    private int buckets;

    public MagmaticGenBlock(Properties properties, int capacity, int transfer, int perTick, int buckets) {
        super(properties, capacity, transfer, perTick);
        this.buckets = buckets;
        setDefaultState(this.stateContainer.getBaseState().with(H_FACING, Direction.NORTH).with(LIT, false).with(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return fullCube();
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof MagmaticGenTile) {
            MagmaticGenTile genTile = (MagmaticGenTile) tile;
            boolean result = FluidUtil.interactWithFluidHandler(player, hand, genTile.tank);
            if (result) {
                genTile.markDirtyAndSync();
                return true;
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, blockRayTraceResult);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MagmaticGenTile(this.capacity, this.maxExtract, this.perTick, this.buckets);
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, TileBase inv) {
        if (this == MagmaticGenerators.BASIC.get()) {
            return new MagmaticGenContainer(IContainers.MAGMATIC_GENERATOR_BASIC, id, playerInventory, (MagmaticGenTile) inv);
        } else if (this == MagmaticGenerators.HARDENED.get()) {
            return new MagmaticGenContainer(IContainers.MAGMATIC_GENERATOR_HARDENED, id, playerInventory, (MagmaticGenTile) inv);
        } else if (this == MagmaticGenerators.BLAZING.get()) {
            return new MagmaticGenContainer(IContainers.MAGMATIC_GENERATOR_BLAZING, id, playerInventory, (MagmaticGenTile) inv);
        } else if (this == MagmaticGenerators.NIOTIC.get()) {
            return new MagmaticGenContainer(IContainers.MAGMATIC_GENERATOR_NIOTIC, id, playerInventory, (MagmaticGenTile) inv);
        } else if (this == MagmaticGenerators.SPIRITED.get()) {
            return new MagmaticGenContainer(IContainers.MAGMATIC_GENERATOR_SPIRITED, id, playerInventory, (MagmaticGenTile) inv);
        }
        return super.getContainer(id, playerInventory, inv);
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    public int getBuckets() {
        return this.buckets;
    }

    public void setBuckets(int buckets) {
        this.buckets = buckets;
    }

    @Override
    protected FacingType getFacingType() {
        return FacingType.HORIZONTAL;
    }
}
