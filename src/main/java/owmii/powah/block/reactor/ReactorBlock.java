package owmii.powah.block.reactor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import owmii.lib.block.AbstractEnergyProviderBlock;
import owmii.lib.block.TileBase;
import owmii.lib.config.IEnergyProviderConfig;
import owmii.lib.inventory.ContainerBase;
import owmii.lib.item.BlockItemBase;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.inventory.IContainers;
import owmii.powah.inventory.ReactorContainer;
import owmii.powah.item.ReactorItem;

import javax.annotation.Nullable;

public class ReactorBlock extends AbstractEnergyProviderBlock<Tier> {
    public static final BooleanProperty CORE = BooleanProperty.create("core");

    public ReactorBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setStateProps(state -> state.with(CORE, false));
    }

    @Override
    public BlockItemBase getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return new ReactorItem(this, properties, group);
    }

    @Override
    public IEnergyProviderConfig<Tier> getEnergyConfig() {
        return Configs.REACTOR;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (state.get(CORE)) {
            return new ReactorTile(this.variant);
        }
        return new ReactorPartTile(this.variant);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        TileEntity tileentity = world.getTileEntity(pos);
        if (tileentity instanceof ReactorTile) {
            ReactorTile tile = (ReactorTile) tileentity;
            tile.shuffle();
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return context.getPlayer() != null ? getDefaultState().with(CORE, true) : super.getStateForPlacement(context);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        TileEntity tileentity = world.getTileEntity(pos);
        if (tileentity instanceof ReactorTile) {
            ReactorTile reactor = (ReactorTile) tileentity;
            if (reactor.isBuilt()) {
                boolean interact = FluidUtil.interactWithFluidHandler(player, hand, reactor.tank);
                if (interact) {
                    reactor.markDirtyAndSync();
                    return ActionResultType.SUCCESS;
                }
                return super.onBlockActivated(state, world, pos, player, hand, result);
            }
        } else if (tileentity instanceof ReactorPartTile) {
            ReactorPartTile reactor = (ReactorPartTile) tileentity;
            if (reactor.isBuilt() && reactor.core().isPresent()) {
                return reactor.getBlock().onBlockActivated(state, world, reactor.getCorePos(), player, hand, result);
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, result);
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, TileBase inv) {
        if (inv instanceof ReactorTile) {
            return new ReactorContainer(IContainers.REACTOR, id, playerInventory, (ReactorTile) inv);
        }
        return null;
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileentity = world.getTileEntity(pos);
        if (tileentity instanceof ReactorTile) {
            ReactorTile tile = (ReactorTile) tileentity;
            tile.demolish(world);
        } else if (tileentity instanceof ReactorPartTile) {
            ReactorPartTile tile = (ReactorPartTile) tileentity;
            tile.demolish(world);
        }
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType) {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(CORE);
    }
}
