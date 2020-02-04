package owmii.powah.config;

import net.minecraftforge.common.ForgeConfigSpec;
import owmii.lib.config.IEnergyConfig;
import owmii.lib.energy.Energy;
import owmii.powah.block.Tier;

import java.util.HashMap;
import java.util.Map;

public class EnergyCableConfig implements IEnergyConfig<Tier> {
    public final Map<Tier, Long> transfer = new HashMap<>();

    public final ForgeConfigSpec.LongValue starterTransfer;
    public final ForgeConfigSpec.LongValue basicTransfer;
    public final ForgeConfigSpec.LongValue hardenedTransfer;
    public final ForgeConfigSpec.LongValue blazingTransfer;
    public final ForgeConfigSpec.LongValue nioticTransfer;
    public final ForgeConfigSpec.LongValue spiritedTransfer;
    public final ForgeConfigSpec.LongValue nitroTransfer;

    public EnergyCableConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Energy_Transfer");
        this.starterTransfer = builder.defineInRange("starterTransfer", 100L, Energy.MIN, Energy.MAX);
        this.basicTransfer = builder.defineInRange("basicTransfer", 2000L, Energy.MIN, Energy.MAX);
        this.hardenedTransfer = builder.defineInRange("hardenedTransfer", 5000L, Energy.MIN, Energy.MAX);
        this.blazingTransfer = builder.defineInRange("blazingTransfer", 16_000L, Energy.MIN, Energy.MAX);
        this.nioticTransfer = builder.defineInRange("nioticTransfer", 38_000L, Energy.MIN, Energy.MAX);
        this.spiritedTransfer = builder.defineInRange("spiritedTransfer", 80_000L, Energy.MIN, Energy.MAX);
        this.nitroTransfer = builder.defineInRange("nitroTransfer", 175_000L, Energy.MIN, Energy.MAX);
        builder.pop();
    }

    @Override
    public void reload() {
        this.transfer.put(Tier.STARTER, this.starterTransfer.get());
        this.transfer.put(Tier.BASIC, this.basicTransfer.get());
        this.transfer.put(Tier.HARDENED, this.hardenedTransfer.get());
        this.transfer.put(Tier.BLAZING, this.blazingTransfer.get());
        this.transfer.put(Tier.NIOTIC, this.nioticTransfer.get());
        this.transfer.put(Tier.SPIRITED, this.spiritedTransfer.get());
        this.transfer.put(Tier.NITRO, this.nitroTransfer.get());
        this.transfer.put(Tier.CREATIVE, Energy.MAX);
    }

    @Override
    public long getCapacity(Tier variant) {
        return 0L;
    }

    @Override
    public long getTransfer(Tier variant) {
        if (this.transfer.containsKey(variant)) {
            return this.transfer.get(variant);
        }
        return 0L;
    }
}
