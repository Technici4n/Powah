package owmii.powah.forge.data;

import com.mojang.datafixers.util.Pair;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import owmii.powah.block.Blcks;
import owmii.powah.item.Itms;

// TODO: find a clean way to share with fabric, for now we just copy/paste generated files over to fabric :/
public class LootTableGenerator extends LootTableProvider {
    public LootTableGenerator(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> tables, ValidationContext ctx) {
        tables.forEach((name, table) -> LootTables.validate(ctx, name, table));
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return List.of(
                Pair.of(PowahBlockLoot::new, LootContextParamSets.BLOCK));
    }

    private static class PowahBlockLoot extends BlockLoot {
        private static Function<Block, LootTable.Builder> uraniniteOre(int dropCount) {
            return block -> {
                return createSilkTouchDispatchTable(block,
                        applyExplosionDecay(block,
                                LootItem.lootTableItem(Itms.URANINITE_RAW.get())
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(dropCount)))
                                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
            };
        }

        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
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
}
