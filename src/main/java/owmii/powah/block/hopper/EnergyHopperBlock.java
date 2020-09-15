package owmii.powah.block.hopper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.items.CapabilityItemHandler;
import owmii.lib.block.AbstractEnergyBlock;
import owmii.lib.block.AbstractTileEntity;
import owmii.lib.item.EnergyBlockItem;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.logistics.inventory.AbstractContainer;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.config.EnergyHopperConfig;
import owmii.powah.inventory.EnergyHopperContainer;

import javax.annotation.Nullable;

public class EnergyHopperBlock extends AbstractEnergyBlock<Tier, EnergyHopperConfig, EnergyHopperBlock> {
    public EnergyHopperBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setDefaultState();
        this.shapes.put(Direction.UP, makeCuboidShape(0, 0, 0, 16, 12, 16));
        this.shapes.put(Direction.DOWN, makeCuboidShape(0, 4, 0, 16, 16, 16));
        this.shapes.put(Direction.NORTH, makeCuboidShape(0, 0, 4, 16, 16, 16));
        this.shapes.put(Direction.SOUTH, makeCuboidShape(0, 0, 0, 16, 16, 12));
        this.shapes.put(Direction.EAST, makeCuboidShape(0, 0, 0, 12, 16, 16));
        this.shapes.put(Direction.WEST, makeCuboidShape(4, 0, 0, 16, 16, 16));
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return super.getBlockItem(properties.maxStackSize(1), group);
    }

    @Override
    public EnergyHopperConfig getConfig() {
        return Configs.ENERGY_HOPPER;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnergyHopperTile(this.variant);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        Direction direction = state.get(BlockStateProperties.FACING);
        TileEntity tile = worldIn.getTileEntity(pos.offset(direction));
        return (tile instanceof IInventory || tile != null
                && tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction).isPresent())
                && !Energy.isPresent(tile, direction);
    }

    @Nullable
    @Override
    public <T extends AbstractTileEntity> AbstractContainer getContainer(int id, PlayerInventory inventory, AbstractTileEntity te, BlockRayTraceResult result) {
        if (te instanceof EnergyHopperTile) {
            return new EnergyHopperContainer(id, inventory, (EnergyHopperTile) te);
        }
        return null;
    }

    @Override
    protected Facing getFacing() {
        return Facing.ALL;
    }
}
