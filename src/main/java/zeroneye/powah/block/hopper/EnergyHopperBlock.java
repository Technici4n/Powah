package zeroneye.powah.block.hopper;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;
import zeroneye.powah.block.PowahBlock;

import javax.annotation.Nullable;

public class EnergyHopperBlock extends PowahBlock implements IWaterLoggable {
    public EnergyHopperBlock(Properties properties, int capacity, int maxReceive) {
        super(properties, capacity, 0, maxReceive);
        setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, false).with(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnergyHopperTile();
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    @Override
    protected boolean waterLogged() {
        return true;
    }

    @Override
    protected FacingType getFacingType() {
        return FacingType.ALL;
    }
}
