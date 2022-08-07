package owmii.powah.lib.util;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Recipe {
    public static <C extends Container, T extends net.minecraft.world.item.crafting.Recipe<C>> List<T> getAll(@Nullable Level world, RecipeType<T> type) { // TODO remove
        if (world != null) {
            RecipeManager manager = world.getRecipeManager();
            return manager.getAllRecipesFor(type);
        }
        return Collections.emptyList();
    }
}
