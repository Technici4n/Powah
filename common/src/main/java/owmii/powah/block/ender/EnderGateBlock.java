package owmii.powah.block.ender;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.config.v2.types.EnderConfig;
import owmii.powah.inventory.EnderCellContainer;
import owmii.powah.lib.block.AbstractEnergyBlock;
import owmii.powah.lib.block.AbstractTileEntity;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.logistics.inventory.AbstractContainer;

public class EnderGateBlock extends AbstractEnergyBlock<EnderConfig, EnderGateBlock> {
    private static final Map<Direction, VoxelShape> SHAPES = new HashMap<>();

    public EnderGateBlock(Properties properties, Tier variant) {
        super(properties, variant);
    }

    static {
        SHAPES.put(Direction.UP, box(6.0D, 15.5D, 6.0D, 10.0D, 16.0D, 10.0D));
        SHAPES.put(Direction.DOWN, box(6.0D, 0.0D, 6.0D, 10.0D, 0.5D, 10.0D));
        SHAPES.put(Direction.NORTH, box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 0.5D));
        SHAPES.put(Direction.SOUTH, box(6.0D, 6.0D, 15.5D, 10.0D, 10.0D, 16.0D));
        SHAPES.put(Direction.EAST, box(15.5D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D));
        SHAPES.put(Direction.WEST, box(0.0D, 6.0D, 6.0D, 0.5D, 10.0D, 10.0D));
    }

    @Override
    public EnderConfig getConfig() {
        return Powah.config().devices.ender_gates;
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ResourceKey<CreativeModeTab> group) {
        return super.getBlockItem(properties, group);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(BlockStateProperties.FACING));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnderGateTile(pos, state, this.variant);
    }

    @Nullable
    @Override
    public AbstractContainer getContainer(int id, Inventory inventory, AbstractTileEntity te, BlockHitResult result) {
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
