package owmii.powah.recipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import owmii.powah.Powah;
import owmii.powah.block.energizing.EnergizingRecipe;

@SuppressWarnings("unchecked")
public class Recipes {
    public static final owmii.lib.registry.Registry<IRecipeSerializer<?>> REG = new owmii.lib.registry.Registry(IRecipeSerializer.class, Powah.MOD_ID);
    public static final EnergizingRecipe.Serializer ENERGIZING_SERIALIZER = REG.register("energizing", new EnergizingRecipe.Serializer());
    public static final IRecipeType<EnergizingRecipe> ENERGIZING = Registry.register(Registry.RECIPE_TYPE, EnergizingRecipe.ID.toString(), new IRecipeType<EnergizingRecipe>() {
        public String toString() {
            return EnergizingRecipe.ID.toString();
        }
    });
}
