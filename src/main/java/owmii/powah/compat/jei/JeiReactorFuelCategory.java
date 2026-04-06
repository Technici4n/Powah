package owmii.powah.compat.jei;

import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.recipe.ReactorFuel;

public class JeiReactorFuelCategory extends AbstractCategory<JeiReactorFuelCategory.Recipe> {
    public static final IRecipeType<Recipe> TYPE = IRecipeType.create(Powah.MOD_ID, "reactor_fuel", Recipe.class);

    public JeiReactorFuelCategory(IGuiHelper guiHelper) {
        super(guiHelper, Blcks.URANINITE.get(), Component.translatable("gui.powah.jei.category.reactor.fuels"), 160, 24);
    }

    @Override
    public IRecipeType<Recipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Recipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 5).setStandardSlotBackground().add(recipe.input());
    }

    @Override
    public void draw(Recipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {

        var amount = Component.translatable("info.lollipop.amount")
                .append(": ")
                .append(Component.translatable("info.lollipop.mb", recipe.reactorFuel.fuelAmount()));

        var minecraft = Minecraft.getInstance();
        guiGraphics.text(minecraft.font, amount, 30, 3, 0xff444444, false);

        var coloredTemperature = Component.literal(String.valueOf(recipe.reactorFuel.temperature())).withStyle(ChatFormatting.DARK_RED);
        var temperatureText = Component.translatable("info.lollipop.temperature").append(": ")
                .append(Component.translatable("info.lollipop.temperature.c", coloredTemperature));
        guiGraphics.text(minecraft.font, temperatureText, 30, 15, 0xff444444, false);
    }

    public static List<Recipe> createRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        for (var entry : BuiltInRegistries.ITEM.getDataMap(ReactorFuel.DATA_MAP_TYPE).entrySet()) {
            var key = entry.getKey();
            var item = BuiltInRegistries.ITEM.getOptional(key).orElse(null);
            if (item != null) {
                recipes.add(new Recipe(item.getDefaultInstance(), key.identifier(), entry.getValue()));
            }
        }
        return recipes;
    }

    public record Recipe(ItemStack input, Identifier id, ReactorFuel reactorFuel) {
    }
}
