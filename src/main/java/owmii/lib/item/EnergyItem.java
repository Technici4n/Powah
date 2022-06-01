package owmii.lib.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
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
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        IEnergyConfig config = getConfig();
        return new Energy.Item.Provider(stack, config.getCapacity(getVariant()), config.getTransfer(getVariant()), config.getTransfer(getVariant()));
    }

    @Override
    public boolean isChargeable(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        Energy.ifPresent(stack, storage -> {
            if (storage instanceof Energy.Item) {
                Energy.Item energy = (Energy.Item) storage;
                if (energy.getCapacity() > 0) {
                    tooltip.add(new TranslatableComponent("info.lollipop.stored").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(new TranslatableComponent("info.lollipop.fe.stored", Util.addCommas(energy.getStored()), Util.numFormat(energy.getCapacity())).withStyle(ChatFormatting.DARK_GRAY)));
                    tooltip.add(new TranslatableComponent("info.lollipop.max.io").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(new TranslatableComponent("info.lollipop.fe.pet.tick", Util.numFormat(energy.getMaxExtract())).withStyle(ChatFormatting.DARK_GRAY)));
                    tooltip.add(new TextComponent(""));
                }
            }
        });
    }

    @Override
    public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
        Energy.ifPresent(stack, storage -> {
            if (storage instanceof Energy.Item) {
                Energy.Item energy = (Energy.Item) storage;
                box.set(new TranslatableComponent("info.lollipop.capacity"), new TranslatableComponent("info.lollipop.fe", Util.addCommas(energy.getCapacity())));
                box.set(new TranslatableComponent("info.lollipop.max.io"), new TranslatableComponent("info.lollipop.fe.pet.tick", Util.addCommas(energy.getMaxExtract())));
            }
        });
        return box;
    }
}
