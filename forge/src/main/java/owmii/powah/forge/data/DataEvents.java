package owmii.powah.forge.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import owmii.powah.Powah;
import owmii.powah.forge.compat.curios.CurioTagsProvider;

public class DataEvents {
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        TagsProvider.Blocks bp = new TagsProvider.Blocks(generator, Powah.MOD_ID, event.getExistingFileHelper());
        generator.addProvider(true, bp);
        generator.addProvider(true, new TagsProvider.Items(generator, bp, Powah.MOD_ID, event.getExistingFileHelper()));
        generator.addProvider(true, new CurioTagsProvider(generator, bp, Powah.MOD_ID, event.getExistingFileHelper()));
        generator.addProvider(true, new LootTableGenerator(generator));
    }
}
