package owmii.lib.client.wiki;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.MavenVersionStringHelper;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import owmii.lib.Lollipop;
import owmii.lib.registry.Registry;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class Wiki {
    public static final Marker MARKER = new MarkerManager.Log4jMarker("Wiki");
    public static final Map<String, Wiki> WIKIS = new HashMap<>();
    private final List<Entry> categories = new ArrayList<>();
    private final Map<IItemProvider, List<IRecipe<?>>> crafting = new HashMap<>();
    private final Map<IItemProvider, List<IRecipe<?>>> smelting = new HashMap<>();
    public final Registry<Item> items;
    private final String modId;

    @Nullable
    private final IModInfo modInfo;

    public Wiki(Registry<Item> items) {
        this.items = items;
        this.modId = items.getId();
        Optional<? extends ModContainer> isd = ModList.get().getModContainerById(this.modId);
        this.modInfo = isd.map(ModContainer::getModInfo).orElse(null);
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

    public Map<IItemProvider, List<IRecipe<?>>> getCrafting() {
        return this.crafting;
    }

    public Map<IItemProvider, List<IRecipe<?>>> getSmelting() {
        return this.smelting;
    }

    public String getModId() {
        return this.modId;
    }

    public String getModName() {
        if (this.modInfo != null) {
            return this.modInfo.getDisplayName();
        }
        return "null";
    }

    public String getModVersion() {
        if (this.modInfo != null) {
            return MavenVersionStringHelper.artifactVersionToString(this.modInfo.getVersion());
        }
        return "null";
    }

    @Nullable
    private IModInfo getModInfo() {
        return this.modInfo;
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void collect(RecipesUpdatedEvent event) {
        StopWatch watch = StopWatch.createStarted();
        Lollipop.LOGGER.info(MARKER, "Started wikis recipes collecting...");
        WIKIS.forEach((s, wiki) -> {
            wiki.items.forEach(item -> {
                List<IRecipe<?>> crafting = new ArrayList<>();
                event.getRecipeManager().getRecipes(IRecipeType.CRAFTING).forEach((location, recipe) -> {
                    if (recipe.getRecipeOutput().isItemEqual(new ItemStack(item))) {
                        crafting.add(recipe);
                    }
                });
                wiki.crafting.put(item, crafting);
                List<IRecipe<?>> smelting = new ArrayList<>();
                event.getRecipeManager().getRecipes(IRecipeType.SMELTING).forEach((location, recipe) -> {
                    if (recipe.getRecipeOutput().isItemEqual(new ItemStack(item))) {
                        smelting.add(recipe);
                    }
                });
                wiki.smelting.put(item, smelting);
            });
        });
        watch.stop();
        Lollipop.LOGGER.info(MARKER, "Wiki recipes collecting completed in : {} ms", watch.getTime());
    }
}
