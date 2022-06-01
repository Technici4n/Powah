package owmii.powah.inventory;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import owmii.lib.registry.Registry;
import owmii.powah.Powah;

public class Containers {
    @SuppressWarnings("unchecked")
    public static final Registry<MenuType<?>> REG = new Registry(MenuType.class, Powah.MOD_ID);
    public static final MenuType<EnergyCellContainer> ENERGY_CELL = REG.register("energy_cell", IForgeMenuType.create(EnergyCellContainer::create));
    public static final MenuType<EnderCellContainer> ENDER_CELL = REG.register("ender_cell", IForgeMenuType.create(EnderCellContainer::create));
    public static final MenuType<FurnatorContainer> FURNATOR = REG.register("furnator", IForgeMenuType.create(FurnatorContainer::create));
    public static final MenuType<MagmatorContainer> MAGMATOR = REG.register("magmator", IForgeMenuType.create(MagmatorContainer::create));
    public static final MenuType<PlayerTransmitterContainer> PLAYER_TRANSMITTER = REG.register("player_transmitter", IForgeMenuType.create(PlayerTransmitterContainer::create));
    public static final MenuType<EnergyHopperContainer> ENERGY_HOPPER = REG.register("energy_hopper", IForgeMenuType.create(EnergyHopperContainer::create));
    public static final MenuType<CableContainer> CABLE = REG.register("cable", IForgeMenuType.create(CableContainer::create));
    public static final MenuType<ReactorContainer> REACTOR = REG.register("reactor", IForgeMenuType.create(ReactorContainer::create));
    public static final MenuType<SolarContainer> SOLAR = REG.register("solar", IForgeMenuType.create(SolarContainer::create));
    public static final MenuType<ThermoContainer> THERMO = REG.register("thermo", IForgeMenuType.create(ThermoContainer::create));
    public static final MenuType<DischargerContainer> DISCHARGER = REG.register("discharger", IForgeMenuType.create(DischargerContainer::create));
}
