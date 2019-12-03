package zeroneye.powah.block.generator.panel.solar;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import zeroneye.lib.block.TileBase;
import zeroneye.lib.inventory.ContainerBase;
import zeroneye.powah.block.generator.GeneratorBlock;
import zeroneye.powah.inventory.IContainers;
import zeroneye.powah.inventory.SolarPanelContainer;

import javax.annotation.Nullable;

public class SolarPanelBlock extends GeneratorBlock implements IWaterLoggable {
    protected static final VoxelShape SHAPE = makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public SolarPanelBlock(Properties properties, int capacity, int transfer, int perTick) {
        super(properties, capacity, transfer, perTick);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 0.75D, 16.0D);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return !state.get(WATERLOGGED);
    }

    @Override
    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
        return (state == null ? getDefaultState() : state).with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
        super.fillStateContainer(builder);
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
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, TileBase.TickableInv inv) {
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
