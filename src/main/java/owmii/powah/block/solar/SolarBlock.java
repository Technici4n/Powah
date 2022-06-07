package owmii.powah.block.solar;

import owmii.powah.lib.block.AbstractGeneratorBlock;
import owmii.powah.lib.block.AbstractTileEntity;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.logistics.inventory.AbstractContainer;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.config.generator.SolarConfig;
import owmii.powah.inventory.SolarContainer;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.ArrayList;
import java.util.List;

public class SolarBlock extends AbstractGeneratorBlock<Tier, SolarConfig, SolarBlock> implements SimpleWaterloggedBlock {
    protected static final VoxelShape SHAPE = box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final BooleanProperty OUTPUT = BooleanProperty.create("output");

    public SolarBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setStateProps(state -> state.setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(OUTPUT, false));
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable CreativeModeTab group) {
        return super.getBlockItem(properties.stacksTo(1), group);
    }

    @Override
    public SolarConfig getConfig() {
        return Configs.SOLAR_PANEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SolarTile(pos, state, this.variant);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public AbstractContainer getContainer(int id, Inventory inventory, AbstractTileEntity te, BlockHitResult result) {
        if (te instanceof SolarTile) {
            return new SolarContainer(id, inventory, (SolarTile) te);
        }
        return null;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        return createState(world, currentPos);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return false;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return createState(context.getLevel(), context.getClickedPos());
    }

    private BlockState createState(LevelAccessor world, BlockPos pos) {
        final BlockState state = defaultBlockState();
        boolean north = canAttach(state, world, pos, Direction.NORTH);
        boolean south = canAttach(state, world, pos, Direction.SOUTH);
        boolean west = canAttach(state, world, pos, Direction.WEST);
        boolean east = canAttach(state, world, pos, Direction.EAST);
        return state.setValue(NORTH, !north).setValue(SOUTH, !south).setValue(WEST, !west).setValue(EAST, !east)
                .setValue(OUTPUT, Energy.isPresent(world.getBlockEntity(pos.below()), Direction.DOWN))
                .setValue(BlockStateProperties.WATERLOGGED, world.getFluidState(pos).getType() == Fluids.WATER);
    }

    public boolean canAttach(BlockState state, LevelAccessor world, BlockPos pos, Direction direction) {
        return world.getBlockState(pos.relative(direction)).getBlock() == this;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, OUTPUT);
        super.createBlockStateDefinition(builder);
    }

    public List<Direction> getConnectedSides(BlockState state) {
        List<Direction> list = new ArrayList<>();
        if (!state.getValue(NORTH)) list.add(Direction.NORTH);
        if (!state.getValue(SOUTH)) list.add(Direction.SOUTH);
        if (!state.getValue(WEST)) list.add(Direction.WEST);
        if (!state.getValue(EAST)) list.add(Direction.EAST);
        return list;
    }
}
