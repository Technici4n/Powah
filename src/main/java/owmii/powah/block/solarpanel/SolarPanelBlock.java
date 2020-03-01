package owmii.powah.block.solarpanel;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SixWayBlock;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import owmii.lib.block.AbstractEnergyProviderBlock;
import owmii.lib.block.TileBase;
import owmii.lib.config.IEnergyProviderConfig;
import owmii.lib.energy.Energy;
import owmii.lib.inventory.ContainerBase;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.inventory.IContainers;
import owmii.powah.inventory.SolarPanelContainer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SolarPanelBlock extends AbstractEnergyProviderBlock<Tier> implements IWaterLoggable {
    protected static final VoxelShape SHAPE = makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    public static final BooleanProperty NORTH = SixWayBlock.NORTH;
    public static final BooleanProperty EAST = SixWayBlock.EAST;
    public static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
    public static final BooleanProperty WEST = SixWayBlock.WEST;
    public static final BooleanProperty OUTPUT = BooleanProperty.create("output");

    public SolarPanelBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setStateProps(state -> state.with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false).with(OUTPUT, false));
    }

    @Override
    public IEnergyProviderConfig<Tier> getEnergyConfig() {
        return Configs.SOLAR_PANEL;
    }

    @Override
    public int stackSize() {
        return 1;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SolarPanelTile(this.variant);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, TileBase te, BlockRayTraceResult result) {
        if (te instanceof SolarPanelTile) {
            return new SolarPanelContainer(IContainers.SOLAR_PANEL, id, playerInventory, (SolarPanelTile) te);
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
                .with(WATERLOGGED, world.getFluidState(pos).getFluid() == Fluids.WATER);
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
