package owmii.powah.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.lib.block.IBlockBase;
import owmii.powah.block.cable.CableBlock;
import owmii.powah.block.cable.Cables;
import owmii.powah.block.discharger.DischargerBlock;
import owmii.powah.block.energizing.EnergizingOrbBlock;
import owmii.powah.block.energizing.EnergizingRodBlock;
import owmii.powah.block.energizing.EnergizingRods;
import owmii.powah.block.generator.furnator.FurnatorBlock;
import owmii.powah.block.generator.furnator.Furnators;
import owmii.powah.block.generator.magmatic.MagmaticGenBlock;
import owmii.powah.block.generator.magmatic.MagmaticGenerators;
import owmii.powah.block.generator.panel.solar.SolarPanelBlock;
import owmii.powah.block.generator.panel.solar.SolarPanels;
import owmii.powah.block.generator.thermoelectric.ThermoGeneratorBlock;
import owmii.powah.block.generator.thermoelectric.ThermoGenerators;
import owmii.powah.block.hopper.EnergyHopperBlock;
import owmii.powah.block.storage.endercell.EnderCellBlock;
import owmii.powah.block.storage.endercell.EnderCells;
import owmii.powah.block.storage.energycell.EnergyCellBlock;
import owmii.powah.block.storage.energycell.EnergyCells;
import owmii.powah.block.transmitter.PlayerTransmitterBlock;
import owmii.powah.item.ItemGroups;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IBlocks {
    public static final List<BlockItem> BLOCK_ITEMS = new ArrayList<>();
    public static final List<Block> BLOCKS = new ArrayList<>();
    public static final EnergyCellBlock[] ENERGY_CELLS;
    public static final EnderCellBlock[] ENDER_CELLS;
    public static final FurnatorBlock[] FURNATORS;
    public static final MagmaticGenBlock[] MAGMATIC_GENERATORS;
    public static final SolarPanelBlock[] SOLAR_PANELS;
    public static final ThermoGeneratorBlock[] THERMO_GENERATORS;
    public static final PlayerTransmitterBlock PLAYER_TRANSMITTER;
    public static final PlayerTransmitterBlock PLAYER_TRANSMITTER_DIM;
    public static final CableBlock[] CABLES;
    public static final DischargerBlock DISCHARGER;
    public static final EnergyHopperBlock ENERGY_HOPPER;
    public static final EnergizingOrbBlock ENERGIZING_ORB;
    public static final EnergizingRodBlock[] ENERGIZING_RODS;

    static {
        EnergyCells[] cells = EnergyCells.values();
        ENERGY_CELLS = new EnergyCellBlock[cells.length];
        for (int i = 0; i < cells.length; i++) {
            EnergyCells cell = cells[i];
            ENERGY_CELLS[i] = register("energy_cell_" + cell.name().toLowerCase(), new EnergyCellBlock(Block.Properties.create(cell.material).hardnessAndResistance(2.0F, cell.resistance), cell.capacity, cell.transfer).setCreative(cell.isCreative));
        }

        EnderCells[] enderCells = EnderCells.values();
        ENDER_CELLS = new EnderCellBlock[enderCells.length];
        for (int i = 0; i < enderCells.length; i++) {
            EnderCells cell = enderCells[i];
            ENDER_CELLS[i] = register("ender_cell_" + cell.name().toLowerCase(), new EnderCellBlock(Block.Properties.create(cell.material).hardnessAndResistance(2.0F, cell.resistance), cell.transfer, cell.transfer, cell.channels));
        }

        Furnators[] furnators = Furnators.values();
        FURNATORS = new FurnatorBlock[furnators.length];
        for (int i = 0; i < furnators.length; i++) {
            Furnators furnator = furnators[i];
            FURNATORS[i] = register("furnator_" + furnator.name().toLowerCase(), new FurnatorBlock(Block.Properties.create(furnator.material).hardnessAndResistance(2.0F, furnator.resistance), furnator.capacity, furnator.transfer, furnator.perTick));
        }

        MagmaticGenerators[] magmaticValues = MagmaticGenerators.values();
        MAGMATIC_GENERATORS = new MagmaticGenBlock[magmaticValues.length];
        for (int i = 0; i < magmaticValues.length; i++) {
            MagmaticGenerators magmaticValue = magmaticValues[i];
            MAGMATIC_GENERATORS[i] = register("magmatic_generator_" + magmaticValue.name().toLowerCase(), new MagmaticGenBlock(Block.Properties.create(magmaticValue.material).hardnessAndResistance(2.0F, magmaticValue.resistance), magmaticValue.capacity, magmaticValue.transfer, magmaticValue.perTick, magmaticValue.buckets));
        }

        ThermoGenerators[] thermoGenerators = ThermoGenerators.values();
        THERMO_GENERATORS = new ThermoGeneratorBlock[thermoGenerators.length];
        for (int i = 0; i < thermoGenerators.length; i++) {
            ThermoGenerators tg = thermoGenerators[i];
            THERMO_GENERATORS[i] = register("thermo_generator_" + tg.name().toLowerCase(), new ThermoGeneratorBlock(Block.Properties.create(tg.material).hardnessAndResistance(2.0F, tg.resistance), tg.capacity, tg.transfer, tg.perTick, tg.buckets));
        }

        SolarPanels[] panels = SolarPanels.values();
        SOLAR_PANELS = new SolarPanelBlock[panels.length];
        for (int i = 0; i < panels.length; i++) {
            SolarPanels panel = panels[i];
            SOLAR_PANELS[i] = register("solar_panel_" + panel.name().toLowerCase(), new SolarPanelBlock(Block.Properties.create(panel.material).hardnessAndResistance(2.0F, panel.resistance), panel.capacity, panel.transfer, panel.perTick));
        }

        ENERGIZING_ORB = register("energizing_orb", new EnergizingOrbBlock(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(1.0F, 15.0F)));
        EnergizingRods[] rods = EnergizingRods.values();
        ENERGIZING_RODS = new EnergizingRodBlock[rods.length];
        for (int i = 0; i < rods.length; i++) {
            EnergizingRods rod = rods[i];
            ENERGIZING_RODS[i] = register("energizing_rod_" + rod.name().toLowerCase(), new EnergizingRodBlock(Block.Properties.create(rod.material).hardnessAndResistance(1.0F, rod.resistance).doesNotBlockMovement(), rod.capacity, rod.transfer, rod.energizingSpeed));
        }

        DISCHARGER = register("discharger", new DischargerBlock(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(1.0F, 15.0F), 500000, 1000));
        ENERGY_HOPPER = register("energy_hopper", new EnergyHopperBlock(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(1.0F, 15.0F), 500000, 1000));
        PLAYER_TRANSMITTER = register("player_transmitter", new PlayerTransmitterBlock(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(1.0F, 15.0F).doesNotBlockMovement(), 10000, 100, 1, false));
        PLAYER_TRANSMITTER_DIM = register("player_transmitter_dim", new PlayerTransmitterBlock(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(1.0F, 15.0F).doesNotBlockMovement(), 100000, 250, 2, true));

        Cables[] cables = Cables.values();
        CABLES = new CableBlock[cables.length];
        for (int i = 0; i < cables.length; i++) {
            Cables cable = cables[i];
            CABLES[i] = register("cable_" + cable.name().toLowerCase(), new CableBlock(Block.Properties.create(cable.material).hardnessAndResistance(1.0F, cable.resistance).doesNotBlockMovement(), cable.transfer, cable.transfer));
        }
    }

    static <T extends Block & IBlockBase> T register(String name, T block) {
        BlockItem itemBlock = block.getBlockItem(new Item.Properties(), ItemGroups.MAIN);
        itemBlock.setRegistryName(name);
        block.setRegistryName(name);
        BLOCK_ITEMS.add(itemBlock);
        BLOCKS.add(block);
        return block;
    }

    @SubscribeEvent
    public static void onRegistry(RegistryEvent.Register<Block> event) {
        BLOCKS.forEach(block -> event.getRegistry().register(block));
    }
}
