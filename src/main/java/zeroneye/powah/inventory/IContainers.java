package zeroneye.powah.inventory;

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

    public static final ContainerType<PlayerTransmitterContainer> PLAYER_TRANSMITTER = register("player_transmitter", PlayerTransmitterContainer::create);
    public static final ContainerType<PlayerTransmitterContainer> PLAYER_TRANSMITTER_DIM = register("player_transmitter_dim", PlayerTransmitterContainer::createDim);


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
