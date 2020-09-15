package owmii.powah.block.solar;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SixWayBlock;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import owmii.lib.block.AbstractGeneratorBlock;
import owmii.lib.block.AbstractTileEntity;
import owmii.lib.item.EnergyBlockItem;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.logistics.inventory.AbstractContainer;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.config.generator.SolarConfig;
import owmii.powah.inventory.SolarContainer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SolarBlock extends AbstractGeneratorBlock<Tier, SolarConfig, SolarBlock> implements IWaterLoggable {
    protected static final VoxelShape SHAPE = makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    public static final BooleanProperty NORTH = SixWayBlock.NORTH;
    public static final BooleanProperty EAST = SixWayBlock.EAST;
    public static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
    public static final BooleanProperty WEST = SixWayBlock.WEST;
    public static final BooleanProperty OUTPUT = BooleanProperty.create("output");

    public SolarBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setStateProps(state -> state.with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false).with(OUTPUT, false));
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return super.getBlockItem(properties.maxStackSize(1), group);
    }

    @Override
    public SolarConfig getConfig() {
        return Configs.SOLAR_PANEL;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SolarTile(this.variant);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public AbstractContainer getContainer(int id, PlayerInventory inventory, AbstractTileEntity te, BlockRayTraceResult result) {
        if (te instanceof SolarTile) {
            return new SolarContainer(id, inventory, (SolarTile) te);
        }
        return null;
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
        return createState(world, currentPos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return createState(context.getWorld(), context.getPos());
    }

    private BlockState createState(IWorld world, BlockPos pos) {
        final BlockState state = getDefaultState();
        boolean north = canAttach(state, world, pos, Direction.NORTH);
        boolean south = canAttach(state, world, pos, Direction.SOUTH);
        boolean west = canAttach(state, world, pos, Direction.WEST);
        boolean east = canAttach(state, world, pos, Direction.EAST);
        return state.with(NORTH, !north).with(SOUTH, !south).with(WEST, !west).with(EAST, !east)
                .with(OUTPUT, Energy.isPresent(world.getTileEntity(pos.down()), Direction.DOWN))
                .with(BlockStateProperties.WATERLOGGED, world.getFluidState(pos).getFluid() == Fluids.WATER);
    }

    public boolean canAttach(BlockState state, IWorld world, BlockPos pos, Direction direction) {
        return world.getBlockState(pos.offset(direction)).getBlock() == this;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, OUTPUT);
        super.fillStateContainer(builder);
    }

    public List<Direction> getConnectedSides(BlockState state) {
        List<Direction> list = new ArrayList<>();
        if (!state.get(NORTH)) list.add(Direction.NORTH);
        if (!state.get(SOUTH)) list.add(Direction.SOUTH);
        if (!state.get(WEST)) list.add(Direction.WEST);
        if (!state.get(EAST)) list.add(Direction.EAST);
        return list;
    }
}
