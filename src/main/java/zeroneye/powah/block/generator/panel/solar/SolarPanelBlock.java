package zeroneye.powah.block.generator.panel.solar;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import zeroneye.lib.block.TileBase;
import zeroneye.lib.inventory.ContainerBase;
import zeroneye.powah.block.generator.GeneratorBlock;
import zeroneye.powah.inventory.IContainers;
import zeroneye.powah.inventory.SolarPanelContainer;

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
        if (this == SolarPanels.BASIC.get()) {
            return new SolarPanelContainer(IContainers.SOLAR_PANEL_BASIC, id, playerInventory, (SolarPanelTile) inv);
        } else if (this == SolarPanels.HARDENED.get()) {
            return new SolarPanelContainer(IContainers.SOLAR_PANEL_HARDENED, id, playerInventory, (SolarPanelTile) inv);
        } else if (this == SolarPanels.BLAZING.get()) {
            return new SolarPanelContainer(IContainers.SOLAR_PANEL_BLAZING, id, playerInventory, (SolarPanelTile) inv);
        } else if (this == SolarPanels.NIOTIC.get()) {
            return new SolarPanelContainer(IContainers.SOLAR_PANEL_NIOTIC, id, playerInventory, (SolarPanelTile) inv);
        } else if (this == SolarPanels.SPIRITED.get()) {
            return new SolarPanelContainer(IContainers.SOLAR_PANEL_SPIRITED, id, playerInventory, (SolarPanelTile) inv);
        }
        return super.getContainer(id, playerInventory, inv);
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    @Override
    protected boolean hasLitProp() {
        return false;
    }
}
