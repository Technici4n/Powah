package owmii.powah.client.book;

import guideme.compiler.PageCompiler;
import guideme.compiler.TagCompiler;
import guideme.compiler.tags.MdxAttrs;
import guideme.document.flow.LytFlowParent;
import guideme.libs.mdast.mdx.model.MdxJsxTextElement;
import java.util.Set;
import net.minecraft.network.chat.Component;
import owmii.powah.config.IEnergyConfig;
import owmii.powah.lib.block.AbstractGeneratorBlock;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.item.EnergyItem;
import owmii.powah.lib.item.ItemBlock;
import owmii.powah.lib.registry.IVariant;
import owmii.powah.util.Util;

public class PowahTagCompiler implements TagCompiler {
    @Override
    public Set<String> getTagNames() {
        return Set.of("powah:EnergyCapacity", "powah:EnergyMaxIO", "powah:EnergyGeneration");
    }

    @Override
    public void compileFlowContext(PageCompiler compiler, LytFlowParent parent, MdxJsxTextElement el) {
        var item = MdxAttrs.getRequiredItem(compiler, parent, el, "id");
        if (item == null) {
            return;
        }

        switch (el.name()) {
        case "powah:EnergyCapacity" -> {
            long capacity = 0L;
            if (item instanceof EnergyItem<?, ?, ?> energyItem) {
                capacity = energyItem.getEnergyInfo().capacity();
            } else if (item instanceof EnergyBlockItem<?, ?> energyBlockItem) {
                capacity = energyBlockItem.getBlock().getConfig().getCapacity(energyBlockItem.getVariant());
            }
            parent.appendComponent(Component.translatable("info.lollipop.fe", Util.addCommas(capacity)));
        }
        case "powah:EnergyMaxIO" -> {
            long maxIo = 0L;
            if (item instanceof EnergyBlockItem<?, ?> energyBlockItem) {
                maxIo = energyBlockItem.getBlock().getConfig().getTransfer(energyBlockItem.getVariant());
            } else if (item instanceof EnergyItem<?, ?, ?> energyItem) {
                maxIo = getEnergyItemTransfer(energyItem);
            }
            parent.appendComponent(Component.translatable("info.lollipop.fe.pet.tick", Util.addCommas(maxIo)));
        }
        case "powah:EnergyGeneration" -> {
            long generation = 0L;
            if (item instanceof ItemBlock<?> blockItem && blockItem.getBlock() instanceof AbstractGeneratorBlock<?> generatorBlock) {
                generation = generatorBlock.getConfig().getGeneration(generatorBlock.getVariant());
            }
            parent.appendComponent(Component.translatable("info.lollipop.fe.pet.tick", Util.addCommas(generation)));
        }
        default -> throw new IllegalStateException("Unexpected value: " + el.name());
        }
    }

    private static <V extends Enum<V> & IVariant<V>, C extends IEnergyConfig<V>, I extends EnergyItem<V, C, I>> long getEnergyItemTransfer(
            EnergyItem<V, C, I> energyItem) {
        return energyItem.getConfig().getTransfer(energyItem.getVariant());
    }
}
