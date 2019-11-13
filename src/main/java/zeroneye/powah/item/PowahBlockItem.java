package zeroneye.powah.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import zeroneye.lib.item.BlockItemBase;
import zeroneye.powah.block.GeneratorBlock;
import zeroneye.powah.block.PowahBlock;

import javax.annotation.Nullable;

public class PowahBlockItem extends BlockItemBase {
    private final PowahBlock block;

    public PowahBlockItem(PowahBlock block, Properties properties, @Nullable ItemGroup group) {
        super(block, properties, group);
        this.block = block;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new PowahItem.EnergyCapabilityProvider(stack, getBlock().getCapacity(), getBlock().getTransfer(), true, !(getBlock() instanceof GeneratorBlock));
    }

    @Override
    public PowahBlock getBlock() {
        return this.block;
    }
}
