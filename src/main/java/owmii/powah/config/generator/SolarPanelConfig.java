package owmii.powah.config.generator;

import net.minecraftforge.common.ForgeConfigSpec;
import owmii.lib.config.IEnergyProviderConfig;
import owmii.lib.energy.Energy;
import owmii.powah.block.Tier;

import java.util.HashMap;
import java.util.Map;

public class SolarPanelConfig implements IEnergyProviderConfig<Tier> {
    public final Map<Tier, Long> capacities = new HashMap<>();
    public final Map<Tier, Long> transfer = new HashMap<>();
    public final Map<Tier, Long> generation = new HashMap<>();

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

    public final ForgeConfigSpec.LongValue starterGeneration;
    public final ForgeConfigSpec.LongValue basicGeneration;
    public final ForgeConfigSpec.LongValue hardenedGeneration;
    public final ForgeConfigSpec.LongValue blazingGeneration;
    public final ForgeConfigSpec.LongValue nioticGeneration;
    public final ForgeConfigSpec.LongValue spiritedGeneration;
    public final ForgeConfigSpec.LongValue nitroGeneration;

    public SolarPanelConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Energy_Capacity");
        this.starterCap = builder.defineInRange("starterCap", 2_000L, Energy.MIN, Energy.MAX);
        this.basicCap = builder.defineInRange("basicCap", 10_000L, Energy.MIN, Energy.MAX);
        this.hardenedCap = builder.defineInRange("hardenedCap", 40_000L, Energy.MIN, Energy.MAX);
        this.blazingCap = builder.defineInRange("blazingCap", 140_000L, Energy.MIN, Energy.MAX);
        this.nioticCap = builder.defineInRange("nioticCap", 280_000L, Energy.MIN, Energy.MAX);
        this.spiritedCap = builder.defineInRange("spiritedCap", 1_000_000L, Energy.MIN, Energy.MAX);
        this.nitroCap = builder.defineInRange("nitroCap", 4_000_000L, Energy.MIN, Energy.MAX);
        builder.pop();

        builder.push("Energy_Generation");
        this.starterGeneration = builder.defineInRange("starterGeneration", 12L, Energy.MIN, Energy.MAX);
        this.basicGeneration = builder.defineInRange("basicGeneration", 50L, Energy.MIN, Energy.MAX);
        this.hardenedGeneration = builder.defineInRange("hardenedGeneration", 180L, Energy.MIN, Energy.MAX);
        this.blazingGeneration = builder.defineInRange("blazingGeneration", 580L * 3, Energy.MIN, Energy.MAX);
        this.nioticGeneration = builder.defineInRange("nioticGeneration", 1_900L, Energy.MIN, Energy.MAX);
        this.spiritedGeneration = builder.defineInRange("spiritedGeneration", 6_000L, Energy.MIN, Energy.MAX);
        this.nitroGeneration = builder.defineInRange("nitroGeneration", 22_000L, Energy.MIN, Energy.MAX);
        builder.pop();

        builder.push("Energy_Transfer");
        this.starterTransfer = builder.defineInRange("starterTransfer", 200L, Energy.MIN, Energy.MAX);
        this.basicTransfer = builder.defineInRange("basicTransfer", 500L, Energy.MIN, Energy.MAX);
        this.hardenedTransfer = builder.defineInRange("hardenedTransfer", 1000L, Energy.MIN, Energy.MAX);
        this.blazingTransfer = builder.defineInRange("blazingTransfer", 2_000L, Energy.MIN, Energy.MAX);
        this.nioticTransfer = builder.defineInRange("nioticTransfer", 5_000L, Energy.MIN, Energy.MAX);
        this.spiritedTransfer = builder.defineInRange("spiritedTransfer", 18_000L, Energy.MIN, Energy.MAX);
        this.nitroTransfer = builder.defineInRange("nitroTransfer", 70_000L, Energy.MIN, Energy.MAX);
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

        this.generation.put(Tier.STARTER, this.starterGeneration.get());
        this.generation.put(Tier.BASIC, this.basicGeneration.get());
        this.generation.put(Tier.HARDENED, this.hardenedGeneration.get());
        this.generation.put(Tier.BLAZING, this.blazingGeneration.get());
        this.generation.put(Tier.NIOTIC, this.nioticGeneration.get());
        this.generation.put(Tier.SPIRITED, this.spiritedGeneration.get());
        this.generation.put(Tier.NITRO, this.nitroGeneration.get());
        this.generation.put(Tier.CREATIVE, Energy.MAX);
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

    @Override
    public long getGeneration(Tier variant) {
        if (this.generation.containsKey(variant)) {
            return this.generation.get(variant);
        }
        return 0L;
    }
}
