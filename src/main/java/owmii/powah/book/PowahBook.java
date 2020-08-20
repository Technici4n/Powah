package owmii.powah.book;

import owmii.powah.block.Blcks;
import owmii.powah.book.content.BookEntry;
import owmii.powah.book.content.page.BookPage;
import owmii.powah.config.Configs;
import owmii.powah.item.Itms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PowahBook {
    private static final List<BookEntry> MAIN_ENTRIES = new ArrayList<>();
    private static final List<BookEntry> ENTRIES = new ArrayList<>();

    public static final BookEntry HOME = registerMain(BookEntry.create("home").icon("icons/home").add(BookPage.create().setHome()));
    public static final BookEntry ENERGIZING = register(BookEntry.create("energizing").icon(Blcks.ENERGIZING_ORB)
            .add(BookPage.create().image("images/energizing", 180, 79).paragraph("energizing", (Configs.ENERGIZING.range.get() * 2 + 1) + "X" + (Configs.ENERGIZING.range.get() * 2 + 1)))
            .add(BookPage.create().image("images/energizing", 180, 79).title("energizing").text("energizing_2")));
    public static final BookEntry ENERGY_CELL = register(BookEntry.create("energy_cells").icon(Blcks.ENERGY_CELL_BASIC)
            .add(BookPage.create().paragraph("energy_cells")));
    public static final BookEntry ENDER_CELL = register(BookEntry.create("ender_cells").icon(Blcks.ENDER_CELL_BASIC)
            .add(BookPage.create().paragraph("ender_cells")));
    public static final BookEntry FURNATOR = register(BookEntry.create("furnators").icon(Blcks.FURNATOR_BASIC)
            .add(BookPage.create().image("images/furnator", 180, 80).paragraph("furnators")));
    public static final BookEntry MAGMATIC_GENERATOR = register(BookEntry.create("magmators").icon(Blcks.MAGMATOR_BASIC)
            .add(BookPage.create().image("images/magmator", 180, 72).paragraph("magmators")));
    public static final BookEntry THERMOELECTRIC_GENERATOR = register(BookEntry.create("thermo_generators").icon(Blcks.THERMO_BASIC)
            .add(BookPage.create().image("images/thermo_generator", 180, 80).paragraph("thermo_generators")));
    public static final BookEntry SOLAR_PANEL = register(BookEntry.create("solar_panels").icon(Blcks.SOLAR_PANEL_BASIC)
            .add(BookPage.create().image("images/solar_panel", 180, 78).paragraph("solar_panels")));
    public static final BookEntry REACTOR = register(BookEntry.create("reactors").icon(Blcks.REACTOR_BASIC)
            .add(BookPage.create().image("images/reactor", 180, 63).paragraph("reactors"))
            .add(BookPage.create().image("images/reactor_gui", 180, 74).paragraph("reactors_cooling"))
            .add(BookPage.create().image("images/reactor_gui", 180, 74).paragraph("reactors_m"))
            .add(BookPage.create().image("images/reactor_gui", 180, 74).paragraph("reactors_redstone")));
    public static final BookEntry CABLE = register(BookEntry.create("cables").icon(Blcks.CABLE_BASIC))
            .add(BookPage.create().item(Blcks.CABLE_BASIC).paragraph("cables"));
    public static final BookEntry ENDER_GATE = register(BookEntry.create("ender_gates").icon(Blcks.ENDER_GATE_BASIC))
            .add(BookPage.create().item(Blcks.ENDER_GATE_BASIC).paragraph("ender_gates"));
    public static final BookEntry DISCHARGER = register(BookEntry.create("discharger").icon(Blcks.ENERGY_DISCHARGER_BASIC)
            .add(BookPage.create().item(Blcks.ENERGY_DISCHARGER_BASIC).info().text("discharger")));
    public static final BookEntry ENERGY_HOPPER = register(BookEntry.create("energy_hopper").icon(Blcks.ENERGY_HOPPER_BASIC)
            .add(BookPage.create().image("images/energy_hopper", 180, 79).paragraph("energy_hopper")));
    public static final BookEntry PLAYER_TRANSMITTER = register(BookEntry.create("player_transmitters").icon(Blcks.PLAYER_TRANSMITTER_BASIC)
            .add(BookPage.create().image("images/player_transmitter", 180, 76).paragraph("player_transmitters")));
    public static final BookEntry BLOCKS = registerMain(BookEntry.create("blocks").icon(Blcks.REACTOR_HARDENED)
            .add(BookPage.create().add(ENERGIZING).add(ENERGY_CELL).add(ENDER_CELL).add(FURNATOR)
                    .add(MAGMATIC_GENERATOR).add(THERMOELECTRIC_GENERATOR).add(SOLAR_PANEL)
                    .add(REACTOR).add(CABLE).add(ENDER_GATE).add(DISCHARGER).add(ENERGY_HOPPER).add(PLAYER_TRANSMITTER)));

    public static final BookEntry WRENCH = register(BookEntry.create("wrench").icon(Itms.WRENCH)
            .add(BookPage.create().item(Itms.WRENCH).text("wrench")));
    public static final BookEntry BATTERY = register(BookEntry.create("battery").icon(Itms.BATTERY_BASIC)
            .add(BookPage.create().item(Itms.BATTERY_BASIC).text("battery")));
    public static final BookEntry PLAYER_AERIAL_PEARL = register(BookEntry.create("player_aerial_pearl").icon(Itms.PLAYER_AERIAL_PEARL)
            .add(BookPage.create().item(Itms.PLAYER_AERIAL_PEARL).text("player_aerial_pearl")));
    public static final BookEntry BINDING_CARD = register(BookEntry.create("binding_card").icon(Itms.BINDING_CARD)
            .add(BookPage.create().item(Itms.BINDING_CARD).text("binding_card"))
            .add(BookPage.create().item(Itms.BINDING_CARD_DIM).text("binding_card_dim")));
    public static final BookEntry CHARGED_SNOWBALL = register(BookEntry.create("charged_snowball").icon(Itms.CHARGED_SNOWBALL)
            .add(BookPage.create().item(Itms.CHARGED_SNOWBALL).text("charged_snowball")));
    public static final BookEntry LENS_OF_ENDER = register(BookEntry.create("lens_of_ender").icon(Itms.LENS_OF_ENDER)
            .add(BookPage.create().item(Itms.LENS_OF_ENDER).text("lens_of_ender")));
    public static final BookEntry ITEMS = registerMain(BookEntry.create("items").icon(Itms.WRENCH)
            .add(BookPage.create().add(WRENCH).add(BATTERY).add(PLAYER_AERIAL_PEARL).add(BINDING_CARD).add(CHARGED_SNOWBALL).add(LENS_OF_ENDER)));

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
