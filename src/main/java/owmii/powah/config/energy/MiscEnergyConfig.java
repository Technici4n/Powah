package owmii.powah.config.energy;

import net.minecraftforge.common.ForgeConfigSpec;
import owmii.powah.block.IBlocks;

public class MiscEnergyConfig {
    public final ForgeConfigSpec.ConfigValue playerTransmitterCap;
    public final ForgeConfigSpec.ConfigValue playerTransmitterTransfer;

    public final ForgeConfigSpec.ConfigValue playerTransmitterDimCap;
    public final ForgeConfigSpec.ConfigValue playerTransmitterDimTransfer;

    public final ForgeConfigSpec.ConfigValue dischargerCap;
    public final ForgeConfigSpec.ConfigValue dischargerTransfer;

    public final ForgeConfigSpec.ConfigValue hopperCap;
    public final ForgeConfigSpec.ConfigValue hopperTransfer;

    public MiscEnergyConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Range: min = 0, max = " + Integer.MAX_VALUE);

        builder.push("Player Transmitter");
        this.playerTransmitterCap = builder.define("capacity", "" + IBlocks.PLAYER_TRANSMITTER.getCapacity());
        this.playerTransmitterTransfer = builder.define("transfer", "" + IBlocks.PLAYER_TRANSMITTER.getMaxReceive());
        builder.pop();

        builder.push("Dimensional Player Transmitter");
        this.playerTransmitterDimCap = builder.define("capacity", "" + IBlocks.PLAYER_TRANSMITTER_DIM.getCapacity());
        this.playerTransmitterDimTransfer = builder.define("transfer", "" + IBlocks.PLAYER_TRANSMITTER_DIM.getMaxReceive());
        builder.pop();

        builder.push("Energy Discharger");
        this.dischargerCap = builder.define("capacity", "" + IBlocks.DISCHARGER.getCapacity());
        this.dischargerTransfer = builder.define("transfer", "" + IBlocks.DISCHARGER.getMaxExtract());
        builder.pop();

        builder.push("Energy Hopper");
        this.hopperCap = builder.define("capacity", "" + IBlocks.ENERGY_HOPPER.getCapacity());
        this.hopperTransfer = builder.define("transfer", "" + IBlocks.ENERGY_HOPPER.getMaxReceive());
        builder.pop();
    }
}
