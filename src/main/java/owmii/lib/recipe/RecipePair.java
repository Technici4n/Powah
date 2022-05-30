package owmii.lib.recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.client.util.MC;

import java.util.Collection;
import java.util.Collections;

public class RecipePair<T extends IRecipeType, S extends IRecipeSerializer<?>> {
    private final T type;
    private final S serializer;

    public RecipePair(T type, S serializer) {
        this.type = type;
        this.serializer = serializer;
    }

    public static <T extends IRecipeType<? extends IRecipe<?>>, S extends IRecipeSerializer<?>> RecipePair<T, S> of(T type, S serializer) {
        return new RecipePair<>(type, serializer);
    }

    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("unchecked")
    public Collection<?> getAll() {
        if (MC.get().world != null) {
            return MC.get().world.getRecipeManager().getRecipes(this.type).values();
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
