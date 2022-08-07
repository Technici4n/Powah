package owmii.powah.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import owmii.powah.compat.jei.magmator.MagmatorCategory;
import owmii.powah.lib.util.Recipe;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.compat.jei.energizing.EnergizingCategory;
import owmii.powah.item.Itms;
import owmii.powah.recipe.Recipes;

@JeiPlugin
public class PowahJEIPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new MagmatorCategory(helper));
        registration.addRecipeCategories(new CoolantCategory(helper));
        registration.addRecipeCategories(new SolidCoolantCategory(helper));
        registration.addRecipeCategories(new HeatSourceCategory(helper));
        registration.addRecipeCategories(new EnergizingCategory(helper));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Blcks.ENERGIZING_ORB.get()), EnergizingCategory.TYPE);
        Blcks.ENERGIZING_ROD.getAll().forEach(block -> registration.addRecipeCatalyst(new ItemStack(block), EnergizingCategory.TYPE));
        Blcks.MAGMATOR.getAll().forEach(block -> registration.addRecipeCatalyst(new ItemStack(block), MagmatorCategory.TYPE));
        Blcks.THERMO_GENERATOR.getAll().forEach(block -> {
            registration.addRecipeCatalyst(new ItemStack(block), HeatSourceCategory.TYPE);
            registration.addRecipeCatalyst(new ItemStack(block), CoolantCategory.TYPE);
        });
        Blcks.REACTOR.getAll().forEach(block -> {
            registration.addRecipeCatalyst(new ItemStack(block), SolidCoolantCategory.TYPE);
            registration.addRecipeCatalyst(new ItemStack(block), CoolantCategory.TYPE);
        });
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(EnergizingCategory.TYPE, Recipe.getAll(Minecraft.getInstance().level, Recipes.ENERGIZING.get()));
        registration.addRecipes(MagmatorCategory.TYPE, MagmatorCategory.Maker.getBucketRecipes(registration.getIngredientManager()));
        registration.addRecipes(CoolantCategory.TYPE, CoolantCategory.Maker.getBucketRecipes(registration.getIngredientManager()));
        registration.addRecipes(SolidCoolantCategory.TYPE, SolidCoolantCategory.Maker.getBucketRecipes(registration.getIngredientManager()));
        registration.addRecipes(HeatSourceCategory.TYPE, HeatSourceCategory.Maker.getBucketRecipes(registration.getIngredientManager()));

        if (Powah.config().general.player_aerial_pearl)
            registration.addIngredientInfo(new ItemStack(Itms.PLAYER_AERIAL_PEARL.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.powah.player_aerial_pearl"));
        if (Powah.config().general.dimensional_binding_card)
            registration.addIngredientInfo(new ItemStack(Itms.BINDING_CARD_DIM.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.powah.binding_card_dim"));
        if (Powah.config().general.lens_of_ender)
            registration.addIngredientInfo(new ItemStack(Itms.LENS_OF_ENDER.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.powah.lens_of_ender"));
    }


    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Powah.MOD_ID, "main");
    }
}
