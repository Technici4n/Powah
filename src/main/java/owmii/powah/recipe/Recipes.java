package owmii.powah.recipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import owmii.lib.util.Recipe;
import owmii.powah.block.energizing.EnergizingRecipe;

public class Recipes {
    public static final IRecipeType<EnergizingRecipe> ENERGIZING = Recipe.register(EnergizingRecipe.ID);
    public static final EnergizingRecipe.Serializer ENERGIZING_SERIALIZER = IRecipeSerializer.register(EnergizingRecipe.ID.toString(), new EnergizingRecipe.Serializer());

    public static void init() {}
}
