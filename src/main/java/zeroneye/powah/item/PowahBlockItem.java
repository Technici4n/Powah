package zeroneye.powah.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import zeroneye.lib.item.BlockItemBase;
import zeroneye.powah.block.PowahBlock;
import zeroneye.powah.energy.ItemEnergyProvider;

import javax.annotation.Nullable;

public class PowahBlockItem extends BlockItemBase {
    private final PowahBlock block;

    public PowahBlockItem(PowahBlock block, Properties properties, @Nullable ItemGroup group) {
        super(block, properties.rarity(block.isCreative() ? Rarity.RARE : Rarity.COMMON), group);
        this.block = block;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemEnergyProvider(stack, getBlock().getCapacity(), 0, getBlock().getMaxExtract(), getBlock().getMaxReceive(), getBlock().isCreative());
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return getBlock().isCreative() || super.hasEffect(stack);
    }

    @Override
    public PowahBlock getBlock() {
        return this.block;
    }
}
