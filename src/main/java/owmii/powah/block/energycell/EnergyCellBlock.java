package owmii.powah.block.energycell;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import owmii.lib.block.AbstractEnergyBlock;
import owmii.lib.block.AbstractTileEntity;
import owmii.lib.item.EnergyBlockItem;
import owmii.lib.logistics.inventory.AbstractContainer;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.config.EnergyCellConfig;
import owmii.powah.inventory.EnergyCellContainer;
import owmii.powah.item.EnergyCellItem;

import javax.annotation.Nullable;

public class EnergyCellBlock extends AbstractEnergyBlock<Tier, EnergyCellConfig, EnergyCellBlock> implements IWaterLoggable {
    public EnergyCellBlock(Properties properties, Tier tier) {
        super(properties, tier);
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return new EnergyCellItem(this, properties.maxStackSize(1), group);
    }

    @Override
    public EnergyCellConfig getConfig() {
        return Configs.ENERGY_CELL;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnergyCellTile(this.variant);
    }

    @Nullable
    @Override
    public AbstractContainer getContainer(int id, PlayerInventory inventory, AbstractTileEntity te, BlockRayTraceResult result) {
        if (te instanceof EnergyCellTile) {
            return new EnergyCellContainer(id, inventory, (EnergyCellTile) te);
        }
        return null;
    }
}
