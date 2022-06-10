package owmii.powah.api.wrench;

import net.minecraft.world.item.ItemStack;

public interface IWrench {
    WrenchMode getWrenchMode(ItemStack stack);
}
