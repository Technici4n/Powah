package owmii.powah.inventory;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import owmii.lib.registry.Registry;
import owmii.powah.Powah;

public class Containers {
    public static final Registry<ContainerType<?>> REG = new Registry<>(Powah.MOD_ID);
    public static final ContainerType<EnergyCellContainer> ENERGY_CELL = REG.register("energy_cell", IForgeContainerType.create(EnergyCellContainer::create));
    public static final ContainerType<EnderCellContainer> ENDER_CELL = REG.register("ender_cell", IForgeContainerType.create(EnderCellContainer::create));
    public static final ContainerType<FurnatorContainer> FURNATOR = REG.register("furnator", IForgeContainerType.create(FurnatorContainer::create));
    public static final ContainerType<MagmatorContainer> MAGMATOR = REG.register("magmator", IForgeContainerType.create(MagmatorContainer::create));
    public static final ContainerType<PlayerTransmitterContainer> PLAYER_TRANSMITTER = REG.register("player_transmitter", IForgeContainerType.create(PlayerTransmitterContainer::create));
    public static final ContainerType<EnergyHopperContainer> ENERGY_HOPPER = REG.register("energy_hopper", IForgeContainerType.create(EnergyHopperContainer::create));
    public static final ContainerType<CableContainer> CABLE = REG.register("cable", IForgeContainerType.create(CableContainer::create));
    public static final ContainerType<ReactorContainer> REACTOR = REG.register("reactor", IForgeContainerType.create(ReactorContainer::create));
    public static final ContainerType<SolarContainer> SOLAR = REG.register("solar", IForgeContainerType.create(SolarContainer::create));
    public static final ContainerType<ThermoContainer> THERMO = REG.register("thermo", IForgeContainerType.create(ThermoContainer::create));
    public static final ContainerType<DischargerContainer> DISCHARGER = REG.register("discharger", IForgeContainerType.create(DischargerContainer::create));
}
