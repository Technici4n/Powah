package owmii.powah.block.transmitter;

import owmii.powah.Powah;
import owmii.powah.config.v2.types.ChargingConfig;
import owmii.powah.lib.block.AbstractEnergyBlock;
import owmii.powah.lib.block.AbstractTileEntity;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.logistics.inventory.AbstractContainer;
import owmii.powah.block.Tier;
import owmii.powah.inventory.PlayerTransmitterContainer;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
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

public class PlayerTransmitterBlock extends AbstractEnergyBlock<ChargingConfig, PlayerTransmitterBlock> {
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final VoxelShape TOP_SHAPE = Shapes.or(box(6, 8, 6, 10, 12, 10), box(7, 4.5D, 7, 9, 8, 9), box(4, 0.5D, 4, 12, 1.5D, 12), box(4, 2D, 4, 12, 3D, 12), box(4, 3.5D, 4, 12, 4.5D, 12), box(1, -15, 1, 15, -1, 15), box(4, -1, 4, 12, 0, 12));
    public static final VoxelShape BOTTOM_SHAPE = Shapes.or(box(6, 24, 6, 10, 28, 10), box(7, 20.5, 7, 9, 24, 9), box(4, 16.5, 4, 12, 17.5, 12), box(4, 18.0, 4, 12, 19.0, 12), box(4, 3.5D + 16, 4, 12, 20.5, 12), box(1, 1, 1, 15, 15, 15), box(4, 15, 4, 12, 16, 12));

    public PlayerTransmitterBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setStateProps(state -> state.setValue(TOP, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return state.getValue(TOP) ? TOP_SHAPE : BOTTOM_SHAPE;
    }

    @Override
    public ChargingConfig getConfig() {
        return Powah.config().devices.player_transmitters;
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable CreativeModeTab group) {
        return super.getBlockItem(properties.stacksTo(1), group);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return !state.getValue(TOP) ? new PlayerTransmitterTile(pos, state, this.variant) : null;
    }

    @Nullable
    @Override
    public AbstractContainer getContainer(int id, Inventory inventory, AbstractTileEntity te, BlockHitResult result) {
        if (te instanceof PlayerTransmitterTile) {
            return new PlayerTransmitterContainer(id, inventory, (PlayerTransmitterTile) te);
        }
        return null;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (state.getValue(TOP)) {
            BlockState bottomState = world.getBlockState(pos.below());
            if (bottomState.getBlock() instanceof PlayerTransmitterBlock) {
                return bottomState.getBlock().use(bottomState, world, pos.below(), player, hand, result);
            }
        }
        return super.use(state, world, pos, player, hand, result);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        BlockState bottomState = world.getBlockState(currentPos.below());
        BlockState topState = world.getBlockState(currentPos.above());
        if (!state.getValue(TOP) && !(topState.getBlock() instanceof PlayerTransmitterBlock) || state.getValue(TOP) && !(bottomState.getBlock() instanceof PlayerTransmitterBlock)) {
            world.setBlock(currentPos, Blocks.AIR.defaultBlockState(), 3);
            return Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
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
        return state.getValue(TOP) ? super.canSurvive(state, world, pos) :
                (world.isEmptyBlock(pos.above()) || world.getBlockState(pos.above()).getBlock() == this);
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (player.isCreative()) {
            world.levelEvent(2001, pos, Block.getId(state));
            return;
        }
        BlockEntity tileEntity = world.getBlockEntity(state.getValue(TOP) ? pos.below() : pos);
        if (!world.isClientSide() && tileEntity instanceof PlayerTransmitterTile) {
            PlayerTransmitterTile tile = (PlayerTransmitterTile) tileEntity;
            ItemStack stack = tile.storeToStack(new ItemStack(this));
            popResource(world, pos, stack);
            world.levelEvent(2001, pos, Block.getId(state));
        }
    }

    @Override
    public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(TOP));
    }
}
