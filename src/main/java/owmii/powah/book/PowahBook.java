package owmii.powah.book;

import owmii.powah.block.IBlocks;
import owmii.powah.block.cable.Cables;
import owmii.powah.block.endercell.EnderCells;
import owmii.powah.block.endergate.EnderGates;
import owmii.powah.block.energizing.EnergizingRods;
import owmii.powah.block.energycell.EnergyCells;
import owmii.powah.block.generator.furnator.Furnators;
import owmii.powah.block.generator.magmatic.MagmaticGenerators;
import owmii.powah.block.generator.panel.solar.SolarPanels;
import owmii.powah.block.generator.reactor.Reactors;
import owmii.powah.block.generator.thermoelectric.ThermoGenerators;
import owmii.powah.book.content.BookEntry;
import owmii.powah.book.content.page.BookPage;
import owmii.powah.config.Config;
import owmii.powah.item.IItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PowahBook {
    private static final List<BookEntry> MAIN_ENTRIES = new ArrayList<>();
    private static final List<BookEntry> ENTRIES = new ArrayList<>();

    public static final BookEntry HOME = registerMain(BookEntry.create("home").icon("icons/home").add(BookPage.create().setHome()));
    public static final BookEntry ENERGIZING = register(BookEntry.create("energizing").icon(IBlocks.ENERGIZING_ORB)
            .add(BookPage.create().image("images/energizing", 180, 87).paragraph("energizing", (Config.ENERGIZING_CONFIG.range.get() * 2 + 1) + "X" + (Config.ENERGIZING_CONFIG.range.get() * 2 + 1)))
            .add(BookPage.create().image("images/energizing", 180, 87).title("energizing").text("energizing_2"))
            .add(BookPage.create().item(EnergizingRods.BASIC.get()).info())
            .add(BookPage.create().item(EnergizingRods.HARDENED.get()).info())
            .add(BookPage.create().item(EnergizingRods.BLAZING.get()).info())
            .add(BookPage.create().item(EnergizingRods.NIOTIC.get()).info())
            .add(BookPage.create().item(EnergizingRods.SPIRITED.get()).info()));
    public static final BookEntry ENERGY_CELL = register(BookEntry.create("energy_cells").icon(EnergyCells.BASIC.get())
            .add(BookPage.create().paragraph("energy_cells"))
            .add(BookPage.create().item(EnergyCells.BASIC.get()).info())
            .add(BookPage.create().item(EnergyCells.HARDENED.get()).info())
            .add(BookPage.create().item(EnergyCells.BLAZING.get()).info())
            .add(BookPage.create().item(EnergyCells.NIOTIC.get()).info())
            .add(BookPage.create().item(EnergyCells.SPIRITED.get()).info()));
    public static final BookEntry ENDER_CELL = register(BookEntry.create("ender_cells").icon(EnderCells.BASIC.get())
            .add(BookPage.create().paragraph("ender_cells"))
            .add(BookPage.create().item(EnderCells.BASIC.get()).info())
            .add(BookPage.create().item(EnderCells.HARDENED.get()).info())
            .add(BookPage.create().item(EnderCells.BLAZING.get()).info())
            .add(BookPage.create().item(EnderCells.NIOTIC.get()).info())
            .add(BookPage.create().item(EnderCells.SPIRITED.get()).info()));
    public static final BookEntry FURNATOR = register(BookEntry.create("furnators").icon(Furnators.BASIC.get())
            .add(BookPage.create().image("images/furnator", 180, 69).paragraph("furnators"))
            .add(BookPage.create().item(Furnators.BASIC.get()).info())
            .add(BookPage.create().item(Furnators.HARDENED.get()).info())
            .add(BookPage.create().item(Furnators.BLAZING.get()).info())
            .add(BookPage.create().item(Furnators.NIOTIC.get()).info())
            .add(BookPage.create().item(Furnators.SPIRITED.get()).info()));
    public static final BookEntry MAGMATIC_GENERATOR = register(BookEntry.create("magmatic_generators").icon(MagmaticGenerators.BASIC.get())
            .add(BookPage.create().image("images/magmatic_generator", 180, 69).paragraph("magmatic_generators"))
            .add(BookPage.create().item(MagmaticGenerators.BASIC.get()).info())
            .add(BookPage.create().item(MagmaticGenerators.HARDENED.get()).info())
            .add(BookPage.create().item(MagmaticGenerators.BLAZING.get()).info())
            .add(BookPage.create().item(MagmaticGenerators.NIOTIC.get()).info())
            .add(BookPage.create().item(MagmaticGenerators.SPIRITED.get()).info()));
    public static final BookEntry THERMOELECTRIC_GENERATOR = register(BookEntry.create("thermo_generators").icon(ThermoGenerators.BASIC.get())
            .add(BookPage.create().image("images/thermo_generator", 180, 85).paragraph("thermo_generators"))
            .add(BookPage.create().item(ThermoGenerators.BASIC.get()).info())
            .add(BookPage.create().item(ThermoGenerators.HARDENED.get()).info())
            .add(BookPage.create().item(ThermoGenerators.BLAZING.get()).info())
            .add(BookPage.create().item(ThermoGenerators.NIOTIC.get()).info())
            .add(BookPage.create().item(ThermoGenerators.SPIRITED.get()).info()));
    public static final BookEntry SOLAR_PANEL = register(BookEntry.create("solar_panels").icon(SolarPanels.BASIC.get())
            .add(BookPage.create().image("images/solar_panel", 180, 63).paragraph("solar_panels"))
            .add(BookPage.create().item(SolarPanels.BASIC.get()).info())
            .add(BookPage.create().item(SolarPanels.HARDENED.get()).info())
            .add(BookPage.create().item(SolarPanels.BLAZING.get()).info())
            .add(BookPage.create().item(SolarPanels.NIOTIC.get()).info())
            .add(BookPage.create().item(SolarPanels.SPIRITED.get()).info()));
    public static final BookEntry REACTOR = register(BookEntry.create("reactors").icon(Reactors.BASIC.get())
            .add(BookPage.create().image("images/reactor", 180, 63).paragraph("reactors"))
            .add(BookPage.create().image("images/reactor_gui", 180, 74).paragraph("reactors_cooling"))
            .add(BookPage.create().image("images/reactor_gui", 180, 74).paragraph("reactors_m"))
            .add(BookPage.create().item(Reactors.BASIC.get()).info())
            .add(BookPage.create().item(Reactors.HARDENED.get()).info())
            .add(BookPage.create().item(Reactors.BLAZING.get()).info())
            .add(BookPage.create().item(Reactors.NIOTIC.get()).info())
            .add(BookPage.create().item(Reactors.SPIRITED.get()).info()));
    public static final BookEntry CABLE = register(BookEntry.create("cables").icon(Cables.BASIC.get())
            .add(BookPage.create().item(Cables.BASIC.get()).info().text("cables"))
            .add(BookPage.create().item(Cables.HARDENED.get()).info().text("cables"))
            .add(BookPage.create().item(Cables.BLAZING.get()).info().text("cables"))
            .add(BookPage.create().item(Cables.NIOTIC.get()).info().text("cables"))
            .add(BookPage.create().item(Cables.SPIRITED.get()).info().text("cables")));
    public static final BookEntry ENDER_GATE = register(BookEntry.create("ender_gates").icon(EnderGates.BASIC.get())
            .add(BookPage.create().item(EnderGates.BASIC.get()).info().text("ender_gates"))
            .add(BookPage.create().item(EnderGates.HARDENED.get()).info().text("ender_gates"))
            .add(BookPage.create().item(EnderGates.BLAZING.get()).info().text("ender_gates"))
            .add(BookPage.create().item(EnderGates.NIOTIC.get()).info().text("ender_gates"))
            .add(BookPage.create().item(EnderGates.SPIRITED.get()).info().text("ender_gates")));
    public static final BookEntry DISCHARGER = register(BookEntry.create("discharger").icon(IBlocks.DISCHARGER)
            .add(BookPage.create().item(IBlocks.DISCHARGER).info().text("discharger")));
    public static final BookEntry ENERGY_HOPPER = register(BookEntry.create("energy_hopper").icon(IBlocks.ENERGY_HOPPER)
            .add(BookPage.create().image("images/energy_hopper", 180, 90).paragraph("energy_hopper"))
            .add(BookPage.create().item(IBlocks.ENERGY_HOPPER).info()));
    public static final BookEntry PLAYER_TRANSMITTER = register(BookEntry.create("player_transmitters").icon(IBlocks.PLAYER_TRANSMITTER)
            .add(BookPage.create().image("images/player_transmitter", 180, 77).paragraph("player_transmitters"))
            .add(BookPage.create().item(IBlocks.PLAYER_TRANSMITTER).info())
            .add(BookPage.create().item(IBlocks.PLAYER_TRANSMITTER_DIM).info()));
    public static final BookEntry BLOCKS = registerMain(BookEntry.create("blocks").icon(Reactors.HARDENED.get())
            .add(BookPage.create().add(ENERGIZING).add(ENERGY_CELL).add(ENDER_CELL).add(FURNATOR)
                    .add(MAGMATIC_GENERATOR).add(THERMOELECTRIC_GENERATOR).add(SOLAR_PANEL)
                    .add(REACTOR).add(CABLE).add(ENDER_GATE).add(DISCHARGER).add(ENERGY_HOPPER).add(PLAYER_TRANSMITTER)));

    public static final BookEntry WRENCH = register(BookEntry.create("wrench").icon(IItems.WRENCH)
            .add(BookPage.create().item(IItems.WRENCH).text("wrench")));
    public static final BookEntry PLAYER_AERIAL_PEARL = register(BookEntry.create("player_aerial_pearl").icon(IItems.PLAYER_AERIAL_PEARL)
            .add(BookPage.create().item(IItems.PLAYER_AERIAL_PEARL).text("player_aerial_pearl")));
    public static final BookEntry BINDING_CARD = register(BookEntry.create("binding_card").icon(IItems.BINDING_CARD)
            .add(BookPage.create().item(IItems.BINDING_CARD).text("binding_card")));
    public static final BookEntry CHARGED_SNOWBALL = register(BookEntry.create("charged_snowball").icon(IItems.CHARGED_SNOWBALL)
            .add(BookPage.create().item(IItems.CHARGED_SNOWBALL).text("charged_snowball")));
    public static final BookEntry ITEMS = registerMain(BookEntry.create("items").icon(IItems.WRENCH)
            .add(BookPage.create().add(WRENCH).add(PLAYER_AERIAL_PEARL).add(BINDING_CARD).add(CHARGED_SNOWBALL)));

    //public static final BookEntry RESOURCES = registerMain(BookEntry.create("resources").icon(IItems.THERMOELECTRIC_PLATE).add(BookPage.create()));
    // public static final BookEntry ABOUT = registerMain(BookEntry.create("about").icon("icons/about").add(BookPage.create()));

    private static BookEntry registerMain(BookEntry entry) {
        MAIN_ENTRIES.add(entry);
        return entry;
    }

    private static BookEntry register(BookEntry entry) {
        ENTRIES.add(entry);
        return entry;
    }

    public static void register() {
    }

    public static BookEntry get(int i) {
        return MAIN_ENTRIES.get(i);
    }

    public static List<BookEntry> getMainEntries() {
        return Collections.unmodifiableList(MAIN_ENTRIES);
    }
}
