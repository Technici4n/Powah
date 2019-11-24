package zeroneye.powah.block.generator.furnator;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;
import zeroneye.lib.block.IInvBase;
import zeroneye.lib.inventory.ContainerBase;
import zeroneye.powah.block.generator.GeneratorBlock;
import zeroneye.powah.inventory.FurnatorContainer;
import zeroneye.powah.inventory.IContainers;

import javax.annotation.Nullable;

public class FurnatorBlock extends GeneratorBlock {

    public FurnatorBlock(Properties properties, int capacity, int transfer, int perTick) {
        super(properties, capacity, transfer, perTick);
        setDefaultState(this.stateContainer.getBaseState().with(H_FACING, Direction.NORTH).with(LIT, false));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FurnatorTile(this.capacity, this.maxExtract, this.perTick);
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, IInvBase inv) {
        if (this == Furnators.BASIC.get()) {
            return new FurnatorContainer(IContainers.FURNATOR_BASIC, id, playerInventory, (FurnatorTile) inv);
        } else if (this == Furnators.HARDENED.get()) {
            return new FurnatorContainer(IContainers.FURNATOR_HARDENED, id, playerInventory, (FurnatorTile) inv);
        } else if (this == Furnators.BLAZING.get()) {
            return new FurnatorContainer(IContainers.FURNATOR_BLAZING, id, playerInventory, (FurnatorTile) inv);
        } else if (this == Furnators.NIOTIC.get()) {
            return new FurnatorContainer(IContainers.FURNATOR_NIOTIC, id, playerInventory, (FurnatorTile) inv);
        } else if (this == Furnators.SPIRITED.get()) {
            return new FurnatorContainer(IContainers.FURNATOR_SPIRITED, id, playerInventory, (FurnatorTile) inv);
        }
        return super.getContainer(id, playerInventory, inv);
    }

    @Override
    protected FacingType getFacingType() {
        return FacingType.HORIZONTAL;
    }
}
