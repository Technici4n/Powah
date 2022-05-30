package owmii.lib.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import owmii.lib.logistics.inventory.AbstractContainer;
import owmii.lib.registry.IRegistryObject;
import owmii.lib.registry.IVariant;
import owmii.lib.registry.IVariantEntry;
import owmii.lib.registry.Registry;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.state.properties.BlockStateProperties.*;

public class AbstractBlock<V extends IVariant, B extends AbstractBlock<V, B>> extends Block implements IVariantEntry<V, B>, IBlock<V, B>, IRegistryObject<Block> {
    public static final VoxelShape SEMI_FULL_SHAPE = makeCuboidShape(0.01D, 0.01D, 0.01D, 15.99D, 15.99D, 15.99D);
    protected final Map<Direction, VoxelShape> shapes = new HashMap<>();
    protected final V variant;

    @SuppressWarnings("NullableProblems")
    private Registry<Block> registry;

    public AbstractBlock(Properties properties) {
        this(properties, IVariant.getEmpty());
    }

    public AbstractBlock(Properties properties, V variant) {
        super(properties);
        this.variant = variant;
        this.shapes.put(Direction.UP, VoxelShapes.fullCube());
        this.shapes.put(Direction.DOWN, VoxelShapes.fullCube());
        this.shapes.put(Direction.NORTH, VoxelShapes.fullCube());
        this.shapes.put(Direction.SOUTH, VoxelShapes.fullCube());
        this.shapes.put(Direction.EAST, VoxelShapes.fullCube());
        this.shapes.put(Direction.WEST, VoxelShapes.fullCube());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (!this.shapes.isEmpty() && !getFacing().equals(Facing.NONE)) {
            return this.shapes.get(state.get(FACING));
        } else {
            return super.getShape(state, worldIn, pos, context);
        }
    }

