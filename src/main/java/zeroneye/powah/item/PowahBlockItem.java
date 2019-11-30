package zeroneye.powah.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import zeroneye.lib.item.BlockItemBase;
import zeroneye.lib.util.Energy;
import zeroneye.powah.block.PowahBlock;
import zeroneye.powah.block.generator.GeneratorBlock;
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
                if (storage.getMaxEnergyStored() > 0) {
                    list.add(new TranslationTextComponent("info.powah.stored", "" + TextFormatting.DARK_GRAY + storage.getEnergyStored(), storage.getMaxEnergyStored()).applyTextStyle(TextFormatting.GRAY));
                }
                if (getBlock() instanceof GeneratorBlock) {
                    list.add(new TranslationTextComponent("info.powah.generates", "" + TextFormatting.DARK_GRAY + ((GeneratorBlock) getBlock()).perTick()).applyTextStyle(TextFormatting.GRAY));
                }
                int maxIn = getBlock().getMaxReceive();
                int maxOut = getBlock().getMaxExtract();
                list.add(new TranslationTextComponent("info.powah.max.io", "" + TextFormatting.DARK_GRAY + (maxIn == maxOut ? maxOut : maxIn == 0 || maxOut == 0 ? Math.max(maxIn, maxOut) : (maxIn + "/" + maxOut))).applyTextStyle(TextFormatting.GRAY));
                list.add(new StringTextComponent(""));
            }
        });
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
