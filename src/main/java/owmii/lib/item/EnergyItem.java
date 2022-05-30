package owmii.lib.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import owmii.lib.client.util.Text;
import owmii.lib.client.wiki.page.panel.InfoBox;
import owmii.lib.config.IEnergyConfig;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.registry.IVariant;
import owmii.lib.util.Util;

import javax.annotation.Nullable;
import java.util.List;

public abstract class EnergyItem<V extends Enum<V> & IVariant<V>, C extends IEnergyConfig<V>, I extends EnergyItem<V, C, I>> extends VarItem<V, I> implements InfoBox.IInfoBoxHolder, IEnergyItemProvider {
    public EnergyItem(Properties properties, V variant) {
        super(properties, variant);
    }

    public EnergyItem(Properties properties) {
        super(properties);
    }

    public abstract IEnergyConfig<V> getConfig();

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        IEnergyConfig config = getConfig();
        return new Energy.Item.Provider(stack, config.getCapacity(getVariant()), config.getTransfer(getVariant()), config.getTransfer(getVariant()));
    }

    @Override
    public boolean isChargeable(ItemStack stack) {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Energy.ifPresent(stack, storage -> {
            if (storage instanceof Energy.Item) {
                Energy.Item energy = (Energy.Item) storage;
                if (energy.getCapacity() > 0) {
                    tooltip.add(new TranslationTextComponent("info.lollipop.stored").mergeStyle(TextFormatting.GRAY).append(Text.COLON).append(new TranslationTextComponent("info.lollipop.fe.stored", Util.addCommas(energy.getStored()), Util.numFormat(energy.getCapacity())).mergeStyle(TextFormatting.DARK_GRAY)));
                    tooltip.add(new TranslationTextComponent("info.lollipop.max.io").mergeStyle(TextFormatting.GRAY).append(Text.COLON).append(new TranslationTextComponent("info.lollipop.fe.pet.tick", Util.numFormat(energy.getMaxExtract())).mergeStyle(TextFormatting.DARK_GRAY)));
                    tooltip.add(new StringTextComponent(""));
                }
            }
        });
    }

    @Override
    public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
        Energy.ifPresent(stack, storage -> {
            if (storage instanceof Energy.Item) {
                Energy.Item energy = (Energy.Item) storage;
                box.set(new TranslationTextComponent("info.lollipop.capacity"), new TranslationTextComponent("info.lollipop.fe", Util.addCommas(energy.getCapacity())));
                box.set(new TranslationTextComponent("info.lollipop.max.io"), new TranslationTextComponent("info.lollipop.fe.pet.tick", Util.addCommas(energy.getMaxExtract())));
            }
        });
        return box;
    }
}
