package owmii.powah.config;

import owmii.powah.block.IBlocks;
import owmii.powah.block.cable.Cables;
import owmii.powah.block.energizing.EnergizingRods;
import owmii.powah.block.generator.furnator.Furnators;
import owmii.powah.block.generator.magmatic.MagmaticGenerators;
import owmii.powah.block.generator.panel.solar.SolarPanels;
import owmii.powah.block.generator.thermoelectric.ThermoGenerators;
import owmii.powah.block.storage.energycell.EnergyCells;

public class ConfigHandler {
    public static void reload() {
        for (int i = 0; i < EnergyCells.values().length; i++) {
            EnergyCells cell = EnergyCells.values()[i];
            if (cell.equals(EnergyCells.CREATIVE)) continue;
            IBlocks.ENERGY_CELLS[i].setCapacity(getSafe(Config.CELL_CONFIG.caps[i].get(), cell.capacity));
            IBlocks.ENERGY_CELLS[i].setMaxExtract(getSafe(Config.CELL_CONFIG.transfers[i].get(), cell.transfer));
            IBlocks.ENERGY_CELLS[i].setMaxReceive(getSafe(Config.CELL_CONFIG.transfers[i].get(), cell.transfer));
        }

        for (int i = 0; i < Furnators.values().length; i++) {
            Furnators furnator = Furnators.values()[i];
            IBlocks.FURNATORS[i].setCapacity(getSafe(Config.FURNATOR_CONFIG.caps[i].get(), furnator.capacity));
            IBlocks.FURNATORS[i].setMaxExtract(getSafe(Config.FURNATOR_CONFIG.transfers[i].get(), furnator.transfer));
            IBlocks.FURNATORS[i].setPerTick(getSafe(Config.FURNATOR_CONFIG.generations[i].get(), furnator.perTick));
        }

        for (int i = 0; i < MagmaticGenerators.values().length; i++) {
            MagmaticGenerators magmaticGenerator = MagmaticGenerators.values()[i];
            IBlocks.MAGMATIC_GENERATORS[i].setCapacity(getSafe(Config.MAGMATIC_GENERATOR.caps[i].get(), magmaticGenerator.capacity));
            IBlocks.MAGMATIC_GENERATORS[i].setMaxExtract(getSafe(Config.MAGMATIC_GENERATOR.transfers[i].get(), magmaticGenerator.transfer));
            IBlocks.MAGMATIC_GENERATORS[i].setPerTick(getSafe(Config.MAGMATIC_GENERATOR.generations[i].get(), magmaticGenerator.perTick));
        }

        for (int i = 0; i < ThermoGenerators.values().length; i++) {
            ThermoGenerators thermoGenerators = ThermoGenerators.values()[i];
            IBlocks.THERMO_GENERATORS[i].setCapacity(getSafe(Config.THERMO_GEN_CONFIG.caps[i].get(), thermoGenerators.capacity));
            IBlocks.THERMO_GENERATORS[i].setMaxExtract(getSafe(Config.THERMO_GEN_CONFIG.transfers[i].get(), thermoGenerators.transfer));
            IBlocks.THERMO_GENERATORS[i].setPerTick(getSafe(Config.THERMO_GEN_CONFIG.generations[i].get(), thermoGenerators.perTick));
        }

        for (int i = 0; i < SolarPanels.values().length; i++) {
            SolarPanels solarPanel = SolarPanels.values()[i];
            IBlocks.SOLAR_PANELS[i].setCapacity(getSafe(Config.SOLAR_PANEL.caps[i].get(), solarPanel.capacity));
            IBlocks.SOLAR_PANELS[i].setMaxExtract(getSafe(Config.SOLAR_PANEL.transfers[i].get(), solarPanel.transfer));
            IBlocks.SOLAR_PANELS[i].setPerTick(getSafe(Config.SOLAR_PANEL.generations[i].get(), solarPanel.perTick));
        }

        for (int i = 0; i < EnergizingRods.values().length; i++) {
            EnergizingRods rods = EnergizingRods.values()[i];
            IBlocks.ENERGIZING_RODS[i].setCapacity(getSafe(Config.ENERGIZING_CONFIG.caps[i].get(), rods.capacity));
            IBlocks.ENERGIZING_RODS[i].setMaxReceive(getSafe(Config.ENERGIZING_CONFIG.transfers[i].get(), rods.transfer));
            IBlocks.ENERGIZING_RODS[i].setEnergizingSpeed(getSafe(Config.ENERGIZING_CONFIG.energizingSpeed[i].get(), rods.energizingSpeed));
        }

        for (int i = 0; i < Cables.values().length; i++) {
            Cables cable = Cables.values()[i];
            IBlocks.CABLES[i].setMaxExtract(getSafe(Config.CABLE_CONFIG.transfers[i].get(), cable.transfer));
        }

        IBlocks.PLAYER_TRANSMITTER.setCapacity(getSafe(Config.MISC_ENERGY.playerTransmitterCap.get(), IBlocks.PLAYER_TRANSMITTER.getCapacity()));
        IBlocks.PLAYER_TRANSMITTER.setTransfer(getSafe(Config.MISC_ENERGY.playerTransmitterTransfer.get(), IBlocks.PLAYER_TRANSMITTER.getMaxExtract()));
        IBlocks.PLAYER_TRANSMITTER_DIM.setCapacity(getSafe(Config.MISC_ENERGY.playerTransmitterDimCap.get(), IBlocks.PLAYER_TRANSMITTER_DIM.getCapacity()));
        IBlocks.PLAYER_TRANSMITTER_DIM.setTransfer(getSafe(Config.MISC_ENERGY.playerTransmitterDimTransfer.get(), IBlocks.PLAYER_TRANSMITTER_DIM.getMaxExtract()));
        IBlocks.DISCHARGER.setCapacity(getSafe(Config.MISC_ENERGY.dischargerCap.get(), IBlocks.DISCHARGER.getCapacity()));
        IBlocks.DISCHARGER.setMaxExtract(getSafe(Config.MISC_ENERGY.dischargerTransfer.get(), IBlocks.DISCHARGER.getMaxExtract()));
        IBlocks.ENERGY_HOPPER.setCapacity(getSafe(Config.MISC_ENERGY.hopperCap.get(), IBlocks.ENERGY_HOPPER.getCapacity()));
        IBlocks.ENERGY_HOPPER.setTransfer(getSafe(Config.MISC_ENERGY.hopperTransfer.get(), IBlocks.ENERGY_HOPPER.getMaxReceive()));
    }

    private static int getSafe(Object s, int defaultV) {
        try {
            int max = Integer.MAX_VALUE;
            return Long.parseLong((String) s) > max ? max : Integer.parseInt((String) s);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultV;
        }
    }
}
