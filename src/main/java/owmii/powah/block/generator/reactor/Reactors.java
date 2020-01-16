package owmii.powah.block.generator.reactor;

import net.minecraft.block.material.Material;
import owmii.powah.block.IBlocks;

public enum Reactors {
    BASIC(Material.ROCK, 3.0F, 5000000, 10000, 500),
    HARDENED(Material.ROCK, 30.0F, 15000000, 40000, 1200),
    BLAZING(Material.IRON, 30.0F, 25000000, 90000, 2700),
    NIOTIC(Material.IRON, 100.0F, 45000000, 250000, 6200),
    SPIRITED(Material.IRON, 100.0F, 100000000, 1000000, 9000);

    public final Material material;
    public final float resistance;
    public final int capacity;
    public final int transfer;
    public final int perTick;

    Reactors(Material material, float resistance, int capacity, int transfer, int perTick) {
        this.material = material;
        this.resistance = resistance;
        this.capacity = capacity;
        this.transfer = transfer;
        this.perTick = perTick;
    }

    public ReactorBlock get() {
        return IBlocks.REACTORS[ordinal()];
    }
}
