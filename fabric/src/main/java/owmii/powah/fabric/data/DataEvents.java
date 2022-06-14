package owmii.powah.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataEvents implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        TagsProvider.Blocks bp = new TagsProvider.Blocks(generator);
        generator.addProvider(bp);
        generator.addProvider(new TagsProvider.Items(generator, bp));
    }
}
