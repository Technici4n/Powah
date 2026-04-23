package owmii.powah.block.energizing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import owmii.powah.Powah;
import owmii.powah.recipe.Recipes;

public class EnergizingRecipe implements Recipe<RecipeInput> {
    public static final Identifier ID = Powah.id("energizing");
    private final ItemStackTemplate output;
    private final long energy;
    private final NonNullList<Ingredient> ingredients;

    public static final MapCodec<EnergizingRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(e -> e.output),
            Codec.LONG.fieldOf("energy").forGetter(e -> e.energy),
            Ingredient.CODEC.listOf(1, 16)
                    .fieldOf("ingredients")
                    .forGetter(e -> e.ingredients))
            .apply(builder, EnergizingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, EnergizingRecipe> STREAM_CODEC = StreamCodec.composite(
            ItemStackTemplate.STREAM_CODEC,
            EnergizingRecipe::getResultItem,
            ByteBufCodecs.VAR_LONG, EnergizingRecipe::getEnergy,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
            EnergizingRecipe::getIngredients,
            EnergizingRecipe::new);

    public EnergizingRecipe(ItemStackTemplate output, long energy, List<Ingredient> ingredients) {
        this.output = output;
        this.energy = energy;
        this.ingredients = NonNullList.copyOf(ingredients);
    }

    @Override
    public boolean matches(RecipeInput inv, Level world) {
        List<Ingredient> stacks = new ArrayList<>(getIngredients());
        for (int i = 1; i < inv.size(); i++) {
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
    public ItemStack assemble(RecipeInput inv) {
        return this.output.create();
    }

    public ItemStackTemplate getResultItem() {
        return output;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public RecipeSerializer<EnergizingRecipe> getSerializer() {
        return Recipes.ENERGIZING_SERIALIZER.get();
    }

    @Override
    public RecipeType<EnergizingRecipe> getType() {
        return Recipes.ENERGIZING.get();
    }

    public long getEnergy() {
        return this.energy;
    }

    public long getScaledEnergy() {
        return Math.max(1, (long) (energy * Powah.config().general.energizing_energy_ratio));
    }

    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

}
