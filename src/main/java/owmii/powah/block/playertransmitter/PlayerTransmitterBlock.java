package owmii.powah.block.playertransmitter;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import owmii.lib.block.AbstractEnergyBlock;
import owmii.lib.block.TileBase;
import owmii.lib.config.IEnergyConfig;
import owmii.lib.energy.Energy;
import owmii.lib.inventory.ContainerBase;
import owmii.lib.util.Text;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.config.PlayerTransmitterConfig;
import owmii.powah.inventory.IContainers;
import owmii.powah.inventory.PlayerTransmitterContainer;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.util.math.shapes.VoxelShapes.combineAndSimplify;

public class PlayerTransmitterBlock extends AbstractEnergyBlock<Tier> {
    private static final Map<Direction, VoxelShape> SHAPES = new HashMap<>();

    public PlayerTransmitterBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setDefaultState();
    }

    static {
        SHAPES.put(Direction.DOWN, makeCuboidShape(10.0D, 0.0D, 10.0D, 6.0D, 14.0D, 6.0D));
        SHAPES.put(Direction.UP, makeCuboidShape(10.0D, 2.0D, 10.0D, 6.0D, 16.0D, 6.0D));
        SHAPES.put(Direction.SOUTH, combineAndSimplify(makeCuboidShape(10.0D, 6.0D, 12.0D, 6.0D, 16.0D, 8.0D), makeCuboidShape(10.0D, 6.0D, 8.0D, 6.0D, 10.0D, 16.0D), IBooleanFunction.OR));
        SHAPES.put(Direction.NORTH, combineAndSimplify(makeCuboidShape(10.0D, 6.0D, 8.0D, 6.0D, 16.0D, 4.0D), makeCuboidShape(10.0D, 6.0D, 0.0D, 6.0D, 10.0D, 8.0D), IBooleanFunction.OR));
        SHAPES.put(Direction.EAST, combineAndSimplify(makeCuboidShape(12.0D, 6.0D, 10.0D, 8.0D, 16.0D, 6.0D), makeCuboidShape(8.0D, 6.0D, 10.0D, 16.0D, 10.0D, 6.0D), IBooleanFunction.OR));
        SHAPES.put(Direction.WEST, combineAndSimplify(makeCuboidShape(8.0D, 6.0D, 10.0D, 4.0D, 16.0D, 6.0D), makeCuboidShape(0.0D, 6.0D, 10.0D, 6.0D, 10.0D, 6.0D), IBooleanFunction.OR));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES.get(state.get(FACING));
    }

    @Override
    public IEnergyConfig<Tier> getEnergyConfig() {
        return Configs.PLAYER_TRANSMITTER;
    }

    @Override
    public int stackSize() {
        return 1;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PlayerTransmitterTile(this.variant);
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, TileBase te, BlockRayTraceResult result) {
        if (te instanceof PlayerTransmitterTile) {
            return new PlayerTransmitterContainer(IContainers.PLAYER_TRANSMITTER, id, playerInventory, (PlayerTransmitterTile) te);
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

    @Override
    public void additionalEnergyInfo(ItemStack stack, Energy.Item energy, List<ITextComponent> tooltip) {
        tooltip.add(new TranslationTextComponent("info.powah.charging.speed", TextFormatting.DARK_GRAY + Text.numFormat(getChargingSpeed())).applyTextStyle(TextFormatting.GRAY));
    }

    public long getChargingSpeed() {
        return ((PlayerTransmitterConfig) getEnergyConfig()).getChargingSpeed(this.variant);
    }
}
