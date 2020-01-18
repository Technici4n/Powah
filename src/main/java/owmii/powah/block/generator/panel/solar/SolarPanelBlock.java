package owmii.powah.block.generator.panel.solar;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import owmii.lib.block.TileBase;
import owmii.lib.inventory.ContainerBase;
import owmii.powah.block.generator.GeneratorBlock;
import owmii.powah.inventory.IContainers;
import owmii.powah.inventory.SolarPanelContainer;

import javax.annotation.Nullable;

public class SolarPanelBlock extends GeneratorBlock implements IWaterLoggable {
    protected static final VoxelShape SHAPE = makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public SolarPanelBlock(Properties properties, int capacity, int transfer, int perTick) {
        super(properties, capacity, transfer, perTick);
        setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 0.75D, 16.0D);
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 5;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SolarPanelTile(this.capacity, this.maxExtract, this.perTick);
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, TileBase inv) {
        if (inv instanceof SolarPanelTile) {
            return new SolarPanelContainer(IContainers.SOLAR_PANEL, id, playerInventory, (SolarPanelTile) inv);
        }
        return null;
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    @Override
    protected boolean hasLitProp() {
        return false;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return false;
    }
}
