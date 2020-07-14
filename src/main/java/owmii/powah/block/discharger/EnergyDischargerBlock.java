package owmii.powah.block.discharger;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import owmii.lib.block.AbstractEnergyBlock;
import owmii.lib.item.EnergyBlockItem;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.config.EnergyDischargerConfig;

import javax.annotation.Nullable;

public class EnergyDischargerBlock extends AbstractEnergyBlock<Tier, EnergyDischargerConfig> {
    public EnergyDischargerBlock(Properties properties, Tier variant) {
        super(properties, variant);
    }

    @Override
    public EnergyDischargerConfig getConfig() {
        return Configs.ENERGY_DISCHARGER;
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return super.getBlockItem(properties.maxStackSize(1), group);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnergyDischargerTile(this.variant);
    }

    @Override
    protected Facing getFacing() {
        return Facing.HORIZONTAL;
    }
}
