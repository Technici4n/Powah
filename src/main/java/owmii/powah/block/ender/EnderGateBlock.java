package owmii.powah.block.ender;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import owmii.lib.block.AbstractEnergyBlock;
import owmii.lib.block.AbstractTileEntity;
import owmii.lib.item.EnergyBlockItem;
import owmii.lib.logistics.inventory.AbstractContainer;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.config.EnderGateConfig;
import owmii.powah.inventory.EnderCellContainer;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class EnderGateBlock extends AbstractEnergyBlock<Tier, EnderGateConfig, EnderGateBlock> {
    private static final Map<Direction, VoxelShape> SHAPES = new HashMap<>();

    public EnderGateBlock(Properties properties, Tier variant) {
        super(properties, variant);
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
    public EnderGateConfig getConfig() {
        return Configs.ENDER_GATE;
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return super.getBlockItem(properties.maxStackSize(1), group);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return SHAPES.get(state.get(BlockStateProperties.FACING));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnderGateTile(this.variant);
    }

    @Nullable
    @Override
    public AbstractContainer getContainer(int id, PlayerInventory inventory, AbstractTileEntity te, BlockRayTraceResult result) {
        if (te instanceof EnderGateTile) {
            return new EnderCellContainer(id, inventory, (EnderGateTile) te);
        }
        return null;
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
