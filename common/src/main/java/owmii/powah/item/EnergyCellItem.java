package owmii.powah.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import owmii.powah.config.v2.types.EnergyConfig;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.api.energy.endernetwork.IEnderExtender;
import owmii.powah.block.Tier;
import owmii.powah.block.energycell.EnergyCellBlock;

import javax.annotation.Nullable;

public class EnergyCellItem extends EnergyBlockItem<EnergyConfig, EnergyCellBlock> implements IEnderExtender {
    public EnergyCellItem(EnergyCellBlock block, Properties properties, @Nullable CreativeModeTab group) {
        super(block, properties, group);
    }

    @Override
    public Info getEnergyInfo() {
        return null;
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        if (getVariant().equals(Tier.CREATIVE)) {
            return Rarity.EPIC;
        }
        return super.getRarity(stack);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return getVariant().equals(Tier.CREATIVE) || super.isFoil(stack);
    }

    @Override
    public long getExtendedCapacity(ItemStack stack) {
        if (getVariant().equals(Tier.CREATIVE)) {
            return 0;
        }
        return getConfig().getCapacity(getVariant());
    }

    @Override
    public long getExtendedEnergy(ItemStack stack) {
        if (getVariant().equals(Tier.CREATIVE)) {
            return 0;
        }
        return Energy.getStored(stack);
    }
}
