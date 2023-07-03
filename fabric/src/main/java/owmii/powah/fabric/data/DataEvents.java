package owmii.powah.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import owmii.powah.world.gen.Features;

public class DataEvents implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        var pack = generator.createPack();
        var blockTagsProvider = pack.addProvider(TagsProvider.Blocks::new);
        pack.addProvider((output, registriesFuture) -> new TagsProvider.Items(output, registriesFuture, blockTagsProvider));
        pack.addProvider((output, registriesFuture) -> new FabricDynamicRegistryProvider(output, registriesFuture) {
            @Override
            protected void configure(HolderLookup.Provider registries, Entries entries) {
                entries.addAll(registries.lookupOrThrow(Registries.CONFIGURED_FEATURE));
                entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE));
            }

            @Override
            public String getName() {
                return "Worldgen";
            }
        });
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.CONFIGURED_FEATURE, Features::initConfiguredFeatures);
        registryBuilder.add(Registries.PLACED_FEATURE, Features::initPlacedFeatures);
    }
}
