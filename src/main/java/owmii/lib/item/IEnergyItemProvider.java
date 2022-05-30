package owmii.lib.item;

import net.minecraft.item.ItemStack;

public interface IEnergyItemProvider {
    boolean isChargeable(ItemStack stack);
}
