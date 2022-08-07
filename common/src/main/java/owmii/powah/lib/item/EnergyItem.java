package owmii.powah.lib.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import owmii.powah.lib.client.util.Text;
import owmii.powah.lib.client.wiki.page.panel.InfoBox;
import owmii.powah.config.IEnergyConfig;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.registry.IVariant;
import owmii.powah.lib.util.Util;

import javax.annotation.Nullable;
import java.util.List;

public abstract class EnergyItem<V extends Enum<V> & IVariant<V>, C extends IEnergyConfig<V>, I extends EnergyItem<V, C, I>> extends VarItem<V, I> implements InfoBox.IInfoBoxHolder, IEnergyItemProvider, IEnergyContainingItem {
    public EnergyItem(Properties properties, V variant) {
        super(properties, variant);
    }

    public EnergyItem(Properties properties) {
        super(properties);
    }

    public abstract IEnergyConfig<V> getConfig();

    @Override
    public Info getEnergyInfo() {
        var config = getConfig();
        return new Info(config.getCapacity(getVariant()), config.getTransfer(getVariant()), config.getTransfer(getVariant()));
    }

    @Override
    public boolean isChargeable(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        Energy.ifPresent(stack, energy -> {
            if (energy.getCapacity() > 0) {
                tooltip.add(Component.translatable("info.lollipop.stored").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(Component.translatable("info.lollipop.fe.stored", Util.addCommas(energy.getStored()), Util.numFormat(energy.getCapacity())).withStyle(ChatFormatting.DARK_GRAY)));
                tooltip.add(Component.translatable("info.lollipop.max.io").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(Component.translatable("info.lollipop.fe.pet.tick", Util.numFormat(energy.getMaxExtract())).withStyle(ChatFormatting.DARK_GRAY)));
                tooltip.add(Component.empty());
            }
        });
    }

    @Override
    public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
        Energy.ifPresent(stack, energy -> {
            box.set(Component.translatable("info.lollipop.capacity"), Component.translatable("info.lollipop.fe", Util.addCommas(energy.getCapacity())));
            box.set(Component.translatable("info.lollipop.max.io"), Component.translatable("info.lollipop.fe.pet.tick", Util.addCommas(energy.getMaxExtract())));
        });
        return box;
    }
}
