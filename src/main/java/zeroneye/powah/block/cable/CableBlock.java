package zeroneye.powah.block.cable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import zeroneye.lib.block.TileBase;
import zeroneye.lib.inventory.ContainerBase;
import zeroneye.lib.util.Energy;
import zeroneye.lib.util.Side;
import zeroneye.powah.api.wrench.IWrenchable;
import zeroneye.powah.api.wrench.WrenchMode;
import zeroneye.powah.block.PowahBlock;
import zeroneye.powah.inventory.CableContainer;
import zeroneye.powah.inventory.IContainers;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CableBlock extends PowahBlock implements IWrenchable {
    public static final BooleanProperty NORTH = SixWayBlock.NORTH;
    public static final BooleanProperty EAST = SixWayBlock.EAST;
    public static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
    public static final BooleanProperty WEST = SixWayBlock.WEST;
    public static final BooleanProperty UP = SixWayBlock.UP;
    public static final BooleanProperty DOWN = SixWayBlock.DOWN;
    public static final BooleanProperty TILE = BooleanProperty.create("tile");

    private static final VoxelShape CABLE = makeCuboidShape(6.5, 6.5, 6.5, 9.5, 9.5, 9.5);
    private static final VoxelShape[] MULTIPARTS = new VoxelShape[]{
            makeCuboidShape(7, 7, 0, 9, 9, 6.5),
            makeCuboidShape(9.5, 7, 7, 16, 9, 9),
            makeCuboidShape(7, 7, 9.5, 9, 9, 16),
            makeCuboidShape(0, 7, 7, 6.5, 9, 9),
            makeCuboidShape(7, 9.5, 7, 9, 16, 9),
            makeCuboidShape(7, 0, 7, 9, 6.5, 9)
    };

    public CableBlock(Properties properties, int maxExtract, int maxReceive) {
        super(properties.sound(SoundType.METAL), 0, maxExtract, maxReceive);
        setDefaultState(getDefaultState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false).with(UP, false).with(DOWN, false).with(TILE, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        VoxelShape voxelShape = CABLE;
        if (state.get(NORTH)) voxelShape = VoxelShapes.or(voxelShape, MULTIPARTS[0]);
        if (state.get(EAST)) voxelShape = VoxelShapes.or(voxelShape, MULTIPARTS[1]);
        if (state.get(SOUTH)) voxelShape = VoxelShapes.or(voxelShape, MULTIPARTS[2]);
        if (state.get(WEST)) voxelShape = VoxelShapes.or(voxelShape, MULTIPARTS[3]);
        if (state.get(UP)) voxelShape = VoxelShapes.or(voxelShape, MULTIPARTS[4]);
        if (state.get(DOWN)) voxelShape = VoxelShapes.or(voxelShape, MULTIPARTS[5]);
        return voxelShape;
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, TileBase.TickableInv inv) {
        if (this == Cables.BASIC.get()) {
            return new CableContainer(IContainers.CABLE_BASIC, id, playerInventory, (CableTile) inv);
        } else if (this == Cables.HARDENED.get()) {
            return new CableContainer(IContainers.CABLE_HARDENED, id, playerInventory, (CableTile) inv);
        } else if (this == Cables.BLAZING.get()) {
            return new CableContainer(IContainers.CABLE_BLAZING, id, playerInventory, (CableTile) inv);
        } else if (this == Cables.NIOTIC.get()) {
            return new CableContainer(IContainers.CABLE_NIOTIC, id, playerInventory, (CableTile) inv);
        } else if (this == Cables.SPIRITED.get()) {
            return new CableContainer(IContainers.CABLE_SPIRITED, id, playerInventory, (CableTile) inv);
        }
        return super.getContainer(id, playerInventory, inv);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(TILE);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CableTile(this.maxReceive, this.maxExtract, this.isCreative);
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager) {
        VoxelShape voxelshape = CABLE;
        voxelshape.forEachBox((p_199284_3_, p_199284_5_, p_199284_7_, p_199284_9_, p_199284_11_, p_199284_13_) -> {
            double d1 = Math.min(1.0D, p_199284_9_ - p_199284_3_);
            double d2 = Math.min(1.0D, p_199284_11_ - p_199284_5_);
            double d3 = Math.min(1.0D, p_199284_13_ - p_199284_7_);
            int i = Math.max(2, MathHelper.ceil(d1 / 0.25D));
            int j = Math.max(2, MathHelper.ceil(d2 / 0.25D));
            int k = Math.max(2, MathHelper.ceil(d3 / 0.25D));
            for (int l = 0; l < i; ++l) {
                for (int i1 = 0; i1 < j; ++i1) {
                    for (int j1 = 0; j1 < k; ++j1) {
                        double d4 = ((double) l + 0.5D) / (double) i;
                        double d5 = ((double) i1 + 0.5D) / (double) j;
                        double d6 = ((double) j1 + 0.5D) / (double) k;
                        double d7 = d4 * d1 + p_199284_3_;
                        double d8 = d5 * d2 + p_199284_5_;
                        double d9 = d6 * d3 + p_199284_7_;
                        Minecraft.getInstance().particles.addEffect((new DiggingParticle(world, (double) pos.getX() + d7, (double) pos.getY() + d8, (double) pos.getZ() + d9, d4 - 0.5D, d5 - 0.5D, d6 - 0.5D, state)).setBlockPos(pos));
                    }
                }
            }
        });
        return true;
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return newState(worldIn, currentPos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return newState(context.getWorld(), context.getPos());
    }

    private BlockState newState(IWorld world, BlockPos pos) {
        final BlockState state = getDefaultState();

        boolean[] north = canAttach(state, world, pos, Direction.NORTH);
        boolean[] south = canAttach(state, world, pos, Direction.SOUTH);
        boolean[] west = canAttach(state, world, pos, Direction.WEST);
        boolean[] east = canAttach(state, world, pos, Direction.EAST);
        boolean[] up = canAttach(state, world, pos, Direction.UP);
        boolean[] down = canAttach(state, world, pos, Direction.DOWN);

        boolean tile = false;
        if (north[1] || south[1] || west[1] || east[1] || up[1] || down[1]) {
            tile = true;
        } else {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof CableTile) {
                InventoryHelper.dropInventoryItems((World) world, pos, (CableTile) tileEntity);
                tileEntity.remove();
            }
        }

        return state.with(NORTH, north[0]).with(SOUTH, south[0]).with(WEST, west[0]).with(EAST, east[0]).with(UP, up[0]).with(DOWN, down[0]).with(TILE, tile);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (worldIn.isRemote) return;
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof CableTile) {
            CableTile cable = (CableTile) tileEntity;
            for (Direction direction : Direction.values()) {
                if (cable.canExtract(direction)) {
                    cable.search(direction);
                }
            }
        } else {
            searchCables(worldIn, pos, pos);
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        searchCables(worldIn, pos, pos);
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    private boolean[] canAttach(BlockState state, IWorld world, BlockPos pos, Direction direction) {
        return new boolean[]{world.getBlockState(pos.offset(direction)).getBlock() instanceof CableBlock || checkTileType(world, pos, direction), checkTileType(world, pos, direction)};
    }

    private boolean checkTileType(IWorld world, BlockPos pos, Direction direction) {
        BlockPos pos1 = pos.offset(direction);
        TileEntity tileEntity = world.getTileEntity(pos1);
        return !(tileEntity instanceof CableTile) && Energy.getForgeEnergy(tileEntity, direction).isPresent();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, TILE);
        super.fillStateContainer(builder);
    }

    public void searchCables(IWorld world, BlockPos pos, CableTile first, Direction side) {
        if (!first.linkedCables.get(side).searchCach.contains(pos)) {
            for (Direction direction : Direction.values()) {
                BlockPos blockPos = pos.offset(direction);
                BlockState state = world.getBlockState(blockPos);
                if (state.getBlock() instanceof CableBlock) {
                    TileEntity tileEntity = world.getTileEntity(blockPos);
                    if (tileEntity instanceof CableTile) {
                        CableTile cable = (CableTile) tileEntity;
                        first.linkedCables.get(side).add(blockPos);
                    }
                    CableBlock cableBlock = (CableBlock) state.getBlock();
                    first.linkedCables.get(side).searchCach.add(pos);
                    cableBlock.searchCables(world, blockPos, first, side);
                }
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, blockIn, fromPos, isMoving);
        if (Energy.getForgeEnergy(world.getTileEntity(fromPos), Side.fromNeighbor(pos, fromPos).getOpposite()).isPresent()) {
            searchCables(world, pos, pos);
        }
    }

    static final Map<BlockPos, Set<BlockPos>> CACH = new HashMap<>();

    public void searchCables(IWorld world, BlockPos poss, BlockPos pos) {
        Set<BlockPos> ss = CACH.get(poss);
        if (ss == null) {
            ss = new HashSet<>();
        }
        if (!ss.contains(pos)) {
            for (Direction direction : Direction.values()) {
                BlockPos blockPos = pos.offset(direction);
                BlockState state = world.getBlockState(blockPos);
                if (state.getBlock() instanceof CableBlock) {
                    TileEntity tileEntity = world.getTileEntity(blockPos);
                    if (tileEntity instanceof CableTile) {
                        CableTile cable = (CableTile) tileEntity;
                        for (Direction side : Direction.values()) {
                            cable.linkedCables.get(side).cables().clear();
                            cable.search(side);
                        }
                    }
                    CableBlock cableBlock = (CableBlock) state.getBlock();
                    ss.add(pos);
                    CACH.put(poss, ss);
                    cableBlock.searchCables(world, poss, blockPos);
                }
            }
        }
        CACH.clear();
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        return super.onBlockActivated(state, world, pos, player, hand, blockRayTraceResult);
    }

    @Override
    public boolean onWrench(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, Direction side, WrenchMode mode, Vec3d hit) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof CableTile) {

        }
        return false;
    }
}
