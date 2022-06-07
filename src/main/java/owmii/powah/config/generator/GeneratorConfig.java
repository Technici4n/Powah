package owmii.powah.config.generator;

import net.minecraftforge.common.ForgeConfigSpec;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.block.Tier;
import owmii.powah.config.EnergyConfig;

import java.util.HashMap;
import java.util.Map;

public class GeneratorConfig extends EnergyConfig {
    public final Map<Tier, Long> generation = new HashMap<>();

    public final ForgeConfigSpec.LongValue starterGeneration;
    public final ForgeConfigSpec.LongValue basicGeneration;
    public final ForgeConfigSpec.LongValue hardenedGeneration;
    public final ForgeConfigSpec.LongValue blazingGeneration;
    public final ForgeConfigSpec.LongValue nioticGeneration;
    public final ForgeConfigSpec.LongValue spiritedGeneration;
    public final ForgeConfigSpec.LongValue nitroGeneration;

    public GeneratorConfig(ForgeConfigSpec.Builder builder, long[] caps, long[] trs, long[] gens) {
        super(builder, caps, trs);
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
        super.reload();
        this.generation.put(Tier.STARTER, this.starterGeneration.get());
        this.generation.put(Tier.BASIC, this.basicGeneration.get());
        this.generation.put(Tier.HARDENED, this.hardenedGeneration.get());
        this.generation.put(Tier.BLAZING, this.blazingGeneration.get());
        this.generation.put(Tier.NIOTIC, this.nioticGeneration.get());
        this.generation.put(Tier.SPIRITED, this.spiritedGeneration.get());
        this.generation.put(Tier.NITRO, this.nitroGeneration.get());
        this.generation.put(Tier.CREATIVE, Energy.MAX);
    }

    public long getGeneration(Tier variant) {
        if (this.generation.containsKey(variant)) {
            return this.generation.get(variant);
        }
        return 0L;
    }
}
