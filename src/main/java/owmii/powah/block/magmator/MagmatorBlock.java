package owmii.powah.block.magmator;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import owmii.lib.block.AbstractEnergyProviderBlock;
import owmii.lib.block.TileBase;
import owmii.lib.config.IEnergyProviderConfig;
import owmii.lib.inventory.ContainerBase;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.inventory.IContainers;
import owmii.powah.inventory.MagmatorContainer;

import javax.annotation.Nullable;

public class MagmatorBlock extends AbstractEnergyProviderBlock<Tier> implements IWaterLoggable {
    public MagmatorBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setDefaultState();
    }

    @Override
    public IEnergyProviderConfig<Tier> getEnergyConfig() {
        return Configs.MAGMATOR;
    }

    @Override
    public int stackSize() {
        return 1;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MagmatorTile(this.variant);
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, TileBase inv) {
        if (inv instanceof MagmatorTile) {
            return new MagmatorContainer(IContainers.MAGMATOR, id, playerInventory, (MagmatorTile) inv);
        }
        return null;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof MagmatorTile) {
            MagmatorTile magmator = (MagmatorTile) tile;
            if (FluidUtil.interactWithFluidHandler(player, hand, magmator.tank)) {
                magmator.markDirtyAndSync();
                return ActionResultType.SUCCESS;
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, result);
    }

    @Override
    protected boolean hasLitProp() {
        return true;
    }

    @Override
    protected Facing getFacing() {
        return Facing.HORIZONTAL;
    }

    @Override
    protected boolean isPlacerFacing() {
        return true;
    }
}
