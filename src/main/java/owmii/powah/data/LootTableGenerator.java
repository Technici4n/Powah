package owmii.powah.data;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.item.Itms;

public class LootTableGenerator extends BlockLootSubProvider {
    public LootTableGenerator(LootTableSubProvider.Context output) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), output);
    }

    private Function<Block, LootTable.Builder> uraniniteOre(int dropCount) {
        return block -> {
            var fortune = enchantments.getOrThrow(Enchantments.FORTUNE);
            return createSilkTouchDispatchTable(block,
                    applyExplosionDecay(block,
                            LootItem.lootTableItem(Itms.URANINITE_RAW.get())
                                    .apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(dropCount)))
                                    .apply(ApplyBonusCount.addOreBonusCount(fortune))));
        };
    }

    @Override
    protected void generate() {
    }

    @Override
    public void run() {
        Map<Block, Function<Block, LootTable.Builder>> builders = new IdentityHashMap<>();

        builders.put(Blcks.DEEPSLATE_URANINITE_ORE_POOR.get(), uraniniteOre(1));
        builders.put(Blcks.DEEPSLATE_URANINITE_ORE.get(), uraniniteOre(2));
        builders.put(Blcks.DEEPSLATE_URANINITE_ORE_DENSE.get(), uraniniteOre(4));
        builders.put(Blcks.URANINITE_ORE_POOR.get(), uraniniteOre(1));
        builders.put(Blcks.URANINITE_ORE.get(), uraniniteOre(2));
        builders.put(Blcks.URANINITE_ORE_DENSE.get(), uraniniteOre(4));

        // Every other block drops itself if it survives an explosion. Blocks registered with noLootTable() are skipped.
        // (Breaking by a player is handled by PowahBaseBlock#playerDestroy, which keeps the stored energy/items.)
        for (var block : BuiltInRegistries.BLOCK) {
            if (!BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(Powah.MOD_ID)) {
                continue;
            }
            block.getLootTable().ifPresent(key -> output.accept(key,
                    builders.getOrDefault(block, this::createSingleItemTable).apply(block)));
        }
    }
}
