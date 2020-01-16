package owmii.powah.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import owmii.powah.Powah;
import owmii.powah.api.recipe.energizing.EnergizingRecipeSorter;
import owmii.powah.block.IBlocks;
import owmii.powah.block.energizing.EnergizingRods;
import owmii.powah.block.generator.magmatic.MagmaticGenerators;
import owmii.powah.block.generator.reactor.Reactors;
import owmii.powah.block.generator.thermoelectric.ThermoGenerators;
import owmii.powah.compat.jei.energizing.EnergizingCategory;
import owmii.powah.compat.jei.magmatic.MagmaticCategory;
import owmii.powah.compat.jei.reactor.ReactorCoolantCategory;
import owmii.powah.compat.jei.reactor.ReactorSCoolantCategory;
import owmii.powah.compat.jei.thermo.ThermoCoolantCategory;
import owmii.powah.compat.jei.thermo.ThermoHeatCategory;
import owmii.powah.config.Config;
import owmii.powah.item.IItems;

@JeiPlugin
public class PowahJEIPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new MagmaticCategory(helper));
        registration.addRecipeCategories(new ThermoCoolantCategory(helper));
        registration.addRecipeCategories(new ReactorCoolantCategory(helper));
        registration.addRecipeCategories(new ReactorSCoolantCategory(helper));
        registration.addRecipeCategories(new ThermoHeatCategory(helper));
        registration.addRecipeCategories(new EnergizingCategory(helper));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(IBlocks.ENERGIZING_ORB), EnergizingCategory.ID);
        for (EnergizingRods er : EnergizingRods.values()) {
            registration.addRecipeCatalyst(new ItemStack(er.get()), EnergizingCategory.ID);
        }
        for (MagmaticGenerators mg : MagmaticGenerators.values()) {
            registration.addRecipeCatalyst(new ItemStack(mg.get()), MagmaticCategory.ID);
        }
        for (ThermoGenerators tg : ThermoGenerators.values()) {
            registration.addRecipeCatalyst(new ItemStack(tg.get()), ThermoCoolantCategory.ID);
            registration.addRecipeCatalyst(new ItemStack(tg.get()), ThermoHeatCategory.ID);
        }

        for (Reactors re : Reactors.values()) {
            registration.addRecipeCatalyst(new ItemStack(re.get()), ReactorCoolantCategory.ID);
            registration.addRecipeCatalyst(new ItemStack(re.get()), ReactorSCoolantCategory.ID);
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(MagmaticCategory.Maker.getBucketRecipes(registration.getIngredientManager()), MagmaticCategory.ID);
        registration.addRecipes(ThermoCoolantCategory.Maker.getBucketRecipes(registration.getIngredientManager()), ThermoCoolantCategory.ID);
        registration.addRecipes(ReactorCoolantCategory.Maker.getBucketRecipes(registration.getIngredientManager()), ReactorCoolantCategory.ID);
        registration.addRecipes(ReactorSCoolantCategory.Maker.getBucketRecipes(registration.getIngredientManager()), ReactorSCoolantCategory.ID);
        registration.addRecipes(ThermoHeatCategory.Maker.getBucketRecipes(registration.getIngredientManager()), ThermoHeatCategory.ID);
        registration.addRecipes(EnergizingRecipeSorter.RECIPES, EnergizingCategory.ID);

        // Info's
        if (Config.GENERAL.capacitor_blazing.get())
            registration.addIngredientInfo(new ItemStack(IItems.CAPACITOR_BLAZING), VanillaTypes.ITEM, I18n.format("wiki.powah.capacitor_blazing"));
        if (Config.GENERAL.capacitor_niotic.get())
            registration.addIngredientInfo(new ItemStack(IItems.CAPACITOR_NIOTIC), VanillaTypes.ITEM, I18n.format("wiki.powah.capacitor_niotic"));
        if (Config.GENERAL.capacitor_spirited.get())
            registration.addIngredientInfo(new ItemStack(IItems.CAPACITOR_SPIRITED), VanillaTypes.ITEM, I18n.format("wiki.powah.capacitor_spirited"));
        if (Config.GENERAL.player_aerial_pearl.get())
            registration.addIngredientInfo(new ItemStack(IItems.PLAYER_AERIAL_PEARL), VanillaTypes.ITEM, I18n.format("wiki.powah.player_aerial_pearl"));

    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Powah.MOD_ID, "main");
    }
}
