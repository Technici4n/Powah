package owmii.powah.data;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import owmii.powah.block.Blcks;
import owmii.powah.item.Itms;

// TODO: find a clean way to share with fabric, for now we just copy/paste generated files over to fabric :/
public class LootTableGenerator extends BlockLootSubProvider {
    public LootTableGenerator() {
        super(Set.of(), FeatureFlagSet.of());
    }

    private Function<Block, LootTable.Builder> uraniniteOre(int dropCount) {
        return block -> {
            return createSilkTouchDispatchTable(block,
                    applyExplosionDecay(block,
                            LootItem.lootTableItem(Itms.URANINITE_RAW.get())
                                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(dropCount)))
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
        };
    }

    @Override
    protected void generate() {
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
        Map<Block, Function<Block, LootTable.Builder>> builders = new IdentityHashMap<>();

        builders.put(Blcks.DEEPSLATE_URANINITE_ORE_POOR.get(), uraniniteOre(1));
        builders.put(Blcks.DEEPSLATE_URANINITE_ORE.get(), uraniniteOre(2));
        builders.put(Blcks.DEEPSLATE_URANINITE_ORE_DENSE.get(), uraniniteOre(4));
        builders.put(Blcks.URANINITE_ORE_POOR.get(), uraniniteOre(1));
        builders.put(Blcks.URANINITE_ORE.get(), uraniniteOre(2));
        builders.put(Blcks.URANINITE_ORE_DENSE.get(), uraniniteOre(4));

        for (var entry : builders.entrySet()) {
            biConsumer.accept(entry.getKey().getLootTable(), entry.getValue().apply(entry.getKey()));
        }
    }
}
