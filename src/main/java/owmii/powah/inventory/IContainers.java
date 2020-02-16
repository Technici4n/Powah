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
    public static final ContainerType<EnergyCellContainer> ENERGY_CELL = register("energy_cell", EnergyCellContainer::create);
    public static final ContainerType<EnderCellContainer> ENDER_CELL = register("ender_cell", EnderCellContainer::create);
    public static final ContainerType<EnergyCableContainer> ENERGY_CABLE = register("energy_cable", EnergyCableContainer::create);
    public static final ContainerType<FurnatorContainer> FURNATOR = register("furnator", FurnatorContainer::create);
    public static final ContainerType<MagmatorContainer> MAGMATOR = register("magmator", MagmatorContainer::create);
    public static final ContainerType<ThermoGenContainer> THERMO_GEN = register("thermo_gen", ThermoGenContainer::create);
    public static final ContainerType<SolarPanelContainer> SOLAR_PANEL = register("solar_panel", SolarPanelContainer::create);
    public static final ContainerType<PlayerTransmitterContainer> PLAYER_TRANSMITTER = register("player_transmitter", PlayerTransmitterContainer::create);
    public static final ContainerType<EnergyHopperContainer> ENERGY_HOPPER = register("energy_hopper", EnergyHopperContainer::create);
    public static final ContainerType<EnergyDischargerContainer> ENERGY_DISCHARGER = register("energy_discharger", EnergyDischargerContainer::create);
    public static final ContainerType<ReactorContainer> REACTOR = register("reactor", ReactorContainer::create);

    private static <T extends Container> ContainerType<T> register(final String name, final IContainerFactory<T> factory) {
        final ContainerType<T> containerType = IForgeContainerType.create(factory);
        containerType.setRegistryName(name);
        IContainers.CONTAINER_TYPES.add(containerType);
        return containerType;
    }

    @SubscribeEvent
    public static void onRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
        IContainers.CONTAINER_TYPES.forEach(block -> event.getRegistry().register(block));
    }
}
