package owmii.powah.block.energyhopper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.items.CapabilityItemHandler;
import owmii.lib.block.AbstractEnergyBlock;
import owmii.lib.block.TileBase;
import owmii.lib.config.IEnergyConfig;
import owmii.lib.energy.Energy;
import owmii.lib.inventory.ContainerBase;
import owmii.lib.util.Text;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.config.EnergyHopperConfig;
import owmii.powah.inventory.EnergyHopperContainer;
import owmii.powah.inventory.IContainers;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnergyHopperBlock extends AbstractEnergyBlock<Tier> {
    public static final Map<Direction, VoxelShape> SHAPES = new HashMap<>();

    public EnergyHopperBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setDefaultState();
    }

    static {
        SHAPES.put(Direction.UP, makeCuboidShape(0, 0, 0, 16, 12, 16));
        SHAPES.put(Direction.DOWN, makeCuboidShape(0, 4, 0, 16, 16, 16));
        SHAPES.put(Direction.NORTH, makeCuboidShape(0, 0, 4, 16, 16, 16));
        SHAPES.put(Direction.SOUTH, makeCuboidShape(0, 0, 0, 16, 16, 12));
        SHAPES.put(Direction.EAST, makeCuboidShape(0, 0, 0, 12, 16, 16));
        SHAPES.put(Direction.WEST, makeCuboidShape(4, 0, 0, 16, 16, 16));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES.get(state.get(FACING));
    }

    @Override
    public IEnergyConfig<Tier> getEnergyConfig() {
        return Configs.ENERGY_HOPPER;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnergyHopperTile(this.variant);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        Direction direction = state.get(FACING);
        TileEntity tile = worldIn.getTileEntity(pos.offset(direction));
        return (tile instanceof IInventory || tile != null
                && tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction).isPresent())
                && !Energy.isPresent(tile, direction);
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, TileBase te, BlockRayTraceResult result) {
        if (te instanceof EnergyHopperTile) {
            return new EnergyHopperContainer(IContainers.ENERGY_HOPPER, id, playerInventory, (EnergyHopperTile) te);
        }
        return null;
    }

    @Override
    protected Facing getFacing() {
        return Facing.ALL;
    }

    @Override
    public void additionalEnergyInfo(ItemStack stack, Energy.Item energy, List<ITextComponent> tooltip) {
        tooltip.add(new TranslationTextComponent("info.powah.charging.speed", TextFormatting.DARK_GRAY + Text.numFormat(getChargingSpeed())).applyTextStyle(TextFormatting.GRAY));
    }

    public long getChargingSpeed() {
        return ((EnergyHopperConfig) getEnergyConfig()).getChargingSpeed(this.variant);
    }
}
