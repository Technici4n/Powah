package owmii.powah.handler.event;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import owmii.powah.Powah;
import owmii.powah.compat.curios.CurioTagsProvider;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataEvents {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        TagsProvider.Blocks bp = new TagsProvider.Blocks(generator, Powah.MOD_ID, event.getExistingFileHelper());
        generator.addProvider(bp);
        generator.addProvider(new TagsProvider.Items(generator, bp, Powah.MOD_ID, event.getExistingFileHelper()));
        generator.addProvider(new CurioTagsProvider(generator, bp, Powah.MOD_ID, event.getExistingFileHelper()));
    }
}