    public ITextComponent getDisplayName(ItemStack stack) {
        return new TranslationTextComponent(asItem().getTranslationKey(stack));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Block> getSiblings() {
        if (getVariant() instanceof IVariant.Single) {
            return getRegistry().getObjects().get(getRegistryName());
        }
        return getRegistry().getObjects().get(getSiblingsKey((B) this));
    }

    @Override
    public Registry<Block> getRegistry() {
        return this.registry;
    }

    @Override
    public void setRegistry(Registry<Block> registry) {
        this.registry = registry;
    }

    @Override
    public V getVariant() {
        return this.variant;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IBlockEntity) {
            ((IBlockEntity) tile).onAdded(world, state, oldState, isMoving);
        }
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IBlockEntity) {
            ((IBlockEntity) tile).onRemoved(world, state, newState, isMoving);
        }
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IBlockEntity) {
            ((IBlockEntity) tile).onPlaced(world, state, placer, stack);
        }
    }

    @Override
    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (te instanceof AbstractTileEntity) {
            AbstractTileEntity tile = (AbstractTileEntity) te;
            ItemStack stack1 = tile.storeToStack(new ItemStack(this));
            spawnAsEntity(world, pos, stack1);
            player.addStat(Stats.BLOCK_MINED.get(this));
            player.addExhaustion(0.005F);
        } else {
            super.harvestBlock(world, player, pos, state, te, stack);
        }
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
        if (this instanceof IWaterLoggable && state.get(WATERLOGGED))
            world.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        if (!state.isValidPosition(world, currentPos)) {
            TileEntity tileEntity = world.getTileEntity(currentPos);
            if (!world.isRemote() && tileEntity instanceof AbstractTileEntity) {
                AbstractTileEntity tile = (AbstractTileEntity) tileEntity;
                ItemStack stack = tile.storeToStack(new ItemStack(this));
                spawnAsEntity((World) world, currentPos, stack);
                world.destroyBlock(currentPos, false);
            }
        }
        return super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof AbstractTileEntity) {
            AbstractTileEntity tile = (AbstractTileEntity) te;
            ItemStack stack = tile.storeToStack(new ItemStack(this));
            return tile.storeToStack(new ItemStack(this));
        }
        return super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof AbstractTileEntity) {
            INamedContainerProvider provider = new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return new ItemStack(AbstractBlock.this).getDisplayName();
                }

                @Nullable
                @Override
                public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    return getContainer(i, playerInventory, (AbstractTileEntity) tile, result);
                }
            };
            Container container = provider.createMenu(0, player.inventory, player);
            if (container != null) {
                if (player instanceof ServerPlayerEntity) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, provider, buffer -> {
                        buffer.writeBlockPos(pos);
                        additionalGuiData(buffer, state, world, pos, player, hand, result);
                    });
                }
                return ActionResultType.SUCCESS;
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, result);
    }

    @Nullable
    public <T extends AbstractTileEntity> AbstractContainer getContainer(int id, PlayerInventory inventory, AbstractTileEntity te, BlockRayTraceResult result) {
        return null;
    }

    protected void additionalGuiData(PacketBuffer buffer, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
    }

    @Override
    public boolean isTransparent(BlockState state) {
        return !state.isSolid();
    }

    protected void setDefaultState() {
        setStateProps(state -> state);
    }

    protected void setStateProps(BaseState baseState) {
        BlockState state = this.stateContainer.getBaseState();
        if (this instanceof IWaterLoggable) {
            state = state.with(WATERLOGGED, false);
        }
        if (!getFacing().equals(Facing.NONE)) {
            state = state.with(FACING, Direction.NORTH);
        }
        if (hasLitProp()) {
            state = state.with(LIT, false);
        }
        setDefaultState(baseState.get(state));
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
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return getFluidState(state).isEmpty() || super.propagatesSkylightDown(state, reader, pos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = getDefaultState();
        if (getFacing().equals(Facing.HORIZONTAL)) {
            if (!isPlacerFacing()) {
                state = facing(context, false);
            } else {
                state = getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
            }
        } else if (getFacing().equals(Facing.ALL)) {
            if (!isPlacerFacing()) {
                state = facing(context, true);
            } else {
                state = getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
            }
        }
        if (state != null && this instanceof IWaterLoggable) {
            FluidState fluidState = context.getWorld().getFluidState(context.getPos());
            state = state.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
        }
        return state;
    }

    @Nullable
    private BlockState facing(BlockItemUseContext context, boolean b) {
        BlockState blockstate = this.getDefaultState();
        for (Direction direction : context.getNearestLookingDirections()) {
            if (b || direction.getAxis().isHorizontal()) {
                blockstate = blockstate.with(FACING, b ? direction : direction.getOpposite());
                if (blockstate.isValidPosition(context.getWorld(), context.getPos())) {
                    return blockstate;
                }
            }
        }
        return null;
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        if (!getFacing().equals(Facing.NONE)) {
            for (Rotation rotation : Rotation.values()) {
                if (!rotation.equals(Rotation.NONE)) {
                    if (isValidPosition(super.rotate(state, world, pos, rotation), world, pos)) {
                        return super.rotate(state, world, pos, rotation);
                    }
                }
            }
        }
        return state;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        if (getFacing().equals(Facing.ALL) || getFacing().equals(Facing.HORIZONTAL)) {
            return state.with(FACING, rot.rotate(state.get(FACING)));
        }
        return super.rotate(state, rot);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        if (getFacing().equals(Facing.ALL) || getFacing().equals(Facing.HORIZONTAL)) {
            return state.rotate(mirror.toRotation(state.get(FACING)));
        }
        return super.mirror(state, mirror);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return this instanceof IWaterLoggable && state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public boolean eventReceived(BlockState state, World world, BlockPos pos, int id, int param) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity != null && tileEntity.receiveClientEvent(id, param);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        if (getFacing().equals(Facing.ALL) || getFacing().equals(Facing.HORIZONTAL)) builder.add(FACING);
        if (this instanceof IWaterLoggable) builder.add(WATERLOGGED);
        if (hasLitProp()) builder.add(LIT);
    }

    protected enum Facing {
        HORIZONTAL, ALL, NONE
    }
}
