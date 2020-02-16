package owmii.powah.block.endergate;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import owmii.lib.config.IEnergyConfig;
import owmii.powah.block.Tier;
import owmii.powah.block.endercell.EnderCellBlock;
import owmii.powah.config.Configs;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class EnderGateBlock extends EnderCellBlock {
    private static final Map<Direction, VoxelShape> SHAPES = new HashMap<>();

    public EnderGateBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setDefaultState();
    }

    static {
        SHAPES.put(Direction.UP, makeCuboidShape(6.0D, 15.5D, 6.0D, 10.0D, 16.0D, 10.0D));
        SHAPES.put(Direction.DOWN, makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 0.5D, 10.0D));
        SHAPES.put(Direction.NORTH, makeCuboidShape(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 0.5D));
        SHAPES.put(Direction.SOUTH, makeCuboidShape(6.0D, 6.0D, 15.5D, 10.0D, 10.0D, 16.0D));
        SHAPES.put(Direction.EAST, makeCuboidShape(15.5D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D));
        SHAPES.put(Direction.WEST, makeCuboidShape(0.0D, 6.0D, 6.0D, 0.5D, 10.0D, 10.0D));
    }

    @Override
    public IEnergyConfig<Tier> getEnergyConfig() {
        return Configs.ENDER_GATE;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return SHAPES.get(state.get(FACING));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnderGateTile(this.variant);
    }

    @Override
    protected boolean checkValidEnergySide() {
        return true;
    }

    @Override
    protected Facing getFacing() {
        return Facing.ALL;
    }
}
