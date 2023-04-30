package owmii.powah.lib.util;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class Recipe {
    public static <C extends Container, T extends net.minecraft.world.item.crafting.Recipe<C>> List<T> getAll(@Nullable Level world,
            RecipeType<T> type) { // TODO remove
        if (world != null) {
            RecipeManager manager = world.getRecipeManager();
            return manager.getAllRecipesFor(type);
        }
        return Collections.emptyList();
    }
}
