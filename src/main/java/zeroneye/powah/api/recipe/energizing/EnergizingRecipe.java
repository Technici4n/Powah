package zeroneye.powah.api.recipe.energizing;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class EnergizingRecipe implements IEnergizingRecipe {
    private final ItemStack output;
    private final int energy;
    private final List<Ingredient> ingredients;

    public EnergizingRecipe(ItemStack output, int energy, Ingredient... ingredients) {
        this.output = output;
        this.energy = energy;
        this.ingredients = Arrays.asList(ingredients);
    }

    @Override
    public boolean match(ItemStackHandler inventory, World world, BlockPos pos) {
        List<Ingredient> stacks = new ArrayList<>(getIngredients());

        for (int i = 1; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
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
    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public int getEnergy() {
        return this.energy;
    }

    @Override
    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }
}
