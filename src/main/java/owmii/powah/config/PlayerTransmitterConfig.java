package owmii.powah.config;

import net.minecraftforge.common.ForgeConfigSpec;
import owmii.lib.energy.Energy;
import owmii.powah.block.Tier;

import java.util.HashMap;
import java.util.Map;

public class PlayerTransmitterConfig extends EnergyConfig {
    public final Map<Tier, Long> chargingSpeed = new HashMap<>();

    public final ForgeConfigSpec.LongValue starterChargingSpeed;
    public final ForgeConfigSpec.LongValue basicChargingSpeed;
    public final ForgeConfigSpec.LongValue hardenedChargingSpeed;
    public final ForgeConfigSpec.LongValue blazingChargingSpeed;
    public final ForgeConfigSpec.LongValue nioticChargingSpeed;
    public final ForgeConfigSpec.LongValue spiritedChargingSpeed;
    public final ForgeConfigSpec.LongValue nitroChargingSpeed;

    public PlayerTransmitterConfig(ForgeConfigSpec.Builder builder) {
        super(builder,
                new long[]{100_000L, 1000_000L, 3000_000L, 5_000_000L, 10_000_000L, 15_000_000L, 40_000_000L},
                new long[]{500L, 2500L, 8000L, 20_000L, 50_000L, 100_000L, 400_000L}
        );
        builder.push("Charging_Speed");
        this.starterChargingSpeed = builder.defineInRange("starterChargingSpeed", 100, Energy.MIN, Energy.MAX);
        this.basicChargingSpeed = builder.defineInRange("basicChargingSpeed", 1000, Energy.MIN, Energy.MAX);
        this.hardenedChargingSpeed = builder.defineInRange("hardenedChargingSpeed", 3000, Energy.MIN, Energy.MAX);
        this.blazingChargingSpeed = builder.defineInRange("blazingChargingSpeed", 8000, Energy.MIN, Energy.MAX);
        this.nioticChargingSpeed = builder.defineInRange("nioticChargingSpeed", 12000, Energy.MIN, Energy.MAX);
        this.spiritedChargingSpeed = builder.defineInRange("spiritedChargingSpeed", 20000, Energy.MIN, Energy.MAX);
        this.nitroChargingSpeed = builder.defineInRange("nitroChargingSpeed", 50000, Energy.MIN, Energy.MAX);
        builder.pop();
    }

    @Override
    public void reload() {
        super.reload();
        this.chargingSpeed.put(Tier.STARTER, this.starterChargingSpeed.get());
        this.chargingSpeed.put(Tier.BASIC, this.basicChargingSpeed.get());
        this.chargingSpeed.put(Tier.HARDENED, this.hardenedChargingSpeed.get());
        this.chargingSpeed.put(Tier.BLAZING, this.blazingChargingSpeed.get());
        this.chargingSpeed.put(Tier.NIOTIC, this.nioticChargingSpeed.get());
        this.chargingSpeed.put(Tier.SPIRITED, this.spiritedChargingSpeed.get());
        this.chargingSpeed.put(Tier.NITRO, this.nitroChargingSpeed.get());
        this.chargingSpeed.put(Tier.CREATIVE, Energy.MAX);
    }

    public long getChargingSpeed(Tier variant) {
        if (this.chargingSpeed.containsKey(variant)) {
            return this.chargingSpeed.get(variant);
        }
        return 0L;
    }
}
