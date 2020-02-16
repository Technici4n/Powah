package owmii.powah.api.energy.endernetwork;

import net.minecraft.item.ItemStack;

public interface IEnderExtender {
    long getExtendedCapacity(ItemStack stack);

    long getExtendedEnergy(ItemStack stack);
}
