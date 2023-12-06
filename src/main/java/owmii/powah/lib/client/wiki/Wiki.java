package owmii.powah.lib.client.wiki;

import java.util.*;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.lib.util.Util;

public class Wiki {
    public static final Marker MARKER = new MarkerManager.Log4jMarker("Wiki");
    public static final Map<String, Wiki> WIKIS = new HashMap<>();
    private final List<Entry> categories = new ArrayList<>();
    private final Map<ItemLike, List<RecipeHolder<CraftingRecipe>>> crafting = new HashMap<>();
    private final Map<ItemLike, List<RecipeHolder<SmeltingRecipe>>> smelting = new HashMap<>();
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

    public Map<ItemLike, List<RecipeHolder<CraftingRecipe>>> getCrafting() {
        return this.crafting;
    }

    public Map<ItemLike, List<RecipeHolder<SmeltingRecipe>>> getSmelting() {
        return this.smelting;
    }

    public String getModId() {
        return this.modId;
    }

    public String getModName() {
        return Util.getModName(this.modId);
    }

    public String getModVersion() {
        return Util.getModVersion(this.modId);
    }

    public static void updateRecipes(RecipesUpdatedEvent event) {
        var recipeManager = event.getRecipeManager();
        var registryAccess = Minecraft.getInstance().level.registryAccess();

        StopWatch watch = StopWatch.createStarted();
        Powah.LOGGER.info(MARKER, "Started wikis recipes collecting...");
        WIKIS.forEach((s, wiki) -> {
            BuiltInRegistries.ITEM.stream().filter(i -> BuiltInRegistries.ITEM.getKey(i).getNamespace().equals(Powah.MOD_ID)).forEach(item -> {
                List<RecipeHolder<CraftingRecipe>> crafting = new ArrayList<>();
                recipeManager.getAllRecipesFor(RecipeType.CRAFTING).forEach(holder -> {
                    if (holder.value().getResultItem(registryAccess).is(item)) {
                        crafting.add(holder);
                    }
                });
                wiki.crafting.put(item, crafting);
                List<RecipeHolder<SmeltingRecipe>> smelting = new ArrayList<>();
                recipeManager.getAllRecipesFor(RecipeType.SMELTING).forEach(holder -> {
                    if (holder.value().getResultItem(registryAccess).is(item)) {
                        smelting.add(holder);
                    }
                });
                wiki.smelting.put(item, smelting);
            });
        });
        watch.stop();
        Powah.LOGGER.info(MARKER, "Wiki recipes collecting completed in : {} ms", watch.getTime());
    }
}
