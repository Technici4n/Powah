package owmii.powah.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import owmii.powah.Powah;
import owmii.powah.compat.curios.CurioTagsProvider;
import owmii.powah.world.gen.Features;

public class PowahDataGenerator {
    /**
     * Datapack registry entries (worldgen, loot tables, recipes). NeoForge generates the JSON for these itself.
     */
    public static void gatherRegistryEntries(GatherDataRegistryEntriesEvent event) {
        event.add(Registries.FEATURE, Features::initFeatures)
                .add(Registries.PLACED_FEATURE, Features::initPlacedFeatures)
                .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Features::initBiomeModifiers)
                .lootTable(new LootTableProvider.SubProviderEntry(LootTableGenerator::new, LootContextParamSets.BLOCK))
                .recipe(RecipeProvider::new);
    }

    public static void gatherData(GatherDataEvent.Client event) {
        event.createBlockAndItemTags(TagsProvider.Blocks::new, TagsProvider.Items::new);
        event.createProvider(CurioTagsProvider::new);
        event.createProvider(PowahDataMapProvider::new);

        // Models
        event.createProvider(PowahModelProvider.create(
                Powah.MOD_ID,
                BlockModelProvider::new,
                ItemModelProvider::new)::create);
    }
}
