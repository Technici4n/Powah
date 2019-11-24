package zeroneye.powah.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import zeroneye.lib.item.ItemBase;
import zeroneye.powah.energy.ItemEnergyProvider;

import javax.annotation.Nullable;

public class PowahItem extends ItemBase {
    public PowahItem(Properties properties) {
        super(properties.maxStackSize(1));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemEnergyProvider(stack, 20, 0, 20, 20, false);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return super.hasEffect(stack);
    }

}
