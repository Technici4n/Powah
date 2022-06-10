package owmii.powah.lib.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.powah.lib.client.util.MC;

import java.util.Collection;
import java.util.Collections;

public class RecipePair<T extends RecipeType, S extends RecipeSerializer<?>> {
    private final T type;
    private final S serializer;

    public RecipePair(T type, S serializer) {
        this.type = type;
        this.serializer = serializer;
    }

    public static <T extends RecipeType<? extends Recipe<?>>, S extends RecipeSerializer<?>> RecipePair<T, S> of(T type, S serializer) {
        return new RecipePair<>(type, serializer);
    }

    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("unchecked")
    public Collection<?> getAll() {
        if (MC.get().level != null) {
            return MC.get().level.getRecipeManager().getAllRecipesFor(this.type);
        }
        return Collections.emptyList();
    }

    public T getType() {
        return this.type;
    }

    public S getSerializer() {
        return this.serializer;
    }
}
