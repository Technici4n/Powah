package owmii.powah.data;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tier;
import owmii.powah.item.Itms;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    public RecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        battery(output);

        tools(output);

        energyCable(output);

        capacitors(output);

        materials(output);

        enderCell(output);

        enderGate(output);

        energizingOrb(output);

        energizingRod(output);

        energyCell(output);

        energyDischarger(output);

        energyHopper(output);

        furnator(output);

        magmator(output);

        playerTransmitter(output);

        reactor(output);

        solarPanel(output);

        thermoGenerator(output);

    }

    private static void tools(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.BINDING_CARD)
                .pattern("br")
                .pattern("rf")
                .define('b', Itms.BLANK_CARD)
                .define('r', Items.REDSTONE)
                .define('f', Items.ROTTEN_FLESH)
                .unlockedBy(getHasName(Itms.BLANK_CARD), has(Itms.BLANK_CARD))
                .save(output, Powah.id("binding_card"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.BLANK_CARD)
                .pattern("p")
                .pattern("p")
                .define('p', Itms.DIELECTRIC_ROD_HORIZONTAL)
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD_HORIZONTAL), has(Itms.DIELECTRIC_ROD_HORIZONTAL))
                .save(output, Powah.id("blank_card"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.BLANK_CARD)
                .pattern("pp")
                .define('p', Itms.DIELECTRIC_ROD)
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD), has(Itms.DIELECTRIC_ROD))
                .save(output, Powah.id("blank_card_2"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Itms.BOOK)
                .requires(Itms.DIELECTRIC_PASTE)
                .requires(Items.BOOK)
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Items.BOOK), has(Items.BOOK))
                .save(output, Powah.id("manual"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.WRENCH)
                .pattern(" ip")
                .pattern(" pi")
                .pattern("p  ")
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('i', Items.IRON_INGOT)
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .save(output, Powah.id("wrench"));
    }

    private static void solarPanel(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.SOLAR_PANEL.get(Tier.BASIC))
                .pattern("ppp")
                .pattern("cdc")
                .pattern("iii")
                .define('c', Itms.CAPACITOR_BASIC)
                .define('i', Items.IRON_INGOT)
                .define('p', Blcks.SOLAR_PANEL.get(Tier.STARTER))
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC), has(Itms.CAPACITOR_BASIC))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .unlockedBy(getHasName(Blcks.SOLAR_PANEL.get(Tier.STARTER)), has(Blcks.SOLAR_PANEL.get(Tier.STARTER)))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("solar_panel_basic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.SOLAR_PANEL.get(Tier.BLAZING))
                .pattern("ppp")
                .pattern("cdc")
                .pattern("bbb")
                .define('c', Itms.CAPACITOR_BLAZING)
                .define('b', Itms.BLAZING_CRYSTAL)
                .define('p', Blcks.SOLAR_PANEL.get(Tier.HARDENED))
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_BLAZING), has(Itms.CAPACITOR_BLAZING))
                .unlockedBy(getHasName(Itms.BLAZING_CRYSTAL), has(Itms.BLAZING_CRYSTAL))
                .unlockedBy(getHasName(Blcks.SOLAR_PANEL.get(Tier.HARDENED)), has(Blcks.SOLAR_PANEL.get(Tier.HARDENED)))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("solar_panel_blazing"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.SOLAR_PANEL.get(Tier.HARDENED))
                .pattern("ppp")
                .pattern("cdc")
                .pattern("ggg")
                .define('c', Itms.CAPACITOR_HARDENED)
                .define('g', Itms.ENERGIZED_STEEL)
                .define('p', Blcks.SOLAR_PANEL.get(Tier.BASIC))
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_HARDENED), has(Itms.CAPACITOR_HARDENED))
                .unlockedBy(getHasName(Itms.ENERGIZED_STEEL), has(Itms.ENERGIZED_STEEL))
                .unlockedBy(getHasName(Blcks.SOLAR_PANEL.get(Tier.BASIC)), has(Blcks.SOLAR_PANEL.get(Tier.BASIC)))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("solar_panel_hardened"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.SOLAR_PANEL.get(Tier.NIOTIC))
                .pattern("ppp")
                .pattern("cdc")
                .pattern("bbb")
                .define('c', Itms.CAPACITOR_NIOTIC)
                .define('b', Itms.NIOTIC_CRYSTAL)
                .define('p', Blcks.SOLAR_PANEL.get(Tier.BLAZING))
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_NIOTIC), has(Itms.CAPACITOR_NIOTIC))
                .unlockedBy(getHasName(Itms.NIOTIC_CRYSTAL), has(Itms.NIOTIC_CRYSTAL))
                .unlockedBy(getHasName(Blcks.SOLAR_PANEL.get(Tier.BLAZING)), has(Blcks.SOLAR_PANEL.get(Tier.BLAZING)))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("solar_panel_niotic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.SOLAR_PANEL.get(Tier.NITRO))
                .pattern("ppp")
                .pattern("cdc")
                .pattern("eee")
                .define('c', Itms.CAPACITOR_NITRO)
                .define('e', Itms.NITRO_CRYSTAL)
                .define('p', Blcks.SOLAR_PANEL.get(Tier.SPIRITED))
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_NITRO), has(Itms.CAPACITOR_NITRO))
                .unlockedBy(getHasName(Itms.NITRO_CRYSTAL), has(Itms.NITRO_CRYSTAL))
                .unlockedBy(getHasName(Blcks.SOLAR_PANEL.get(Tier.SPIRITED)), has(Blcks.SOLAR_PANEL.get(Tier.SPIRITED)))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("solar_panel_nitro"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.SOLAR_PANEL.get(Tier.SPIRITED))
                .pattern("ppp")
                .pattern("cdc")
                .pattern("eee")
                .define('c', Itms.CAPACITOR_SPIRITED)
                .define('e', Itms.SPIRITED_CRYSTAL)
                .define('p', Blcks.SOLAR_PANEL.get(Tier.NIOTIC))
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_SPIRITED), has(Itms.CAPACITOR_SPIRITED))
                .unlockedBy(getHasName(Itms.SPIRITED_CRYSTAL), has(Itms.SPIRITED_CRYSTAL))
                .unlockedBy(getHasName(Blcks.SOLAR_PANEL.get(Tier.NIOTIC)), has(Blcks.SOLAR_PANEL.get(Tier.NIOTIC)))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("solar_panel_spirited"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.SOLAR_PANEL.get(Tier.STARTER))
                .pattern("ppp")
                .pattern("cdc")
                .pattern("eee")
                .define('c', Itms.CAPACITOR_BASIC_TINY)
                .define('e', Itms.DIELECTRIC_PASTE)
                .define('p', Itms.PHOTOELECTRIC_PANE)
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC_TINY), has(Itms.CAPACITOR_BASIC_TINY))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.PHOTOELECTRIC_PANE), has(Itms.PHOTOELECTRIC_PANE))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("solar_panel_starter"));
    }

    private static void reactor(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.REACTOR.get(Tier.BASIC), 4)
                .pattern("rlr")
                .pattern("lul")
                .pattern("rlr")
                .define('u', Itms.URANINITE)
                .define('l', Itms.CAPACITOR_BASIC_LARGE)
                .define('r', Blcks.REACTOR.get(Tier.STARTER))
                .unlockedBy(getHasName(Itms.URANINITE), has(Itms.URANINITE))
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC_LARGE), has(Itms.CAPACITOR_BASIC_LARGE))
                .unlockedBy(getHasName(Blcks.REACTOR.get(Tier.STARTER)), has(Blcks.REACTOR.get(Tier.STARTER)))
                .save(output, Powah.id("reactor_basic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.REACTOR.get(Tier.BLAZING), 4)
                .pattern("rlr")
                .pattern("lul")
                .pattern("rlr")
                .define('u', Itms.URANINITE)
                .define('l', Itms.CAPACITOR_BLAZING)
                .define('r', Blcks.REACTOR.get(Tier.HARDENED))
                .unlockedBy(getHasName(Itms.URANINITE), has(Itms.URANINITE))
                .unlockedBy(getHasName(Itms.CAPACITOR_BLAZING), has(Itms.CAPACITOR_BLAZING))
                .unlockedBy(getHasName(Blcks.REACTOR.get(Tier.HARDENED)), has(Blcks.REACTOR.get(Tier.HARDENED)))
                .save(output, Powah.id("reactor_blazing"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.REACTOR.get(Tier.HARDENED), 4)
                .pattern("rlr")
                .pattern("lul")
                .pattern("rlr")
                .define('u', Itms.URANINITE)
                .define('l', Itms.CAPACITOR_HARDENED)
                .define('r', Blcks.REACTOR.get(Tier.BASIC))
                .unlockedBy(getHasName(Itms.URANINITE), has(Itms.URANINITE))
                .unlockedBy(getHasName(Itms.CAPACITOR_HARDENED), has(Itms.CAPACITOR_HARDENED))
                .unlockedBy(getHasName(Blcks.REACTOR.get(Tier.BASIC)), has(Blcks.REACTOR.get(Tier.BASIC)))
                .save(output, Powah.id("reactor_hardened"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.REACTOR.get(Tier.NIOTIC), 4)
                .pattern("rlr")
                .pattern("lul")
                .pattern("rlr")
                .define('u', Itms.URANINITE)
                .define('l', Itms.CAPACITOR_NIOTIC)
                .define('r', Blcks.REACTOR.get(Tier.BLAZING))
                .unlockedBy(getHasName(Itms.URANINITE), has(Itms.URANINITE))
                .unlockedBy(getHasName(Itms.CAPACITOR_NIOTIC), has(Itms.CAPACITOR_NIOTIC))
                .unlockedBy(getHasName(Blcks.REACTOR.get(Tier.BLAZING)), has(Blcks.REACTOR.get(Tier.BLAZING)))
                .save(output, Powah.id("reactor_niotic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.REACTOR.get(Tier.NITRO), 4)
                .pattern("rlr")
                .pattern("lul")
                .pattern("rlr")
                .define('u', Itms.URANINITE)
                .define('l', Itms.CAPACITOR_NITRO)
                .define('r', Blcks.REACTOR.get(Tier.SPIRITED))
                .unlockedBy(getHasName(Itms.URANINITE), has(Itms.URANINITE))
                .unlockedBy(getHasName(Itms.CAPACITOR_NITRO), has(Itms.CAPACITOR_NITRO))
                .unlockedBy(getHasName(Blcks.REACTOR.get(Tier.SPIRITED)), has(Blcks.REACTOR.get(Tier.SPIRITED)))
                .save(output, Powah.id("reactor_nitro"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.REACTOR.get(Tier.SPIRITED), 4)
                .pattern("rlr")
                .pattern("lul")
                .pattern("rlr")
                .define('u', Itms.URANINITE)
                .define('l', Itms.CAPACITOR_SPIRITED)
                .define('r', Blcks.REACTOR.get(Tier.NIOTIC))
                .unlockedBy(getHasName(Itms.URANINITE), has(Itms.URANINITE))
                .unlockedBy(getHasName(Itms.CAPACITOR_SPIRITED), has(Itms.CAPACITOR_SPIRITED))
                .unlockedBy(getHasName(Blcks.REACTOR.get(Tier.NIOTIC)), has(Blcks.REACTOR.get(Tier.NIOTIC)))
                .save(output, Powah.id("reactor_spirited"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.REACTOR.get(Tier.STARTER), 4)
                .pattern("ulu")
                .pattern("lcl")
                .pattern("ulu")
                .define('u', Itms.URANINITE)
                .define('l', Itms.CAPACITOR_BASIC_TINY)
                .define('c', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.URANINITE), has(Itms.URANINITE))
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC_TINY), has(Itms.CAPACITOR_BASIC_TINY))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("reactor_starter"));
    }

    private static void playerTransmitter(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.PLAYER_TRANSMITTER.get(Tier.BASIC))
                .pattern(" p ")
                .pattern("ici")
                .pattern(" r ")
                .define('p', Blcks.PLAYER_TRANSMITTER.get(Tier.STARTER))
                .define('i', Itms.CAPACITOR_BASIC)
                .define('c', Itms.DIELECTRIC_CASING)
                .define('r', Itms.DIELECTRIC_ROD)
                .unlockedBy(getHasName(Blcks.PLAYER_TRANSMITTER.get(Tier.STARTER)), has(Blcks.PLAYER_TRANSMITTER.get(Tier.STARTER)))
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC), has(Itms.CAPACITOR_BASIC))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD), has(Itms.DIELECTRIC_ROD))
                .save(output, Powah.id("player_tranmitter_basic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.PLAYER_TRANSMITTER.get(Tier.BLAZING))
                .pattern(" p ")
                .pattern("ici")
                .pattern(" r ")
                .define('p', Blcks.PLAYER_TRANSMITTER.get(Tier.HARDENED))
                .define('i', Itms.CAPACITOR_BLAZING)
                .define('c', Itms.DIELECTRIC_CASING)
                .define('r', Itms.DIELECTRIC_ROD)
                .unlockedBy(getHasName(Blcks.PLAYER_TRANSMITTER.get(Tier.HARDENED)), has(Blcks.PLAYER_TRANSMITTER.get(Tier.HARDENED)))
                .unlockedBy(getHasName(Itms.CAPACITOR_BLAZING), has(Itms.CAPACITOR_BLAZING))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD), has(Itms.DIELECTRIC_ROD))
                .save(output, Powah.id("player_tranmitter_blazing"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.PLAYER_TRANSMITTER.get(Tier.HARDENED))
                .pattern(" p ")
                .pattern("ici")
                .pattern(" r ")
                .define('p', Blcks.PLAYER_TRANSMITTER.get(Tier.BASIC))
                .define('i', Itms.CAPACITOR_HARDENED)
                .define('c', Itms.DIELECTRIC_CASING)
                .define('r', Itms.DIELECTRIC_ROD)
                .unlockedBy(getHasName(Blcks.PLAYER_TRANSMITTER.get(Tier.BASIC)), has(Blcks.PLAYER_TRANSMITTER.get(Tier.BASIC)))
                .unlockedBy(getHasName(Itms.CAPACITOR_HARDENED), has(Itms.CAPACITOR_HARDENED))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD), has(Itms.DIELECTRIC_ROD))
                .save(output, Powah.id("player_tranmitter_hardened"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.PLAYER_TRANSMITTER.get(Tier.NIOTIC))
                .pattern(" p ")
                .pattern("ici")
                .pattern(" r ")
                .define('p', Blcks.PLAYER_TRANSMITTER.get(Tier.BLAZING))
                .define('i', Itms.CAPACITOR_NIOTIC)
                .define('c', Itms.DIELECTRIC_CASING)
                .define('r', Itms.DIELECTRIC_ROD)
                .unlockedBy(getHasName(Blcks.PLAYER_TRANSMITTER.get(Tier.BLAZING)), has(Blcks.PLAYER_TRANSMITTER.get(Tier.BLAZING)))
                .unlockedBy(getHasName(Itms.CAPACITOR_NIOTIC), has(Itms.CAPACITOR_NIOTIC))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD), has(Itms.DIELECTRIC_ROD))
                .save(output, Powah.id("player_tranmitter_niotic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.PLAYER_TRANSMITTER.get(Tier.NITRO))
                .pattern(" p ")
                .pattern("ici")
                .pattern(" r ")
                .define('p', Blcks.PLAYER_TRANSMITTER.get(Tier.SPIRITED))
                .define('i', Itms.CAPACITOR_NITRO)
                .define('c', Itms.DIELECTRIC_CASING)
                .define('r', Itms.DIELECTRIC_ROD)
                .unlockedBy(getHasName(Blcks.PLAYER_TRANSMITTER.get(Tier.SPIRITED)), has(Blcks.PLAYER_TRANSMITTER.get(Tier.SPIRITED)))
                .unlockedBy(getHasName(Itms.CAPACITOR_NITRO), has(Itms.CAPACITOR_NITRO))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD), has(Itms.DIELECTRIC_ROD))
                .save(output, Powah.id("player_tranmitter_nitro"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.PLAYER_TRANSMITTER.get(Tier.SPIRITED))
                .pattern(" p ")
                .pattern("ici")
                .pattern(" r ")
                .define('p', Blcks.PLAYER_TRANSMITTER.get(Tier.NIOTIC))
                .define('i', Itms.CAPACITOR_SPIRITED)
                .define('c', Itms.DIELECTRIC_CASING)
                .define('r', Itms.DIELECTRIC_ROD)
                .unlockedBy(getHasName(Blcks.PLAYER_TRANSMITTER.get(Tier.NIOTIC)), has(Blcks.PLAYER_TRANSMITTER.get(Tier.NIOTIC)))
                .unlockedBy(getHasName(Itms.CAPACITOR_SPIRITED), has(Itms.CAPACITOR_SPIRITED))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD), has(Itms.DIELECTRIC_ROD))
                .save(output, Powah.id("player_tranmitter_spirited"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.PLAYER_TRANSMITTER.get(Tier.STARTER))
                .pattern(" p ")
                .pattern("ici")
                .pattern(" r ")
                .define('p', Itms.PLAYER_AERIAL_PEARL)
                .define('i', Itms.CAPACITOR_BASIC_TINY)
                .define('c', Itms.DIELECTRIC_CASING)
                .define('r', Itms.DIELECTRIC_ROD)
                .unlockedBy(getHasName(Itms.PLAYER_AERIAL_PEARL), has(Itms.PLAYER_AERIAL_PEARL))
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC_TINY), has(Itms.CAPACITOR_BASIC_TINY))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD), has(Itms.DIELECTRIC_ROD))
                .save(output, Powah.id("player_tranmitter_starter"));
    }

    private static void magmator(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.MAGMATOR.get(Tier.BASIC))
                .pattern("iii")
                .pattern("cdc")
                .pattern("ifi")
                .define('c', Itms.CAPACITOR_BASIC)
                .define('i', Items.IRON_INGOT)
                .define('f', Blcks.MAGMATOR.get(Tier.STARTER))
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC), has(Itms.CAPACITOR_BASIC))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .unlockedBy(getHasName(Blcks.MAGMATOR.get(Tier.STARTER)), has(Blcks.MAGMATOR.get(Tier.STARTER)))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("magmator_basic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.MAGMATOR.get(Tier.BLAZING))
                .pattern("bbb")
                .pattern("cdc")
                .pattern("bfb")
                .define('c', Itms.CAPACITOR_BLAZING)
                .define('b', Itms.BLAZING_CRYSTAL)
                .define('f', Blcks.MAGMATOR.get(Tier.HARDENED))
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_BLAZING), has(Itms.CAPACITOR_BLAZING))
                .unlockedBy(getHasName(Itms.BLAZING_CRYSTAL), has(Itms.BLAZING_CRYSTAL))
                .unlockedBy(getHasName(Blcks.MAGMATOR.get(Tier.HARDENED)), has(Blcks.MAGMATOR.get(Tier.HARDENED)))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("magmator_blazing"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.MAGMATOR.get(Tier.HARDENED))
                .pattern("ggg")
                .pattern("cdc")
                .pattern("gfg")
                .define('c', Itms.CAPACITOR_HARDENED)
                .define('g', Itms.ENERGIZED_STEEL)
                .define('f', Blcks.MAGMATOR.get(Tier.BASIC))
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_HARDENED), has(Itms.CAPACITOR_HARDENED))
                .unlockedBy(getHasName(Itms.ENERGIZED_STEEL), has(Itms.ENERGIZED_STEEL))
                .unlockedBy(getHasName(Blcks.MAGMATOR.get(Tier.BASIC)), has(Blcks.MAGMATOR.get(Tier.BASIC)))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("magmator_hardened"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.MAGMATOR.get(Tier.NIOTIC))
                .pattern("ggg")
                .pattern("cdc")
                .pattern("gfg")
                .define('c', Itms.CAPACITOR_NIOTIC)
                .define('g', Itms.NIOTIC_CRYSTAL)
                .define('f', Blcks.MAGMATOR.get(Tier.BLAZING))
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_NIOTIC), has(Itms.CAPACITOR_NIOTIC))
                .unlockedBy(getHasName(Itms.NIOTIC_CRYSTAL), has(Itms.NIOTIC_CRYSTAL))
                .unlockedBy(getHasName(Blcks.MAGMATOR.get(Tier.BLAZING)), has(Blcks.MAGMATOR.get(Tier.BLAZING)))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("magmator_niotic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.MAGMATOR.get(Tier.NITRO))
                .pattern("ggg")
                .pattern("cdc")
                .pattern("gfg")
                .define('c', Itms.CAPACITOR_NITRO)
                .define('g', Itms.NITRO_CRYSTAL)
                .define('f', Blcks.MAGMATOR.get(Tier.SPIRITED))
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_NITRO), has(Itms.CAPACITOR_NITRO))
                .unlockedBy(getHasName(Itms.NITRO_CRYSTAL), has(Itms.NITRO_CRYSTAL))
                .unlockedBy(getHasName(Blcks.MAGMATOR.get(Tier.SPIRITED)), has(Blcks.MAGMATOR.get(Tier.SPIRITED)))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("magmator_nitro"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.MAGMATOR.get(Tier.SPIRITED))
                .pattern("ggg")
                .pattern("cdc")
                .pattern("gfg")
                .define('c', Itms.CAPACITOR_SPIRITED)
                .define('g', Itms.SPIRITED_CRYSTAL)
                .define('f', Blcks.MAGMATOR.get(Tier.NIOTIC))
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_SPIRITED), has(Itms.CAPACITOR_SPIRITED))
                .unlockedBy(getHasName(Itms.SPIRITED_CRYSTAL), has(Itms.SPIRITED_CRYSTAL))
                .unlockedBy(getHasName(Blcks.MAGMATOR.get(Tier.NIOTIC)), has(Blcks.MAGMATOR.get(Tier.NIOTIC)))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("magmator_spirited"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.MAGMATOR.get(Tier.STARTER))
                .pattern("ggg")
                .pattern("cdc")
                .pattern("gfg")
                .define('c', Itms.CAPACITOR_BASIC_TINY)
                .define('g', Itms.DIELECTRIC_PASTE)
                .define('f', Items.BUCKET)
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC_TINY), has(Itms.CAPACITOR_BASIC_TINY))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Items.BUCKET), has(Items.BUCKET))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("magmator_starter"));
    }

    private static void furnator(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.FURNATOR.get(Tier.BASIC))
                .pattern("iii")
                .pattern("cdc")
                .pattern("ifi")
                .define('c', Itms.CAPACITOR_BASIC)
                .define('i', Items.IRON_INGOT)
                .define('f', Blcks.FURNATOR.get(Tier.STARTER))
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC), has(Itms.CAPACITOR_BASIC))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .unlockedBy(getHasName(Blcks.FURNATOR.get(Tier.STARTER)), has(Blcks.FURNATOR.get(Tier.STARTER)))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("furnator_basic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.FURNATOR.get(Tier.BLAZING))
                .pattern("bbb")
                .pattern("cdc")
                .pattern("bfb")
                .define('c', Itms.CAPACITOR_BLAZING)
                .define('b', Itms.BLAZING_CRYSTAL)
                .define('f', Blcks.FURNATOR.get(Tier.HARDENED))
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_BLAZING), has(Itms.CAPACITOR_BLAZING))
                .unlockedBy(getHasName(Itms.BLAZING_CRYSTAL), has(Itms.BLAZING_CRYSTAL))
                .unlockedBy(getHasName(Blcks.FURNATOR.get(Tier.HARDENED)), has(Blcks.FURNATOR.get(Tier.HARDENED)))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("furnator_blazing"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.FURNATOR.get(Tier.HARDENED))
                .pattern("ggg")
                .pattern("cdc")
                .pattern("gfg")
                .define('c', Itms.CAPACITOR_HARDENED)
                .define('g', Itms.ENERGIZED_STEEL)
                .define('f', Blcks.FURNATOR.get(Tier.BASIC))
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_HARDENED), has(Itms.CAPACITOR_HARDENED))
                .unlockedBy(getHasName(Itms.ENERGIZED_STEEL), has(Itms.ENERGIZED_STEEL))
                .unlockedBy(getHasName(Blcks.FURNATOR.get(Tier.BASIC)), has(Blcks.FURNATOR.get(Tier.BASIC)))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("furnator_hardened"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.FURNATOR.get(Tier.NIOTIC))
                .pattern("ggg")
                .pattern("cdc")
                .pattern("gfg")
                .define('c', Itms.CAPACITOR_NIOTIC)
                .define('g', Itms.NIOTIC_CRYSTAL)
                .define('f', Blcks.FURNATOR.get(Tier.BLAZING))
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_NIOTIC), has(Itms.CAPACITOR_NIOTIC))
                .unlockedBy(getHasName(Itms.NIOTIC_CRYSTAL), has(Itms.NIOTIC_CRYSTAL))
                .unlockedBy(getHasName(Blcks.FURNATOR.get(Tier.BLAZING)), has(Blcks.FURNATOR.get(Tier.BLAZING)))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("furnator_niotic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.FURNATOR.get(Tier.NITRO))
                .pattern("ggg")
                .pattern("cdc")
                .pattern("gfg")
                .define('c', Itms.CAPACITOR_NITRO)
                .define('g', Itms.NITRO_CRYSTAL)
                .define('f', Blcks.FURNATOR.get(Tier.SPIRITED))
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_NITRO), has(Itms.CAPACITOR_NITRO))
                .unlockedBy(getHasName(Itms.NITRO_CRYSTAL), has(Itms.NITRO_CRYSTAL))
                .unlockedBy(getHasName(Blcks.FURNATOR.get(Tier.SPIRITED)), has(Blcks.FURNATOR.get(Tier.SPIRITED)))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("furnator_nitro"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.FURNATOR.get(Tier.SPIRITED))
                .pattern("ggg")
                .pattern("cdc")
                .pattern("gfg")
                .define('c', Itms.CAPACITOR_SPIRITED)
                .define('g', Itms.SPIRITED_CRYSTAL)
                .define('f', Blcks.FURNATOR.get(Tier.NIOTIC))
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_SPIRITED), has(Itms.CAPACITOR_SPIRITED))
                .unlockedBy(getHasName(Itms.SPIRITED_CRYSTAL), has(Itms.SPIRITED_CRYSTAL))
                .unlockedBy(getHasName(Blcks.FURNATOR.get(Tier.NIOTIC)), has(Blcks.FURNATOR.get(Tier.NIOTIC)))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("furnator_spirited"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.FURNATOR.get(Tier.STARTER))
                .pattern("ggg")
                .pattern("cdc")
                .pattern("gfg")
                .define('c', Itms.CAPACITOR_BASIC_TINY)
                .define('g', Itms.DIELECTRIC_PASTE)
                .define('f', Items.FURNACE)
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC_TINY), has(Itms.CAPACITOR_BASIC_TINY))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Items.FURNACE), has(Items.FURNACE))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("furnator_starter"));
    }

    private static void energyHopper(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_HOPPER.get(Tier.BASIC))
                .pattern("ppp")
                .pattern("cdc")
                .pattern("php")
                .define('c', Itms.CAPACITOR_BASIC)
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('d', Itms.DIELECTRIC_CASING)
                .define('h', Blcks.ENERGY_HOPPER.get(Tier.STARTER))
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC), has(Itms.CAPACITOR_BASIC))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .unlockedBy(getHasName(Blcks.ENERGY_HOPPER.get(Tier.STARTER)), has(Blcks.ENERGY_HOPPER.get(Tier.STARTER)))
                .save(output, Powah.id("energy_hopper_basic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_HOPPER.get(Tier.BLAZING))
                .pattern("ppp")
                .pattern("cdc")
                .pattern("php")
                .define('c', Itms.CAPACITOR_BLAZING)
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('d', Itms.DIELECTRIC_CASING)
                .define('h', Blcks.ENERGY_HOPPER.get(Tier.HARDENED))
                .unlockedBy(getHasName(Itms.CAPACITOR_BLAZING), has(Itms.CAPACITOR_BLAZING))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .unlockedBy(getHasName(Blcks.ENERGY_HOPPER.get(Tier.HARDENED)), has(Blcks.ENERGY_HOPPER.get(Tier.HARDENED)))
                .save(output, Powah.id("energy_hopper_blazing"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_HOPPER.get(Tier.HARDENED))
                .pattern("ppp")
                .pattern("cdc")
                .pattern("php")
                .define('c', Itms.CAPACITOR_HARDENED)
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('d', Itms.DIELECTRIC_CASING)
                .define('h', Blcks.ENERGY_HOPPER.get(Tier.BASIC))
                .unlockedBy(getHasName(Itms.CAPACITOR_HARDENED), has(Itms.CAPACITOR_HARDENED))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .unlockedBy(getHasName(Blcks.ENERGY_HOPPER.get(Tier.BASIC)), has(Blcks.ENERGY_HOPPER.get(Tier.BASIC)))
                .save(output, Powah.id("energy_hopper_hardened"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_HOPPER.get(Tier.NIOTIC))
                .pattern("ppp")
                .pattern("cdc")
                .pattern("php")
                .define('c', Itms.CAPACITOR_NIOTIC)
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('d', Itms.DIELECTRIC_CASING)
                .define('h', Blcks.ENERGY_HOPPER.get(Tier.BLAZING))
                .unlockedBy(getHasName(Itms.CAPACITOR_NIOTIC), has(Itms.CAPACITOR_NIOTIC))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .unlockedBy(getHasName(Blcks.ENERGY_HOPPER.get(Tier.BLAZING)), has(Blcks.ENERGY_HOPPER.get(Tier.BLAZING)))
                .save(output, Powah.id("energy_hopper_niotic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_HOPPER.get(Tier.NITRO))
                .pattern("ppp")
                .pattern("cdc")
                .pattern("php")
                .define('c', Itms.CAPACITOR_NITRO)
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('d', Itms.DIELECTRIC_CASING)
                .define('h', Blcks.ENERGY_HOPPER.get(Tier.SPIRITED))
                .unlockedBy(getHasName(Itms.CAPACITOR_NITRO), has(Itms.CAPACITOR_NITRO))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .unlockedBy(getHasName(Blcks.ENERGY_HOPPER.get(Tier.SPIRITED)), has(Blcks.ENERGY_HOPPER.get(Tier.SPIRITED)))
                .save(output, Powah.id("energy_hopper_nitro"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_HOPPER.get(Tier.SPIRITED))
                .pattern("ppp")
                .pattern("cdc")
                .pattern("php")
                .define('c', Itms.CAPACITOR_SPIRITED)
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('d', Itms.DIELECTRIC_CASING)
                .define('h', Blcks.ENERGY_HOPPER.get(Tier.NIOTIC))
                .unlockedBy(getHasName(Itms.CAPACITOR_SPIRITED), has(Itms.CAPACITOR_SPIRITED))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .unlockedBy(getHasName(Blcks.ENERGY_HOPPER.get(Tier.NIOTIC)), has(Blcks.ENERGY_HOPPER.get(Tier.NIOTIC)))
                .save(output, Powah.id("energy_hopper_spirited"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_HOPPER.get(Tier.STARTER))
                .pattern("ppp")
                .pattern("cdc")
                .pattern("php")
                .define('c', Itms.CAPACITOR_BASIC_TINY)
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('d', Itms.DIELECTRIC_CASING)
                .define('h', Items.HOPPER)
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC_TINY), has(Itms.CAPACITOR_BASIC_TINY))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .unlockedBy(getHasName(Items.HOPPER), has(Items.HOPPER))
                .save(output, Powah.id("energy_hopper_starter"));
    }

    private static void energyDischarger(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_DISCHARGER.get(Tier.BASIC))
                .pattern("pcp")
                .pattern("pdp")
                .pattern("pcp")
                .define('c', Itms.CAPACITOR_BASIC)
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC), has(Itms.CAPACITOR_BASIC))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energy_discharger_basic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_DISCHARGER.get(Tier.BLAZING))
                .pattern("pcp")
                .pattern("pdp")
                .pattern("pcp")
                .define('c', Itms.CAPACITOR_BLAZING)
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_BLAZING), has(Itms.CAPACITOR_BLAZING))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energy_discharger_blazing"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_DISCHARGER.get(Tier.HARDENED))
                .pattern("pcp")
                .pattern("pdp")
                .pattern("pcp")
                .define('c', Itms.CAPACITOR_HARDENED)
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_HARDENED), has(Itms.CAPACITOR_HARDENED))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energy_discharger_hardened"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_DISCHARGER.get(Tier.NIOTIC))
                .pattern("pcp")
                .pattern("pdp")
                .pattern("pcp")
                .define('c', Itms.CAPACITOR_NIOTIC)
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_NIOTIC), has(Itms.CAPACITOR_NIOTIC))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energy_discharger_niotic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_DISCHARGER.get(Tier.NITRO))
                .pattern("pcp")
                .pattern("pdp")
                .pattern("pcp")
                .define('c', Itms.CAPACITOR_NITRO)
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_NITRO), has(Itms.CAPACITOR_NITRO))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energy_discharger_nitro"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_DISCHARGER.get(Tier.SPIRITED))
                .pattern("pcp")
                .pattern("pdp")
                .pattern("pcp")
                .define('c', Itms.CAPACITOR_SPIRITED)
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_SPIRITED), has(Itms.CAPACITOR_SPIRITED))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energy_discharger_spirited"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_DISCHARGER.get(Tier.STARTER))
                .pattern("pcp")
                .pattern("pdp")
                .pattern("pcp")
                .define('c', Itms.CAPACITOR_BASIC_TINY)
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC_TINY), has(Itms.CAPACITOR_BASIC_TINY))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energy_discharger_starter"));
    }

    private static void energyCell(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CELL.get(Tier.BASIC))
                .pattern("ici")
                .pattern("cdc")
                .pattern("ici")
                .define('c', Itms.CAPACITOR_BASIC)
                .define('i', Items.IRON_INGOT)
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC), has(Itms.CAPACITOR_BASIC))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energy_cell_basic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CELL.get(Tier.BASIC))
                .pattern("iii")
                .pattern("cdc")
                .pattern("iii")
                .define('c', Blcks.ENERGY_CELL.get(Tier.STARTER))
                .define('i', Items.IRON_INGOT)
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Blcks.ENERGY_CELL.get(Tier.STARTER)), has(Blcks.ENERGY_CELL.get(Tier.STARTER)))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energy_cell_basic_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CELL.get(Tier.BLAZING))
                .pattern("bgb")
                .pattern("cdc")
                .pattern("bgb")
                .define('c', Blcks.ENERGY_CELL.get(Tier.HARDENED))
                .define('g', Itms.CAPACITOR_BLAZING)
                .define('b', Itms.BLAZING_CRYSTAL)
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Blcks.ENERGY_CELL.get(Tier.HARDENED)), has(Blcks.ENERGY_CELL.get(Tier.HARDENED)))
                .unlockedBy(getHasName(Itms.CAPACITOR_BLAZING), has(Itms.CAPACITOR_BLAZING))
                .unlockedBy(getHasName(Itms.BLAZING_CRYSTAL), has(Itms.BLAZING_CRYSTAL))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energy_cell_blazing"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CELL.get(Tier.HARDENED))
                .pattern("ghg")
                .pattern("cdc")
                .pattern("ghg")
                .define('h', Itms.CAPACITOR_HARDENED)
                .define('c', Blcks.ENERGY_CELL.get(Tier.BASIC))
                .define('g', Itms.ENERGIZED_STEEL)
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_HARDENED), has(Itms.CAPACITOR_HARDENED))
                .unlockedBy(getHasName(Blcks.ENERGY_CELL.get(Tier.BASIC)), has(Blcks.ENERGY_CELL.get(Tier.BASIC)))
                .unlockedBy(getHasName(Itms.ENERGIZED_STEEL), has(Itms.ENERGIZED_STEEL))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energy_cell_hardened"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CELL.get(Tier.NIOTIC))
                .pattern("bnb")
                .pattern("cdc")
                .pattern("bnb")
                .define('c', Blcks.ENERGY_CELL.get(Tier.BLAZING))
                .define('n', Itms.CAPACITOR_NIOTIC)
                .define('b', Itms.NIOTIC_CRYSTAL)
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Blcks.ENERGY_CELL.get(Tier.BLAZING)), has(Blcks.ENERGY_CELL.get(Tier.BLAZING)))
                .unlockedBy(getHasName(Itms.CAPACITOR_NIOTIC), has(Itms.CAPACITOR_NIOTIC))
                .unlockedBy(getHasName(Itms.NIOTIC_CRYSTAL), has(Itms.NIOTIC_CRYSTAL))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energy_cell_niotic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CELL.get(Tier.NITRO))
                .pattern("bnb")
                .pattern("cdc")
                .pattern("bnb")
                .define('c', Blcks.ENERGY_CELL.get(Tier.SPIRITED))
                .define('n', Itms.CAPACITOR_NITRO)
                .define('b', Itms.NITRO_CRYSTAL)
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Blcks.ENERGY_CELL.get(Tier.SPIRITED)), has(Blcks.ENERGY_CELL.get(Tier.SPIRITED)))
                .unlockedBy(getHasName(Itms.CAPACITOR_NITRO), has(Itms.CAPACITOR_NITRO))
                .unlockedBy(getHasName(Itms.NITRO_CRYSTAL), has(Itms.NITRO_CRYSTAL))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energy_cell_nitro"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CELL.get(Tier.SPIRITED))
                .pattern("bnb")
                .pattern("cdc")
                .pattern("bnb")
                .define('c', Blcks.ENERGY_CELL.get(Tier.NIOTIC))
                .define('n', Itms.CAPACITOR_SPIRITED)
                .define('b', Itms.SPIRITED_CRYSTAL)
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Blcks.ENERGY_CELL.get(Tier.NIOTIC)), has(Blcks.ENERGY_CELL.get(Tier.NIOTIC)))
                .unlockedBy(getHasName(Itms.CAPACITOR_SPIRITED), has(Itms.CAPACITOR_SPIRITED))
                .unlockedBy(getHasName(Itms.SPIRITED_CRYSTAL), has(Itms.SPIRITED_CRYSTAL))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energy_cell_spirited"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CELL.get(Tier.STARTER))
                .pattern("ici")
                .pattern("cdc")
                .pattern("ici")
                .define('c', Itms.CAPACITOR_BASIC_TINY)
                .define('i', Items.IRON_INGOT)
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC_TINY), has(Itms.CAPACITOR_BASIC_TINY))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energy_cell_starter"));
    }

    private static void energizingRod(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGIZING_ROD.get(Tier.BASIC))
                .pattern(" q ")
                .pattern("bcb")
                .pattern(" h ")
                .define('q', Tags.Items.STORAGE_BLOCKS_QUARTZ)
                .define('h', Blcks.ENERGIZING_ROD.get(Tier.STARTER))
                .define('b', Itms.CAPACITOR_BASIC)
                .define('c', Itms.DIELECTRIC_CASING)
                .unlockedBy("has_quartz_block", has(Tags.Items.STORAGE_BLOCKS_QUARTZ))
                .unlockedBy(getHasName(Blcks.ENERGIZING_ROD.get(Tier.STARTER)), has(Blcks.ENERGIZING_ROD.get(Tier.STARTER)))
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC), has(Itms.CAPACITOR_BASIC))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energizing_rod_basic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGIZING_ROD.get(Tier.BLAZING))
                .pattern(" q ")
                .pattern("bcb")
                .pattern(" h ")
                .define('q', Tags.Items.STORAGE_BLOCKS_QUARTZ)
                .define('h', Blcks.ENERGIZING_ROD.get(Tier.HARDENED))
                .define('b', Itms.CAPACITOR_BLAZING)
                .define('c', Itms.DIELECTRIC_CASING)
                .unlockedBy("has_quartz_block", has(Tags.Items.STORAGE_BLOCKS_QUARTZ))
                .unlockedBy(getHasName(Blcks.ENERGIZING_ROD.get(Tier.HARDENED)), has(Blcks.ENERGIZING_ROD.get(Tier.HARDENED)))
                .unlockedBy(getHasName(Itms.CAPACITOR_BLAZING), has(Itms.CAPACITOR_BLAZING))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energizing_rod_blazing"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGIZING_ROD.get(Tier.HARDENED))
                .pattern(" q ")
                .pattern("hch")
                .pattern(" e ")
                .define('q', Tags.Items.STORAGE_BLOCKS_QUARTZ)
                .define('e', Blcks.ENERGIZING_ROD.get(Tier.BASIC))
                .define('h', Itms.CAPACITOR_HARDENED)
                .define('c', Itms.DIELECTRIC_CASING)
                .unlockedBy("has_quartz_block", has(Tags.Items.STORAGE_BLOCKS_QUARTZ))
                .unlockedBy(getHasName(Blcks.ENERGIZING_ROD.get(Tier.BASIC)), has(Blcks.ENERGIZING_ROD.get(Tier.BASIC)))
                .unlockedBy(getHasName(Itms.CAPACITOR_HARDENED), has(Itms.CAPACITOR_HARDENED))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energizing_rod_hardened"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGIZING_ROD.get(Tier.NIOTIC))
                .pattern(" q ")
                .pattern("bcb")
                .pattern(" h ")
                .define('q', Tags.Items.STORAGE_BLOCKS_QUARTZ)
                .define('h', Blcks.ENERGIZING_ROD.get(Tier.BLAZING))
                .define('b', Itms.CAPACITOR_NIOTIC)
                .define('c', Itms.DIELECTRIC_CASING)
                .unlockedBy("has_quartz_block", has(Tags.Items.STORAGE_BLOCKS_QUARTZ))
                .unlockedBy(getHasName(Blcks.ENERGIZING_ROD.get(Tier.BLAZING)), has(Blcks.ENERGIZING_ROD.get(Tier.BLAZING)))
                .unlockedBy(getHasName(Itms.CAPACITOR_NIOTIC), has(Itms.CAPACITOR_NIOTIC))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energizing_rod_niotic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGIZING_ROD.get(Tier.NITRO))
                .pattern(" q ")
                .pattern("bcb")
                .pattern(" h ")
                .define('q', Tags.Items.STORAGE_BLOCKS_QUARTZ)
                .define('h', Blcks.ENERGIZING_ROD.get(Tier.SPIRITED))
                .define('b', Itms.CAPACITOR_NITRO)
                .define('c', Itms.DIELECTRIC_CASING)
                .unlockedBy("has_quartz_block", has(Tags.Items.STORAGE_BLOCKS_QUARTZ))
                .unlockedBy(getHasName(Blcks.ENERGIZING_ROD.get(Tier.SPIRITED)), has(Blcks.ENERGIZING_ROD.get(Tier.SPIRITED)))
                .unlockedBy(getHasName(Itms.CAPACITOR_NITRO), has(Itms.CAPACITOR_NITRO))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energizing_rod_nitro"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGIZING_ROD.get(Tier.SPIRITED))
                .pattern(" q ")
                .pattern("bcb")
                .pattern(" h ")
                .define('q', Tags.Items.STORAGE_BLOCKS_QUARTZ)
                .define('h', Blcks.ENERGIZING_ROD.get(Tier.NIOTIC))
                .define('b', Itms.CAPACITOR_SPIRITED)
                .define('c', Itms.DIELECTRIC_CASING)
                .unlockedBy("has_quartz_block", has(Tags.Items.STORAGE_BLOCKS_QUARTZ))
                .unlockedBy(getHasName(Blcks.ENERGIZING_ROD.get(Tier.NIOTIC)), has(Blcks.ENERGIZING_ROD.get(Tier.NIOTIC)))
                .unlockedBy(getHasName(Itms.CAPACITOR_SPIRITED), has(Itms.CAPACITOR_SPIRITED))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energizing_rod_spirited"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGIZING_ROD.get(Tier.STARTER))
                .pattern(" q ")
                .pattern("bcb")
                .pattern(" h ")
                .define('q', Items.QUARTZ)
                .define('h', Itms.DIELECTRIC_ROD)
                .define('b', Itms.CAPACITOR_BASIC_TINY)
                .define('c', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Items.QUARTZ), has(Items.QUARTZ))
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD), has(Itms.DIELECTRIC_ROD))
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC_TINY), has(Itms.CAPACITOR_BASIC_TINY))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energizing_rod_starter"));
    }

    private static void energizingOrb(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGIZING_ORB)
                .pattern("ggg")
                .pattern("gcg")
                .pattern("rrr")
                .define('r', Itms.DIELECTRIC_ROD_HORIZONTAL)
                .define('g', Tags.Items.GLASS)
                .define('c', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD_HORIZONTAL), has(Itms.DIELECTRIC_ROD_HORIZONTAL))
                .unlockedBy("has_glass", has(Tags.Items.GLASS))
                .unlockedBy(getHasName(Itms.DIELECTRIC_CASING), has(Itms.DIELECTRIC_CASING))
                .save(output, Powah.id("energizing_orb"));
    }

    private static void enderGate(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENDER_GATE.get(Tier.BASIC), 4)
                .pattern("oio")
                .pattern("ici")
                .pattern("oio")
                .define('c', Itms.ENDER_CORE)
                .define('i', Blcks.ENERGY_CABLE.get(Tier.BASIC))
                .define('o', Items.OBSIDIAN)
                .unlockedBy(getHasName(Itms.ENDER_CORE), has(Itms.ENDER_CORE))
                .unlockedBy(getHasName(Blcks.ENERGY_CABLE.get(Tier.BASIC)), has(Blcks.ENERGY_CABLE.get(Tier.BASIC)))
                .unlockedBy(getHasName(Items.OBSIDIAN), has(Items.OBSIDIAN))
                .save(output, Powah.id("ender_gate_basic"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blcks.ENDER_GATE.get(Tier.BASIC))
                .requires(Blcks.ENDER_GATE.get(Tier.BASIC))
                .unlockedBy(getHasName(Blcks.ENDER_GATE.get(Tier.BASIC)), has(Blcks.ENDER_GATE.get(Tier.BASIC)))
                .save(output, Powah.id("ender_gate_basic_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENDER_GATE.get(Tier.BLAZING), 4)
                .pattern("oio")
                .pattern("ici")
                .pattern("oio")
                .define('c', Itms.ENDER_CORE)
                .define('i', Blcks.ENERGY_CABLE.get(Tier.BLAZING))
                .define('o', Items.OBSIDIAN)
                .unlockedBy(getHasName(Itms.ENDER_CORE), has(Itms.ENDER_CORE))
                .unlockedBy(getHasName(Blcks.ENERGY_CABLE.get(Tier.BLAZING)), has(Blcks.ENERGY_CABLE.get(Tier.BLAZING)))
                .unlockedBy(getHasName(Items.OBSIDIAN), has(Items.OBSIDIAN))
                .save(output, Powah.id("ender_gate_blazing"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blcks.ENDER_GATE.get(Tier.BLAZING))
                .requires(Blcks.ENDER_GATE.get(Tier.BLAZING))
                .unlockedBy(getHasName(Blcks.ENDER_GATE.get(Tier.BLAZING)), has(Blcks.ENDER_GATE.get(Tier.BLAZING)))
                .save(output, Powah.id("ender_gate_blazing_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENDER_GATE.get(Tier.HARDENED), 4)
                .pattern("oio")
                .pattern("ici")
                .pattern("oio")
                .define('c', Itms.ENDER_CORE)
                .define('i', Blcks.ENERGY_CABLE.get(Tier.HARDENED))
                .define('o', Items.OBSIDIAN)
                .unlockedBy(getHasName(Itms.ENDER_CORE), has(Itms.ENDER_CORE))
                .unlockedBy(getHasName(Blcks.ENERGY_CABLE.get(Tier.HARDENED)), has(Blcks.ENERGY_CABLE.get(Tier.HARDENED)))
                .unlockedBy(getHasName(Items.OBSIDIAN), has(Items.OBSIDIAN))
                .save(output, Powah.id("ender_gate_hardened"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blcks.ENDER_GATE.get(Tier.HARDENED))
                .requires(Blcks.ENDER_GATE.get(Tier.HARDENED))
                .unlockedBy(getHasName(Blcks.ENDER_GATE.get(Tier.HARDENED)), has(Blcks.ENDER_GATE.get(Tier.HARDENED)))
                .save(output, Powah.id("ender_gate_hardened_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENDER_GATE.get(Tier.NIOTIC), 4)
                .pattern("oio")
                .pattern("ici")
                .pattern("oio")
                .define('c', Itms.ENDER_CORE)
                .define('i', Blcks.ENERGY_CABLE.get(Tier.NIOTIC))
                .define('o', Items.OBSIDIAN)
                .unlockedBy(getHasName(Itms.ENDER_CORE), has(Itms.ENDER_CORE))
                .unlockedBy(getHasName(Blcks.ENERGY_CABLE.get(Tier.NIOTIC)), has(Blcks.ENERGY_CABLE.get(Tier.NIOTIC)))
                .unlockedBy(getHasName(Items.OBSIDIAN), has(Items.OBSIDIAN))
                .save(output, Powah.id("ender_gate_niotic"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blcks.ENDER_GATE.get(Tier.NIOTIC))
                .requires(Blcks.ENDER_GATE.get(Tier.NIOTIC))
                .unlockedBy(getHasName(Blcks.ENDER_GATE.get(Tier.NIOTIC)), has(Blcks.ENDER_GATE.get(Tier.NIOTIC)))
                .save(output, Powah.id("ender_gate_niotic_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENDER_GATE.get(Tier.NITRO), 4)
                .pattern("oio")
                .pattern("ici")
                .pattern("oio")
                .define('c', Itms.ENDER_CORE)
                .define('i', Blcks.ENERGY_CABLE.get(Tier.NITRO))
                .define('o', Items.OBSIDIAN)
                .unlockedBy(getHasName(Itms.ENDER_CORE), has(Itms.ENDER_CORE))
                .unlockedBy(getHasName(Blcks.ENERGY_CABLE.get(Tier.NITRO)), has(Blcks.ENERGY_CABLE.get(Tier.NITRO)))
                .unlockedBy(getHasName(Items.OBSIDIAN), has(Items.OBSIDIAN))
                .save(output, Powah.id("ender_gate_nitro"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blcks.ENDER_GATE.get(Tier.NITRO))
                .requires(Blcks.ENDER_GATE.get(Tier.NITRO))
                .unlockedBy(getHasName(Blcks.ENDER_GATE.get(Tier.NITRO)), has(Blcks.ENDER_GATE.get(Tier.NITRO)))
                .save(output, Powah.id("ender_gate_nitro_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENDER_GATE.get(Tier.SPIRITED), 4)
                .pattern("oio")
                .pattern("ici")
                .pattern("oio")
                .define('c', Itms.ENDER_CORE)
                .define('i', Blcks.ENERGY_CABLE.get(Tier.SPIRITED))
                .define('o', Items.OBSIDIAN)
                .unlockedBy(getHasName(Itms.ENDER_CORE), has(Itms.ENDER_CORE))
                .unlockedBy(getHasName(Blcks.ENERGY_CABLE.get(Tier.SPIRITED)), has(Blcks.ENERGY_CABLE.get(Tier.SPIRITED)))
                .unlockedBy(getHasName(Items.OBSIDIAN), has(Items.OBSIDIAN))
                .save(output, Powah.id("ender_gate_spirited"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blcks.ENDER_GATE.get(Tier.SPIRITED))
                .requires(Blcks.ENDER_GATE.get(Tier.SPIRITED))
                .unlockedBy(getHasName(Blcks.ENDER_GATE.get(Tier.SPIRITED)), has(Blcks.ENDER_GATE.get(Tier.SPIRITED)))
                .save(output, Powah.id("ender_gate_spirited_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENDER_GATE.get(Tier.STARTER), 4)
                .pattern("oio")
                .pattern("ici")
                .pattern("oio")
                .define('c', Itms.ENDER_CORE)
                .define('i', Blcks.ENERGY_CABLE.get(Tier.STARTER))
                .define('o', Items.OBSIDIAN)
                .unlockedBy(getHasName(Itms.ENDER_CORE), has(Itms.ENDER_CORE))
                .unlockedBy(getHasName(Blcks.ENERGY_CABLE.get(Tier.STARTER)), has(Blcks.ENERGY_CABLE.get(Tier.STARTER)))
                .unlockedBy(getHasName(Items.OBSIDIAN), has(Items.OBSIDIAN))
                .save(output, Powah.id("ender_gate_starter"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blcks.ENDER_GATE.get(Tier.STARTER))
                .requires(Blcks.ENDER_GATE.get(Tier.STARTER))
                .unlockedBy(getHasName(Blcks.ENDER_GATE.get(Tier.STARTER)), has(Blcks.ENDER_GATE.get(Tier.STARTER)))
                .save(output, Powah.id("ender_gate_starter_2"));
    }

    private static void enderCell(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENDER_CELL.get(Tier.BASIC))
                .pattern("oio")
                .pattern("ici")
                .pattern("oio")
                .define('c', Itms.ENDER_CORE)
                .define('i', Items.IRON_INGOT)
                .define('o', Items.OBSIDIAN)
                .unlockedBy(getHasName(Itms.ENDER_CORE), has(Itms.ENDER_CORE))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .unlockedBy(getHasName(Items.OBSIDIAN), has(Items.OBSIDIAN))
                .save(output, Powah.id("ender_cell_basic"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blcks.ENDER_CELL.get(Tier.BASIC))
                .requires(Blcks.ENDER_CELL.get(Tier.BASIC))
                .unlockedBy(getHasName(Blcks.ENDER_CELL.get(Tier.BASIC)), has(Blcks.ENDER_CELL.get(Tier.BASIC)))
                .save(output, Powah.id("ender_cell_basic_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENDER_CELL.get(Tier.BLAZING))
                .pattern("oio")
                .pattern("ici")
                .pattern("oio")
                .define('c', Itms.ENDER_CORE)
                .define('i', Itms.BLAZING_CRYSTAL)
                .define('o', Items.OBSIDIAN)
                .unlockedBy(getHasName(Itms.ENDER_CORE), has(Itms.ENDER_CORE))
                .unlockedBy(getHasName(Itms.BLAZING_CRYSTAL), has(Itms.BLAZING_CRYSTAL))
                .unlockedBy(getHasName(Items.OBSIDIAN), has(Items.OBSIDIAN))
                .save(output, Powah.id("ender_cell_blazing"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blcks.ENDER_CELL.get(Tier.BLAZING))
                .requires(Blcks.ENDER_CELL.get(Tier.BLAZING))
                .unlockedBy(getHasName(Blcks.ENDER_CELL.get(Tier.BLAZING)), has(Blcks.ENDER_CELL.get(Tier.BLAZING)))
                .save(output, Powah.id("ender_cell_blazing_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENDER_CELL.get(Tier.HARDENED))
                .pattern("oio")
                .pattern("ici")
                .pattern("oio")
                .define('c', Itms.ENDER_CORE)
                .define('i', Itms.ENERGIZED_STEEL)
                .define('o', Items.OBSIDIAN)
                .unlockedBy(getHasName(Itms.ENDER_CORE), has(Itms.ENDER_CORE))
                .unlockedBy(getHasName(Itms.ENERGIZED_STEEL), has(Itms.ENERGIZED_STEEL))
                .unlockedBy(getHasName(Items.OBSIDIAN), has(Items.OBSIDIAN))
                .save(output, Powah.id("ender_cell_hardened"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blcks.ENDER_CELL.get(Tier.HARDENED))
                .requires(Blcks.ENDER_CELL.get(Tier.HARDENED))
                .unlockedBy(getHasName(Blcks.ENDER_CELL.get(Tier.HARDENED)), has(Blcks.ENDER_CELL.get(Tier.HARDENED)))
                .save(output, Powah.id("ender_cell_hardened_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENDER_CELL.get(Tier.NIOTIC))
                .pattern("oio")
                .pattern("ici")
                .pattern("oio")
                .define('c', Itms.ENDER_CORE)
                .define('i', Itms.NIOTIC_CRYSTAL)
                .define('o', Items.OBSIDIAN)
                .unlockedBy(getHasName(Itms.ENDER_CORE), has(Itms.ENDER_CORE))
                .unlockedBy(getHasName(Itms.NIOTIC_CRYSTAL), has(Itms.NIOTIC_CRYSTAL))
                .unlockedBy(getHasName(Items.OBSIDIAN), has(Items.OBSIDIAN))
                .save(output, Powah.id("ender_cell_niotic"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blcks.ENDER_CELL.get(Tier.NIOTIC))
                .requires(Blcks.ENDER_CELL.get(Tier.NIOTIC))
                .unlockedBy(getHasName(Blcks.ENDER_CELL.get(Tier.NIOTIC)), has(Blcks.ENDER_CELL.get(Tier.NIOTIC)))
                .save(output, Powah.id("ender_cell_niotic_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENDER_CELL.get(Tier.NITRO))
                .pattern("oio")
                .pattern("ici")
                .pattern("oio")
                .define('c', Itms.ENDER_CORE)
                .define('i', Itms.NITRO_CRYSTAL)
                .define('o', Items.OBSIDIAN)
                .unlockedBy(getHasName(Itms.ENDER_CORE), has(Itms.ENDER_CORE))
                .unlockedBy(getHasName(Itms.NITRO_CRYSTAL), has(Itms.NITRO_CRYSTAL))
                .unlockedBy(getHasName(Items.OBSIDIAN), has(Items.OBSIDIAN))
                .save(output, Powah.id("ender_cell_nitro"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blcks.ENDER_CELL.get(Tier.NITRO))
                .requires(Blcks.ENDER_CELL.get(Tier.NITRO))
                .unlockedBy(getHasName(Blcks.ENDER_CELL.get(Tier.NITRO)), has(Blcks.ENDER_CELL.get(Tier.NITRO)))
                .save(output, Powah.id("ender_cell_nitro_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENDER_CELL.get(Tier.SPIRITED))
                .pattern("oio")
                .pattern("ici")
                .pattern("oio")
                .define('c', Itms.ENDER_CORE)
                .define('i', Itms.SPIRITED_CRYSTAL)
                .define('o', Items.OBSIDIAN)
                .unlockedBy(getHasName(Itms.ENDER_CORE), has(Itms.ENDER_CORE))
                .unlockedBy(getHasName(Itms.SPIRITED_CRYSTAL), has(Itms.SPIRITED_CRYSTAL))
                .unlockedBy(getHasName(Items.OBSIDIAN), has(Items.OBSIDIAN))
                .save(output, Powah.id("ender_cell_spirited"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blcks.ENDER_CELL.get(Tier.SPIRITED))
                .requires(Blcks.ENDER_CELL.get(Tier.SPIRITED))
                .unlockedBy(getHasName(Blcks.ENDER_CELL.get(Tier.SPIRITED)), has(Blcks.ENDER_CELL.get(Tier.SPIRITED)))
                .save(output, Powah.id("ender_cell_spirited_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENDER_CELL.get(Tier.STARTER), 2)
                .pattern("oio")
                .pattern("ici")
                .pattern("oio")
                .define('c', Itms.ENDER_CORE)
                .define('i', Items.IRON_NUGGET)
                .define('o', Items.OBSIDIAN)
                .unlockedBy(getHasName(Itms.ENDER_CORE), has(Itms.ENDER_CORE))
                .save(output, Powah.id("ender_cell_starter"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blcks.ENDER_CELL.get(Tier.STARTER))
                .requires(Blcks.ENDER_CELL.get(Tier.STARTER))
                .unlockedBy(getHasName(Blcks.ENDER_CELL.get(Tier.STARTER)), has(Blcks.ENDER_CELL.get(Tier.STARTER)))
                .save(output, Powah.id("ender_cell_starter_2"));
    }

    private static void materials(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.AERIAL_PEARL)
                .pattern("pbp")
                .pattern("beb")
                .pattern("pbp")
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('e', Items.ENDER_PEARL)
                .define('b', Items.IRON_BARS)
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .save(output, Powah.id("aerial_pearl"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blcks.BLAZING_CRYSTAL)
                .requires(Itms.BLAZING_CRYSTAL)
                .requires(Itms.BLAZING_CRYSTAL)
                .requires(Itms.BLAZING_CRYSTAL)
                .requires(Itms.BLAZING_CRYSTAL)
                .requires(Itms.BLAZING_CRYSTAL)
                .requires(Itms.BLAZING_CRYSTAL)
                .requires(Itms.BLAZING_CRYSTAL)
                .requires(Itms.BLAZING_CRYSTAL)
                .requires(Itms.BLAZING_CRYSTAL)
                .unlockedBy(getHasName(Itms.BLAZING_CRYSTAL), has(Itms.BLAZING_CRYSTAL))
                .save(output, Powah.id("blazing_crystal_block"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Itms.BLAZING_CRYSTAL, 9)
                .requires(Blcks.BLAZING_CRYSTAL)
                .unlockedBy(getHasName(Blcks.BLAZING_CRYSTAL), has(Blcks.BLAZING_CRYSTAL))
                .save(output, Powah.id("blazing_crystal"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Itms.NIOTIC_CRYSTAL, 9)
                .requires(Blcks.NIOTIC_CRYSTAL)
                .unlockedBy(getHasName(Blcks.NIOTIC_CRYSTAL), has(Blcks.NIOTIC_CRYSTAL))
                .save(output, Powah.id("niotic_crystal"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Itms.NITRO_CRYSTAL, 9)
                .requires(Blcks.NITRO_CRYSTAL)
                .unlockedBy(getHasName(Blcks.NITRO_CRYSTAL), has(Blcks.NITRO_CRYSTAL))
                .save(output, Powah.id("nitro_crystal"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Itms.SPIRITED_CRYSTAL, 9)
                .requires(Blcks.SPIRITED_CRYSTAL)
                .unlockedBy(getHasName(Blcks.SPIRITED_CRYSTAL), has(Blcks.SPIRITED_CRYSTAL))
                .save(output, Powah.id("spirited_crystal"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.DIELECTRIC_CASING)
                .pattern("ihi")
                .pattern("v v")
                .pattern("ihi")
                .define('v', Itms.DIELECTRIC_ROD)
                .define('h', Itms.DIELECTRIC_ROD_HORIZONTAL)
                .define('i', Items.IRON_INGOT)
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD), has(Itms.DIELECTRIC_ROD))
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD_HORIZONTAL), has(Itms.DIELECTRIC_ROD_HORIZONTAL))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output, Powah.id("dielectric_casing"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Itms.DIELECTRIC_PASTE, 24)
                .requires(ItemTags.COALS)
                .requires(ItemTags.COALS)
                .requires(ItemTags.COALS)
                .requires(Items.CLAY_BALL)
                .requires(Items.CLAY_BALL)
                .requires(Items.LAVA_BUCKET)
                .unlockedBy("has_coals", has(ItemTags.COALS))
                .unlockedBy(getHasName(Items.CLAY_BALL), has(Items.CLAY_BALL))
                .unlockedBy(getHasName(Items.LAVA_BUCKET), has(Items.LAVA_BUCKET))
                .save(output, Powah.id("dielectric_paste"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Itms.DIELECTRIC_PASTE, 16)
                .requires(ItemTags.COALS)
                .requires(ItemTags.COALS)
                .requires(Items.CLAY_BALL)
                .requires(Items.BLAZE_POWDER)
                .unlockedBy("has_coals", has(ItemTags.COALS))
                .unlockedBy("has_coals", has(ItemTags.COALS))
                .unlockedBy(getHasName(Items.CLAY_BALL), has(Items.CLAY_BALL))
                .unlockedBy(getHasName(Items.BLAZE_POWDER), has(Items.BLAZE_POWDER))
                .save(output, Powah.id("dielectric_paste_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.DIELECTRIC_ROD, 8)
                .pattern("pip")
                .pattern("pip")
                .pattern("pip")
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('i', Items.IRON_BARS)
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Items.IRON_BARS), has(Items.IRON_BARS))
                .save(output, Powah.id("dielectric_rod"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Itms.DIELECTRIC_ROD)
                .requires(Itms.DIELECTRIC_ROD_HORIZONTAL)
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD_HORIZONTAL), has(Itms.DIELECTRIC_ROD_HORIZONTAL))
                .save(output, Powah.id("dielectric_rod_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.DIELECTRIC_ROD_HORIZONTAL, 8)
                .pattern("ppp")
                .pattern("iii")
                .pattern("ppp")
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('i', Items.IRON_BARS)
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Items.IRON_BARS), has(Items.IRON_BARS))
                .save(output, Powah.id("dielectric_rod_h"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Itms.DIELECTRIC_ROD_HORIZONTAL)
                .requires(Itms.DIELECTRIC_ROD)
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD), has(Itms.DIELECTRIC_ROD))
                .save(output, Powah.id("dielectric_rod_h_2"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blcks.ENERGIZED_STEEL)
                .requires(Itms.ENERGIZED_STEEL)
                .requires(Itms.ENERGIZED_STEEL)
                .requires(Itms.ENERGIZED_STEEL)
                .requires(Itms.ENERGIZED_STEEL)
                .requires(Itms.ENERGIZED_STEEL)
                .requires(Itms.ENERGIZED_STEEL)
                .requires(Itms.ENERGIZED_STEEL)
                .requires(Itms.ENERGIZED_STEEL)
                .requires(Itms.ENERGIZED_STEEL)
                .unlockedBy(getHasName(Itms.ENERGIZED_STEEL), has(Itms.ENERGIZED_STEEL))
                .save(output, Powah.id("energized_steel_block"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blcks.NIOTIC_CRYSTAL)
                .requires(Itms.NIOTIC_CRYSTAL)
                .requires(Itms.NIOTIC_CRYSTAL)
                .requires(Itms.NIOTIC_CRYSTAL)
                .requires(Itms.NIOTIC_CRYSTAL)
                .requires(Itms.NIOTIC_CRYSTAL)
                .requires(Itms.NIOTIC_CRYSTAL)
                .requires(Itms.NIOTIC_CRYSTAL)
                .requires(Itms.NIOTIC_CRYSTAL)
                .requires(Itms.NIOTIC_CRYSTAL)
                .unlockedBy(getHasName(Itms.NIOTIC_CRYSTAL), has(Itms.NIOTIC_CRYSTAL))
                .save(output, Powah.id("niotic_crystal_block"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blcks.NITRO_CRYSTAL)
                .requires(Itms.NITRO_CRYSTAL)
                .requires(Itms.NITRO_CRYSTAL)
                .requires(Itms.NITRO_CRYSTAL)
                .requires(Itms.NITRO_CRYSTAL)
                .requires(Itms.NITRO_CRYSTAL)
                .requires(Itms.NITRO_CRYSTAL)
                .requires(Itms.NITRO_CRYSTAL)
                .requires(Itms.NITRO_CRYSTAL)
                .requires(Itms.NITRO_CRYSTAL)
                .unlockedBy(getHasName(Itms.NITRO_CRYSTAL), has(Itms.NITRO_CRYSTAL))
                .save(output, Powah.id("nitro_crystal_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.PHOTOELECTRIC_PANE)
                .pattern("dld")
                .pattern("lpl")
                .pattern("dld")
                .define('p', Tags.Items.GLASS_PANES)
                .define('l', Items.LAPIS_LAZULI)
                .define('d', Itms.DIELECTRIC_PASTE)
                .unlockedBy("has_glass_panes", has(Tags.Items.GLASS_PANES))
                .unlockedBy(getHasName(Items.LAPIS_LAZULI), has(Items.LAPIS_LAZULI))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .save(output, Powah.id("photoelectric_pane"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blcks.SPIRITED_CRYSTAL)
                .requires(Itms.SPIRITED_CRYSTAL)
                .requires(Itms.SPIRITED_CRYSTAL)
                .requires(Itms.SPIRITED_CRYSTAL)
                .requires(Itms.SPIRITED_CRYSTAL)
                .requires(Itms.SPIRITED_CRYSTAL)
                .requires(Itms.SPIRITED_CRYSTAL)
                .requires(Itms.SPIRITED_CRYSTAL)
                .requires(Itms.SPIRITED_CRYSTAL)
                .requires(Itms.SPIRITED_CRYSTAL)
                .unlockedBy(getHasName(Itms.SPIRITED_CRYSTAL), has(Itms.SPIRITED_CRYSTAL))
                .save(output, Powah.id("spirited_crystal_block"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Itms.ENERGIZED_STEEL, 9)
                .requires(Blcks.ENERGIZED_STEEL)
                .unlockedBy(getHasName(Blcks.ENERGIZED_STEEL), has(Blcks.ENERGIZED_STEEL))
                .save(output, Powah.id("energized_steel"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.THERMOELECTRIC_PLATE)
                .pattern("brb")
                .pattern("rtr")
                .pattern("brb")
                .define('b', Items.BLAZE_POWDER)
                .define('r', Items.REDSTONE)
                .define('t', Itms.CAPACITOR_BASIC_TINY)
                .unlockedBy(getHasName(Items.BLAZE_POWDER), has(Items.BLAZE_POWDER))
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC_TINY), has(Itms.CAPACITOR_BASIC_TINY))
                .save(output, Powah.id("thermoelectric_plate"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Itms.URANINITE, 9)
                .requires(Blcks.URANINITE)
                .unlockedBy(getHasName(Blcks.URANINITE), has(Blcks.URANINITE))
                .save(output, Powah.id("uraninite"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blcks.URANINITE)
                .requires(Itms.URANINITE)
                .requires(Itms.URANINITE)
                .requires(Itms.URANINITE)
                .requires(Itms.URANINITE)
                .requires(Itms.URANINITE)
                .requires(Itms.URANINITE)
                .requires(Itms.URANINITE)
                .requires(Itms.URANINITE)
                .requires(Itms.URANINITE)
                .unlockedBy(getHasName(Itms.URANINITE), has(Itms.URANINITE))
                .save(output, Powah.id("uraninite_block"));

    }

    private static void capacitors(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.CAPACITOR_BASIC, 4)
                .pattern(" ip")
                .pattern("iri")
                .pattern("pi ")
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('i', Items.IRON_INGOT)
                .define('r', Items.REDSTONE_BLOCK)
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .unlockedBy(getHasName(Items.REDSTONE_BLOCK), has(Items.REDSTONE_BLOCK))
                .save(output, Powah.id("capacitor_basic"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Itms.CAPACITOR_BASIC_LARGE)
                .requires(Itms.CAPACITOR_BASIC)
                .requires(Itms.CAPACITOR_BASIC)
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC), has(Itms.CAPACITOR_BASIC))
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC), has(Itms.CAPACITOR_BASIC))
                .save(output, Powah.id("capacitor_basic_large"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Itms.CAPACITOR_BASIC_TINY, 2)
                .requires(Itms.CAPACITOR_BASIC)
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC), has(Itms.CAPACITOR_BASIC))
                .save(output, Powah.id("capacitor_basic_tiny"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.CAPACITOR_BLAZING, 2)
                .pattern("pbp")
                .pattern("bcb")
                .pattern("pbp")
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('c', Itms.CAPACITOR_BASIC_LARGE)
                .define('b', Itms.BLAZING_CRYSTAL)
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC_LARGE), has(Itms.CAPACITOR_BASIC_LARGE))
                .unlockedBy(getHasName(Itms.BLAZING_CRYSTAL), has(Itms.BLAZING_CRYSTAL))
                .save(output, Powah.id("capacitor_blazing"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.CAPACITOR_HARDENED, 2)
                .pattern("pbp")
                .pattern("bcb")
                .pattern("pbp")
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('c', Itms.CAPACITOR_BASIC_LARGE)
                .define('b', Itms.ENERGIZED_STEEL)
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC_LARGE), has(Itms.CAPACITOR_BASIC_LARGE))
                .unlockedBy(getHasName(Itms.ENERGIZED_STEEL), has(Itms.ENERGIZED_STEEL))
                .save(output, Powah.id("capacitor_hardened"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.CAPACITOR_NIOTIC)
                .pattern("pbp")
                .pattern("bcb")
                .pattern("pbp")
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('c', Itms.CAPACITOR_BASIC_LARGE)
                .define('b', Itms.NIOTIC_CRYSTAL)
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC_LARGE), has(Itms.CAPACITOR_BASIC_LARGE))
                .unlockedBy(getHasName(Itms.NIOTIC_CRYSTAL), has(Itms.NIOTIC_CRYSTAL))
                .save(output, Powah.id("capacitor_niotic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.CAPACITOR_NITRO)
                .pattern("pbp")
                .pattern("bcb")
                .pattern("pbp")
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('c', Itms.CAPACITOR_BASIC_LARGE)
                .define('b', Itms.NITRO_CRYSTAL)
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC_LARGE), has(Itms.CAPACITOR_BASIC_LARGE))
                .unlockedBy(getHasName(Itms.NITRO_CRYSTAL), has(Itms.NITRO_CRYSTAL))
                .save(output, Powah.id("capacitor_nitro"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.CAPACITOR_SPIRITED)
                .pattern("pbp")
                .pattern("bcb")
                .pattern("pbp")
                .define('p', Itms.DIELECTRIC_PASTE)
                .define('c', Itms.CAPACITOR_BASIC_LARGE)
                .define('b', Itms.SPIRITED_CRYSTAL)
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC_LARGE), has(Itms.CAPACITOR_BASIC_LARGE))
                .unlockedBy(getHasName(Itms.SPIRITED_CRYSTAL), has(Itms.SPIRITED_CRYSTAL))
                .save(output, Powah.id("capacitor_spirited"));
    }

    private static void energyCable(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CABLE.get(Tier.BASIC), 6)
                .pattern("ddd")
                .pattern("tct")
                .pattern("ddd")
                .define('d', Itms.DIELECTRIC_ROD_HORIZONTAL)
                .define('c', Itms.CAPACITOR_BASIC)
                .define('t', Blcks.ENERGY_CABLE.get(Tier.STARTER))
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD_HORIZONTAL), has(Itms.DIELECTRIC_ROD_HORIZONTAL))
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC), has(Itms.CAPACITOR_BASIC))
                .unlockedBy(getHasName(Blcks.ENERGY_CABLE.get(Tier.STARTER)), has(Blcks.ENERGY_CABLE.get(Tier.STARTER)))
                .save(output, Powah.id("cable_basic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CABLE.get(Tier.BASIC), 12)
                .pattern("ddd")
                .pattern("ici")
                .pattern("ddd")
                .define('d', Itms.DIELECTRIC_ROD_HORIZONTAL)
                .define('c', Itms.CAPACITOR_BASIC)
                .define('i', Items.IRON_INGOT)
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD_HORIZONTAL), has(Itms.DIELECTRIC_ROD_HORIZONTAL))
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC), has(Itms.CAPACITOR_BASIC))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output, Powah.id("cable_basic_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CABLE.get(Tier.BLAZING), 6)
                .pattern("ddd")
                .pattern("tct")
                .pattern("ddd")
                .define('d', Itms.DIELECTRIC_ROD_HORIZONTAL)
                .define('c', Itms.CAPACITOR_BLAZING)
                .define('t', Blcks.ENERGY_CABLE.get(Tier.HARDENED))
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD_HORIZONTAL), has(Itms.DIELECTRIC_ROD_HORIZONTAL))
                .unlockedBy(getHasName(Itms.CAPACITOR_BLAZING), has(Itms.CAPACITOR_BLAZING))
                .unlockedBy(getHasName(Blcks.ENERGY_CABLE.get(Tier.HARDENED)), has(Blcks.ENERGY_CABLE.get(Tier.HARDENED)))
                .save(output, Powah.id("cable_blazing"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CABLE.get(Tier.BLAZING), 12)
                .pattern("ddd")
                .pattern("kck")
                .pattern("ddd")
                .define('d', Itms.DIELECTRIC_ROD_HORIZONTAL)
                .define('c', Itms.CAPACITOR_BLAZING)
                .define('k', Itms.BLAZING_CRYSTAL)
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD_HORIZONTAL), has(Itms.DIELECTRIC_ROD_HORIZONTAL))
                .unlockedBy(getHasName(Itms.CAPACITOR_BLAZING), has(Itms.CAPACITOR_BLAZING))
                .unlockedBy(getHasName(Itms.BLAZING_CRYSTAL), has(Itms.BLAZING_CRYSTAL))
                .save(output, Powah.id("cable_blazing_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CABLE.get(Tier.HARDENED), 6)
                .pattern("ddd")
                .pattern("tct")
                .pattern("ddd")
                .define('d', Itms.DIELECTRIC_ROD_HORIZONTAL)
                .define('c', Itms.CAPACITOR_HARDENED)
                .define('t', Blcks.ENERGY_CABLE.get(Tier.BASIC))
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD_HORIZONTAL), has(Itms.DIELECTRIC_ROD_HORIZONTAL))
                .unlockedBy(getHasName(Itms.CAPACITOR_HARDENED), has(Itms.CAPACITOR_HARDENED))
                .unlockedBy(getHasName(Blcks.ENERGY_CABLE.get(Tier.BASIC)), has(Blcks.ENERGY_CABLE.get(Tier.BASIC)))
                .save(output, Powah.id("cable_hardened"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CABLE.get(Tier.HARDENED), 12)
                .pattern("ddd")
                .pattern("kck")
                .pattern("ddd")
                .define('d', Itms.DIELECTRIC_ROD_HORIZONTAL)
                .define('c', Itms.CAPACITOR_HARDENED)
                .define('k', Itms.ENERGIZED_STEEL)
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD_HORIZONTAL), has(Itms.DIELECTRIC_ROD_HORIZONTAL))
                .unlockedBy(getHasName(Itms.CAPACITOR_HARDENED), has(Itms.CAPACITOR_HARDENED))
                .unlockedBy(getHasName(Itms.ENERGIZED_STEEL), has(Itms.ENERGIZED_STEEL))
                .save(output, Powah.id("cable_hardened_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CABLE.get(Tier.NIOTIC), 6)
                .pattern("ddd")
                .pattern("tct")
                .pattern("ddd")
                .define('d', Itms.DIELECTRIC_ROD_HORIZONTAL)
                .define('c', Itms.CAPACITOR_NIOTIC)
                .define('t', Blcks.ENERGY_CABLE.get(Tier.BLAZING))
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD_HORIZONTAL), has(Itms.DIELECTRIC_ROD_HORIZONTAL))
                .unlockedBy(getHasName(Itms.CAPACITOR_NIOTIC), has(Itms.CAPACITOR_NIOTIC))
                .unlockedBy(getHasName(Blcks.ENERGY_CABLE.get(Tier.BLAZING)), has(Blcks.ENERGY_CABLE.get(Tier.BLAZING)))
                .save(output, Powah.id("cable_niotic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CABLE.get(Tier.NIOTIC), 12)
                .pattern("ddd")
                .pattern("kck")
                .pattern("ddd")
                .define('d', Itms.DIELECTRIC_ROD_HORIZONTAL)
                .define('c', Itms.CAPACITOR_NIOTIC)
                .define('k', Itms.NIOTIC_CRYSTAL)
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD_HORIZONTAL), has(Itms.DIELECTRIC_ROD_HORIZONTAL))
                .unlockedBy(getHasName(Itms.CAPACITOR_NIOTIC), has(Itms.CAPACITOR_NIOTIC))
                .unlockedBy(getHasName(Itms.NIOTIC_CRYSTAL), has(Itms.NIOTIC_CRYSTAL))
                .save(output, Powah.id("cable_niotic_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CABLE.get(Tier.NITRO), 6)
                .pattern("ddd")
                .pattern("tct")
                .pattern("ddd")
                .define('d', Itms.DIELECTRIC_ROD_HORIZONTAL)
                .define('c', Itms.CAPACITOR_NITRO)
                .define('t', Blcks.ENERGY_CABLE.get(Tier.SPIRITED))
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD_HORIZONTAL), has(Itms.DIELECTRIC_ROD_HORIZONTAL))
                .unlockedBy(getHasName(Itms.CAPACITOR_NITRO), has(Itms.CAPACITOR_NITRO))
                .unlockedBy(getHasName(Blcks.ENERGY_CABLE.get(Tier.SPIRITED)), has(Blcks.ENERGY_CABLE.get(Tier.SPIRITED)))
                .save(output, Powah.id("cable_nitro"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CABLE.get(Tier.NITRO), 12)
                .pattern("ddd")
                .pattern("kck")
                .pattern("ddd")
                .define('d', Itms.DIELECTRIC_ROD_HORIZONTAL)
                .define('c', Itms.CAPACITOR_NITRO)
                .define('k', Itms.NITRO_CRYSTAL)
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD_HORIZONTAL), has(Itms.DIELECTRIC_ROD_HORIZONTAL))
                .unlockedBy(getHasName(Itms.CAPACITOR_NITRO), has(Itms.CAPACITOR_NITRO))
                .unlockedBy(getHasName(Itms.NITRO_CRYSTAL), has(Itms.NITRO_CRYSTAL))
                .save(output, Powah.id("cable_nitro_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CABLE.get(Tier.SPIRITED), 6)
                .pattern("ddd")
                .pattern("tct")
                .pattern("ddd")
                .define('d', Itms.DIELECTRIC_ROD_HORIZONTAL)
                .define('c', Itms.CAPACITOR_SPIRITED)
                .define('t', Blcks.ENERGY_CABLE.get(Tier.NIOTIC))
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD_HORIZONTAL), has(Itms.DIELECTRIC_ROD_HORIZONTAL))
                .unlockedBy(getHasName(Itms.CAPACITOR_SPIRITED), has(Itms.CAPACITOR_SPIRITED))
                .unlockedBy(getHasName(Blcks.ENERGY_CABLE.get(Tier.NIOTIC)), has(Blcks.ENERGY_CABLE.get(Tier.NIOTIC)))
                .save(output, Powah.id("cable_spirited"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CABLE.get(Tier.SPIRITED), 12)
                .pattern("ddd")
                .pattern("kck")
                .pattern("ddd")
                .define('d', Itms.DIELECTRIC_ROD_HORIZONTAL)
                .define('c', Itms.CAPACITOR_SPIRITED)
                .define('k', Itms.SPIRITED_CRYSTAL)
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD_HORIZONTAL), has(Itms.DIELECTRIC_ROD_HORIZONTAL))
                .unlockedBy(getHasName(Itms.CAPACITOR_SPIRITED), has(Itms.CAPACITOR_SPIRITED))
                .unlockedBy(getHasName(Itms.SPIRITED_CRYSTAL), has(Itms.SPIRITED_CRYSTAL))
                .save(output, Powah.id("cable_spirited_2"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.ENERGY_CABLE.get(Tier.STARTER), 12)
                .pattern("ddd")
                .pattern("iti")
                .pattern("ddd")
                .define('d', Itms.DIELECTRIC_ROD_HORIZONTAL)
                .define('i', Items.IRON_NUGGET)
                .define('t', Itms.CAPACITOR_BASIC_TINY)
                .unlockedBy(getHasName(Itms.DIELECTRIC_ROD_HORIZONTAL), has(Itms.DIELECTRIC_ROD_HORIZONTAL))
                .unlockedBy(getHasName(Items.IRON_NUGGET), has(Items.IRON_NUGGET))
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC_TINY), has(Itms.CAPACITOR_BASIC_TINY))
                .save(output, Powah.id("cable_starter"));
    }

    private static void battery(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.BATTERY.get(Tier.BASIC))
                .pattern("oko")
                .pattern("iri")
                .pattern("oco")
                .define('r', Items.REDSTONE_BLOCK)
                .define('c', Itms.BATTERY.get(Tier.STARTER))
                .define('i', Itms.CAPACITOR_BASIC_LARGE)
                .define('o', Itms.DIELECTRIC_PASTE)
                .define('k', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.REDSTONE_BLOCK), has(Items.REDSTONE_BLOCK))
                .unlockedBy(getHasName(Itms.BATTERY.get(Tier.STARTER)), has(Itms.BATTERY.get(Tier.STARTER)))
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC_LARGE), has(Itms.CAPACITOR_BASIC_LARGE))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output, Powah.id("battery_basic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.BATTERY.get(Tier.BLAZING))
                .pattern("oko")
                .pattern("iri")
                .pattern("oco")
                .define('r', Items.REDSTONE_BLOCK)
                .define('c', Itms.BATTERY.get(Tier.HARDENED))
                .define('i', Itms.CAPACITOR_BLAZING)
                .define('o', Itms.DIELECTRIC_PASTE)
                .define('k', Itms.BLAZING_CRYSTAL)
                .unlockedBy(getHasName(Items.REDSTONE_BLOCK), has(Items.REDSTONE_BLOCK))
                .unlockedBy(getHasName(Itms.BATTERY.get(Tier.HARDENED)), has(Itms.BATTERY.get(Tier.HARDENED)))
                .unlockedBy(getHasName(Itms.CAPACITOR_BLAZING), has(Itms.CAPACITOR_BLAZING))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.BLAZING_CRYSTAL), has(Itms.BLAZING_CRYSTAL))
                .save(output, Powah.id("battery_blazing"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.BATTERY.get(Tier.HARDENED))
                .pattern("oko")
                .pattern("iri")
                .pattern("oco")
                .define('r', Items.REDSTONE_BLOCK)
                .define('c', Itms.BATTERY.get(Tier.BASIC))
                .define('i', Itms.CAPACITOR_HARDENED)
                .define('o', Itms.DIELECTRIC_PASTE)
                .define('k', Itms.ENERGIZED_STEEL)
                .unlockedBy(getHasName(Items.REDSTONE_BLOCK), has(Items.REDSTONE_BLOCK))
                .unlockedBy(getHasName(Itms.BATTERY.get(Tier.BASIC)), has(Itms.BATTERY.get(Tier.BASIC)))
                .unlockedBy(getHasName(Itms.CAPACITOR_HARDENED), has(Itms.CAPACITOR_HARDENED))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.ENERGIZED_STEEL), has(Itms.ENERGIZED_STEEL))
                .save(output, Powah.id("battery_hardened"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.BATTERY.get(Tier.NIOTIC))
                .pattern("oko")
                .pattern("iri")
                .pattern("oco")
                .define('r', Items.REDSTONE_BLOCK)
                .define('c', Itms.BATTERY.get(Tier.BLAZING))
                .define('i', Itms.CAPACITOR_NIOTIC)
                .define('o', Itms.DIELECTRIC_PASTE)
                .define('k', Itms.NIOTIC_CRYSTAL)
                .unlockedBy(getHasName(Items.REDSTONE_BLOCK), has(Items.REDSTONE_BLOCK))
                .unlockedBy(getHasName(Itms.BATTERY.get(Tier.BLAZING)), has(Itms.BATTERY.get(Tier.BLAZING)))
                .unlockedBy(getHasName(Itms.CAPACITOR_NIOTIC), has(Itms.CAPACITOR_NIOTIC))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.NIOTIC_CRYSTAL), has(Itms.NIOTIC_CRYSTAL))
                .save(output, Powah.id("battery_niotic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.BATTERY.get(Tier.NITRO))
                .pattern("oko")
                .pattern("iri")
                .pattern("oco")
                .define('r', Items.REDSTONE_BLOCK)
                .define('c', Itms.BATTERY.get(Tier.SPIRITED))
                .define('i', Itms.CAPACITOR_NITRO)
                .define('o', Itms.DIELECTRIC_PASTE)
                .define('k', Itms.NITRO_CRYSTAL)
                .unlockedBy(getHasName(Items.REDSTONE_BLOCK), has(Items.REDSTONE_BLOCK))
                .unlockedBy(getHasName(Itms.BATTERY.get(Tier.SPIRITED)), has(Itms.BATTERY.get(Tier.SPIRITED)))
                .unlockedBy(getHasName(Itms.CAPACITOR_NITRO), has(Itms.CAPACITOR_NITRO))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.NITRO_CRYSTAL), has(Itms.NITRO_CRYSTAL))
                .save(output, Powah.id("battery_nitro"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.BATTERY.get(Tier.SPIRITED))
                .pattern("oko")
                .pattern("iri")
                .pattern("oco")
                .define('r', Items.REDSTONE_BLOCK)
                .define('c', Itms.BATTERY.get(Tier.NIOTIC))
                .define('i', Itms.CAPACITOR_SPIRITED)
                .define('o', Itms.DIELECTRIC_PASTE)
                .define('k', Itms.SPIRITED_CRYSTAL)
                .unlockedBy(getHasName(Items.REDSTONE_BLOCK), has(Items.REDSTONE_BLOCK))
                .unlockedBy(getHasName(Itms.BATTERY.get(Tier.NIOTIC)), has(Itms.BATTERY.get(Tier.NIOTIC)))
                .unlockedBy(getHasName(Itms.CAPACITOR_SPIRITED), has(Itms.CAPACITOR_SPIRITED))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .unlockedBy(getHasName(Itms.SPIRITED_CRYSTAL), has(Itms.SPIRITED_CRYSTAL))
                .save(output, Powah.id("battery_spirited"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Itms.BATTERY.get(Tier.STARTER))
                .pattern("oio")
                .pattern("ici")
                .pattern("oio")
                .define('c', Items.REDSTONE_BLOCK)
                .define('i', Itms.CAPACITOR_BASIC)
                .define('o', Itms.DIELECTRIC_PASTE)
                .unlockedBy(getHasName(Items.REDSTONE_BLOCK), has(Items.REDSTONE_BLOCK))
                .unlockedBy(getHasName(Itms.CAPACITOR_BASIC), has(Itms.CAPACITOR_BASIC))
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .save(output, Powah.id("battery_starter"));
    }

    private static void thermoGenerator(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.THERMO_GENERATOR.get(Tier.BASIC))
                .pattern("eie")
                .pattern("cdc")
                .pattern("ptp")
                .define('c', Itms.CAPACITOR_BASIC)
                .define('e', Itms.DIELECTRIC_PASTE)
                .define('i', Items.IRON_INGOT)
                .define('p', Itms.THERMOELECTRIC_PLATE)
                .define('d', Itms.DIELECTRIC_CASING)
                .define('t', Blcks.THERMO_GENERATOR.get(Tier.STARTER))
                .unlockedBy(getHasName(Blcks.THERMO_GENERATOR.get(Tier.STARTER)), has(Blcks.THERMO_GENERATOR.get(Tier.STARTER)))
                .save(output, Powah.id("thermo_generator_basic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.THERMO_GENERATOR.get(Tier.BLAZING))
                .pattern("igi")
                .pattern("cdc")
                .pattern("ptp")
                .define('c', Itms.CAPACITOR_BLAZING)
                .define('i', Itms.DIELECTRIC_PASTE)
                .define('g', Itms.BLAZING_CRYSTAL)
                .define('p', Itms.THERMOELECTRIC_PLATE)
                .define('d', Itms.DIELECTRIC_CASING)
                .define('t', Blcks.THERMO_GENERATOR.get(Tier.HARDENED))
                .unlockedBy(getHasName(Blcks.THERMO_GENERATOR.get(Tier.HARDENED)), has(Blcks.THERMO_GENERATOR.get(Tier.HARDENED)))
                .save(output, Powah.id("thermo_generator_blazing"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.THERMO_GENERATOR.get(Tier.HARDENED))
                .pattern("igi")
                .pattern("cdc")
                .pattern("ptp")
                .define('c', Itms.CAPACITOR_HARDENED)
                .define('i', Itms.DIELECTRIC_PASTE)
                .define('g', Itms.ENERGIZED_STEEL)
                .define('p', Itms.THERMOELECTRIC_PLATE)
                .define('d', Itms.DIELECTRIC_CASING)
                .define('t', Blcks.THERMO_GENERATOR.get(Tier.BASIC))
                .unlockedBy(getHasName(Blcks.THERMO_GENERATOR.get(Tier.BASIC)), has(Blcks.THERMO_GENERATOR.get(Tier.BASIC)))
                .save(output, Powah.id("thermo_generator_hardened"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.THERMO_GENERATOR.get(Tier.NIOTIC))
                .pattern("igi")
                .pattern("cdc")
                .pattern("ptp")
                .define('c', Itms.CAPACITOR_NIOTIC)
                .define('i', Itms.DIELECTRIC_PASTE)
                .define('g', Itms.NIOTIC_CRYSTAL)
                .define('p', Itms.THERMOELECTRIC_PLATE)
                .define('d', Itms.DIELECTRIC_CASING)
                .define('t', Blcks.THERMO_GENERATOR.get(Tier.BLAZING))
                .unlockedBy(getHasName(Blcks.THERMO_GENERATOR.get(Tier.BLAZING)), has(Blcks.THERMO_GENERATOR.get(Tier.BLAZING)))
                .save(output, Powah.id("thermo_generator_niotic"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.THERMO_GENERATOR.get(Tier.NITRO))
                .pattern("igi")
                .pattern("cdc")
                .pattern("ptp")
                .define('c', Itms.CAPACITOR_NITRO)
                .define('i', Itms.DIELECTRIC_PASTE)
                .define('g', Itms.NITRO_CRYSTAL)
                .define('p', Itms.THERMOELECTRIC_PLATE)
                .define('d', Itms.DIELECTRIC_CASING)
                .define('t', Blcks.THERMO_GENERATOR.get(Tier.SPIRITED))
                .unlockedBy(getHasName(Blcks.THERMO_GENERATOR.get(Tier.SPIRITED)), has(Blcks.THERMO_GENERATOR.get(Tier.SPIRITED)))
                .save(output, Powah.id("thermo_generator_nitro"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.THERMO_GENERATOR.get(Tier.SPIRITED))
                .pattern("igi")
                .pattern("cdc")
                .pattern("ptp")
                .define('c', Itms.CAPACITOR_SPIRITED)
                .define('i', Itms.DIELECTRIC_PASTE)
                .define('g', Itms.SPIRITED_CRYSTAL)
                .define('p', Itms.THERMOELECTRIC_PLATE)
                .define('d', Itms.DIELECTRIC_CASING)
                .define('t', Blcks.THERMO_GENERATOR.get(Tier.NIOTIC))
                .unlockedBy(getHasName(Blcks.THERMO_GENERATOR.get(Tier.NIOTIC)), has(Blcks.THERMO_GENERATOR.get(Tier.NIOTIC)))
                .save(output, Powah.id("thermo_generator_spirited"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blcks.THERMO_GENERATOR.get(Tier.STARTER))
                .pattern("iii")
                .pattern("cdc")
                .pattern("ppp")
                .define('c', Itms.CAPACITOR_BASIC_TINY)
                .define('i', Itms.DIELECTRIC_PASTE)
                .define('p', Itms.THERMOELECTRIC_PLATE)
                .define('d', Itms.DIELECTRIC_CASING)
                .unlockedBy(getHasName(Itms.DIELECTRIC_PASTE), has(Itms.DIELECTRIC_PASTE))
                .save(output, Powah.id("thermo_generator_starter"));
    }
}
