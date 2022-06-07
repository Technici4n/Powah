package owmii.powah.lib.util;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class Recipe {
    public static Collection<? extends net.minecraft.world.item.crafting.Recipe<?>> getAll(@Nullable Level world, RecipeType<?> type) { // TODO remove
        if (world != null) {
            RecipeManager manager = world.getRecipeManager();
            return manager.getRecipes().stream().filter(r -> r.getType() == type).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
