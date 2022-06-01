package owmii.powah.block.energycell;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class EnergyCellBlock extends AbstractEnergyBlock<Tier, EnergyCellConfig, EnergyCellBlock> implements SimpleWaterloggedBlock {
    public EnergyCellBlock(Properties properties, Tier tier) {
        super(properties, tier);
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable CreativeModeTab group) {
        return new EnergyCellItem(this, properties.stacksTo(1), group);
    }

    @Override
    public EnergyCellConfig getConfig() {
        return Configs.ENERGY_CELL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnergyCellTile(pos, state, this.variant);
    }

    @Nullable
    @Override
    public AbstractContainer getContainer(int id, Inventory inventory, AbstractTileEntity te, BlockHitResult result) {
        if (te instanceof EnergyCellTile) {
            return new EnergyCellContainer(id, inventory, (EnergyCellTile) te);
        }
        return null;
    }
}
