package owmii.powah.client.screen;

import owmii.lib.client.screen.Texture;
import owmii.lib.logistics.TransferType;
import owmii.powah.Powah;

import java.util.HashMap;
import java.util.Map;

public class Textures {
    private static final Texture.Builder BUILDER = new Texture.Builder(Powah.MOD_ID);
    public static final Texture BOOK = BUILDER.make("book/background", 196, 230, 0, 0);
    public static final Texture BOOK_NAV_R = BUILDER.make("book/background", 12, 23, 208, 58);
    public static final Texture BOOK_NAV_L = BUILDER.make("book/background", 12, 23, 196, 58);
    public static final Texture BOOK_HOME = BUILDER.make("book/background", 15, 15, 196, 81);
    public static final Texture BOOK_BACK = BUILDER.make("book/background", 10, 13, 196, 96);
    public static final Texture BOOK_ITEM_BG = BUILDER.make("book/background", 24, 24, 196, 0);
    public static final Texture BOOK_CAT = BUILDER.make("book/background", 30, 24, 220, 0);

    public static final Texture WIDE_ENERGY = BUILDER.make("container/wide_energy", 176, 141, 0, 0);
    public static final Texture WIDE_ENERGY_GAUGE = BUILDER.make("container/wide_energy", 164, 37, 0, 141);

    public static final Texture ENERGY_CELL = BUILDER.make("container/energy_cell", 176, 141, 0, 0);
    public static final Texture ENERGY_CELL_GAUGE = BUILDER.make("container/energy_cell", 139, 37, 0, 141);

    public static final Texture ENDER_CELL = BUILDER.make("container/ender_cell", 176, 164, 0, 0);
    public static final Texture ENDER_CELL_GAUGE = BUILDER.make("container/ender_cell", 139, 37, 0, 164);
    public static final Texture ENDER_CELL_BTN_0 = BUILDER.make("container/ender_cell", 12, 12, 176, 0);
    public static final Texture ENDER_CELL_BTN_1 = BUILDER.make("container/ender_cell", 12, 12, 188, 0);

    public static final Texture FURNATOR = BUILDER.make("container/furnator", 176, 166, 0, 0);
    public static final Texture FURNATOR_GAUGE = BUILDER.make("container/furnator", 14, 39, 176, 0);
    public static final Texture FURNATOR_CARBON_GAUGE = BUILDER.make("container/furnator", 4, 16, 190, 0);
    public static final Texture FURNATOR_BUFFER = BUILDER.make("container/furnator", 11, 18, 176, 39);

    public static final Texture MAGMATOR = BUILDER.make("container/magmator", 176, 166, 0, 0);
    public static final Texture MAGMATOR_BUFFER = BUILDER.make("container/magmator", 11, 18, 176, 39);

    public static final Texture THERMO = BUILDER.make("container/thermo", 176, 166, 0, 0);
    public static final Texture THERMO_GAUGE = BUILDER.make("container/thermo", 14, 39, 176, 0);

    public static final Texture PLAYER_TRANSMITTER = BUILDER.make("container/player_transmitter", 176, 141, 0, 0);
    public static final Texture PLAYER_TRANSMITTER_GAUGE = BUILDER.make("container/player_transmitter", 139, 37, 0, 141);
    public static final Texture PLAYER_TRANSMITTER_ON = BUILDER.make("container/player_transmitter", 6, 6, 176, 0);

    public static final Texture REACTOR = BUILDER.make("container/reactor", 176, 166, 0, 0);
    public static final Texture REACTOR_GAUGE = BUILDER.make("container/reactor", 14, 39, 176, 0);
    public static final Texture REACTOR_GAUGE_URN = BUILDER.make("container/reactor", 5, 48, 190, 16);
    public static final Texture REACTOR_GAUGE_CARBON = BUILDER.make("container/reactor", 5, 16, 190, 0);
    public static final Texture REACTOR_GAUGE_REDSTONE = BUILDER.make("container/reactor", 5, 16, 195, 0);
    public static final Texture REACTOR_GAUGE_COOLANT = BUILDER.make("container/reactor", 5, 16, 200, 0);
    public static final Texture REACTOR_GAUGE_TEMP = BUILDER.make("container/reactor", 4, 18, 205, 0);
    public static final Texture REACTOR_GEN_MODE_BG = BUILDER.make("container/reactor", 15, 16, 209, 0);
    public static final Map<Boolean, Texture> REACTOR_GEN_MODE = new HashMap<>();
    public static final Texture REACTOR_GEN_MODE_OFF = BUILDER.make("container/reactor", 8, 8, 224, 0);
    public static final Texture REACTOR_GEN_MODE_ON = BUILDER.make("container/reactor", 8, 8, 224, 8);

    public static final Texture DISCHARGER = BUILDER.make("container/discharger", 176, 166, 0, 0);
    public static final Texture DISCHARGER_GAUGE = BUILDER.make("container/discharger", 164, 37, 0, 166);

    public static final Map<TransferType, Texture> CABLE_CONFIG = new HashMap<>();
    public static final Texture CABLE = BUILDER.make("container/cable", 153, 29, 0, 0);
    public static final Texture CABLE_ALL = BUILDER.make("container/cable", 18, 19, 153, 0);
    public static final Texture CABLE_OUT = BUILDER.make("container/cable", 18, 19, 171, 0);
    public static final Texture CABLE_IN = BUILDER.make("container/cable", 18, 19, 189, 0);
    public static final Texture CABLE_OFF = BUILDER.make("container/cable", 18, 19, 207, 0);

    static {
        CABLE_CONFIG.put(TransferType.ALL, CABLE_ALL);
        CABLE_CONFIG.put(TransferType.EXTRACT, CABLE_OUT);
        CABLE_CONFIG.put(TransferType.RECEIVE, CABLE_IN);
        CABLE_CONFIG.put(TransferType.NONE, CABLE_OFF);
        REACTOR_GEN_MODE.put(true, REACTOR_GEN_MODE_ON);
        REACTOR_GEN_MODE.put(false, REACTOR_GEN_MODE_OFF);
    }
}
