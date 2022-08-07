package owmii.powah.block.energizing;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import owmii.powah.Powah;
import owmii.powah.lib.logistics.inventory.RecipeWrapper;
import owmii.powah.recipe.Recipes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

public class EnergizingRecipe implements Recipe<RecipeWrapper> {
    public static final ResourceLocation ID = new ResourceLocation(Powah.MOD_ID, "energizing");
    protected final ResourceLocation id;
    private final ItemStack output;
    private final long energy;
    private final NonNullList<Ingredient> ingredients;

    public EnergizingRecipe(ResourceLocation id, ItemStack output, long energy, Ingredient... ingredients) {
        this(id, output, energy, NonNullList.of(Ingredient.EMPTY, ingredients));
    }

    public EnergizingRecipe(ResourceLocation id, ItemStack output, long energy, NonNullList<Ingredient> ingredients) {
        this.id = id;
        this.output = output;
        this.energy = energy;
        this.ingredients = ingredients;
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level world) {
        List<Ingredient> stacks = new ArrayList<>(getIngredients());
        for (int i = 1; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                boolean flag = false;
                Iterator<Ingredient> itr = stacks.iterator();
                while (itr.hasNext()) {
                    Ingredient ingredient = itr.next();
                    if (ingredient.test(stack)) {
                        flag = true;
                        itr.remove();
                        break;
                    }
                }
                if (!flag) {
                    return false;
                }
            }
        }
        return stacks.isEmpty();
    }

    @Override
    public ItemStack assemble(RecipeWrapper inv) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return this.output;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Recipes.ENERGIZING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Recipes.ENERGIZING.get();
    }

    public long getEnergy() {
        return this.energy;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public static class Serializer implements RecipeSerializer<EnergizingRecipe> {
        @Override
        public EnergizingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            NonNullList<Ingredient> list = readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
            long energy = Long.parseLong(GsonHelper.getAsString(json, "energy", "0"));
            // Apply multiplier from config
            energy = Math.max(1, (long) (energy * Powah.config().general.energizing_energy_ratio));

            if (list.isEmpty()) {
                throw new JsonParseException("No ingredients for energizing recipe");
            } else if (list.size() > 6) {
                throw new JsonParseException("Too many ingredients for energizing recipe the max is 6");
            } else if (energy <= 0) {
                throw new JsonParseException("Energizing recipe require energy to work!!");
            }

            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            return new EnergizingRecipe(recipeId, result, energy, list);
        }

        private static NonNullList<Ingredient> readIngredients(JsonArray elements) {
            NonNullList<Ingredient> list = NonNullList.create();
            IntStream.range(0, elements.size())
                    .mapToObj(i -> Ingredient.fromJson(elements.get(i)))
                    .filter(ingredient -> !ingredient.isEmpty())
                    .forEach(list::add);
            return list;
        }

        @Nullable
        @Override
        public EnergizingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            NonNullList<Ingredient> list = NonNullList.withSize(buffer.readInt(), Ingredient.EMPTY);
            IntStream.range(0, list.size()).forEach(i -> list.set(i, Ingredient.fromNetwork(buffer)));
            return new EnergizingRecipe(recipeId, buffer.readItem(), buffer.readLong(), list);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, EnergizingRecipe recipe) {
            buffer.writeInt(recipe.ingredients.size());
            recipe.ingredients.forEach(ingredient -> ingredient.toNetwork(buffer));
            buffer.writeItem(recipe.output);
            buffer.writeLong(recipe.energy);
        }
    }
}