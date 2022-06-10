package owmii.powah.recipe;

import com.google.common.base.Suppliers;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import owmii.powah.Powah;
import owmii.powah.block.energizing.EnergizingRecipe;

import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class Recipes {
    public static final DeferredRegister<RecipeSerializer<?>> DR_SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Powah.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> DR_TYPE = DeferredRegister.create(Registry.RECIPE_TYPE.key(), Powah.MOD_ID);

    public static final Supplier<EnergizingRecipe.Serializer> ENERGIZING_SERIALIZER = DR_SERIALIZER.register("energizing", EnergizingRecipe.Serializer::new);
    public static final Supplier<RecipeType<EnergizingRecipe>> ENERGIZING = DR_TYPE.register("energizing", () -> new RecipeType<>() {
        public String toString() {
            return EnergizingRecipe.ID.toString();
        }
    });
}
