package zeroneye.powah.block.generator.magmatic;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import zeroneye.lib.block.IInvBase;
import zeroneye.lib.inventory.ContainerBase;
import zeroneye.powah.block.generator.GeneratorBlock;
import zeroneye.powah.inventory.IContainers;
import zeroneye.powah.inventory.MagmaticGenContainer;

import javax.annotation.Nullable;

public class MagmaticGenBlock extends GeneratorBlock {
    private final int buckets;

    public MagmaticGenBlock(Properties properties, int capacity, int transfer, int perTick, int buckets) {
        super(properties, capacity, transfer, perTick);
        this.buckets = buckets;
        setDefaultState(this.stateContainer.getBaseState().with(H_FACING, Direction.NORTH).with(LIT, false));
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof MagmaticGenTile) {
            MagmaticGenTile genTile = (MagmaticGenTile) tile;
            boolean result = FluidUtil.interactWithFluidHandler(player, hand, genTile.tank);
            if (result) {
                genTile.markDirtyAndSync();
                return true;
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, blockRayTraceResult);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MagmaticGenTile(this.capacity, this.maxExtract, this.perTick, this.buckets);
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, IInvBase inv) {
        if (this == MagmaticGenerators.BASIC.get()) {
            return new MagmaticGenContainer(IContainers.MAGMATIC_GENERATOR_BASIC, id, playerInventory, (MagmaticGenTile) inv);
        } else if (this == MagmaticGenerators.HARDENED.get()) {
            return new MagmaticGenContainer(IContainers.MAGMATIC_GENERATOR_HARDENED, id, playerInventory, (MagmaticGenTile) inv);
        } else if (this == MagmaticGenerators.BLAZING.get()) {
            return new MagmaticGenContainer(IContainers.MAGMATIC_GENERATOR_BLAZING, id, playerInventory, (MagmaticGenTile) inv);
        } else if (this == MagmaticGenerators.NIOTIC.get()) {
            return new MagmaticGenContainer(IContainers.MAGMATIC_GENERATOR_NIOTIC, id, playerInventory, (MagmaticGenTile) inv);
        } else if (this == MagmaticGenerators.SPIRITED.get()) {
            return new MagmaticGenContainer(IContainers.MAGMATIC_GENERATOR_SPIRITED, id, playerInventory, (MagmaticGenTile) inv);
        }
        return super.getContainer(id, playerInventory, inv);
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    public int getBuckets() {
        return buckets;
    }

    @Override
    protected FacingType getFacingType() {
        return FacingType.HORIZONTAL;
    }
}
