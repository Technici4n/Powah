package zeroneye.powah.block.cable;

import net.minecraft.block.material.Material;
import zeroneye.powah.block.IBlocks;

public enum Cables {
    BASIC(Material.IRON, 3.0F, 2000),
    HARDENED(Material.IRON, 30.0F, 10000),
    BLAZING(Material.IRON, 30.0F, 40000),
    NIOTIC(Material.IRON, 100.0F, 200000),
    SPIRITED(Material.IRON, 100.0F, 1000000);

    public final Material material;
    public final float resistance;
    public final int transfer;

    Cables(Material material, float resistance, int transfer) {
        this.material = material;
        this.resistance = resistance;
        this.transfer = transfer;
    }

    public CableBlock get() {
        return IBlocks.CABLES[ordinal()];
    }
}
