package owmii.powah.api.wrench;

import net.minecraft.item.ItemStack;

public interface IWrench {
    WrenchMode getWrenchMode(ItemStack stack);
}
