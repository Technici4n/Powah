package owmii.powah.config;

import net.minecraftforge.common.ForgeConfigSpec;
import owmii.powah.block.Tier;

import java.util.HashMap;
import java.util.Map;

public class EnderCellConfig extends EnergyConfig {
    public final Map<Tier, Integer> channels = new HashMap<>();

    public final ForgeConfigSpec.IntValue starterChannels;
    public final ForgeConfigSpec.IntValue basicChannels;
    public final ForgeConfigSpec.IntValue hardenedChannels;
    public final ForgeConfigSpec.IntValue blazingChannels;
    public final ForgeConfigSpec.IntValue nioticChannels;
    public final ForgeConfigSpec.IntValue spiritedChannels;
    public final ForgeConfigSpec.IntValue nitroChannels;

    public EnderCellConfig(ForgeConfigSpec.Builder builder) {
        this(builder,
                new long[]{0, 0, 0, 0, 0, 0, 0},
                new long[]{200L, 2500L, 8000L, 20_000L, 50_000L, 100_000L, 400_000L},
                new int[]{1, 2, 4, 6, 9, 12, 16}
        );
    }

    public EnderCellConfig(ForgeConfigSpec.Builder builder, long[] caps, long[] trs, int[] channels) {
        super(builder, caps, trs);
        builder.push("Channels");
        this.starterChannels = builder.defineInRange("starterChannels", channels[0], 1, 16);
        this.basicChannels = builder.defineInRange("basicChannels", channels[1], 1, 16);
        this.hardenedChannels = builder.defineInRange("hardenedChannels", channels[2], 1, 16);
        this.blazingChannels = builder.defineInRange("blazingChannels", channels[3], 1, 16);
        this.nioticChannels = builder.defineInRange("nioticChannels", channels[4], 1, 16);
        this.spiritedChannels = builder.defineInRange("spiritedChannels", channels[5], 1, 16);
        this.nitroChannels = builder.defineInRange("nitroChannels", channels[6], 1, 16);
        builder.pop();
    }

    @Override
    public void reload() {
        super.reload();
        this.channels.put(Tier.STARTER, this.starterChannels.get());
        this.channels.put(Tier.BASIC, this.basicChannels.get());
        this.channels.put(Tier.HARDENED, this.hardenedChannels.get());
        this.channels.put(Tier.BLAZING, this.blazingChannels.get());
        this.channels.put(Tier.NIOTIC, this.nioticChannels.get());
        this.channels.put(Tier.SPIRITED, this.spiritedChannels.get());
        this.channels.put(Tier.NITRO, this.nitroChannels.get());
    }

    public int getChannels(Tier variant) {
        if (this.channels.containsKey(variant)) {
            return this.channels.get(variant);
        }
        return 0;
    }

    @Override
    public long getCapacity(Tier variant) {
        return 0L;
    }
}
