package owmii.powah.client.book;

import guideme.compiler.PageCompiler;
import guideme.compiler.TagCompiler;
import guideme.compiler.tags.MdxAttrs;
import guideme.document.flow.LytFlowParent;
import guideme.libs.mdast.mdx.model.MdxJsxTextElement;
import net.minecraft.network.chat.Component;
import owmii.powah.lib.block.AbstractGeneratorBlock;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.item.EnergyItem;
import owmii.powah.lib.item.ItemBlock;
import owmii.powah.util.Util;

import java.util.Set;

public class PowahTagCompiler implements TagCompiler {
    @Override
    public Set<String> getTagNames() {
        return Set.of("powah:EnergyCapacity", "powah:EnergyMaxIO", "powah:EnergyGeneration");
    }

    @Override
    public void compileFlowContext(PageCompiler compiler, LytFlowParent parent, MdxJsxTextElement el) {
        switch (el.name()) {
            case "powah:EnergyCapacity" -> {
                var item = MdxAttrs.getRequiredItem(compiler, parent, el, "id");
                long capacity = 0L;
                if (item instanceof EnergyItem<?,?,?> energyItem) {
                    capacity = energyItem.getEnergyInfo().capacity();
                } else if (item instanceof EnergyBlockItem<?,?> energyBlockItem) {
                    capacity = energyBlockItem.getBlock().getConfig().getCapacity(energyBlockItem.getVariant());
                }
                parent.appendComponent(Component.translatable("info.lollipop.fe", Util.addCommas(capacity)));
            }
            case "powah:EnergyMaxIO" -> {
                var item = MdxAttrs.getRequiredItem(compiler, parent, el, "id");
                long maxIo = 0L;
                if (item instanceof EnergyBlockItem<?,?> energyBlockItem) {
                    maxIo = energyBlockItem.getBlock().getConfig().getTransfer(energyBlockItem.getVariant());
                }
                parent.appendComponent(Component.translatable("info.lollipop.fe.pet.tick", Util.addCommas(maxIo)));
            }
            case "powah:EnergyGeneration" -> {
                var item = MdxAttrs.getRequiredItem(compiler, parent, el, "id");
                long generation = 0L;
                if (item instanceof ItemBlock<?> blockItem && blockItem.getBlock() instanceof AbstractGeneratorBlock<?> generatorBlock) {
                    generation = generatorBlock.getConfig().getGeneration(generatorBlock.getVariant());
                }
                parent.appendComponent(Component.translatable("info.lollipop.fe.pet.tick", Util.addCommas(generation)));
            }
            default -> throw new IllegalStateException("Unexpected value: " + el.name());
        }
    }
}
