package owmii.powah.block.energizing;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import owmii.powah.Powah;
import owmii.powah.recipe.Recipes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

public class EnergizingRecipe implements IRecipe<RecipeWrapper> {
    public static final ResourceLocation ID = new ResourceLocation(Powah.MOD_ID, "energizing");
    protected final ResourceLocation id;
    private final ItemStack output;
    private final long energy;
    private final NonNullList<Ingredient> ingredients;


    public EnergizingRecipe(ItemStack output, long energy, Ingredient... ingredients) {
        this(ID, output, energy, NonNullList.from(Ingredient.EMPTY, ingredients));
    }

    public EnergizingRecipe(ResourceLocation id, ItemStack output, long energy, Ingredient... ingredients) {
        this(id, output, energy, NonNullList.from(Ingredient.EMPTY, ingredients));
    }

    public EnergizingRecipe(ResourceLocation id, ItemStack output, long energy, NonNullList<Ingredient> ingredients) {
        this.id = id;
        this.output = output;
        this.energy = energy;
        this.ingredients = ingredients;
    }

    @Override
    public boolean matches(RecipeWrapper inv, World world) {
        List<Ingredient> stacks = new ArrayList<>(getIngredients());
        for (int i = 1; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
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
    public ItemStack getCraftingResult(RecipeWrapper inv) {
        return this.output.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return Recipes.ENERGIZING_SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return Recipes.ENERGIZING;
    }

    public long getEnergy() {
        return this.energy;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<EnergizingRecipe> {
        @Override
        public EnergizingRecipe read(ResourceLocation recipeId, JsonObject json) {
            NonNullList<Ingredient> list = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
            long energy = Long.parseLong(JSONUtils.getString(json, "energy", "0"));

            if (list.isEmpty()) {
                throw new JsonParseException("No ingredients for energizing recipe");
            } else if (list.size() > 6) {
                throw new JsonParseException("Too many ingredients for energizing recipe the max is 6");
            } else if (energy <= 0) {
                throw new JsonParseException("Energizing recipe require energy to work!!");
            }

            ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            return new EnergizingRecipe(recipeId, result, energy, list);
        }

        private static NonNullList<Ingredient> readIngredients(JsonArray elements) {
            NonNullList<Ingredient> list = NonNullList.create();
            IntStream.range(0, elements.size())
                    .mapToObj(i -> Ingredient.deserialize(elements.get(i)))
                    .filter(ingredient -> !ingredient.hasNoMatchingItems())
                    .forEach(list::add);
            return list;
        }

        @Nullable
        @Override
        public EnergizingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            NonNullList<Ingredient> list = NonNullList.withSize(buffer.readInt(), Ingredient.EMPTY);
            IntStream.range(0, list.size()).forEach(i -> list.set(i, Ingredient.read(buffer)));
            return new EnergizingRecipe(recipeId, buffer.readItemStack(), buffer.readLong(), list);
        }

        @Override
        public void write(PacketBuffer buffer, EnergizingRecipe recipe) {
            buffer.writeInt(recipe.ingredients.size());
            recipe.ingredients.forEach(ingredient -> ingredient.write(buffer));
            buffer.writeItemStack(recipe.output);
            buffer.writeLong(recipe.energy);
        }
    }
}