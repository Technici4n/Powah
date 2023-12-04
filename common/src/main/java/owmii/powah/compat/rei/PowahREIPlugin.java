package owmii.powah.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.client.BuiltinClientPlugin;
import net.minecraft.network.chat.Component;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.block.energizing.EnergizingRecipe;
import owmii.powah.compat.rei.energizing.EnergizingCategory;
import owmii.powah.compat.rei.energizing.EnergizingDisplay;
import owmii.powah.compat.rei.magmator.MagmatorCategory;
import owmii.powah.compat.rei.magmator.MagmatorDisplay;
import owmii.powah.item.Itms;
import owmii.powah.lib.client.screen.container.AbstractContainerScreen;
import owmii.powah.recipe.Recipes;

public class PowahREIPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new MagmatorCategory());
        registry.add(new CoolantCategory());
        registry.add(new SolidCoolantCategory());
        registry.add(new HeatSourceCategory());
        registry.add(new EnergizingCategory());

        registry.addWorkstations(EnergizingCategory.ID, EntryStacks.of(Blcks.ENERGIZING_ORB.get()));
        Blcks.ENERGIZING_ROD.getAll().forEach(block -> registry.addWorkstations(EnergizingCategory.ID, EntryStacks.of(block)));
        Blcks.MAGMATOR.getAll().forEach(block -> registry.addWorkstations(MagmatorCategory.ID, EntryStacks.of(block)));
        Blcks.THERMO_GENERATOR.getAll().forEach(block -> {
            registry.addWorkstations(HeatSourceCategory.ID, EntryStacks.of(block));
            registry.addWorkstations(CoolantCategory.ID, EntryStacks.of(block));
        });
        Blcks.REACTOR.getAll().forEach(block -> {
            registry.addWorkstations(SolidCoolantCategory.ID, EntryStacks.of(block));
            registry.addWorkstations(CoolantCategory.ID, EntryStacks.of(block));
        });
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(EnergizingRecipe.class, Recipes.ENERGIZING.get(), EnergizingDisplay::new);
        MagmatorDisplay.Maker.getBucketRecipes().forEach(recipe -> registry.add(new MagmatorDisplay(recipe)));
        CoolantDisplay.Maker.getBucketRecipes().forEach(recipe -> registry.add(new CoolantDisplay(recipe)));
        SolidCoolantDisplay.Maker.getBucketRecipes().forEach(recipe -> registry.add(new SolidCoolantDisplay(recipe)));
        HeatSourceDisplay.Maker.getBucketRecipes().forEach(recipe -> registry.add(new HeatSourceDisplay(recipe)));

        if (Powah.config().general.player_aerial_pearl)
            BuiltinClientPlugin.getInstance().registerInformation(EntryStacks.of(Itms.PLAYER_AERIAL_PEARL.get()), Component.empty(), l -> {
                l.add(Component.translatable("jei.powah.player_aerial_pearl"));
                return l;
            });
        if (Powah.config().general.dimensional_binding_card)
            BuiltinClientPlugin.getInstance().registerInformation(EntryStacks.of(Itms.BINDING_CARD_DIM.get()), Component.empty(), l -> {
                l.add(Component.translatable("jei.powah.binding_card_dim"));
                return l;
            });
        if (Powah.config().general.lens_of_ender)
            BuiltinClientPlugin.getInstance().registerInformation(EntryStacks.of(Itms.LENS_OF_ENDER.get()), Component.empty(), l -> {
                l.add(Component.translatable("jei.powah.lens_of_ender"));
                return l;
            });
    }

    @Override
    public void registerExclusionZones(ExclusionZones zones) {
        zones.register(AbstractContainerScreen.class, new GuiContainerHandler());
    }
}
