package owmii.powah.forge.data;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import owmii.powah.Powah;
import owmii.powah.forge.compat.curios.CurioTagsProvider;
import owmii.powah.world.gen.Features;

public class DataEvents {
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var existingFileHelper = event.getExistingFileHelper();
        var pack = generator.getVanillaPack(true);

        var registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
        var registries = createPowahProvider(registryAccess);

        var blockTagsProvider = pack
                .addProvider(packOutput -> new TagsProvider.Blocks(packOutput, registries, existingFileHelper));
        pack.addProvider(
                packOutput -> new TagsProvider.Items(packOutput, registries, blockTagsProvider.contentsGetter(),
                        existingFileHelper));

        pack.addProvider(
                packOutput -> new CurioTagsProvider(packOutput, registries, blockTagsProvider.contentsGetter(), event.getExistingFileHelper()));
        pack.addProvider(DataEvents::createLoot);
        pack.addProvider(output -> new DatapackBuiltinEntriesProvider(output, registries, Set.of(Powah.MOD_ID)));
    }

    public static LootTableProvider createLoot(PackOutput output) {
        return new LootTableProvider(
                output,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(LootTableGenerator::new, LootContextParamSets.BLOCK)));
    }

    private static <T extends DataProvider> DataProvider.Factory<T> bindRegistries(
            BiFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, T> factory,
            CompletableFuture<HolderLookup.Provider> factories) {
        return packOutput -> factory.apply(packOutput, factories);
    }

    /**
     * See {@link VanillaRegistries#createLookup()}
     */
    private static CompletableFuture<HolderLookup.Provider> createPowahProvider(RegistryAccess registryAccess) {

        var vanillaLookup = CompletableFuture.supplyAsync(VanillaRegistries::createLookup, Util.backgroundExecutor());

        return vanillaLookup.thenApply(provider -> {
            var builder = new RegistrySetBuilder()
                    .add(Registries.CONFIGURED_FEATURE, Features::initConfiguredFeatures)
                    .add(Registries.PLACED_FEATURE, Features::initPlacedFeatures);

            return builder.buildPatch(registryAccess, provider);
        });
    }
}
