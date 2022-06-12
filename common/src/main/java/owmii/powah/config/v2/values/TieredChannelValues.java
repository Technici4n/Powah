package owmii.powah.config.v2.values;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import owmii.powah.block.Tier;
import owmii.powah.lib.logistics.energy.Energy;

public class TieredChannelValues {
	@ConfigEntry.BoundedDiscrete(max = 12)
	public int starter;
	@ConfigEntry.BoundedDiscrete(max = 12)
	public int basic;
	@ConfigEntry.BoundedDiscrete(max = 12)
	public int hardened;
	@ConfigEntry.BoundedDiscrete(max = 12)
	public int blazing;
	@ConfigEntry.BoundedDiscrete(max = 12)
	public int niotic;
	@ConfigEntry.BoundedDiscrete(max = 12)
	public int spirited;
	@ConfigEntry.BoundedDiscrete(max = 12)
	public int nitro;

	public TieredChannelValues() {
		this(1, 2, 3, 5, 7, 9, 12);
	}

	public TieredChannelValues(int starter, int basic, int hardened, int blazing, int niotic, int spirited, int nitro) {
		this.starter = starter;
		this.basic = basic;
		this.hardened = hardened;
		this.blazing = blazing;
		this.niotic = niotic;
		this.spirited = spirited;
		this.nitro = nitro;
	}

	public int get(Tier tier) {
		return switch (tier) {
			case STARTER -> starter;
			case BASIC -> basic;
			case HARDENED -> hardened;
			case BLAZING -> blazing;
			case NIOTIC -> niotic;
			case SPIRITED -> spirited;
			case NITRO -> nitro;
			case CREATIVE -> 0;
		};
	}
}
