package zeroneye.powah.energy;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;

public class ItemEnergyProvider implements ICapabilityProvider {
    private final ItemStack stack;
    private int capacity;
    private int transfer;
    private int stored;
    private boolean isCreative;

    public ItemEnergyProvider(ItemStack stack, int capacity, int transfer, int stored, boolean isCreative) {
        this.stack = stack;
        this.capacity = capacity;
        this.transfer = transfer;
        this.stored = stored;
        this.isCreative = isCreative;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction dire) {
        return cap == CapabilityEnergy.ENERGY ? LazyOptional.of(() -> {
            return new ItemPowahStorage(this.stack, this.capacity, this.transfer, this.transfer, this.stored, this.isCreative);
        }).cast() : LazyOptional.empty();
    }
}
