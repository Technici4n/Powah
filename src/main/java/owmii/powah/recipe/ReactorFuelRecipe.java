package owmii.powah.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import owmii.powah.Powah;

public record ReactorFuelRecipe(Ingredient fuel, int fuelAmount, int temperature) implements NonCraftableRecipe {

    public static final Codec<ReactorFuelRecipe> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("fuel").forGetter(ReactorFuelRecipe::fuel),
            Codec.INT.fieldOf("fuelAmount").forGetter(ReactorFuelRecipe::fuelAmount),
            Codec.INT.fieldOf("temperature").forGetter(ReactorFuelRecipe::temperature)).apply(builder, ReactorFuelRecipe::new));

    public static final RecipeSerializer<ReactorFuelRecipe> SERIALIZER = new RecipeSerializer<>() {
        @Override
        public Codec<ReactorFuelRecipe> codec() {
            return CODEC;
        }

        @Override
        public ReactorFuelRecipe fromNetwork(FriendlyByteBuf buffer) {
            return new ReactorFuelRecipe(
                    Ingredient.fromNetwork(buffer),
                    buffer.readVarInt(),
                    buffer.readVarInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ReactorFuelRecipe recipe) {
            recipe.fuel().toNetwork(buffer);
            buffer.writeVarInt(recipe.fuelAmount());
            buffer.writeVarInt(recipe.temperature());
        }
    };

    public static final ResourceLocation TYPE_ID = Powah.id("reactor_fuel");

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, fuel);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return Recipes.REACTOR_FUEL.get();
    }
}
