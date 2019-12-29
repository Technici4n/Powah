package owmii.powah.block.endergate;

import net.minecraft.block.material.Material;
import owmii.powah.block.IBlocks;

public enum EnderGates {
    BASIC(Material.ROCK, 3.0F, 1000, 2),
    HARDENED(Material.ROCK, 30.0F, 5000, 4),
    BLAZING(Material.IRON, 30.0F, 12000, 7),
    NIOTIC(Material.IRON, 100.0F, 32000, 11),
    SPIRITED(Material.IRON, 100.0F, 74000, 16);

    public final Material material;
    public final float resistance;
    public final int transfer;
    public final int channels;

    EnderGates(Material material, float resistance, int transfer, int channels) {
        this.material = material;
        this.resistance = resistance;
        this.transfer = transfer;
        this.channels = channels;
    }

    public EnderGateBlock get() {
        return IBlocks.ENDER_GATES[ordinal()];
    }
}
