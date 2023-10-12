package owmii.powah.lib.block;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import owmii.powah.block.Tier;
import owmii.powah.config.v2.types.GeneratorConfig;
import owmii.powah.lib.client.util.Text;
import owmii.powah.lib.client.wiki.page.panel.InfoBox;
import owmii.powah.lib.logistics.Transfer;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.util.Util;

public abstract class AbstractGeneratorBlock<B extends AbstractGeneratorBlock<B>> extends AbstractEnergyBlock<GeneratorConfig, B> {
    public AbstractGeneratorBlock(Properties properties) {
        super(properties);
    }

    public AbstractGeneratorBlock(Properties properties, Tier variant) {
        super(properties, variant);
    }

    @Override
    public void additionalEnergyInfo(ItemStack stack, Energy.Item energy, List<Component> tooltip) {
        tooltip.add(Component.translatable("info.lollipop.generates").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                .append(Component.translatable("info.lollipop.fe.pet.tick", Util.numFormat(getConfig().getGeneration(this.variant)))
                        .withStyle(ChatFormatting.DARK_GRAY))
        );
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
                box.set(Component.translatable("info.lollipop.capacity"),
                        Component.translatable("info.lollipop.fe", Util.addCommas(energy.getCapacity())));
                box.set(Component.translatable("info.lollipop.generates"),
                        Component.translatable("info.lollipop.fe.pet.tick", Util.addCommas(getConfig().getGeneration(this.variant))));
                box.set(Component.translatable("info.lollipop.max.extract"),
                        Component.translatable("info.lollipop.fe.pet.tick", Util.addCommas(energy.getMaxExtract())));
            }
        });
        return box;
    }
}
