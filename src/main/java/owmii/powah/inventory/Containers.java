package owmii.powah.inventory;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import owmii.powah.Powah;

public class Containers {
    public static final DeferredRegister<MenuType<?>> DR = DeferredRegister.create(Registries.MENU, Powah.MOD_ID);

    public static final Supplier<MenuType<EnergyCellContainer>> ENERGY_CELL = DR.register("energy_cell",
            () -> IMenuTypeExtension.create(EnergyCellContainer::create));
    public static final Supplier<MenuType<EnderCellContainer>> ENDER_CELL = DR.register("ender_cell",
            () -> IMenuTypeExtension.create(EnderCellContainer::create));
    public static final Supplier<MenuType<FurnatorContainer>> FURNATOR = DR.register("furnator",
            () -> IMenuTypeExtension.create(FurnatorContainer::create));
    public static final Supplier<MenuType<MagmatorContainer>> MAGMATOR = DR.register("magmator",
            () -> IMenuTypeExtension.create(MagmatorContainer::create));
    public static final Supplier<MenuType<PlayerTransmitterContainer>> PLAYER_TRANSMITTER = DR.register("player_transmitter",
            () -> IMenuTypeExtension.create(PlayerTransmitterContainer::create));
    public static final Supplier<MenuType<EnergyHopperContainer>> ENERGY_HOPPER = DR.register("energy_hopper",
            () -> IMenuTypeExtension.create(EnergyHopperContainer::create));
    public static final Supplier<MenuType<CableContainer>> CABLE = DR.register("cable", () -> IMenuTypeExtension.create(CableContainer::create));
    public static final Supplier<MenuType<ReactorContainer>> REACTOR = DR.register("reactor",
            () -> IMenuTypeExtension.create(ReactorContainer::create));
    public static final Supplier<MenuType<SolarContainer>> SOLAR = DR.register("solar", () -> IMenuTypeExtension.create(SolarContainer::create));
    public static final Supplier<MenuType<ThermoContainer>> THERMO = DR.register("thermo", () -> IMenuTypeExtension.create(ThermoContainer::create));
    public static final Supplier<MenuType<DischargerContainer>> DISCHARGER = DR.register("discharger",
            () -> IMenuTypeExtension.create(DischargerContainer::create));
}
