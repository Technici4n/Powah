package owmii.powah.lib.client.wiki;

import dev.architectury.event.events.client.ClientRecipeUpdateEvent;
import dev.architectury.platform.Platform;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import owmii.powah.Powah;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class Wiki {
    public static final Marker MARKER = new MarkerManager.Log4jMarker("Wiki");
    public static final Map<String, Wiki> WIKIS = new HashMap<>();
    private final List<Entry> categories = new ArrayList<>();
    private final Map<ItemLike, List<Recipe<?>>> crafting = new HashMap<>();
    private final Map<ItemLike, List<Recipe<?>>> smelting = new HashMap<>();
    private final String modId;

    public Wiki() {
        this.modId = Powah.MOD_ID;
        WIKIS.put(this.modId, this);
    }

    public Wiki e(String name, Consumer<Entry> consumer) {
        return e(name, null, consumer);
    }

    public Wiki e(String name, @Nullable Icon icon, Consumer<Entry> consumer) {
        Entry entry = new Entry(name, icon, this);
        entry.setMain(true);
        entry.setParent(entry);
        consumer.accept(entry);
        register(entry);
        return this;
    }

    public Entry register(Entry entry) {
        this.categories.add(entry);
        return entry;
    }

    public List<Entry> getCategories() {
        return this.categories;
    }

    public Map<ItemLike, List<Recipe<?>>> getCrafting() {
        return this.crafting;
    }

    public Map<ItemLike, List<Recipe<?>>> getSmelting() {
        return this.smelting;
    }

    public String getModId() {
        return this.modId;
    }

    public String getModName() {
        return Platform.getMod(this.modId).getName();
    }

    public String getModVersion() {
        return Platform.getMod(this.modId).getVersion();
    }

    static {
        ClientRecipeUpdateEvent.EVENT.register(recipeManager -> {
            StopWatch watch = StopWatch.createStarted();
            Powah.LOGGER.info(MARKER, "Started wikis recipes collecting...");
            WIKIS.forEach((s, wiki) -> {
                Registry.ITEM.stream().filter(i -> Registry.ITEM.getKey(i).getNamespace().equals(Powah.MOD_ID)).forEach(item -> {
                    List<Recipe<?>> crafting = new ArrayList<>();
                    recipeManager.getAllRecipesFor(RecipeType.CRAFTING).forEach(recipe -> {
                        if (recipe.getResultItem().sameItem(new ItemStack(item))) {
                            crafting.add(recipe);
                        }
                    });
                    wiki.crafting.put(item, crafting);
                    List<Recipe<?>> smelting = new ArrayList<>();
                    recipeManager.getAllRecipesFor(RecipeType.CRAFTING).forEach(recipe -> {
                        if (recipe.getResultItem().sameItem(new ItemStack(item))) {
                            smelting.add(recipe);
                        }
                    });
                    wiki.smelting.put(item, smelting);
                });
            });
            watch.stop();
            Powah.LOGGER.info(MARKER, "Wiki recipes collecting completed in : {} ms", watch.getTime());
        });
    }
}
