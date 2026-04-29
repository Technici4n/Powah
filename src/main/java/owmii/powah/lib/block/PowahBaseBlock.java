package owmii.powah.lib.block;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import owmii.powah.lib.item.PowahBlockItem;
import owmii.powah.lib.logistics.inventory.BaseMenu;

public abstract class PowahBaseBlock<B extends PowahBaseBlock<B>> extends Block implements net.minecraft.world.level.block.EntityBlock {
    protected final Map<Direction, VoxelShape> shapes = new HashMap<>();

    public PowahBaseBlock(Properties properties) {
        super(properties);
        this.shapes.put(Direction.UP, Shapes.block());
        this.shapes.put(Direction.DOWN, Shapes.block());
        this.shapes.put(Direction.NORTH, Shapes.block());
        this.shapes.put(Direction.SOUTH, Shapes.block());
        this.shapes.put(Direction.EAST, Shapes.block());
        this.shapes.put(Direction.WEST, Shapes.block());
        setDefaultState();
    }

    public static VoxelShape box(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.box(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2), Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        if (getFacing() == Facing.ALL) {
            return this.shapes.get(state.getValue(FACING));
        } else if (getFacing() == Facing.HORIZONTAL) {
            return this.shapes.get(state.getValue(HORIZONTAL_FACING));
        } else {
            return super.getShape(state, worldIn, pos, context);
        }
    }

    public Component getDisplayName(ItemStack stack) {
        return asItem().getName(stack);
    }

    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder,
            TooltipFlag tooltipFlag) {
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof IBlockEntity) {
            ((IBlockEntity) tile).onAdded(world, state, oldState, isMoving);
        }
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof IBlockEntity) {
            ((IBlockEntity) tile).onPlaced(world, state, placer, stack);
        }
    }

    @Override
    public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
        if (te instanceof PowahBaseBlockEntity) {
            PowahBaseBlockEntity tile = (PowahBaseBlockEntity) te;
            ItemStack stack1 = tile.storeToStack(new ItemStack(this));
            popResource(world, pos, stack1);
            player.awardStat(Stats.BLOCK_MINED.get(this));
            player.causeFoodExhaustion(0.005F);
        } else {
            super.playerDestroy(world, player, pos, state, te, stack);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour,
            BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (this instanceof SimpleWaterloggedBlock && state.getValue(WATERLOGGED))
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        if (!state.canSurvive(level, pos)) {
            if (!level.isClientSide() && level.getBlockEntity(pos) instanceof PowahBaseBlockEntity<?> blockEntity) {
                ItemStack stack = blockEntity.storeToStack(new ItemStack(this));
                popResource((Level) level, pos, stack);
                return Blocks.AIR.defaultBlockState();
            }
        }
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    // Called on Forge, it's an override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return getCloneItemStack(world, pos);
    }

    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof PowahBaseBlockEntity tile) {
            return tile.storeToStack(new ItemStack(this));
        }
        return new ItemStack(this);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult result) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof PowahBaseBlockEntity) {
            MenuProvider provider = new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return new ItemStack(PowahBaseBlock.this).getHoverName();
                }

                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
                    return getContainer(i, playerInventory, (PowahBaseBlockEntity) tile, result);
                }
            };
            AbstractContainerMenu container = provider.createMenu(0, player.getInventory(), player);
            if (container != null) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.openMenu(provider, buffer -> {
                        buffer.writeBlockPos(pos);
                        additionalGuiData(buffer, state, world, pos, player, result);
                    });
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.useWithoutItem(state, world, pos, player, result);
    }

    @Nullable
    public <T extends PowahBaseBlockEntity> BaseMenu getContainer(int id, Inventory inventory, PowahBaseBlockEntity te,
            BlockHitResult result) {
        return null;
    }

    protected void additionalGuiData(FriendlyByteBuf buffer, BlockState state, Level world, BlockPos pos, Player player,
            BlockHitResult result) {
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return !state.canOcclude();
    }

    protected void setDefaultState() {
        setStateProps(state -> state);
    }

    protected void setStateProps(BaseState baseState) {
        BlockState state = this.stateDefinition.any();
        if (this instanceof SimpleWaterloggedBlock) {
            state = state.setValue(WATERLOGGED, false);
        }
        switch (getFacing()) {
        case HORIZONTAL -> state = state.setValue(HORIZONTAL_FACING, Direction.NORTH);
        case ALL -> state = state.setValue(FACING, Direction.NORTH);
        }
        if (hasLitProp()) {
            state = state.setValue(LIT, false);
        }
        registerDefaultState(baseState.get(state));
    }

    protected interface BaseState {
        BlockState get(BlockState state);
    }

    protected boolean isPlacerFacing() {
        return false;
    }

    protected Facing getFacing() {
        return Facing.NONE;
    }

    protected boolean hasLitProp() {
        return false;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return getFluidState(state).isEmpty() || super.propagatesSkylightDown(state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState();
        if (getFacing().equals(Facing.HORIZONTAL)) {
            if (!isPlacerFacing()) {
                state = facing(context, false);
            } else {
                state = defaultBlockState().setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
            }
        } else if (getFacing().equals(Facing.ALL)) {
            if (!isPlacerFacing()) {
                state = facing(context, true);
            } else {
                state = defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
            }
        }
        if (state != null && this instanceof SimpleWaterloggedBlock) {
            FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
            state = state.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        }
        return state;
    }

    @Nullable
    private BlockState facing(BlockPlaceContext context, boolean b) {
        var prop = getFacing() == Facing.ALL ? FACING : HORIZONTAL_FACING;
        BlockState blockstate = this.defaultBlockState();
        for (Direction direction : context.getNearestLookingDirections()) {
            if (b || direction.getAxis().isHorizontal()) {
                blockstate = blockstate.setValue(prop, b ? direction : direction.getOpposite());
                if (blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
                    return blockstate;
                }
            }
        }
        return null;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return switch (getFacing()) {
        case HORIZONTAL -> state.setValue(HORIZONTAL_FACING, rot.rotate(state.getValue(HORIZONTAL_FACING)));
        case ALL -> state.setValue(FACING, rot.rotate(state.getValue(FACING)));
        case NONE -> super.rotate(state, rot);
        };
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return switch (getFacing()) {
        case HORIZONTAL -> state.setValue(HORIZONTAL_FACING, mirror.mirror(state.getValue(HORIZONTAL_FACING)));
        case ALL -> state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
        case NONE -> super.mirror(state, mirror);
        };
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return this instanceof SimpleWaterloggedBlock && state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int id, int param) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        return tileEntity != null && tileEntity.triggerEvent(id, param);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        switch (getFacing()) {
        case ALL -> builder.add(FACING);
        case HORIZONTAL -> builder.add(HORIZONTAL_FACING);
        }
        if (this instanceof SimpleWaterloggedBlock)
            builder.add(WATERLOGGED);
        if (hasLitProp())
            builder.add(LIT);
    }

    @SuppressWarnings("unchecked")
    public PowahBlockItem<B> getBlockItem(Item.Properties properties, @Nullable ResourceKey<CreativeModeTab> group) {
        return new PowahBlockItem<>((B) this, properties, group);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (newBlockEntity(BlockPos.ZERO, state) instanceof PowahBaseTickingBlockEntity<?>) {
            return (l, p, s, be) -> ((PowahBaseTickingBlockEntity<?>) be).tick();
        }
        return null;
    }

    protected enum Facing {
        HORIZONTAL,
        ALL,
        NONE
    }
}
