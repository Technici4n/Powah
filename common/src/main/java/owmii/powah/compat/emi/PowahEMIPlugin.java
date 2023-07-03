package owmii.powah.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Blocks;
import owmii.powah.Powah;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tier;
import owmii.powah.item.Itms;
import owmii.powah.recipe.Recipes;

public class PowahEMIPlugin implements EmiPlugin {
    public static final EmiRecipeCategory MAGMATOR_CATEGORY = new EmiRecipeCategory(new ResourceLocation(Powah.MOD_ID, "magmatic"),
            EmiStack.of(Blcks.MAGMATOR.get(Tier.BASIC))) {
        @Override
        public Component getName() {
            return Component.translatable("gui.powah.jei.category.magmatic");
        }
    };
    public static final EmiRecipeCategory COOLANT_CATEGORY = new EmiRecipeCategory(new ResourceLocation(Powah.MOD_ID, "coolants"),
            EmiStack.of(Items.WATER_BUCKET)) {
        @Override
        public Component getName() {
            return Component.translatable("gui.powah.jei.category.coolant");
        }
    };
    public static final EmiRecipeCategory SOLID_COOLANT_CATEGORY = new EmiRecipeCategory(new ResourceLocation(Powah.MOD_ID, "solid_coolants"),
            EmiStack.of(Blcks.DRY_ICE.get())) {
        @Override
        public Component getName() {
            return Component.translatable("gui.powah.jei.category.solid.coolant");
        }
    };
    public static final EmiRecipeCategory HEAT_SOURCE_CATEGORY = new EmiRecipeCategory(new ResourceLocation(Powah.MOD_ID, "heat_sources"),
            EmiStack.of(Blocks.MAGMA_BLOCK)) {
        @Override
        public Component getName() {
            return Component.translatable("gui.powah.jei.category.heat.sources");
        }
    };
    public static final EmiRecipeCategory ENERGIZING_CATEGORY = new EmiRecipeCategory(new ResourceLocation(Powah.MOD_ID, "energizing"),
            EmiStack.of(Blcks.ENERGIZING_ORB.get())) {
        @Override
        public Component getName() {
            return Component.translatable("gui.powah.jei.category.energizing");
        }
    };

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(MAGMATOR_CATEGORY);
        registry.addCategory(COOLANT_CATEGORY);
        registry.addCategory(SOLID_COOLANT_CATEGORY);
        registry.addCategory(HEAT_SOURCE_CATEGORY);
        registry.addCategory(ENERGIZING_CATEGORY);

        registry.addWorkstation(ENERGIZING_CATEGORY, EmiStack.of(Blcks.ENERGIZING_ORB.get()));
        Blcks.ENERGIZING_ROD.getAll().forEach(block -> registry.addWorkstation(ENERGIZING_CATEGORY, EmiStack.of(block)));
        Blcks.MAGMATOR.getAll().forEach(block -> registry.addWorkstation(MAGMATOR_CATEGORY, EmiStack.of(block)));
        Blcks.THERMO_GENERATOR.getAll().forEach(block -> {
            registry.addWorkstation(HEAT_SOURCE_CATEGORY, EmiStack.of(block));
            registry.addWorkstation(COOLANT_CATEGORY, EmiStack.of(block));
        });
        Blcks.REACTOR.getAll().forEach(block -> {
            registry.addWorkstation(SOLID_COOLANT_CATEGORY, EmiStack.of(block));
            registry.addWorkstation(COOLANT_CATEGORY, EmiStack.of(block));
        });

        RecipeManager manager = registry.getRecipeManager();
        manager.getAllRecipesFor(Recipes.ENERGIZING.get()).forEach(r -> registry.addRecipe(new EnergizingEmiRecipe(r)));

        BuiltInRegistries.FLUID.entrySet().forEach(f -> {
            var fluidId = f.getKey().location();
            var magmatic_heat = PowahAPI.MAGMATIC_FLUIDS.get(fluidId);
            if (magmatic_heat != null) {
                registry.addRecipe(new MagmatorEmiRecipe(f.getValue(), magmatic_heat));
            }

            var coldness = PowahAPI.COOLANT_FLUIDS.get(fluidId);
            if (coldness != null) {
                registry.addRecipe(new CoolantEmiRecipe(f.getValue(), coldness));
            }

            var heat_source = PowahAPI.HEAT_SOURCES.getOrDefault(fluidId, 0);
            if (heat_source != 0) {
                registry.addRecipe(new HeatSourceEmiRecipe(EmiStack.of(f.getValue()), heat_source));
            }
        });

        BuiltInRegistries.ITEM.entrySet().forEach(i -> {
            var id = i.getKey().location();
            var coolantInfo = PowahAPI.SOLID_COOLANTS.get(id);
            if (coolantInfo != null) {
                registry.addRecipe(new SolidCoolantEmiRecipe(i.getValue(), coolantInfo.getLeft(), coolantInfo.getRight()));
            }

            if (i.getValue() instanceof BlockItem blockItem) {
                var block = blockItem.getBlock();
                var blockId = BuiltInRegistries.BLOCK.getKey(block);
                var heat = PowahAPI.HEAT_SOURCES.getOrDefault(blockId, 0);
                if (heat != 0) {
                    registry.addRecipe(new HeatSourceEmiRecipe(EmiStack.of(blockItem), heat));
                }
            }
        });

        if (Powah.config().general.player_aerial_pearl)
            registry.addRecipe(new EmiInfoRecipe(List.of(EmiStack.of(Itms.PLAYER_AERIAL_PEARL.get())),
                    List.of(Component.translatable("jei.powah.player_aerial_pearl")), null));
        if (Powah.config().general.dimensional_binding_card)
            registry.addRecipe(new EmiInfoRecipe(List.of(EmiStack.of(Itms.BINDING_CARD_DIM.get())),
                    List.of(Component.translatable("jei.powah.binding_card_dim")), null));
        if (Powah.config().general.lens_of_ender)
            registry.addRecipe(new EmiInfoRecipe(List.of(EmiStack.of(Itms.LENS_OF_ENDER.get())),
                    List.of(Component.translatable("jei.powah.lens_of_ender")), null));

    }
}
