package owmii.powah.block.storage.endercell;

import net.minecraft.block.material.Material;
import owmii.powah.block.IBlocks;

public enum EnderCells {
    BASIC(Material.ROCK, 3.0F, 5000, 2),
    HARDENED(Material.ROCK, 30.0F, 20000, 4),
    BLAZING(Material.IRON, 30.0F, 100000, 7),
    NIOTIC(Material.IRON, 100.0F, 250000, 11),
    SPIRITED(Material.IRON, 100.0F, 1000000, 16);

    public final Material material;
    public final float resistance;
    public final int transfer;
    public final int channels;

    EnderCells(Material material, float resistance, int transfer, int channels) {
        this.material = material;
        this.resistance = resistance;
        this.transfer = transfer;
        this.channels = channels;
    }

    public EnderCellBlock get() {
        return IBlocks.ENDER_CELLS[ordinal()];
    }
}
