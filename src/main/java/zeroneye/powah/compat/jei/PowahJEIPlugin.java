package zeroneye.powah.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import zeroneye.powah.Powah;
import zeroneye.powah.block.IBlocks;
import zeroneye.powah.block.generator.furnator.Furnators;
import zeroneye.powah.block.generator.magmatic.MagmaticGenerators;
import zeroneye.powah.block.storage.EnergyCells;
import zeroneye.powah.config.Config;
import zeroneye.powah.item.IItems;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@JeiPlugin
public class PowahJEIPlugin implements IModPlugin {
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addIngredientInfo(Stream.of(EnergyCells.values())
                .map(EnergyCells::get)
                .map(ItemStack::new)
                .collect(Collectors.toList()), VanillaTypes.ITEM, I18n.format("wiki.powah.energy_cell"));
        registration.addIngredientInfo(Stream.of(MagmaticGenerators.values())
                .map(MagmaticGenerators::get)
                .map(ItemStack::new)
                .collect(Collectors.toList()), VanillaTypes.ITEM, I18n.format("wiki.powah.magmatic_generator"));
        registration.addIngredientInfo(Stream.of(Furnators.values())
                .map(Furnators::get)
                .map(ItemStack::new)
                .collect(Collectors.toList()), VanillaTypes.ITEM, I18n.format("wiki.powah.furnator"));

        registration.addIngredientInfo(new ItemStack(IBlocks.PLAYER_TRANSMITTER), VanillaTypes.ITEM, I18n.format("wiki.powah.player_transmitter"));
        registration.addIngredientInfo(new ItemStack(IBlocks.PLAYER_TRANSMITTER_DIM), VanillaTypes.ITEM, I18n.format("wiki.powah.player_transmitter_dim"));
        registration.addIngredientInfo(new ItemStack(IItems.BINDING_CARD), VanillaTypes.ITEM, I18n.format("wiki.powah.binding_card"));

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
