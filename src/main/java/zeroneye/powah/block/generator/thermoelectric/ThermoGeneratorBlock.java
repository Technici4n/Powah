package zeroneye.powah.block.generator.thermoelectric;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import zeroneye.lib.block.TileBase;
import zeroneye.lib.inventory.ContainerBase;
import zeroneye.lib.item.BlockItemBase;
import zeroneye.powah.block.generator.GeneratorBlock;
import zeroneye.powah.inventory.IContainers;
import zeroneye.powah.inventory.ThermoGenContainer;
import zeroneye.powah.item.ThermoGeneratorItem;

import javax.annotation.Nullable;

import static net.minecraft.util.math.shapes.VoxelShapes.combineAndSimplify;
import static net.minecraft.util.math.shapes.VoxelShapes.fullCube;

public class ThermoGeneratorBlock extends GeneratorBlock implements IWaterLoggable {
    public static final VoxelShape SHAPE = combineAndSimplify(makeCuboidShape(2.0D, 1.0D, 2.0D, 14.0D, 14.0D, 14.0D), combineAndSimplify(makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D), combineAndSimplify(makeCuboidShape(0.5D, 1.5D, 0.5D, 15.5D, 2.5D, 15.5D), makeCuboidShape(0.5D, 3.25D, 0.5D, 15.5D, 4.25D, 15.5D), IBooleanFunction.OR), IBooleanFunction.OR), IBooleanFunction.OR);
    public int buckets;

    public ThermoGeneratorBlock(Properties properties, int capacity, int transfer, int perTick, int buckets) {
        super(properties, capacity, transfer, perTick);
        this.buckets = buckets;
        setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return fullCube();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ThermoGeneratorTile(this.capacity, this.maxExtract, this.perTick, this.buckets);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof ThermoGeneratorTile) {
            ThermoGeneratorTile genTile = (ThermoGeneratorTile) tile;
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
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, TileBase.TickableInv inv) {
        if (this == ThermoGenerators.BASIC.get()) {
            return new ThermoGenContainer(IContainers.THERMO_GENERATOR_BASIC, id, playerInventory, (ThermoGeneratorTile) inv);
        } else if (this == ThermoGenerators.HARDENED.get()) {
            return new ThermoGenContainer(IContainers.THERMO_GENERATOR_HARDENED, id, playerInventory, (ThermoGeneratorTile) inv);
        } else if (this == ThermoGenerators.BLAZING.get()) {
            return new ThermoGenContainer(IContainers.THERMO_GENERATOR_BLAZING, id, playerInventory, (ThermoGeneratorTile) inv);
        } else if (this == ThermoGenerators.NIOTIC.get()) {
            return new ThermoGenContainer(IContainers.THERMO_GENERATOR_NIOTIC, id, playerInventory, (ThermoGeneratorTile) inv);
        } else if (this == ThermoGenerators.SPIRITED.get()) {
            return new ThermoGenContainer(IContainers.THERMO_GENERATOR_SPIRITED, id, playerInventory, (ThermoGeneratorTile) inv);
        }
        return super.getContainer(id, playerInventory, inv);
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    @Override
    public BlockItemBase getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return new ThermoGeneratorItem(this, properties, group);
    }

    public void setBuckets(int buckets) {
        this.buckets = buckets;
    }

    @Override
    protected boolean hasLitProp() {
        return false;
    }

    @Override
    protected boolean waterLogged() {
        return true;
    }
}
