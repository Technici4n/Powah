package owmii.lib.data;

import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import java.util.function.Consumer;

public class CookingRecipeBuilder {
    private final ItemStack result;
    private final Ingredient ingredient;
    private final float experience;
    private final int cookingTime;
    private final SimpleCookingSerializer<?> recipeSerializer;

    private CookingRecipeBuilder(ItemStack resultIn, Ingredient ingredientIn, float experienceIn, int cookingTimeIn, SimpleCookingSerializer<?> serializer) {
        this.result = resultIn;
        this.ingredient = ingredientIn;
        this.experience = experienceIn;
        this.cookingTime = cookingTimeIn;
        this.recipeSerializer = serializer;
    }

    public static CookingRecipeBuilder cookingRecipe(Ingredient ingredient, ItemStack result, float experience, int cookingTime, SimpleCookingSerializer<?> serializer) {
        return new CookingRecipeBuilder(result, ingredient, experience, cookingTime, serializer);
    }

    public static CookingRecipeBuilder blastingRecipe(Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        return cookingRecipe(ingredient, result, experience, cookingTime, RecipeSerializer.BLASTING_RECIPE);
    }

    public static CookingRecipeBuilder smeltingRecipe(Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        return cookingRecipe(ingredient, result, experience, cookingTime, RecipeSerializer.SMELTING_RECIPE);
    }

    public void build(Consumer<FinishedRecipe> consumerIn) {
        this.build(consumerIn, Registry.ITEM.getKey(this.result.getItem()));
    }

    public void build(Consumer<FinishedRecipe> consumerIn, String save) {
        ResourceLocation location = Registry.ITEM.getKey(this.result.getItem());
        ResourceLocation location1 = new ResourceLocation(save);
        if (location1.equals(location)) {
            throw new IllegalStateException("Recipe " + location1 + " should remove its 'save' argument");
        } else {
            this.build(consumerIn, location1);
        }
    }

    public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
        consumerIn.accept(new CookingRecipeBuilder.Result(id, this.ingredient, this.result, this.experience, this.cookingTime, this.recipeSerializer));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient ingredient;
        private final ItemStack result;
        private final float experience;
        private final int cookingTime;
        private final RecipeSerializer<? extends AbstractCookingRecipe> serializer;

        public Result(ResourceLocation idIn, Ingredient ingredientIn, ItemStack resultIn, float experienceIn, int cookingTimeIn, RecipeSerializer<? extends AbstractCookingRecipe> serializerIn) {
            this.id = idIn;
            this.ingredient = ingredientIn;
            this.result = resultIn;
            this.experience = experienceIn;
            this.cookingTime = cookingTimeIn;
            this.serializer = serializerIn;
        }

        public void serializeRecipeData(JsonObject json) {
            json.add("ingredient", this.ingredient.toJson());

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("item", Registry.ITEM.getKey(this.result.getItem()).toString());
            if (this.result.getCount() > 1) {
                jsonObject.addProperty("count", this.result.getCount());
            }
            json.add("result", jsonObject);

            json.addProperty("experience", this.experience);
            json.addProperty("cookingtime", this.cookingTime);
        }

        public RecipeSerializer<?> getType() {
            return this.serializer;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        /**
         * Gets the ID for the recipe.
         */
        public ResourceLocation getId() {
            return this.id;
        }

        /**
         * Gets the ID for the advancement associated with this recipe. Should not be null if {@link #serializeAdvancement}
         * is non-null.
         */
        @Nullable
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}