package zeroneye.powah.config;

import zeroneye.powah.block.IBlocks;
import zeroneye.powah.block.cable.Cables;
import zeroneye.powah.block.generator.furnator.Furnators;
import zeroneye.powah.block.generator.magmatic.MagmaticGenerators;
import zeroneye.powah.block.generator.panel.solar.SolarPanels;
import zeroneye.powah.block.storage.EnergyCells;

public class ConfigHandler {
    public static void reload() {
        for (int i = 0; i < EnergyCells.values().length; i++) {
            EnergyCells cell = EnergyCells.values()[i];
            if (cell.equals(EnergyCells.CREATIVE)) continue;
            IBlocks.ENERGY_CELLS[i].setCapacity(getSafe(Config.CELL.caps[i].get(), cell.capacity));
            IBlocks.ENERGY_CELLS[i].setMaxExtract(getSafe(Config.CELL.transfers[i].get(), cell.transfer));
            IBlocks.ENERGY_CELLS[i].setMaxReceive(getSafe(Config.CELL.transfers[i].get(), cell.transfer));
        }

        for (int i = 0; i < Furnators.values().length; i++) {
            Furnators furnator = Furnators.values()[i];
            IBlocks.FURNATORS[i].setCapacity(getSafe(Config.FURNATOR.caps[i].get(), furnator.capacity));
            IBlocks.FURNATORS[i].setMaxExtract(getSafe(Config.FURNATOR.transfers[i].get(), furnator.transfer));
            IBlocks.FURNATORS[i].setPerTick(getSafe(Config.FURNATOR.generations[i].get(), furnator.perTick));
        }

        for (int i = 0; i < MagmaticGenerators.values().length; i++) {
            MagmaticGenerators magmaticGenerator = MagmaticGenerators.values()[i];
            IBlocks.MAGMATIC_GENERATORS[i].setCapacity(getSafe(Config.MAGMATIC_GENERATOR.caps[i].get(), magmaticGenerator.capacity));
            IBlocks.MAGMATIC_GENERATORS[i].setMaxExtract(getSafe(Config.MAGMATIC_GENERATOR.transfers[i].get(), magmaticGenerator.transfer));
            IBlocks.MAGMATIC_GENERATORS[i].setPerTick(getSafe(Config.MAGMATIC_GENERATOR.generations[i].get(), magmaticGenerator.perTick));
        }

        for (int i = 0; i < SolarPanels.values().length; i++) {
            SolarPanels solarPanel = SolarPanels.values()[i];
            IBlocks.SOLAR_PANELS[i].setCapacity(getSafe(Config.SOLAR_PANEL.caps[i].get(), solarPanel.capacity));
            IBlocks.SOLAR_PANELS[i].setMaxExtract(getSafe(Config.SOLAR_PANEL.transfers[i].get(), solarPanel.transfer));
            IBlocks.SOLAR_PANELS[i].setPerTick(getSafe(Config.SOLAR_PANEL.generations[i].get(), solarPanel.perTick));
        }

        for (int i = 0; i < Cables.values().length; i++) {
            Cables cable = Cables.values()[i];
            IBlocks.CABLES[i].setMaxExtract(getSafe(Config.CABLE.transfers[i].get(), cable.transfer));
        }

        IBlocks.PLAYER_TRANSMITTER.setCapacity(getSafe(Config.MISC_ENERGY.playerTransmitterCap.get(), IBlocks.PLAYER_TRANSMITTER.getCapacity()));
        IBlocks.PLAYER_TRANSMITTER.setTransfer(getSafe(Config.MISC_ENERGY.playerTransmitterTransfer.get(), IBlocks.PLAYER_TRANSMITTER.getMaxExtract()));
        IBlocks.PLAYER_TRANSMITTER_DIM.setCapacity(getSafe(Config.MISC_ENERGY.playerTransmitterDimCap.get(), IBlocks.PLAYER_TRANSMITTER_DIM.getCapacity()));
        IBlocks.PLAYER_TRANSMITTER_DIM.setTransfer(getSafe(Config.MISC_ENERGY.playerTransmitterDimTransfer.get(), IBlocks.PLAYER_TRANSMITTER_DIM.getMaxExtract()));
        IBlocks.DISCHARGER.setCapacity(getSafe(Config.MISC_ENERGY.dischargerCap.get(), IBlocks.DISCHARGER.getCapacity()));
        IBlocks.DISCHARGER.setMaxExtract(getSafe(Config.MISC_ENERGY.dischargerTransfer.get(), IBlocks.DISCHARGER.getMaxExtract()));
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
