package zeroneye.powah.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import zeroneye.lib.energy.ItemEnergyStorage;
import zeroneye.lib.item.ItemBase;

import javax.annotation.Nullable;

public class PowahItem extends ItemBase {
    public PowahItem(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new EnergyCapabilityProvider(stack, 0, 0, true, true);
    }

    public static class EnergyCapabilityProvider implements ICapabilityProvider {
        private final ItemStack stack;
        private final int capacity;
        private final int transfer;
        private final boolean canExtract;
        private final boolean canReceive;

        public EnergyCapabilityProvider(ItemStack stack, int capacity, int transfer, boolean canExtract, boolean canReceive) {
            this.stack = stack;
            this.capacity = capacity;
            this.transfer = transfer;
            this.canExtract = canExtract;
            this.canReceive = canReceive;
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction dire) {
            return cap == CapabilityEnergy.ENERGY ? LazyOptional.of(() -> new ItemEnergyStorage(this.stack) {
                @Override
                public int getMaxEnergyStored() {
                    return EnergyCapabilityProvider.this.capacity;
                }

                @Override
                public int getMaxExtract() {
                    return EnergyCapabilityProvider.this.transfer;
                }

                @Override
                public int getMaxReceive() {
                    return EnergyCapabilityProvider.this.transfer;
                }

                @Override
                public boolean canExtract() {
                    return EnergyCapabilityProvider.this.canExtract && super.canExtract();
                }

                @Override
                public boolean canReceive() {
                    return EnergyCapabilityProvider.this.canReceive && super.canReceive();
                }
            }).cast() : LazyOptional.empty();
        }
    }
}
