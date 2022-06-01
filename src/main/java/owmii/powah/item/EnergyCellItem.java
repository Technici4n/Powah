package owmii.powah.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import owmii.lib.item.EnergyBlockItem;
import owmii.lib.logistics.energy.Energy;
import owmii.powah.api.energy.endernetwork.IEnderExtender;
import owmii.powah.block.Tier;
import owmii.powah.block.energycell.EnergyCellBlock;
import owmii.powah.config.EnergyCellConfig;

import javax.annotation.Nullable;

public class EnergyCellItem extends EnergyBlockItem<Tier, EnergyCellConfig, EnergyCellBlock> implements IEnderExtender {
    public EnergyCellItem(EnergyCellBlock block, Properties properties, @Nullable CreativeModeTab group) {
        super(block, properties, group);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if (!getVariant().equals(Tier.CREATIVE)) {
            return super.initCapabilities(stack, nbt);
        }
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
