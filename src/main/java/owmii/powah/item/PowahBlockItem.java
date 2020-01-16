package owmii.powah.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import owmii.lib.item.BlockItemBase;
import owmii.lib.util.Text;
import owmii.powah.block.PowahBlock;
import owmii.powah.block.generator.GeneratorBlock;
import owmii.powah.book.content.page.IBookInfo;
import owmii.powah.energy.ItemEnergyProvider;

import javax.annotation.Nullable;
import java.util.Map;

public class PowahBlockItem extends BlockItemBase implements IBookInfo {
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
    @OnlyIn(Dist.CLIENT)
    public Map<String, Object[]> getBookInfo(ItemStack stack, Map<String, Object[]> lines) {
        if (getBlock().getCapacity() > 0)
            lines.put("info.powah.capacity", new Object[]{Text.addCommas(getBlock().getCapacity())});
        if (getBlock() instanceof GeneratorBlock)
            lines.put("info.powah.generates", new Object[]{Text.addCommas(((GeneratorBlock) getBlock()).perTick())});
        int maxIn = getBlock().getMaxReceive();
        int maxOut = getBlock().getMaxExtract();
        lines.put("info.powah.max.io", new Object[]{(maxIn == maxOut ? Text.addCommas(maxOut) : maxIn == 0 || maxOut == 0 ? Text.addCommas(Math.max(maxIn, maxOut)) : (Text.addCommas(maxIn) + "/" + Text.addCommas(maxOut)))});
        return lines;
    }

    @Override
    public PowahBlock getBlock() {
        return this.block;
    }
}
