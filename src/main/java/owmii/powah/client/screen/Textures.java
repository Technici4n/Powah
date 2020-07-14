package owmii.powah.client.screen;

import net.minecraft.util.ResourceLocation;
import owmii.lib.client.screen.Texture;
import owmii.lib.logistics.TransferType;
import owmii.powah.Powah;

import java.util.HashMap;
import java.util.Map;

public class Textures {
    public static final Texture WIDE_ENERGY = register("container/wide_energy", 176, 141, 0, 0);
    public static final Texture WIDE_ENERGY_GAUGE = register("container/wide_energy", 164, 37, 0, 141);

    public static final Texture ENERGY_CELL = register("container/energy_cell", 176, 141, 0, 0);
    public static final Texture ENERGY_CELL_GAUGE = register("container/energy_cell", 139, 37, 0, 141);

    public static final Texture ENDER_CELL = register("container/ender_cell", 176, 164, 0, 0);
    public static final Texture ENDER_CELL_GAUGE = register("container/ender_cell", 139, 37, 0, 164);
    public static final Texture ENDER_CELL_BTN_0 = register("container/ender_cell", 12, 12, 176, 0);
    public static final Texture ENDER_CELL_BTN_1 = register("container/ender_cell", 12, 12, 188, 0);

    public static final Texture FURNATOR = register("container/furnator", 176, 166, 0, 0);
    public static final Texture FURNATOR_GAUGE = register("container/furnator", 14, 39, 176, 0);
    public static final Texture FURNATOR_CARBON_GAUGE = register("container/furnator", 4, 16, 190, 0);
    public static final Texture FURNATOR_BUFFER = register("container/furnator", 11, 18, 176, 39);

    public static final Texture MAGMATOR = register("container/magmator", 176, 166, 0, 0);
    public static final Texture MAGMATOR_BUFFER = register("container/magmator", 11, 18, 176, 39);

    public static final Texture THERMO = register("container/thermo", 176, 166, 0, 0);
    public static final Texture THERMO_GAUGE = register("container/thermo", 11, 18, 176, 39);

    public static final Texture PLAYER_TRANSMITTER = register("container/player_transmitter", 176, 141, 0, 0);
    public static final Texture PLAYER_TRANSMITTER_GAUGE = register("container/player_transmitter", 139, 37, 0, 141);
    public static final Texture PLAYER_TRANSMITTER_ON = register("container/player_transmitter", 6, 6, 176, 0);

    public static final Texture REACTOR = register("container/reactor", 176, 166, 0, 0);
    public static final Texture REACTOR_GAUGE = register("container/reactor", 14, 39, 176, 0);
    public static final Texture REACTOR_GAUGE_URN = register("container/reactor", 5, 48, 190, 16);
    public static final Texture REACTOR_GAUGE_CARBON = register("container/reactor", 5, 16, 190, 0);
    public static final Texture REACTOR_GAUGE_REDSTONE = register("container/reactor", 5, 16, 195, 0);
    public static final Texture REACTOR_GAUGE_COOLANT = register("container/reactor", 5, 16, 200, 0);
    public static final Texture REACTOR_GAUGE_TEMP = register("container/reactor", 4, 18, 205, 0);

    public static final Map<TransferType, Texture> CABLE_CONFIG = new HashMap<>();
    public static final Texture CABLE = register("container/cable", 153, 29, 0, 0);
    public static final Texture CABLE_ALL = register("container/cable", 18, 19, 153, 0);
    public static final Texture CABLE_OUT = register("container/cable", 18, 19, 171, 0);
    public static final Texture CABLE_IN = register("container/cable", 18, 19, 189, 0);
    public static final Texture CABLE_OFF = register("container/cable", 18, 19, 207, 0);

    static Texture register(String path, int width, int height, int u, int v) {
        return new Texture(new ResourceLocation(Powah.MOD_ID, "textures/gui/" + path + ".png"), width, height, u, v);
    }

    static {
        CABLE_CONFIG.put(TransferType.ALL, CABLE_ALL);
        CABLE_CONFIG.put(TransferType.EXTRACT, CABLE_OUT);
        CABLE_CONFIG.put(TransferType.RECEIVE, CABLE_IN);
        CABLE_CONFIG.put(TransferType.NONE, CABLE_OFF);
    }
}
