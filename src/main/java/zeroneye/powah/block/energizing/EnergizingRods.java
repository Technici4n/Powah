package zeroneye.powah.block.energizing;

import net.minecraft.block.material.Material;
import zeroneye.powah.block.IBlocks;

public enum EnergizingRods {
    BASIC(Material.IRON, 3.0F, 10000, 60, 40),
    HARDENED(Material.IRON, 30.0F, 30000, 140, 100),
    BLAZING(Material.IRON, 30.0F, 90000, 400, 300),
    NIOTIC(Material.IRON, 100.0F, 200000, 1000, 700),
    SPIRITED(Material.IRON, 100.0F, 500000, 2800, 1200);

    public final Material material;
    public final float resistance;
    public final int capacity;
    public final int transfer;
    public final int energizingSpeed;

    EnergizingRods(Material material, float resistance, int capacity, int transfer, int energizingSpeed) {
        this.material = material;
        this.resistance = resistance;
        this.capacity = capacity;
        this.transfer = transfer;
        this.energizingSpeed = energizingSpeed;
    }

    public EnergizingRodBlock get() {
        return IBlocks.ENERGIZING_RODS[ordinal()];
    }
}
