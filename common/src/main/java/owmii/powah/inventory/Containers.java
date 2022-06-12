package owmii.powah.inventory;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;
import owmii.powah.Powah;

import java.util.function.Supplier;

public class Containers {
    public static final DeferredRegister<MenuType<?>> DR = DeferredRegister.create(Powah.MOD_ID, Registry.MENU_REGISTRY);

    public static final Supplier<MenuType<EnergyCellContainer>> ENERGY_CELL = DR.register("energy_cell", () -> MenuRegistry.ofExtended(EnergyCellContainer::create));
    public static final Supplier<MenuType<EnderCellContainer>> ENDER_CELL = DR.register("ender_cell", () -> MenuRegistry.ofExtended(EnderCellContainer::create));
    public static final Supplier<MenuType<FurnatorContainer>> FURNATOR = DR.register("furnator", () -> MenuRegistry.ofExtended(FurnatorContainer::create));
    public static final Supplier<MenuType<MagmatorContainer>> MAGMATOR = DR.register("magmator", () -> MenuRegistry.ofExtended(MagmatorContainer::create));
    public static final Supplier<MenuType<PlayerTransmitterContainer>> PLAYER_TRANSMITTER = DR.register("player_transmitter", () -> MenuRegistry.ofExtended(PlayerTransmitterContainer::create));
    public static final Supplier<MenuType<EnergyHopperContainer>> ENERGY_HOPPER = DR.register("energy_hopper", () -> MenuRegistry.ofExtended(EnergyHopperContainer::create));
    public static final Supplier<MenuType<CableContainer>> CABLE = DR.register("cable", () -> MenuRegistry.ofExtended(CableContainer::create));
    public static final Supplier<MenuType<ReactorContainer>> REACTOR = DR.register("reactor", () -> MenuRegistry.ofExtended(ReactorContainer::create));
    public static final Supplier<MenuType<SolarContainer>> SOLAR = DR.register("solar", () -> MenuRegistry.ofExtended(SolarContainer::create));
    public static final Supplier<MenuType<ThermoContainer>> THERMO = DR.register("thermo", () -> MenuRegistry.ofExtended(ThermoContainer::create));
    public static final Supplier<MenuType<DischargerContainer>> DISCHARGER = DR.register("discharger", () -> MenuRegistry.ofExtended(DischargerContainer::create));
}
