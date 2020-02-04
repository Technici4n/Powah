package owmii.powah.api.recipe.energizing;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public interface IEnergizingRecipe {
    boolean match(ItemStackHandler iInventory, World world, BlockPos pos);

    ItemStack getOutput();

    int getEnergy();

    List<Ingredient> getIngredients();
}
