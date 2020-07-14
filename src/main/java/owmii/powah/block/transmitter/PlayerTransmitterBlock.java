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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
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

public class PlayerTransmitterBlock extends AbstractEnergyBlock<Tier, PlayerTransmitterConfig> {
    public static final BooleanProperty TOP = BooleanProperty.create("top");

    public PlayerTransmitterBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setStateProps(state -> state.with(TOP, false));
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
        if (state.get(TOP) && (!(bottomState.getBlock() instanceof PlayerTransmitterBlock) || bottomState.get(TOP))) {
            world.setBlockState(currentPos, Blocks.AIR.getDefaultState(), 3);
            return Blocks.AIR.getDefaultState();
        } else {
            return super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onBlockAdded(state, world, pos, oldState, isMoving);
        if (!state.get(TOP)) {
            world.setBlockState(pos.up(), getDefaultState().with(TOP, true), 3);
        }
    }

    @Override
    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (state.get(TOP)) {
            BlockState bottomState = world.getBlockState(pos.down());
            if (bottomState.getBlock() instanceof PlayerTransmitterBlock) {
                bottomState.getBlock().harvestBlock(world, player, pos.down(), bottomState, world.getTileEntity(pos.down()), stack);
                world.setBlockState(pos.down(), Blocks.AIR.getDefaultState(), 3);
            }
        } else {
            super.harvestBlock(world, player, pos, state, te, stack);
            world.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 3);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder.add(TOP));
    }
}
