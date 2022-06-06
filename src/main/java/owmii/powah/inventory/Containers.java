package owmii.powah.inventory;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import owmii.lib.registry.Registry;
import owmii.powah.Powah;

import java.util.function.Supplier;

public class Containers {
    public static final DeferredRegister<MenuType<?>> DR = DeferredRegister.create(ForgeRegistries.CONTAINERS, Powah.MOD_ID);

    public static final Supplier<MenuType<EnergyCellContainer>> ENERGY_CELL = DR.register("energy_cell", () -> IForgeMenuType.create(EnergyCellContainer::create));
    public static final Supplier<MenuType<EnderCellContainer>> ENDER_CELL = DR.register("ender_cell", () -> IForgeMenuType.create(EnderCellContainer::create));
    public static final Supplier<MenuType<FurnatorContainer>> FURNATOR = DR.register("furnator", () -> IForgeMenuType.create(FurnatorContainer::create));
    public static final Supplier<MenuType<MagmatorContainer>> MAGMATOR = DR.register("magmator", () -> IForgeMenuType.create(MagmatorContainer::create));
    public static final Supplier<MenuType<PlayerTransmitterContainer>> PLAYER_TRANSMITTER = DR.register("player_transmitter", () -> IForgeMenuType.create(PlayerTransmitterContainer::create));
    public static final Supplier<MenuType<EnergyHopperContainer>> ENERGY_HOPPER = DR.register("energy_hopper", () -> IForgeMenuType.create(EnergyHopperContainer::create));
    public static final Supplier<MenuType<CableContainer>> CABLE = DR.register("cable", () -> IForgeMenuType.create(CableContainer::create));
    public static final Supplier<MenuType<ReactorContainer>> REACTOR = DR.register("reactor", () -> IForgeMenuType.create(ReactorContainer::create));
    public static final Supplier<MenuType<SolarContainer>> SOLAR = DR.register("solar", () -> IForgeMenuType.create(SolarContainer::create));
    public static final Supplier<MenuType<ThermoContainer>> THERMO = DR.register("thermo", () -> IForgeMenuType.create(ThermoContainer::create));
    public static final Supplier<MenuType<DischargerContainer>> DISCHARGER = DR.register("discharger", () -> IForgeMenuType.create(DischargerContainer::create));
}
