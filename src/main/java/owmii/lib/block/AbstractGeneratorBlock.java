package owmii.lib.block;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import owmii.lib.client.util.Text;
import owmii.lib.client.wiki.page.panel.InfoBox;
import owmii.lib.config.IEnergyConfig;
import owmii.lib.logistics.Transfer;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.registry.IVariant;
import owmii.lib.util.Util;

import java.util.List;

public abstract class AbstractGeneratorBlock<V extends Enum<V> & IVariant<V>, C extends IEnergyConfig<V>, B extends AbstractGeneratorBlock<V, C, B>> extends AbstractEnergyBlock<V, C, B> {
    public AbstractGeneratorBlock(Properties properties) {
        super(properties);
    }

    public AbstractGeneratorBlock(Properties properties, V variant) {
        super(properties, variant);
    }

    @Override
    public void additionalEnergyInfo(ItemStack stack, Energy.Item energy, List<ITextComponent> tooltip) {
        tooltip.add(new TranslationTextComponent("info.lollipop.generates").mergeStyle(TextFormatting.GRAY).append(Text.COLON).append(new StringTextComponent(Util.numFormat(getConfig().getGeneration(this.variant))).append(new TranslationTextComponent("info.lollipop.fe.pet.tick")).mergeStyle(TextFormatting.DARK_GRAY)));
    }

    @Override
    public Transfer getTransferType() {
        return Transfer.EXTRACT;
    }

    @Override
    public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
        Energy.ifPresent(stack, storage -> {
            if (storage instanceof Energy.Item) {
                Energy.Item energy = (Energy.Item) storage;
                box.set(new TranslationTextComponent("info.lollipop.capacity"), new TranslationTextComponent("info.lollipop.fe", Util.addCommas(energy.getCapacity())));
                box.set(new TranslationTextComponent("info.lollipop.generates"), new TranslationTextComponent("info.lollipop.fe.pet.tick", Util.addCommas(getConfig().getGeneration(this.variant))));
                box.set(new TranslationTextComponent("info.lollipop.max.extract"), new TranslationTextComponent("info.lollipop.fe.pet.tick", Util.addCommas(energy.getMaxExtract())));
            }
        });
        return box;
    }
}
