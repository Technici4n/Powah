package owmii.powah.block.transmitter;

import java.util.function.LongSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.inventory.PlayerTransmitterMenu;
import owmii.powah.lib.block.PowahBaseBlockEntity;
import owmii.powah.lib.block.PowahBaseEnergyBlock;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.logistics.inventory.BaseMenu;

public class PlayerTransmitterBlock extends PowahBaseEnergyBlock<PlayerTransmitterBlock> {
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final VoxelShape TOP_SHAPE = Shapes.or(box(6, 8, 6, 10, 12, 10), box(7, 4.5D, 7, 9, 8, 9), box(4, 0.5D, 4, 12, 1.5D, 12),
            box(4, 2D, 4, 12, 3D, 12), box(4, 3.5D, 4, 12, 4.5D, 12), box(1, -15, 1, 15, -1, 15), box(4, -1, 4, 12, 0, 12));
    public static final VoxelShape BOTTOM_SHAPE = Shapes.or(box(6, 24, 6, 10, 28, 10), box(7, 20.5, 7, 9, 24, 9), box(4, 16.5, 4, 12, 17.5, 12),
            box(4, 18.0, 4, 12, 19.0, 12), box(4, 3.5D + 16, 4, 12, 20.5, 12), box(1, 1, 1, 15, 15, 15), box(4, 15, 4, 12, 16, 12));

    private final LongSupplier chargingSpeedSupplier;

    public PlayerTransmitterBlock(Properties properties, Tier tier) {
        var config = Powah.config().devices.player_transmitters;
        super(properties, tier, () -> config.getCapacity(tier), () -> config.getTransfer(tier));
        setStateProps(state -> state.setValue(TOP, false));
        this.chargingSpeedSupplier = () -> config.getChargingRate(tier);
    }

    public long getChargingSpeed() {
        return chargingSpeedSupplier.getAsLong();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return state.getValue(TOP) ? TOP_SHAPE : BOTTOM_SHAPE;
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ResourceKey<CreativeModeTab> group) {
        return super.getBlockItem(properties.stacksTo(1), group);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return !state.getValue(TOP) ? new PlayerTransmitterBlockEntity(pos, state) : null;
    }

    @Nullable
    @Override
    public BaseMenu getContainer(int id, Inventory inventory, PowahBaseBlockEntity te, BlockHitResult result) {
        if (te instanceof PlayerTransmitterBlockEntity) {
            return new PlayerTransmitterMenu(id, inventory, (PlayerTransmitterBlockEntity) te);
        }
        return null;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult result) {
        if (state.getValue(TOP)) {
            BlockState bottomState = world.getBlockState(pos.below());
            if (bottomState.getBlock() instanceof PlayerTransmitterBlock transmitterBlock) {
                return transmitterBlock.useWithoutItem(bottomState, world, pos.below(), player, result);
            }
        }
        return super.useWithoutItem(state, world, pos, player, result);
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour,
            BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        BlockState bottomState = level.getBlockState(pos.below());
        BlockState topState = level.getBlockState(pos.above());
        if (!state.getValue(TOP) && !(topState.getBlock() instanceof PlayerTransmitterBlock)
                || state.getValue(TOP) && !(bottomState.getBlock() instanceof PlayerTransmitterBlock)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
        }
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, world, pos, oldState, isMoving);
        if (!state.getValue(TOP) && world.isEmptyBlock(pos.above())) {
            world.setBlock(pos.above(), defaultBlockState().setValue(TOP, true), 3);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return state.getValue(TOP) ? super.canSurvive(state, world, pos)
                : (world.isEmptyBlock(pos.above()) || world.getBlockState(pos.above()).getBlock() == this);
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (player.isCreative()) {
            world.levelEvent(2001, pos, Block.getId(state));
            return state;
        }
        BlockEntity tileEntity = world.getBlockEntity(state.getValue(TOP) ? pos.below() : pos);
        if (!world.isClientSide() && tileEntity instanceof PlayerTransmitterBlockEntity tile) {
            ItemStack stack = tile.storeToStack(new ItemStack(this));
            popResource(world, pos, stack);
            world.levelEvent(2001, pos, Block.getId(state));
        }
        return state;
    }

    @Override
    public void playerDestroy(ServerLevel world, ServerPlayer player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(TOP));
    }
}
