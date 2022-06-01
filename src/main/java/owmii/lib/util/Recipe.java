package owmii.lib.util;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import owmii.lib.item.Stacks;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class Recipe {
    public static Collection<? extends net.minecraft.world.item.crafting.Recipe<?>> getAll(@Nullable Level world, RecipeType<?> type) { // TODO remove
        if (world != null) {
            RecipeManager manager = world.getRecipeManager();
            return manager.getRecipes().stream().filter(r -> r.getType() == type).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
