package owmii.powah.config.energy;

import net.minecraftforge.common.ForgeConfigSpec;
import owmii.powah.block.storage.energycell.EnergyCells;

public class CellConfig {
    public final ForgeConfigSpec.ConfigValue[] caps = new ForgeConfigSpec.ConfigValue[EnergyCells.values().length - 1];
    public final ForgeConfigSpec.ConfigValue[] transfers = new ForgeConfigSpec.ConfigValue[EnergyCells.values().length - 1];

    public CellConfig(ForgeConfigSpec.Builder builder) {
        EnergyCells[] values = EnergyCells.values();
        for (int i = 0; i < values.length; i++) {
            EnergyCells cell = values[i];
            if (cell.equals(EnergyCells.CREATIVE)) continue;
            String name = cell.name().toLowerCase();
            String cap = name.substring(0, 1).toUpperCase() + name.substring(1);
            if (i == 0) {
                builder.comment("Range: min = 0, max = " + Integer.MAX_VALUE);
            }
            builder.push(cap + " Enregy Cell");
            this.caps[i] = builder.define("capacity", "" + cell.capacity);
            this.transfers[i] = builder.define("transfer", "" + cell.transfer);
            builder.pop();
        }
    }
}
