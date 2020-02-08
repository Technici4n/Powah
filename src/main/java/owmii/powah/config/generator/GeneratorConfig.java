package owmii.powah.config.generator;

import net.minecraftforge.common.ForgeConfigSpec;
import owmii.lib.config.IEnergyProviderConfig;
import owmii.lib.energy.Energy;
import owmii.powah.block.Tier;

import java.util.HashMap;
import java.util.Map;

public class GeneratorConfig implements IEnergyProviderConfig<Tier> {
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

    public GeneratorConfig(ForgeConfigSpec.Builder builder, long[] caps, long[] trs, long[] gens) {
        builder.push("Energy_Capacity");
        this.starterCap = builder.defineInRange("starterCap", caps[0], Energy.MIN, Energy.MAX);
        this.basicCap = builder.defineInRange("basicCap", caps[1], Energy.MIN, Energy.MAX);
        this.hardenedCap = builder.defineInRange("hardenedCap", caps[2], Energy.MIN, Energy.MAX);
        this.blazingCap = builder.defineInRange("blazingCap", caps[3], Energy.MIN, Energy.MAX);
        this.nioticCap = builder.defineInRange("nioticCap", caps[4], Energy.MIN, Energy.MAX);
        this.spiritedCap = builder.defineInRange("spiritedCap", caps[5], Energy.MIN, Energy.MAX);
        this.nitroCap = builder.defineInRange("nitroCap", caps[6], Energy.MIN, Energy.MAX);
        builder.pop();

        builder.push("Energy_Transfer");
        this.starterTransfer = builder.defineInRange("starterTransfer", trs[0], Energy.MIN, Energy.MAX);
        this.basicTransfer = builder.defineInRange("basicTransfer", trs[1], Energy.MIN, Energy.MAX);
        this.hardenedTransfer = builder.defineInRange("hardenedTransfer", trs[2], Energy.MIN, Energy.MAX);
        this.blazingTransfer = builder.defineInRange("blazingTransfer", trs[3], Energy.MIN, Energy.MAX);
        this.nioticTransfer = builder.defineInRange("nioticTransfer", trs[4], Energy.MIN, Energy.MAX);
        this.spiritedTransfer = builder.defineInRange("spiritedTransfer", trs[5], Energy.MIN, Energy.MAX);
        this.nitroTransfer = builder.defineInRange("nitroTransfer", trs[6], Energy.MIN, Energy.MAX);
        builder.pop();

        builder.push("Energy_Generation");
        this.starterGeneration = builder.defineInRange("starterGeneration", gens[0], Energy.MIN, Energy.MAX);
        this.basicGeneration = builder.defineInRange("basicGeneration", gens[1], Energy.MIN, Energy.MAX);
        this.hardenedGeneration = builder.defineInRange("hardenedGeneration", gens[2], Energy.MIN, Energy.MAX);
        this.blazingGeneration = builder.defineInRange("blazingGeneration", gens[3], Energy.MIN, Energy.MAX);
        this.nioticGeneration = builder.defineInRange("nioticGeneration", gens[4], Energy.MIN, Energy.MAX);
        this.spiritedGeneration = builder.defineInRange("spiritedGeneration", gens[5], Energy.MIN, Energy.MAX);
        this.nitroGeneration = builder.defineInRange("nitroGeneration", gens[6], Energy.MIN, Energy.MAX);
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
