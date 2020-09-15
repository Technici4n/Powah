package owmii.powah.block.thermo;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import owmii.lib.block.AbstractGeneratorBlock;
import owmii.lib.block.AbstractTileEntity;
import owmii.lib.item.EnergyBlockItem;
import owmii.lib.logistics.inventory.AbstractContainer;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.config.generator.ThermoConfig;
import owmii.powah.inventory.ThermoContainer;

import javax.annotation.Nullable;

public class ThermoBlock extends AbstractGeneratorBlock<Tier, ThermoConfig, ThermoBlock> {
    public ThermoBlock(Properties properties, Tier variant) {
        super(properties, variant);
    }

    @Override
    public ThermoConfig getConfig() {
        return Configs.THERMO;
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return super.getBlockItem(properties.maxStackSize(1), group);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ThermoTile(this.variant);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof ThermoTile) {
            ThermoTile genTile = (ThermoTile) tile;
            boolean result = FluidUtil.interactWithFluidHandler(player, hand, genTile.getTank());
            if (result) {
                genTile.sync();
                return ActionResultType.SUCCESS;
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, blockRayTraceResult);
    }

    @Nullable
    @Override
    public AbstractContainer getContainer(int id, PlayerInventory inventory, AbstractTileEntity te, BlockRayTraceResult result) {
        if (te instanceof ThermoTile) {
            return new ThermoContainer(id, inventory, (ThermoTile) te);
        }
        return null;
    }
}
