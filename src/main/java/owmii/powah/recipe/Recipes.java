package owmii.powah.recipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import owmii.powah.block.energizing.EnergizingRecipe;

public class Recipes {
    public static final EnergizingRecipe.Serializer ENERGIZING_SERIALIZER = IRecipeSerializer.register(EnergizingRecipe.ID.toString(), new EnergizingRecipe.Serializer());
    public static final IRecipeType<EnergizingRecipe> ENERGIZING = Registry.register(Registry.RECIPE_TYPE, EnergizingRecipe.ID.toString(), new IRecipeType<EnergizingRecipe>() {
        public String toString() { return EnergizingRecipe.ID.toString(); }
    });

    public static void init() {}
}
