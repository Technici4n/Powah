package owmii.powah.forge.compat.curios;

import javax.annotation.Nullable;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import owmii.powah.item.Itms;
import top.theillusivec4.curios.api.CuriosApi;

public class CurioTagsProvider extends ItemTagsProvider {
    public CurioTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, String modId,
            @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagsProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(CurioTags.CURIO).add(Itms.BATTERY.getAll().toArray(new Item[0]));
    }

    public static class CurioTags {
        public static final TagKey<Item> CURIO = tag("curio");

        /**
         * We have to use the curios namespace.
         *
         * @see <a href="https://github.com/TheIllusiveC4/Curios/wiki/How-to-Use:-Developers#marking-items-with-curio-types">Marking Items with Curio
         *      Types</a>
         */
        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(CuriosApi.MODID, name));
        }
    }
}
