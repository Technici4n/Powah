package owmii.powah.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.compat.common.FluidCoolant;
import owmii.powah.compat.common.MagmatorFuel;
import owmii.powah.compat.common.PassiveHeatSource;
import owmii.powah.compat.common.SolidCoolant;
import owmii.powah.item.Itms;
import owmii.powah.lib.client.screen.container.AbstractContainerScreen;
import owmii.powah.recipe.Recipes;

@EmiEntrypoint
public class PowahEmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(EmiMagmatorRecipe.CATEGORY);
        for (var block : Blcks.MAGMATOR.getAll()) {
            registry.addWorkstation(EmiMagmatorRecipe.CATEGORY, EmiStack.of(block));
        }

        registry.addCategory(EmiFluidCoolantRecipe.CATEGORY);
        registry.addCategory(EmiSolidCoolantRecipe.CATEGORY);
        registry.addCategory(EmiHeatSourceRecipe.CATEGORY);
        registry.addCategory(EmiEnergizingRecipe.CATEGORY);
        registry.addCategory(EmiReactorFuelRecipe.CATEGORY);

        registry.addWorkstation(EmiEnergizingRecipe.CATEGORY, EmiStack.of(Blcks.ENERGIZING_ORB.get()));
        Blcks.ENERGIZING_ROD.getAll().forEach(block -> registry.addWorkstation(EmiEnergizingRecipe.CATEGORY, EmiStack.of(block)));
        Blcks.MAGMATOR.getAll().forEach(block -> registry.addWorkstation(EmiMagmatorRecipe.CATEGORY, EmiStack.of(block)));
        Blcks.THERMO_GENERATOR.getAll().forEach(block -> {
            registry.addWorkstation(EmiHeatSourceRecipe.CATEGORY, EmiStack.of(block));
            registry.addWorkstation(EmiFluidCoolantRecipe.CATEGORY, EmiStack.of(block));
        });
        Blcks.REACTOR.getAll().forEach(block -> {
            registry.addWorkstation(EmiSolidCoolantRecipe.CATEGORY, EmiStack.of(block));
            registry.addWorkstation(EmiFluidCoolantRecipe.CATEGORY, EmiStack.of(block));
            registry.addWorkstation(EmiReactorFuelRecipe.CATEGORY, EmiStack.of(block));
        });

        adaptRecipeType(registry, Recipes.ENERGIZING.get(), EmiEnergizingRecipe::new);
        adaptRecipeType(registry, Recipes.REACTOR_FUEL.get(), EmiReactorFuelRecipe::new);

        MagmatorFuel.getAll().forEach(recipe -> registry.addRecipe(new EmiMagmatorRecipe(recipe)));
        FluidCoolant.getAll().forEach(recipe -> registry.addRecipe(new EmiFluidCoolantRecipe(recipe)));
        SolidCoolant.getAll().forEach(recipe -> registry.addRecipe(new EmiSolidCoolantRecipe(recipe)));
        PassiveHeatSource.getAll().forEach(recipe -> registry.addRecipe(new EmiHeatSourceRecipe(recipe)));

        if (Powah.config().general.player_aerial_pearl) {
            registry.addRecipe(new EmiInfoRecipe(
                    List.of(EmiStack.of(Itms.PLAYER_AERIAL_PEARL.get())),
                    List.of(Component.translatable("jei.powah.player_aerial_pearl")),
                    null));
        }
        if (Powah.config().general.dimensional_binding_card) {
            registry.addRecipe(new EmiInfoRecipe(
                    List.of(EmiStack.of(Itms.BINDING_CARD_DIM.get())),
                    List.of(Component.translatable("jei.powah.binding_card_dim")),
                    null));
        }
        if (Powah.config().general.lens_of_ender) {
            registry.addRecipe(new EmiInfoRecipe(
                    List.of(EmiStack.of(Itms.LENS_OF_ENDER.get())),
                    List.of(Component.translatable("jei.powah.lens_of_ender")),
                    null));
        }

        registry.addGenericExclusionArea(PowahEmiPlugin::getExclusionAreas);
    }

    private static <C extends Container, T extends Recipe<C>> void adaptRecipeType(EmiRegistry registry,
            RecipeType<T> recipeType,
            Function<RecipeHolder<T>, ? extends EmiRecipe> adapter) {
        registry.getRecipeManager().getAllRecipesFor(recipeType)
                .stream()
                .map(adapter)
                .forEach(registry::addRecipe);
    }

    private static void getExclusionAreas(Screen screen, Consumer<Bounds> consumer) {
        if (screen instanceof AbstractContainerScreen<?>containerScreen) {
            for (var extraArea : containerScreen.getExtraAreas()) {
                consumer.accept(new Bounds(
                        extraArea.getX(),
                        extraArea.getY(),
                        extraArea.getWidth(),
                        extraArea.getHeight()));
            }
        }
    }
}
