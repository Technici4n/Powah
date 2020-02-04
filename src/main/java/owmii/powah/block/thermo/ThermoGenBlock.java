package owmii.powah.block.thermo;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import owmii.lib.block.AbstractEnergyProviderBlock;
import owmii.lib.block.TileBase;
import owmii.lib.config.IEnergyProviderConfig;
import owmii.lib.inventory.ContainerBase;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;

import javax.annotation.Nullable;

import static net.minecraft.util.math.shapes.VoxelShapes.combineAndSimplify;
import static net.minecraft.util.math.shapes.VoxelShapes.fullCube;

public class ThermoGenBlock extends AbstractEnergyProviderBlock<Tier> implements IWaterLoggable {
    public static final VoxelShape SHAPE = combineAndSimplify(makeCuboidShape(2.0D, 1.0D, 2.0D, 14.0D, 14.0D, 14.0D), combineAndSimplify(makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D), combineAndSimplify(makeCuboidShape(0.5D, 1.5D, 0.5D, 15.5D, 2.5D, 15.5D), makeCuboidShape(0.5D, 3.25D, 0.5D, 15.5D, 4.25D, 15.5D), IBooleanFunction.OR), IBooleanFunction.OR), IBooleanFunction.OR);

    public ThermoGenBlock(Properties properties, Tier variant) {
        super(properties, variant);
    }

    @Override
    public IEnergyProviderConfig<Tier> getEnergyConfig() {
        return Configs.SOLAR_PANEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return fullCube();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ThermoGenTile(this.variant);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof ThermoGenTile) {
            ThermoGenTile genTile = (ThermoGenTile) tile;
            boolean result = FluidUtil.interactWithFluidHandler(player, hand, genTile.tank);
            if (result) {
                genTile.markDirtyAndSync();
                return ActionResultType.SUCCESS;
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, blockRayTraceResult);
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, TileBase inv) {
        if (inv instanceof ThermoGenTile) {
            // return new ThermoGenContainer(IContainers.THERMO_GENERATOR, id, playerInventory, (ThermoGeneratorTile) inv);
        }
        return null;
    }

//    @Override
//    public BlockItemBase getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
//        return new ThermoGeneratorItem(this, properties, group);
//    }
}
