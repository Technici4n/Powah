package owmii.lib.item;

import net.minecraft.world.item.ItemStack;

public interface IEnergyItemProvider {
    boolean isChargeable(ItemStack stack);
}
