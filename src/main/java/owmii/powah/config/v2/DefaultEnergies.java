package owmii.powah.config.v2;

import owmii.powah.config.v2.values.TieredEnergyValues;

/**
 * Abstracts default energy values across platforms.
 */
public class DefaultEnergies {
    // MISC
    public static long energyPerFuelTick() {
        return 30;
    }

    // SCALING VALUES
    private static TieredEnergyValues baseScaling() {
        return new TieredEnergyValues(1, 4, 10, 40, 100, 400, 2000);
    }

    private static TieredEnergyValues baseSlowScaling() {
        return new TieredEnergyValues(1, 3, 5, 10, 20, 40, 100);
    }

    // SOURCE VALUES (everything else is derived from those)
    public static double energizingRatio() {
        return 1.0;
    }

    public static TieredEnergyValues passiveProduction() {
        return baseSlowScaling().copy(20);
    }

    public static TieredEnergyValues activeProduction() {
        return baseScaling().copy(20);
    }

    public static TieredEnergyValues reactorProduction() {
        return baseScaling().copy(250);
    }

    public static TieredEnergyValues energizingTransfer() {
        return baseScaling().copy(100);
    }

    // DERIVED VALUES
    public static TieredEnergyValues generatorCapacity() {
        return activeProduction().copy(1000);
    }

    public static TieredEnergyValues reactorCapacity() {
        return reactorProduction().copy(1000);
    }

    public static TieredEnergyValues generatorTransfer() {
        return activeProduction().copy(4);
    }

    public static TieredEnergyValues reactorTransfer() {
        return reactorProduction().copy(4);
    }

    // DEVICES
    public static TieredEnergyValues batteryCapacity() {
        return reactorProduction().copy(4000);
    }

    public static TieredEnergyValues batteryTransfer() {
        return cableTransfer().copy(2);
    }

    public static TieredEnergyValues cableTransfer() {
        return reactorProduction().copy(2);
    }

    public static TieredEnergyValues chargingTransfer() {
        return cableTransfer();
    }

    public static TieredEnergyValues energizingCapacity() {
        return energizingTransfer().copy(100);
    }
}
