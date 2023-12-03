package owmii.powah.forge.compat.curios;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.item.Itms;
import top.theillusivec4.curios.api.CuriosApi;

public class CurioTagsProvider extends ItemTagsProvider {

    public CurioTagsProvider(PackOutput output,
            CompletableFuture<HolderLookup.Provider> providerFuture,
            CompletableFuture<TagLookup<Block>> blockLookup,
            @Nullable ExistingFileHelper existingFileHelper) {
        super(output, providerFuture, blockLookup, Powah.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "CurioTags";
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
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
