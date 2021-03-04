package owmii.powah.compat.curios;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import owmii.powah.item.Itms;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;

public class CurioTagsProvider extends ItemTagsProvider {
    public CurioTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagsProvider, modId, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(CurioTags.CURIO).add(Itms.BATTERY.getAll().toArray(new Item[0]));
    }

    public static class CurioTags {
        public static final Tags.IOptionalNamedTag<Item> CURIO = tag("curio");

        /**
         * We have to use the curios namespace.
         *
         * @see <a href="https://github.com/TheIllusiveC4/Curios/wiki/How-to-Use:-Developers#marking-items-with-curio-types">Marking Items with Curio Types</a>
         */
        private static Tags.IOptionalNamedTag<Item> tag(String name) {
            return ItemTags.createOptional(new ResourceLocation(CuriosApi.MODID, name));
        }
    }
}
