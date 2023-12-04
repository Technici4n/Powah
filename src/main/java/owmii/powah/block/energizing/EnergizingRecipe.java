package owmii.powah.block.energizing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipeCodecs;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import owmii.powah.Powah;
import owmii.powah.lib.logistics.inventory.RecipeWrapper;
import owmii.powah.recipe.Recipes;

public class EnergizingRecipe implements Recipe<RecipeWrapper> {
    public static final ResourceLocation ID = new ResourceLocation(Powah.MOD_ID, "energizing");
    private final ItemStack output;
    private final long energy;
    private final NonNullList<Ingredient> ingredients;

    public EnergizingRecipe(ItemStack output, long energy, Ingredient... ingredients) {
        this(output, energy, NonNullList.of(Ingredient.EMPTY, ingredients));
    }

    public EnergizingRecipe(ItemStack output, long energy, NonNullList<Ingredient> ingredients) {
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
    public ItemStack assemble(RecipeWrapper inv, RegistryAccess registry) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    public ItemStack getResultItem() {
        return output;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.output;
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

    public long getScaledEnergy() {
        return Math.max(1, (long) (energy * Powah.config().general.energizing_energy_ratio));
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<EnergizingRecipe> {

        public static final Codec<EnergizingRecipe> CODEC = RecordCodecBuilder.create(builder -> {
            return builder.group(
                    Codec.LONG.fieldOf("energy").forGetter(EnergizingRecipe::getEnergy),
                    Ingredient.CODEC_NONEMPTY
                            .listOf()
                            .fieldOf("ingredients")
                            .forGetter(EnergizingRecipe::getIngredients),
                    CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC.fieldOf("result").forGetter(EnergizingRecipe::getResultItem))
                    .apply(builder, (energy, ingredients, result) -> {
                        var nnIngredients = NonNullList.<Ingredient>create();
                        nnIngredients.addAll(ingredients);
                        return new EnergizingRecipe(result, energy, nnIngredients);
                    });
        });

        @Override
        public Codec<EnergizingRecipe> codec() {
            return CODEC;
        }

        @Override
        public EnergizingRecipe fromNetwork(FriendlyByteBuf buffer) {
            NonNullList<Ingredient> list = NonNullList.withSize(buffer.readInt(), Ingredient.EMPTY);
            IntStream.range(0, list.size()).forEach(i -> list.set(i, Ingredient.fromNetwork(buffer)));
            return new EnergizingRecipe(buffer.readItem(), buffer.readLong(), list);
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
