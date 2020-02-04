package owmii.powah.config;

import net.minecraftforge.common.ForgeConfigSpec;
import owmii.lib.config.IEnergyConfig;
import owmii.lib.energy.Energy;
import owmii.powah.block.Tier;

import java.util.HashMap;
import java.util.Map;

public class EnergyCellConfig implements IEnergyConfig<Tier> {
    public final Map<Tier, Long> capacities = new HashMap<>();
    public final Map<Tier, Long> transfer = new HashMap<>();

    public final ForgeConfigSpec.LongValue starterCap;
    public final ForgeConfigSpec.LongValue basicCap;
    public final ForgeConfigSpec.LongValue hardenedCap;
    public final ForgeConfigSpec.LongValue blazingCap;
    public final ForgeConfigSpec.LongValue nioticCap;
    public final ForgeConfigSpec.LongValue spiritedCap;
    public final ForgeConfigSpec.LongValue nitroCap;

    public final ForgeConfigSpec.LongValue starterTransfer;
    public final ForgeConfigSpec.LongValue basicTransfer;
    public final ForgeConfigSpec.LongValue hardenedTransfer;
    public final ForgeConfigSpec.LongValue blazingTransfer;
    public final ForgeConfigSpec.LongValue nioticTransfer;
    public final ForgeConfigSpec.LongValue spiritedTransfer;
    public final ForgeConfigSpec.LongValue nitroTransfer;

    public EnergyCellConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Capacity");
        this.starterCap = builder.defineInRange("starterCap", 100_000L, Energy.MIN, Energy.MAX);
        this.basicCap = builder.defineInRange("basicCap", 1000_000L, Energy.MIN, Energy.MAX);
        this.hardenedCap = builder.defineInRange("hardenedCap", 4000_000L, Energy.MIN, Energy.MAX);
        this.blazingCap = builder.defineInRange("blazingCap", 10_000_000L, Energy.MIN, Energy.MAX);
        this.nioticCap = builder.defineInRange("nioticCap", 25_000_000L, Energy.MIN, Energy.MAX);
        this.spiritedCap = builder.defineInRange("spiritedCap", 60_000_000L, Energy.MIN, Energy.MAX);
        this.nitroCap = builder.defineInRange("nitroCap", 140_000_000L, Energy.MIN, Energy.MAX);
        builder.pop();

        builder.push("Transfer");
        this.starterTransfer = builder.defineInRange("starterTransfer", 200L, Energy.MIN, Energy.MAX);
        this.basicTransfer = builder.defineInRange("basicTransfer", 2500L, Energy.MIN, Energy.MAX);
        this.hardenedTransfer = builder.defineInRange("hardenedTransfer", 8000L, Energy.MIN, Energy.MAX);
        this.blazingTransfer = builder.defineInRange("blazingTransfer", 20_000L, Energy.MIN, Energy.MAX);
        this.nioticTransfer = builder.defineInRange("nioticTransfer", 50_000L, Energy.MIN, Energy.MAX);
        this.spiritedTransfer = builder.defineInRange("spiritedTransfer", 100_000L, Energy.MIN, Energy.MAX);
        this.nitroTransfer = builder.defineInRange("nitroTransfer", 400_000L, Energy.MIN, Energy.MAX);
        builder.pop();
    }

    @Override
    public void reload() {
        this.capacities.put(Tier.STARTER, this.starterCap.get());
        this.capacities.put(Tier.BASIC, this.basicCap.get());
        this.capacities.put(Tier.HARDENED, this.hardenedCap.get());
        this.capacities.put(Tier.BLAZING, this.blazingCap.get());
        this.capacities.put(Tier.NIOTIC, this.nioticCap.get());
        this.capacities.put(Tier.SPIRITED, this.spiritedCap.get());
        this.capacities.put(Tier.NITRO, this.nitroCap.get());
        this.capacities.put(Tier.CREATIVE, Energy.MAX);

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
        if (this.capacities.containsKey(variant)) {
            return this.capacities.get(variant);
        }
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
