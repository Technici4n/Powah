package zeroneye.powah.block.generator.thermoelectric;

import net.minecraft.block.material.Material;
import zeroneye.powah.block.IBlocks;

public enum ThermoGenerators {
    BASIC(Material.ROCK, 3.0F, 5000, 40, 20, 1),
    HARDENED(Material.ROCK, 30.0F, 40000, 80, 40, 2),
    BLAZING(Material.IRON, 30.0F, 100000, 300, 140, 4),
    NIOTIC(Material.IRON, 100.0F, 250000, 900, 450, 6),
    SPIRITED(Material.IRON, 100.0F, 1000000, 2000, 1200, 10);

    public final Material material;
    public final float resistance;
    public final int capacity;
    public final int transfer;
    public final int perTick;
    public final int buckets;

    ThermoGenerators(Material material, float resistance, int capacity, int transfer, int perTick, int buckets) {
        this.material = material;
        this.resistance = resistance;
        this.capacity = capacity;
        this.transfer = transfer;
        this.perTick = perTick;
        this.buckets = buckets;
    }

    public ThermoGeneratorBlock get() {
        return IBlocks.THERMO_GENERATORS[ordinal()];
    }
}
