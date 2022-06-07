package owmii.powah.client.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import owmii.powah.lib.util.Recipe;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.client.compat.jei.energizing.EnergizingCategory;
import owmii.powah.client.compat.jei.magmator.MagmatorCategory;
import owmii.powah.config.Configs;
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
        registration.addRecipeCatalyst(new ItemStack(Blcks.ENERGIZING_ORB.get()), EnergizingCategory.ID);
        Blcks.ENERGIZING_ROD.getAll().forEach(block -> registration.addRecipeCatalyst(new ItemStack(block), EnergizingCategory.ID));
        Blcks.MAGMATOR.getAll().forEach(block -> registration.addRecipeCatalyst(new ItemStack(block), MagmatorCategory.ID));
        Blcks.THERMO_GENERATOR.getAll().forEach(block -> {
            registration.addRecipeCatalyst(new ItemStack(block), HeatSourceCategory.ID);
            registration.addRecipeCatalyst(new ItemStack(block), CoolantCategory.ID);
        });
        Blcks.REACTOR.getAll().forEach(block -> {
            registration.addRecipeCatalyst(new ItemStack(block), SolidCoolantCategory.ID);
            registration.addRecipeCatalyst(new ItemStack(block), CoolantCategory.ID);
        });
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(Recipe.getAll(Minecraft.getInstance().level, Recipes.ENERGIZING.get()), EnergizingCategory.ID);
        registration.addRecipes(MagmatorCategory.Maker.getBucketRecipes(registration.getIngredientManager()), MagmatorCategory.ID);
        registration.addRecipes(CoolantCategory.Maker.getBucketRecipes(registration.getIngredientManager()), CoolantCategory.ID);
        registration.addRecipes(SolidCoolantCategory.Maker.getBucketRecipes(registration.getIngredientManager()), SolidCoolantCategory.ID);
        registration.addRecipes(HeatSourceCategory.Maker.getBucketRecipes(registration.getIngredientManager()), HeatSourceCategory.ID);

        if (Configs.GENERAL.player_aerial_pearl.get())
            registration.addIngredientInfo(new ItemStack(Itms.PLAYER_AERIAL_PEARL.get()), VanillaTypes.ITEM, new TranslatableComponent("jei.powah.player_aerial_pearl"));
        if (Configs.GENERAL.binding_card_dim.get())
            registration.addIngredientInfo(new ItemStack(Itms.BINDING_CARD_DIM.get()), VanillaTypes.ITEM, new TranslatableComponent("jei.powah.binding_card_dim"));
        if (Configs.GENERAL.lens_of_ender.get())
            registration.addIngredientInfo(new ItemStack(Itms.LENS_OF_ENDER.get()), VanillaTypes.ITEM, new TranslatableComponent("jei.powah.lens_of_ender"));
    }


    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Powah.MOD_ID, "main");
    }
}
