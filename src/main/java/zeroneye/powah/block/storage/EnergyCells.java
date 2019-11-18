package zeroneye.powah.block.storage;

import net.minecraft.block.material.Material;
import zeroneye.powah.block.IBlocks;

public enum EnergyCells {
    BASIC(Material.ROCK, 3.0F, 250000, 250, false),
    HARDENED(Material.ROCK, 30.0F, 1000000, 1000, false),
    BLAZING(Material.IRON, 30.0F, 5000000, 4000, false),
    NIOTIC(Material.IRON, 100.0F, 25000000, 20000, false),
    SPIRITED(Material.IRON, 100.0F, 100000000, 500000, false),
    CREATIVE(Material.IRON, -1.0F, Integer.MAX_VALUE, Integer.MAX_VALUE, true);

    public final Material material;
    public final float resistance;
    public final int capacity;
    public final int transfer;
    public final boolean isCreative;

    EnergyCells(Material material, float resistance, int capacity, int transfer, boolean isCreative) {
        this.material = material;
        this.resistance = resistance;
        this.capacity = capacity;
        this.transfer = transfer;
        this.isCreative = isCreative;
    }

    public EnergyCellBlock get() {
        return IBlocks.ENERGY_CELLS[ordinal()];
    }
}
