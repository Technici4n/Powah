package owmii.lib.data;

import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.CookingRecipeSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class CookingRecipeBuilder {
    private final ItemStack result;
    private final Ingredient ingredient;
    private final float experience;
    private final int cookingTime;
    private final CookingRecipeSerializer<?> recipeSerializer;

    private CookingRecipeBuilder(ItemStack resultIn, Ingredient ingredientIn, float experienceIn, int cookingTimeIn, CookingRecipeSerializer<?> serializer) {
        this.result = resultIn;
        this.ingredient = ingredientIn;
        this.experience = experienceIn;
        this.cookingTime = cookingTimeIn;
        this.recipeSerializer = serializer;
    }

    public static CookingRecipeBuilder cookingRecipe(Ingredient ingredient, ItemStack result, float experience, int cookingTime, CookingRecipeSerializer<?> serializer) {
        return new CookingRecipeBuilder(result, ingredient, experience, cookingTime, serializer);
    }

    public static CookingRecipeBuilder blastingRecipe(Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        return cookingRecipe(ingredient, result, experience, cookingTime, IRecipeSerializer.BLASTING);
    }

    public static CookingRecipeBuilder smeltingRecipe(Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        return cookingRecipe(ingredient, result, experience, cookingTime, IRecipeSerializer.SMELTING);
    }

    public void build(Consumer<IFinishedRecipe> consumerIn) {
        this.build(consumerIn, Registry.ITEM.getKey(this.result.getItem()));
    }

    public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
        ResourceLocation location = Registry.ITEM.getKey(this.result.getItem());
        ResourceLocation location1 = new ResourceLocation(save);
        if (location1.equals(location)) {
            throw new IllegalStateException("Recipe " + location1 + " should remove its 'save' argument");
        } else {
            this.build(consumerIn, location1);
        }
    }

    public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
        consumerIn.accept(new CookingRecipeBuilder.Result(id, this.ingredient, this.result, this.experience, this.cookingTime, this.recipeSerializer));
    }

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient ingredient;
        private final ItemStack result;
        private final float experience;
        private final int cookingTime;
        private final IRecipeSerializer<? extends AbstractCookingRecipe> serializer;

        public Result(ResourceLocation idIn, Ingredient ingredientIn, ItemStack resultIn, float experienceIn, int cookingTimeIn, IRecipeSerializer<? extends AbstractCookingRecipe> serializerIn) {
            this.id = idIn;
            this.ingredient = ingredientIn;
            this.result = resultIn;
            this.experience = experienceIn;
            this.cookingTime = cookingTimeIn;
            this.serializer = serializerIn;
        }

        public void serialize(JsonObject json) {
            json.add("ingredient", this.ingredient.serialize());

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("item", Registry.ITEM.getKey(this.result.getItem()).toString());
            if (this.result.getCount() > 1) {
                jsonObject.addProperty("count", this.result.getCount());
            }
            json.add("result", jsonObject);

            json.addProperty("experience", this.experience);
            json.addProperty("cookingtime", this.cookingTime);
        }

        public IRecipeSerializer<?> getSerializer() {
            return this.serializer;
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson() {
            return null;
        }

        /**
         * Gets the ID for the recipe.
         */
        public ResourceLocation getID() {
            return this.id;
        }

        /**
         * Gets the ID for the advancement associated with this recipe. Should not be null if {@link #getAdvancementJson}
         * is non-null.
         */
        @Nullable
        public ResourceLocation getAdvancementID() {
            return null;
        }
    }
}