package owmii.lib.block;

import owmii.lib.client.util.Text;
import owmii.lib.client.wiki.page.panel.InfoBox;
import owmii.lib.config.IEnergyConfig;
import owmii.lib.logistics.Transfer;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.registry.IVariant;
import owmii.lib.util.Util;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractGeneratorBlock<V extends Enum<V> & IVariant<V>, C extends IEnergyConfig<V>, B extends AbstractGeneratorBlock<V, C, B>> extends AbstractEnergyBlock<V, C, B> {
    public AbstractGeneratorBlock(Properties properties) {
        super(properties);
    }

    public AbstractGeneratorBlock(Properties properties, V variant) {
        super(properties, variant);
    }

    @Override
    public void additionalEnergyInfo(ItemStack stack, Energy.Item energy, List<Component> tooltip) {
        tooltip.add(new TranslatableComponent("info.lollipop.generates").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(new TextComponent(Util.numFormat(getConfig().getGeneration(this.variant))).append(new TranslatableComponent("info.lollipop.fe.pet.tick")).withStyle(ChatFormatting.DARK_GRAY)));
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
                box.set(new TranslatableComponent("info.lollipop.capacity"), new TranslatableComponent("info.lollipop.fe", Util.addCommas(energy.getCapacity())));
                box.set(new TranslatableComponent("info.lollipop.generates"), new TranslatableComponent("info.lollipop.fe.pet.tick", Util.addCommas(getConfig().getGeneration(this.variant))));
                box.set(new TranslatableComponent("info.lollipop.max.extract"), new TranslatableComponent("info.lollipop.fe.pet.tick", Util.addCommas(energy.getMaxExtract())));
            }
        });
        return box;
    }
}
