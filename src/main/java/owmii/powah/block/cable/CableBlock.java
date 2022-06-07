package owmii.powah.block.cable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.IBlockRenderProperties;
import owmii.powah.api.energy.IEnergyConnector;
import owmii.powah.lib.block.AbstractEnergyBlock;
import owmii.powah.lib.block.AbstractTileEntity;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.logistics.inventory.AbstractContainer;
import owmii.powah.block.Tier;
import owmii.powah.config.CableConfig;
import owmii.powah.config.Configs;
import owmii.powah.inventory.CableContainer;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class CableBlock extends AbstractEnergyBlock<Tier, CableConfig, CableBlock> implements SimpleWaterloggedBlock, IEnergyConnector {
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final BooleanProperty UP = PipeBlock.UP;
    public static final BooleanProperty DOWN = PipeBlock.DOWN;
    public static final BooleanProperty TILE = BooleanProperty.create("tile");
    private static final VoxelShape CABLE = box(6.25, 6.25, 6.25, 9.75, 9.75, 9.75);
    private static final VoxelShape[] MULTIPART = new VoxelShape[]{box(6.5, 6.5, 0, 9.5, 9.5, 7), box(9.5, 6.5, 6.5, 16, 9.5, 9.5), box(6.5, 6.5, 9.5, 9.5, 9.5, 16), box(0, 6.5, 6.5, 6.5, 9.5, 9.5), box(6.5, 9.5, 6.5, 9.5, 16, 9.5), box(6.5, 0, 6.5, 9.5, 7, 9.5)};

    public CableBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setStateProps(state -> state.setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(UP, false).setValue(DOWN, false).setValue(TILE, false));
    }

    @Override
    public CableConfig getConfig() {
        return Configs.CABLE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(TILE) ? new CableTile(pos, state, this.variant) : null;
    }

    @Override
    public boolean isChargeable(ItemStack stack) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape voxelShape = CABLE;
        if (state.getValue(NORTH) || canConnectEnergy(world, pos, Direction.NORTH))
            voxelShape = Shapes.or(voxelShape, MULTIPART[0]);
        if (state.getValue(EAST) || canConnectEnergy(world, pos, Direction.EAST))
            voxelShape = Shapes.or(voxelShape, MULTIPART[1]);
        if (state.getValue(SOUTH) || canConnectEnergy(world, pos, Direction.SOUTH))
            voxelShape = Shapes.or(voxelShape, MULTIPART[2]);
        if (state.getValue(WEST) || canConnectEnergy(world, pos, Direction.WEST))
            voxelShape = Shapes.or(voxelShape, MULTIPART[3]);
        if (state.getValue(UP) || canConnectEnergy(world, pos, Direction.UP))
            voxelShape = Shapes.or(voxelShape, MULTIPART[4]);
        if (state.getValue(DOWN) || canConnectEnergy(world, pos, Direction.DOWN))
            voxelShape = Shapes.or(voxelShape, MULTIPART[5]);
        return voxelShape;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        if (world.getBlockEntity(currentPos) instanceof CableTile cable) {
            cable.energySides.clear();
            for (Direction direction : Direction.values()) {
                if (canConnectEnergy(world, currentPos, direction)) {
                    cable.energySides.add(direction);
                }
            }
            cable.sync();
        }

        return createCableState(world, currentPos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return createCableState(context.getLevel(), context.getClickedPos());
    }

    private BlockState createCableState(LevelAccessor world, BlockPos pos) {
        final BlockState state = defaultBlockState();
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
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof CableTile cable) {
                cable.getInventory().drop((Level) world, pos);
                cable.setRemoved();
            }
        }
        FluidState fluidState = world.getFluidState(pos);
        return state.setValue(NORTH, north[0] && !north[1]).setValue(SOUTH, south[0] && !south[1]).setValue(WEST, west[0] && !west[1]).setValue(EAST, east[0] && !east[1]).setValue(UP, up[0] && !up[1]).setValue(DOWN, down[0] && !down[1]).setValue(TILE, tile).setValue(BlockStateProperties.WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        Optional<Direction> hitSide = getHitSide(result.getLocation(), pos);
        if (hitSide.isPresent() && !canConnectEnergy(world, pos, hitSide.get())) {
            return InteractionResult.FAIL;
        }
        return super.use(state, world, pos, player, hand, result);
    }

    @Nullable
    @Override
    public <T extends AbstractTileEntity> AbstractContainer getContainer(int id, Inventory inventory, AbstractTileEntity te, BlockHitResult result) {
        if (te instanceof CableTile) {
            return new CableContainer(id, inventory, (CableTile) te);
        }
        return super.getContainer(id, inventory, te, result);
    }

    @Override
    protected void additionalGuiData(FriendlyByteBuf buffer, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        getHitSide(result.getLocation(), pos).ifPresent(side -> buffer.writeInt(side.get3DDataValue()));
        super.additionalGuiData(buffer, state, world, pos, player, hand, result);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        if (world.isClientSide) return;
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof CableTile cable) {
            for (Direction direction : Direction.values()) {
                if (cable.canExtractEnergy(direction)) {
                    cable.search(this, direction);
                }
            }
        } else {
            findCables(world, pos, pos);
        }
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof CableTile cable) {
            cable.energySides.clear();
            for (Direction direction : Direction.values()) {
                if (canConnectEnergy(world, pos, direction)) {
                    cable.energySides.add(direction);
                }
            }
            cable.sync();
        }
        super.onPlace(state, world, pos, oldState, isMoving);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        findCables(world, pos, pos);
        super.onRemove(state, world, pos, newState, isMoving);
    }

    public boolean[] canAttach(BlockState state, LevelAccessor world, BlockPos pos, Direction direction) {
        return new boolean[]{world.getBlockState(pos.relative(direction)).getBlock() == this || canConnectEnergy(world, pos, direction), canConnectEnergy(world, pos, direction)};
    }

    public boolean canConnectEnergy(BlockGetter world, BlockPos pos, Direction direction) {
        BlockEntity tile = world.getBlockEntity(pos.relative(direction));
        return !(tile instanceof CableTile) && Energy.isPresent(tile, direction);
    }

    static final Map<BlockPos, Set<BlockPos>> CACHE = new HashMap<>();

    public void searchCables(LevelAccessor world, BlockPos pos, CableTile first, Direction side) {
        if (!first.proxyMap.get(side).searchCache.contains(pos)) {
            for (Direction direction : Direction.values()) {
                BlockPos blockPos = pos.relative(direction);
                if (blockPos.equals(first.getBlockPos())) continue;
                BlockState state = world.getBlockState(blockPos);
                if (state.getBlock() == this) {
                    BlockEntity tileEntity = world.getBlockEntity(blockPos);
                    if (tileEntity instanceof CableTile) {
                        CableTile cable = (CableTile) tileEntity;
                        first.proxyMap.get(side).add(blockPos);
                    }
                    CableBlock cableBlock = (CableBlock) state.getBlock();
                    first.proxyMap.get(side).searchCache.add(pos);
                    cableBlock.searchCables(world, blockPos, first, side);
                }
            }
        }
    }

    public void findCables(LevelAccessor world, BlockPos poss, BlockPos pos) {
        Set<BlockPos> ss = CACHE.get(poss);
        if (ss == null) {
            ss = new HashSet<>();
        }
        if (!ss.contains(pos)) {
            for (Direction direction : Direction.values()) {
                BlockPos blockPos = pos.relative(direction);
                BlockState state = world.getBlockState(blockPos);
                if (state.getBlock() == this) {
                    BlockEntity tileEntity = world.getBlockEntity(blockPos);
                    if (tileEntity instanceof CableTile cable) {
                        for (Direction side : Direction.values()) {
                            cable.proxyMap.get(side).cables().clear();
                            cable.search(this, side);
                        }
                    }
                    CableBlock cableBlock = (CableBlock) state.getBlock();
                    ss.add(pos);
                    CACHE.put(poss, ss);
                    cableBlock.findCables(world, poss, blockPos);
                }
            }
        }
        CACHE.clear();
    }

    public static Optional<Direction> getHitSide(Vec3 hit, BlockPos pos) {
        double x = hit.x - pos.getX();
        double y = hit.y - pos.getY();
        double z = hit.z - pos.getZ();
        if (x > 0.0D && x < 0.4D) return Optional.of(Direction.WEST);
        else if (x > 0.6D && x < 1.0D) return Optional.of(Direction.EAST);
        else if (z > 0.0D && z < 0.4D) return Optional.of(Direction.NORTH);
        else if (z > 0.6D && z < 1.0D) return Optional.of(Direction.SOUTH);
        else if (y > 0.6D && y < 1.0D) return Optional.of(Direction.UP);
        else if (y > 0.0D && y < 0.4D) return Optional.of(Direction.DOWN);
        return Optional.empty();
    }

    @Override
    public void initializeClient(Consumer<IBlockRenderProperties> consumer) {
        consumer.accept(new IBlockRenderProperties() {
            @Override
            public boolean addDestroyEffects(BlockState state, Level level, BlockPos pos, ParticleEngine manager) {
                VoxelShape voxelshape = CABLE;
                voxelshape.forAllBoxes((x, y, z, xx, yy, zz) -> {
                    double d1 = Math.min(1.0D, xx - x);
                    double d2 = Math.min(1.0D, yy - y);
                    double d3 = Math.min(1.0D, zz - z);
                    int i = Math.max(2, Mth.ceil(d1 / 0.25D));
                    int j = Math.max(2, Mth.ceil(d2 / 0.25D));
                    int k = Math.max(2, Mth.ceil(d3 / 0.25D));
                    for (int l = 0; l < i; ++l) {
                        for (int i1 = 0; i1 < j; ++i1) {
                            for (int j1 = 0; j1 < k; ++j1) {
                                double d4 = ((double) l + 0.5D) / (double) i;
                                double d5 = ((double) i1 + 0.5D) / (double) j;
                                double d6 = ((double) j1 + 0.5D) / (double) k;
                                double d7 = d4 * d1 + x;
                                double d8 = d5 * d2 + y;
                                double d9 = d6 * d3 + z;
                                Minecraft.getInstance().particleEngine.add((new TerrainParticle((ClientLevel) level, (double) pos.getX() + d7, (double) pos.getY() + d8, (double) pos.getZ() + d9, d4 - 0.5D, d5 - 0.5D, d6 - 0.5D, state)).updateSprite(state, pos));
                            }
                        }
                    }
                });
                return true;
            }
        });
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, TILE);
        super.createBlockStateDefinition(builder);
    }
}
