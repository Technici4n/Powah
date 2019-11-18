package zeroneye.powah.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import zeroneye.lib.item.BlockItemBase;
import zeroneye.lib.util.Energy;
import zeroneye.lib.util.Text;
import zeroneye.powah.block.PowahBlock;
import zeroneye.powah.energy.ItemEnergyProvider;
import zeroneye.powah.energy.ItemPowahStorage;

import javax.annotation.Nullable;
import java.util.List;

public class PowahBlockItem extends BlockItemBase {
    private final PowahBlock block;

    public PowahBlockItem(PowahBlock block, Properties properties, @Nullable ItemGroup group) {
        super(block, properties.maxStackSize(1).rarity(block.isCreative() ? Rarity.RARE : Rarity.COMMON), group);
        this.block = block;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
        Energy.getForgeEnergy(stack).ifPresent(storage -> {
            if (storage instanceof ItemPowahStorage && !getBlock().isCreative()) {
                list.add(Text.format(storage.getEnergyStored() + "/" + storage.getMaxEnergyStored()));
                list.add(new StringTextComponent(""));
            }
        });
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemEnergyProvider(stack, getBlock().getCapacity(), getBlock().getTransfer(), 0, getBlock().isCreative());
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
