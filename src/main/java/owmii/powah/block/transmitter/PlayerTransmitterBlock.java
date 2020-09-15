package owmii.powah.block.transmitter;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import owmii.lib.block.AbstractEnergyBlock;
import owmii.lib.block.AbstractTileEntity;
import owmii.lib.item.EnergyBlockItem;
import owmii.lib.logistics.inventory.AbstractContainer;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.config.PlayerTransmitterConfig;
import owmii.powah.inventory.PlayerTransmitterContainer;

import javax.annotation.Nullable;

public class PlayerTransmitterBlock extends AbstractEnergyBlock<Tier, PlayerTransmitterConfig, PlayerTransmitterBlock> {
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final VoxelShape TOP_SHAPE = VoxelShapes.or(makeCuboidShape(6, 8, 6, 10, 12, 10), makeCuboidShape(7, 4.5D, 7, 9, 8, 9), makeCuboidShape(4, 0.5D, 4, 12, 1.5D, 12), makeCuboidShape(4, 2D, 4, 12, 3D, 12), makeCuboidShape(4, 3.5D, 4, 12, 4.5D, 12), makeCuboidShape(1, -15, 1, 15, -1, 15), makeCuboidShape(4, -1, 4, 12, 0, 12));
    public static final VoxelShape BOTTOM_SHAPE = VoxelShapes.or(makeCuboidShape(6, 24, 6, 10, 28, 10), makeCuboidShape(7, 20.5, 7, 9, 24, 9), makeCuboidShape(4, 16.5, 4, 12, 17.5, 12), makeCuboidShape(4, 18.0, 4, 12, 19.0, 12), makeCuboidShape(4, 3.5D + 16, 4, 12, 20.5, 12), makeCuboidShape(1, 1, 1, 15, 15, 15), makeCuboidShape(4, 15, 4, 12, 16, 12));

    public PlayerTransmitterBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setStateProps(state -> state.with(TOP, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return state.get(TOP) ? TOP_SHAPE : BOTTOM_SHAPE;
    }

    @Override
    public PlayerTransmitterConfig getConfig() {
        return Configs.PLAYER_TRANSMITTER;
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return super.getBlockItem(properties.maxStackSize(1), group);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PlayerTransmitterTile(this.variant);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return !state.get(TOP);
    }

    @Nullable
    @Override
    public AbstractContainer getContainer(int id, PlayerInventory inventory, AbstractTileEntity te, BlockRayTraceResult result) {
        if (te instanceof PlayerTransmitterTile) {
            return new PlayerTransmitterContainer(id, inventory, (PlayerTransmitterTile) te);
        }
        return null;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (state.get(TOP)) {
            BlockState bottomState = world.getBlockState(pos.down());
            if (bottomState.getBlock() instanceof PlayerTransmitterBlock) {
                return bottomState.getBlock().onBlockActivated(bottomState, world, pos.down(), player, hand, result);
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, result);
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
        BlockState bottomState = world.getBlockState(currentPos.down());
        BlockState topState = world.getBlockState(currentPos.up());
        if (!state.get(TOP) && !(topState.getBlock() instanceof PlayerTransmitterBlock) || state.get(TOP) && !(bottomState.getBlock() instanceof PlayerTransmitterBlock)) {
            world.setBlockState(currentPos, Blocks.AIR.getDefaultState(), 3);
            return Blocks.AIR.getDefaultState();
        } else {
            return super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
        }
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onBlockAdded(state, world, pos, oldState, isMoving);
        if (!state.get(TOP) && world.isAirBlock(pos.up())) {
            world.setBlockState(pos.up(), getDefaultState().with(TOP, true), 3);
        }
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        return state.get(TOP) ? super.isValidPosition(state, world, pos) :
                (world.isAirBlock(pos.up()) || world.getBlockState(pos.up()).getBlock() == this);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (player.isCreative()) {
            world.playEvent(2001, pos, Block.getStateId(state));
            return;
        }
        TileEntity tileEntity = world.getTileEntity(state.get(TOP) ? pos.down() : pos);
        if (!world.isRemote() && tileEntity instanceof PlayerTransmitterTile) {
            PlayerTransmitterTile tile = (PlayerTransmitterTile) tileEntity;
            ItemStack stack = tile.storeToStack(new ItemStack(this));
            spawnAsEntity(world, pos, stack);
            world.playEvent(2001, pos, Block.getStateId(state));
        }
    }

    @Override
    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {

    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder.add(TOP));
    }
}
