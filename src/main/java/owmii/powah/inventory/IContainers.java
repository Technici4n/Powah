package owmii.powah.inventory;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.IContainerFactory;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IContainers {
    public static final List<ContainerType<?>> CONTAINER_TYPES = new ArrayList<>();
    public static final ContainerType<EnergyCellContainer> ENERGY_CELL_BASIC = register("energy_cell_basic", EnergyCellContainer::createBasic);
    public static final ContainerType<EnergyCellContainer> ENERGY_CELL_HARDENED = register("energy_cell_hardened", EnergyCellContainer::createHardened);
    public static final ContainerType<EnergyCellContainer> ENERGY_CELL_BLAZING = register("energy_cell_blazing", EnergyCellContainer::createBlazing);
    public static final ContainerType<EnergyCellContainer> ENERGY_CELL_NIOTIC = register("energy_cell_niotic", EnergyCellContainer::createNiotic);
    public static final ContainerType<EnergyCellContainer> ENERGY_CELL_SPIRITED = register("energy_cell_spirited", EnergyCellContainer::createSpirited);
    public static final ContainerType<EnergyCellContainer> ENERGY_CELL_CREATIVE = register("energy_cell_creative", EnergyCellContainer::createCreative);

    public static final ContainerType<EnderCellContainer> ENDER_CELL_BASIC = register("ender_cell_basic", EnderCellContainer::createBasic);
    public static final ContainerType<EnderCellContainer> ENDER_CELL_HARDENED = register("ender_cell_hardened", EnderCellContainer::createHardened);
    public static final ContainerType<EnderCellContainer> ENDER_CELL_BLAZING = register("ender_cell_blazing", EnderCellContainer::createBlazing);
    public static final ContainerType<EnderCellContainer> ENDER_CELL_NIOTIC = register("ender_cell_niotic", EnderCellContainer::createNiotic);
    public static final ContainerType<EnderCellContainer> ENDER_CELL_SPIRITED = register("ender_cell_spirited", EnderCellContainer::createSpirited);

    public static final ContainerType<MagmaticGenContainer> MAGMATIC_GENERATOR_BASIC = register("magmatic_generator_basic", MagmaticGenContainer::createBasic);
    public static final ContainerType<MagmaticGenContainer> MAGMATIC_GENERATOR_HARDENED = register("magmatic_generator_hardened", MagmaticGenContainer::createHardened);
    public static final ContainerType<MagmaticGenContainer> MAGMATIC_GENERATOR_BLAZING = register("magmatic_generator_blazing", MagmaticGenContainer::createBlazing);
    public static final ContainerType<MagmaticGenContainer> MAGMATIC_GENERATOR_NIOTIC = register("magmatic_generator_niotic", MagmaticGenContainer::createNiotic);
    public static final ContainerType<MagmaticGenContainer> MAGMATIC_GENERATOR_SPIRITED = register("magmatic_generator_spirited", MagmaticGenContainer::createSpirited);

    public static final ContainerType<FurnatorContainer> FURNATOR_BASIC = register("furnator_basic", FurnatorContainer::createBasic);
    public static final ContainerType<FurnatorContainer> FURNATOR_HARDENED = register("furnator_hardened", FurnatorContainer::createHardened);
    public static final ContainerType<FurnatorContainer> FURNATOR_BLAZING = register("furnator_blazing", FurnatorContainer::createBlazing);
    public static final ContainerType<FurnatorContainer> FURNATOR_NIOTIC = register("furnator_niotic", FurnatorContainer::createNiotic);
    public static final ContainerType<FurnatorContainer> FURNATOR_SPIRITED = register("furnator_spirited", FurnatorContainer::createSpirited);

    public static final ContainerType<SolarPanelContainer> SOLAR_PANEL_BASIC = register("solar_panel_basic", SolarPanelContainer::createBasic);
    public static final ContainerType<SolarPanelContainer> SOLAR_PANEL_HARDENED = register("solar_panel_hardened", SolarPanelContainer::createHardened);
    public static final ContainerType<SolarPanelContainer> SOLAR_PANEL_BLAZING = register("solar_panel_blazing", SolarPanelContainer::createBlazing);
    public static final ContainerType<SolarPanelContainer> SOLAR_PANEL_NIOTIC = register("solar_panel_niotic", SolarPanelContainer::createNiotic);
    public static final ContainerType<SolarPanelContainer> SOLAR_PANEL_SPIRITED = register("solar_panel_spirited", SolarPanelContainer::createSpirited);

    public static final ContainerType<ThermoGenContainer> THERMO_GENERATOR_BASIC = register("thermo_generator_basic", ThermoGenContainer::createBasic);
    public static final ContainerType<ThermoGenContainer> THERMO_GENERATOR_HARDENED = register("thermo_generator_hardened", ThermoGenContainer::createHardened);
    public static final ContainerType<ThermoGenContainer> THERMO_GENERATOR_BLAZING = register("thermo_generator_blazing", ThermoGenContainer::createBlazing);
    public static final ContainerType<ThermoGenContainer> THERMO_GENERATOR_NIOTIC = register("thermo_generator_niotic", ThermoGenContainer::createNiotic);
    public static final ContainerType<ThermoGenContainer> THERMO_GENERATOR_SPIRITED = register("thermo_generator_spirited", ThermoGenContainer::createSpirited);

    public static final ContainerType<PlayerTransmitterContainer> PLAYER_TRANSMITTER = register("player_transmitter", PlayerTransmitterContainer::create);
    public static final ContainerType<PlayerTransmitterContainer> PLAYER_TRANSMITTER_DIM = register("player_transmitter_dim", PlayerTransmitterContainer::createDim);

    public static final ContainerType<CableContainer> CABLE_BASIC = register("cable_basic", CableContainer::createBasic);
    public static final ContainerType<CableContainer> CABLE_HARDENED = register("cable_hardened", CableContainer::createHardened);
    public static final ContainerType<CableContainer> CABLE_BLAZING = register("cable_blazing", CableContainer::createBlazing);
    public static final ContainerType<CableContainer> CABLE_NIOTIC = register("cable_niotic", CableContainer::createNiotic);
    public static final ContainerType<CableContainer> CABLE_SPIRITED = register("cable_spirited", CableContainer::createSpirited);

    public static final ContainerType<DischargerContainer> DISCHARGER = register("discharger", DischargerContainer::create);
    public static final ContainerType<EnergyHopperContainer> ENERGY_HOPPER = register("energy_hopper", EnergyHopperContainer::create);

    private static <T extends Container> ContainerType<T> register(String name, IContainerFactory<T> factory) {
        ContainerType<T> containerType = IForgeContainerType.create(factory);
        containerType.setRegistryName(name);
        CONTAINER_TYPES.add(containerType);
        return containerType;
    }

    @SubscribeEvent
    public static void onRegistry(RegistryEvent.Register<ContainerType<?>> event) {
        CONTAINER_TYPES.forEach(block -> event.getRegistry().register(block));
    }
}
