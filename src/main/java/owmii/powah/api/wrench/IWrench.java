package owmii.powah.api.wrench;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public interface IWrench {
    default WrenchMode getWrenchMode(ItemStack stack) {
        return WrenchMode.values()[getWrenchNBT(stack).getInt("WrenchMode")];
    }

    default boolean changeWrenchMode(ItemStack stack, boolean next) {
        if (stack.getItem() instanceof IWrench)
            if (next) {
                nextWrenchMode(stack);
                return true;
            } else {
                prevWrenchMode(stack);
                return true;
            }
        return false;
    }

    default void nextWrenchMode(ItemStack stack) {
        CompoundNBT nbt = getWrenchNBT(stack);
        int i = nbt.getInt("WrenchMode") + 1;
        int j = WrenchMode.values().length - 1;
        nbt.putInt("WrenchMode", i > j ? 0 : i);
    }

    default void prevWrenchMode(ItemStack stack) {
        CompoundNBT nbt = getWrenchNBT(stack);
        int i = nbt.getInt("WrenchMode") - 1;
        int j = WrenchMode.values().length - 1;
        nbt.putInt("WrenchMode", i < j ? j : i);
    }

    default CompoundNBT getWrenchNBT(ItemStack stack) {
        return stack.getOrCreateChildTag("PowahWrenchNBT");
    }
}
